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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * A randomly generated code with low probability of collisions
 */
public class SecurityCode {
	private static final int SECURITY_CODE_LENGTH = 16;
	
	private byte[] code;
	
	/**
	 * Inserts a value into a byte array starting at the indicated offset
	 * @param value the value to insert
	 * @param array the destination array
	 * @param offset the index on which to start the copy
	 */
	private void longToBytes(long value, byte[] array, int offset) {
		
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(value);
		byte[] longArray = buffer.array();
		
		//Process every byte in the long
		for(int i = 0; i < longArray.length; ++i) {
			array[i + offset] = longArray[i];
		}
	}
	
	/**
	 * Creates a new random code
	 */
	public SecurityCode() {
		//Generate a security code using UUIDs
		UUID uuid = UUID.randomUUID();
		long mostSignificantBits = uuid.getMostSignificantBits();
		long leastSignificantBits = uuid.getLeastSignificantBits();
		
		code = new byte[SECURITY_CODE_LENGTH];
		longToBytes(mostSignificantBits, code, 8); //Must be inserted a long (8 bytes) after the beginning
		longToBytes(leastSignificantBits, code, 0);
	}
	
	/**
	 * Creates the code as a copy of the given code
	 * 
	 * @param code the code to copy
	 */
	SecurityCode(byte[] code) {
		if(code.length != SECURITY_CODE_LENGTH) {
			throw new IllegalArgumentException("Supplied SecurityCode length is " + code.length + " but required length " + SECURITY_CODE_LENGTH);
		}
		
		//Copies the argument
		this.code = Arrays.copyOf(code, SECURITY_CODE_LENGTH);
	}
	
	/**
	 * Creates the code as a copy of the given code
	 * 
	 * @param code the code to copy
	 */
	public SecurityCode(String code) {
		byte[] codeBytes = Base64.getUrlDecoder().decode(code);
		
		if(codeBytes.length != SECURITY_CODE_LENGTH) {
			throw new IllegalArgumentException("Supplied SecurityCode length is " + codeBytes.length + " but required length " + SECURITY_CODE_LENGTH);
		}
		
		//Copies the argument
		this.code = Arrays.copyOf(codeBytes, SECURITY_CODE_LENGTH);
	}
	
	public boolean match(SecurityCode other) {
		for(int i = 0; i < code.length; ++i) {
			if(code[i] != other.code[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(code, code.length);
	}
	
	public String getString() {
		return Base64.getUrlEncoder().encodeToString(code);
	}
}
