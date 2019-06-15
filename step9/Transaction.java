package net.bplaced.javacrypto.blockchain.step9;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.Serializable;
import java.security.*;
import java.util.ArrayList;

public class Transaction implements Serializable{

	private static final long serialVersionUID = -3795222285878701219L;
	public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public boolean asReward = false; 
    
    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public boolean processTransaction() {
        if (verifySignature() == false) {
            System.out.println("# Transaktion - Die Signatur ist nicht korrekt");
            return false;
        }
        for (TransactionInput i : inputs) {
            i.UTXO = JavaCryptoCoin.UTXOs.get(i.transactionOutputId);
        }
        if (getInputsValue() < JavaCryptoCoin.minimumTransaction) {
            System.out.println("# Transaktion Der Input ist zu gering: " + getInputsValue());
            System.out.println("# Bitte geben Sie einen Betrag groesser als " + JavaCryptoCoin.minimumTransaction + " ein.");
            return false;
        }
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender		
        for (TransactionOutput o : outputs) {
        	JavaCryptoCoin.UTXOs.put(o.id, o);
        }
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;            }
            JavaCryptoCoin.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                continue;
            }
            total += i.UTXO.value;
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.generateECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    public String calculateHash() {
        sequence++;
        return StringUtil.generateSha256(
                StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(recipient)
                + Float.toString(value) + sequence
        );
    }
}
