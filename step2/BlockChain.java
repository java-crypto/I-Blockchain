package net.bplaced.javacrypto.blockchain.step2;

/*
* Herkunft/Origin: http://javacrypto.bplaced.net/
* Programmierer/Programmer: Michael Fehr
* Copyright/Copyright: frei verwendbares Programm (Public Domain)
* Copyright: This is free and unencumbered software released into the public domain.
* Lizenztext/Licence: <http://unlicense.org>
* getestet mit/tested with: Java Runtime Environment 8 Update 191 x64
* getestet mit/tested with: Java Runtime Environment 11.0.1 x64
* Datum/Date (dd.mm.jjjj): 28.05.2019
* Projekt/Project: Blockchain
* Funktion: Schritt 2: Verkettung der Liste mit Hashwerten
* Function: Step 2: chaining the blocks with hashes
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class BlockChain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("I02 BlockChain für Anfänger Stufe 02");

		int blockNumber;

		System.out.println("\nAnzahl Einträge in der blockchain:" + blockchain.size());
		// datensatz 1
		blockNumber = 1;
		System.out.println("\nErgänze Block " + blockNumber);
		addBlock(new Block("0", "Data " + blockNumber)); // genesis block
		// daten der blockchain
		System.out.println("Anzahl Einträge in der blockchain:" + blockchain.size());
		System.out.println("Inhalt der BlockChain:\n" + getJson(blockchain));

		// datensatz 2
		blockNumber = 2;
		System.out.println("\nErgänze Block " + blockNumber);
		addBlock(new Block(blockchain.get(blockchain.size() - 1).hash, "Data " + blockNumber));
		// daten der blockchain
		System.out.println("Anzahl Einträge in der blockchain:" + blockchain.size());
		System.out.println("Inhalt der BlockChain:\n" + getJson(blockchain));
	}

	public static void addBlock(Block newBlock) {
		blockchain.add(newBlock);
	}

	public static String getJson(Object o) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}

	public static String generateSha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
