package net.bplaced.javacrypto.blockchain.step6;

/*
* Herkunft/Origin: http://javacrypto.bplaced.net/
* Programmierer/Programmer: Michael Fehr
* Copyright/Copyright: frei verwendbares Programm (Public Domain)
* Copyright: This is free and unencumbered software released into the public domain.
* Lizenztext/Licence: <http://unlicense.org>
* getestet mit/tested with: Java Runtime Environment 8 Update 191 x64
* getestet mit/tested with: Java Runtime Environment 11.0.1 x64
* Datum/Date (dd.mm.jjjj): 29.05.2019
* Projekt/Project: Blockchain
* Funktion: Schritt 6: Erstellung einer Transaktion
* Function: Step 6: creation of a transaction
*
* Sicherheitshinweis/Security notice
* Die Programmroutinen dienen nur der Darstellung und haben keinen Anspruch auf eine korrekte Funktion, 
* insbesondere mit Blick auf die Sicherheit ! 
* Prüfen Sie die Sicherheit bevor das Programm in der echten Welt eingesetzt wird.
* The program routines just show the function but please be aware of the security part - 
* check yourself before using in the real world !
* 
* Das Programm benötigt die nachfolgende Bibliothek:
* The programm uses this external library:
* jar-Datei: https://mvnrepository.com/artifact/com.google.code.gson/gson 
* 
* Das Projekt basiert auf dem nachfolgenden Artikel:
* The project is based on this article:
* https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
*/

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class BlockChain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 4;

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("I06 BlockChain für Anfänger Stufe 06");

		System.out.println("\nErzeuge walletA");
		Wallet walletA = new Wallet();
		System.out.println("walletA privateKey:" + walletA.privateKey);
		System.out.println("walletA publicKey :" + walletA.publicKey);
		System.out.println("\nErzeuge walletB");
		Wallet walletB = new Wallet();
		System.out.println("walletB privateKey:" + walletB.privateKey);
		System.out.println("walletB publicKey :" + walletB.publicKey);
		
		System.out.println("\nErzeuge Transactionen");
		Transaction transaction1 = new Transaction(walletA.publicKey, walletB.publicKey, 10);
		Transaction transaction2 = new Transaction(walletB.publicKey, walletA.publicKey, 5);
		// System.out.println("transaction1:" + StringUtil.getJson(transaction1));
		// System.out.println("transaction2:" + StringUtil.getJson(transaction2));
		System.out.println("\nSigniere Transactionen");
		transaction1.generateSignature(walletA.privateKey);
		transaction2.generateSignature(walletB.privateKey);
		// System.out.println("transaction1:" + StringUtil.getJson(transaction1));
		System.out.println("\nVerifiziere Transactionen");
		Boolean verifyBool = transaction1.verifySignature();
		System.out.println("transaction1 gültig:" + verifyBool);
		verifyBool = transaction2.verifySignature();
		System.out.println("transaction2 gültig:" + verifyBool);
}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

	public static Boolean blockchainIsValid() {
		Block currentBlock;
		Block previousBlock;
		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				return false;
			}
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				return false;
			}
		}
		return true;
	}

	public static String createDemonstationData(int number) {
		Date date = new Date();
		return "Dataset:" + number + " " + new Timestamp(date.getTime());
	}
}
