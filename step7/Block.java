package net.bplaced.javacrypto.blockchain.step8;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Block implements Serializable {

	private static final long serialVersionUID = 1984888774694344646L;
	public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public long timeStamp;
    public int nonce;
    public long mineStartTimeStamp;
    public long mineEndTimeStamp;
    public String difficultyString = "";
    public boolean mined = false;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calculatedhash = StringUtil.generateSha256(
                previousHash + 
                Long.toString(timeStamp) + 
                Integer.toString(nonce) + 
                merkleRoot + 
                Long.toString(mineStartTimeStamp)
        );
        return calculatedhash;
    }

    public void mineBlock(int difficulty) {
        mineStartTimeStamp = new Date().getTime();
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty, merkleRoot); 
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        mineEndTimeStamp = new Date().getTime();
        difficultyString = target;
        mined = true;
        System.out.println("Block Mined!!! " + (mineEndTimeStamp - mineStartTimeStamp) + " ms: " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if ((!"0".equals(previousHash)) && !transaction.asReward) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Die Transaktion konnte nicht bearbeitet werden, der Vorgang wurde abgebrochen.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Die Transaktion wurde dem Block erfolgreich hinzugefügt");
        return true;
    }
}
