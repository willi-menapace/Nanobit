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

import java.security.MessageDigest;
import java.util.Arrays;

public class HashDigest {

	private static final int HASH_DIGEST_LENGTH = 64;
	private static final String HASH_ALGORITHM = "SHA-512";
	
	private byte[] hashDigest;
	
	public HashDigest(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
			hashDigest = md.digest(text.getBytes("UTF-8"));
		} catch(Exception e) {
			throw new IllegalStateException("Application server does not support required hash algorithm " + HASH_ALGORITHM);
		}
	}
	
	HashDigest(byte[] hashDigest) {
		if(hashDigest.length != HASH_DIGEST_LENGTH) {
			throw new IllegalArgumentException("Supplied HashDigest has length " + hashDigest.length + " but required length is " + HASH_DIGEST_LENGTH);
		}
		
		this.hashDigest = Arrays.copyOf(hashDigest, HASH_DIGEST_LENGTH);
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(hashDigest, hashDigest.length);
	}
	
}
