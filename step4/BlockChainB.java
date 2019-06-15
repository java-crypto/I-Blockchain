package net.bplaced.javacrypto.blockchain.step4;

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
* Funktion: Schritt 4: Mining eines Blockes
* Function: Step 4: mining of a block
*
* Sicherheitshinweis/Security notice
* Die Programmroutinen dienen nur der Darstellung und haben keinen Anspruch auf eine korrekte Funktion, 
* insbesondere mit Blick auf die Sicherheit ! 
* Pr�fen Sie die Sicherheit bevor das Programm in der echten Welt eingesetzt wird.
* The program routines just show the function but please be aware of the security part - 
* check yourself before using in the real world !
* 
* Das Programm ben�tigt die nachfolgende Bibliothek:
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

public class BlockChainB {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 4;

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("I04b BlockChain f�r Anf�nger Stufe 04");

		int blockNumber;

		// genesis datensatz
		blockNumber = 1;
		System.out.println("\nGenesis Block " + blockNumber);
		addBlock(new Block("0", "Genesis Block")); // genesis block
		// daten der blockchain
		System.out.println("Anzahl Eintr�ge in der blockchain:" + blockchain.size());
		System.out.println("Inhalt der BlockChain:\n" + StringUtil.getJson(blockchain));
		System.out.println("Ist die blockchain g�ltig:" + blockchainIsValid());

		for (blockNumber = 2; blockNumber < 12; blockNumber++) {
			System.out.println("\nErg�nze Block " + blockNumber);
			addBlock(new Block(blockchain.get(blockchain.size() - 1).hash, "Data " + blockNumber));
		}
		// daten der blockchain
		System.out.println("Anzahl Eintr�ge in der blockchain:" + blockchain.size());
		System.out.println("Inhalt der BlockChain:\n" + StringUtil.getJson(blockchain));
		System.out.println("Ist die blockchain g�ltig:" + blockchainIsValid());
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
