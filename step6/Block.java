package net.bplaced.javacrypto.blockchain.step6;

/*
* Diese Klasse gehört zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.util.Date;

public class Block {

	public String hash;
	public String previousHash;
	private long timeStamp;
	private String data;
	private int nonce;

	public Block(String previousHash, String data) {
		this.previousHash = previousHash;
		this.data = data;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}

	public String calculateHash() {
		String calculatedhash = StringUtil
				.generateSha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
		return calculatedhash;
	}

	public void mineBlock(int difficulty) {
		String target = StringUtil.getDifficultyString(difficulty);
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
	}
}
