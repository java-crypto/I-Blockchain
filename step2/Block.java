package net.bplaced.javacrypto.blockchain.step2;

/*
* Diese Klasse gehört zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash; 
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private String data;
	  
	public Block(String previousHash, String data) {
		this.previousHash = previousHash;
		this.data = data;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public String calculateHash() {
		String calculatedhash = BlockChain.generateSha256( 
				previousHash +
				Long.toString(timeStamp) +
				data 
				);
		return calculatedhash;
	}
}
