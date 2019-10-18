/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.db.indexing;

import it.webapp.db.dao.jdbc.DaoException;
import it.webapp.db.dao.jdbc.DaoFactoryException;
import it.webapp.db.dao.jdbc.JdbcDaoFactory;
import it.webapp.db.dao.jdbc.JdbcItemDao;
import it.webapp.db.entities.ItemEntity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Helper class for Lucene index management.
 * Must be initialized before being used
 */
public class LuceneIndexHelper {

	private static final String ID_FIELD_NAME = "id";
	private static final String TITLE_FIELD_NAME = "title";
	
	private static Directory indexDirectory;
	private static DirectoryReader directoryReader;
	
	private LuceneIndexHelper() {
		//Enforces non instantiability
	}
	
	/**
	 * Retrieves the index reader for the most recent update of the index
	 * 
	 * @return reader updated for the latest version of the index
	 * @throws IOException In case it is not possible to read the index
	 */
	private synchronized static DirectoryReader getLatestDirectoryReader() throws IOException {
		DirectoryReader latestReader = DirectoryReader.openIfChanged(directoryReader);
		
		if(latestReader != null) {
			directoryReader = latestReader;
		}
		
		return directoryReader;
	}
	
	/**
	 * Initializes the index.
	 * 
	 * @param indexDirectoryPath Path to the index directory
	 * @throws IOException In case it is not possible to read the index
	 * @throws DaoException In case it is not possible to retrieve items
	 * @throws DaoFactoryException In case it is not possible to obtain the dao
	 */
	public static void init(String indexDirectoryPath) throws IOException, DaoException, DaoFactoryException {
		if(indexDirectory != null) {
			throw new IllegalStateException("Lucene index is already initialized");
		}
		if(indexDirectoryPath == null) {
			throw new IllegalArgumentException("Index directory must be not null");
		}
		
		indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
		buildIndex();
		directoryReader = DirectoryReader.open(indexDirectory);
	}
	
	/**
	 * Builds the index from scratch
	 * 
	 * @throws IOException In case it is not possible to use the index
	 * @throws it.webapp.db.dao.jdbc.DaoException In case it is not possible to retrieve items
	 * @throws it.webapp.db.dao.jdbc.DaoFactoryException In case it is not possible to obtain the dao
	 */
	private static void buildIndex() throws IOException, DaoException, DaoFactoryException {
		
		JdbcItemDao itemDao = JdbcDaoFactory.getItemDao();
		
		List<ItemEntity> items = itemDao.getAll();
		
		Analyzer analyzer = new StandardAnalyzer();
		
		//Inserts item info in the index
		IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
		//Creates a brand new index overwriting the existing one
		writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		try(IndexWriter writer = new IndexWriter(indexDirectory, writerConfig)) {
		
			Document doc = new Document();
			
			Field idField = new StoredField(ID_FIELD_NAME, 0);
			Field titleField = new TextField(TITLE_FIELD_NAME, "", Field.Store.YES);

			for(ItemEntity currentItem : items) {
				int itemId = currentItem.getItemId();
				String itemTitle = currentItem.getTitle();

				idField.setIntValue(itemId);
				titleField.setStringValue(itemTitle);

				doc.add(idField);
				doc.add(titleField);

				writer.addDocument(doc);
			}
		}
	}
	
	/**
	 * Gets a list of item descriptors from a search query
	 * 
	 * @param query The search terms. Must be not null and not empty
	 * @param count The maxumum number of results to retrieve
	 * @return Ids and titles of items matching the search terms
	 */
	public static List<Pair<Integer, String>> getFromQuery(String query, int count) throws IOException {
		if(indexDirectory == null) {
			throw new IllegalStateException("Lucene index must be initialied before being used");
		}
		if(query == null || query.isEmpty()) {
			throw new IllegalArgumentException("Query must be a not null not empty string");
		}
		
		query = query.toLowerCase(); //Searches are most effective if performed in lowercase
		
		String[] stringTokens = query.split("[ ]");
		//Processes every term
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		for(int i = 0; i < stringTokens.length; ++i) {
			String currentToken = stringTokens[i];
			
			Term currentTerm = new Term(TITLE_FIELD_NAME, currentToken);
			
			//Builds the query one fragment at a time
			Query currentQuery = new FuzzyQuery(currentTerm);
			builder.add(currentQuery, BooleanClause.Occur.SHOULD);
		}
		
		Query luceneQuery = builder.build();
		
		//Executed the query
		List<Pair<Integer, String>> results = executeQuery(luceneQuery, count);
		return results;
		
	}
	
	/**
	 * Gets a list of item descriptors from a partial search query that has not been fully typed
	 * 
	 * @param query The search terms. Must be not null and not empty
	 * @param count The maxumum number of results to retrieve
	 * @return Ids and titles of items matching the search terms
	 */
	public static List<Pair<Integer, String>> getFromPartialQuery(String query, int count) throws IOException {
		if(indexDirectory == null) {
			throw new IllegalStateException("Lucene index must be initialied before being used");
		}
		if(query == null || query.isEmpty()) {
			throw new IllegalArgumentException("Query must be a not null not empty string");
		}
		
		query = query.toLowerCase(); //Searches are most effective if performed in lowercase
		
		String[] stringTokens = query.split("[ ]");
		//Processes every term
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		for(int i = 0; i < stringTokens.length; ++i) {
			String currentToken = stringTokens[i];
			
			Term currentTerm = new Term(TITLE_FIELD_NAME, currentToken);
			Query currentQuery;
			//Last term must be matched only partially
			if(i != stringTokens.length - 1) {
				currentQuery = new FuzzyQuery(currentTerm);
			} else {
				//The last term is incomplete so only a prefix match is required
				currentQuery = new PrefixQuery(currentTerm); 
			}
			builder.add(currentQuery, BooleanClause.Occur.MUST);
		}
		
		Query luceneQuery = builder.build();
		
		//Executed the query
		List<Pair<Integer, String>> results = executeQuery(luceneQuery, count);
		return results;
	}
	
	/**
	 * Gets a list of item descriptors from a given query
	 * 
	 * @param query The serch query. Must be not null
	 * @param count The maxumum number of results to retrieve
	 * @return Ids and titles of items matching the search terms
	 */
	private static List<Pair<Integer, String>> executeQuery(Query query, int count) throws IOException {
		List<Pair<Integer, String>> results = new ArrayList<>();
		
		DirectoryReader latestReader = getLatestDirectoryReader();
		
		IndexSearcher indexSearcher = new IndexSearcher(latestReader);
		
		TopDocs topDocs = indexSearcher.search(query, count);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		
		for(ScoreDoc currentScoreDoc: scoreDocs) {
			Document result = indexSearcher.doc(currentScoreDoc.doc);
			Integer itemId = Integer.parseInt(result.get(ID_FIELD_NAME));
			String itemTitle = result.get(TITLE_FIELD_NAME);
			
			//Builds the descriptor and adds it to the results
			Pair<Integer, String> itemDescriptor = new Pair<>(itemId, itemTitle);
			results.add(itemDescriptor);
		}
		
		return results;
	}
	
}
