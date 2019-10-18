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

package it.webapp.db.entities;

public enum IssueResult {
	MONEY_BACK(1, "Soldi indietro"),
	BAD_REPORTING_TO_SELLER(2, "Valutazione negativa venditore"),
	NOT_PROCEED(3, "Impossibile procedere"),
	REJECT_ANOMALY(4, "Anomalia rigettata"),
	ARTICLE_REPLACEMENT(5, "Sostituzione articolo");

	private final int issueResultId;
	private final String description;
	private final static int NUMBER_OF_ISSUE_RESULTS = 5;
        
	public static IssueResult getById(int issueResultId) {
            IssueResult issueResult = null;
            
            switch(issueResultId) {
                case 1:
                    issueResult = MONEY_BACK;
                    break;
                case 2:
                    issueResult = BAD_REPORTING_TO_SELLER;
                    break;
                case 3:
                    issueResult = NOT_PROCEED;
                    break;
                case 4:
                    issueResult = REJECT_ANOMALY;
                    break;
                case 5:
                    issueResult = ARTICLE_REPLACEMENT;
                    break;  
                default:
                    //Ensure we do not miss to update the swtich statement with new IssueResults
                    throw new UnsupportedOperationException("No NotificationType corresponding to id: " + issueResultId);                    
            }
            
            return issueResult;
	}

        public static int getNumberOfIssueResults() {
            return NUMBER_OF_ISSUE_RESULTS;
        }
        
	IssueResult(int issueResultId, String description) {
            this.issueResultId = issueResultId;
			this.description = description;
	}
        
	public int getId() {
            return issueResultId;
	}
	
	public String getDescription() {
            return description;
	}
      
}
