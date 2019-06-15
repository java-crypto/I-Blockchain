package net.bplaced.javacrypto.blockchain.step8;

/*
* Herkunft/Origin: http://javacrypto.bplaced.net/
* Programmierer/Programmer: Michael Fehr
* Copyright/Copyright: frei verwendbares Programm (Public Domain)
* Copyright: This is free and unencumbered software released into the public domain.
* Lizenztext/Licence: <http://unlicense.org>
* getestet mit/tested with: Java Runtime Environment 8 Update 191 x64
* getestet mit/tested with: Java Runtime Environment 11.0.1 x64
* Datum/Date (dd.mm.jjjj): 12.06.2019
* Projekt/Project: Blockchain / JavaCryptoCoin
* Funktion: Schritt 8: die neue Krypto-Währung - JavaCryptoCoin
* Function: Step 8: the new crypto currency - JavaCryptoCoin
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
* https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
*/

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

public class JavaCryptoCoin {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	public static int difficulty = 4; // kann ganz vorsichtig erhöht werden
	public static float minimumTransaction = 0.1f;
	public static Wallet coinbase;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Wallet miner;
	public static Transaction genesisTransaction;

	public static void main(String[] args) throws IOException {
		System.out.println("I08 BlockChain für Anfänger Stufe 08 JavaCryptoCoin");

		// unsere variablen für das programm
		String filenameBlockchain = "i08_blockchain.txt";
		String filenameBlockchainJson = "i08_blockchain.json";
		String filenameWalletA = "i08_walleta.txt";
		String filenameWalletAJson = "i08_walleta.json";
		String filenameWalletB = "i08_walletb.txt";
		String filenameMiner = "i08_miner.txt";
		
		// generierung der wallets
		coinbase = new Wallet();
		walletA = new Wallet();
		walletB = new Wallet();
		miner = new Wallet();

		// erzeuge die genesis transaktion und sende 100 javacryptocoins an walletA
		// ("initial coin offering")
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey); // manuelle signatur der genesis transaktion
		genesisTransaction.transactionId = "0"; // manuelles setzen der transaktion id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionId)); // manuelles hinzufügen der transaktion zu den Transactions Output
		// es ist wichtig die erste transaktion in der UTXOs liste zu speichern
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		// der erste block wird in die blockchain geschrieben und gemined
		System.out.println("\nErzeuge und mine den Genesis block");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		System.out.println("\nGuthaben der Wallets:");
		System.out.println("Guthaben von coinbase: " + coinbase.getBalance());
		System.out.println("Guthaben von walletA : " + walletA.getBalance());
		System.out.println("Guthaben von walletB : " + walletB.getBalance());
		// System.out.println("Guthaben von miner : " + miner.getBalance());
		// speicherung der blockchain und der wallets
		System.out.println("\nSpeicherung der Blockchain und der Wallets");
		FileUtil.saveObject(filenameBlockchain, blockchain);
		FileUtil.saveObject(filenameWalletA, walletA);
		FileUtil.saveObject(filenameWalletB, walletB);
		FileUtil.saveObject(filenameMiner, miner);

		// der public key ist die empfänger-adresse, daher sichern wir diese daten
		System.out.println("\nEmpfänger-Adressen von walletA und walletB");
		PublicKey publicKeyA = walletA.publicKey;
		PublicKey publicKeyB = walletB.publicKey;
		String publicKeyAString = StringUtil.getStringFromKey(publicKeyA);
		String publicKeyBString = StringUtil.getStringFromKey(publicKeyB);
		System.out.println("publicKeyAString:" + publicKeyAString);
		System.out.println("publicKeyBString:" + publicKeyBString);

		// eine überweisung von walletA an walletB
		System.out.println("\nÜberweisung 40 JCC von walletA an walletB");
		Block blockNewA = new Block(blockchain.get(blockchain.size() - 1).hash);
		blockNewA.addTransaction(walletA.sendFunds(publicKeyB, 40f));
		// ergänze die blockchain um den neuen block und lasse ihn minen
		addBlock(blockNewA);
		System.out.println("\nGuthaben der Wallets:");
		System.out.println("Guthaben von walletA : " + walletA.getBalance());
		System.out.println("Guthaben von walletB : " + walletB.getBalance());

		// zwei überweisungen von walletB an walletA
		System.out.println("\n2 Überweisungen je 25 JCC von walletB an walletA");
		Block blockNewB = new Block(blockchain.get(blockchain.size() - 1).hash);
		blockNewB.addTransaction(walletB.sendFunds(publicKeyA, 25f));
		blockNewB.addTransaction(walletB.sendFunds(publicKeyA, 25f));
		// ergänze die blockchain um den neuen block und lasse ihn minen
		addBlock(blockNewB);
		System.out.println("\nGuthaben der Wallets:");
		System.out.println("Guthaben von walletA : " + walletA.getBalance());
		System.out.println("Guthaben von walletB : " + walletB.getBalance());
		// System.out.println(StringUtil.getJson(blockchain));

		// speicherung der blockchain und der wallets
		System.out.println("\nSpeicherung der Blockchain und der Wallets");
		FileUtil.saveObject(filenameBlockchain, blockchain);
		FileUtil.saveObject(filenameWalletA, walletA);
		FileUtil.saveObject(filenameWalletB, walletB);
		FileUtil.saveObject(filenameMiner, miner);

		// die blockchain funktioniert ohne online wallets, daher löschen wir walletB
		System.out.println("\nObjekt walletB gelöscht");
		walletB = null;

		// mehrere transaktionen kommen in einen block
		Block block1 = new Block(blockchain.get(blockchain.size() - 1).hash);
		block1.addTransaction(walletA.sendFunds(publicKeyB, 40f));
		block1.addTransaction(walletA.sendFunds(publicKeyB, 10.3f));
		addBlock(block1);

		block1 = new Block(blockchain.get(blockchain.size() - 1).hash);
		block1.addTransaction(walletA.sendFunds(publicKeyB, 20f));
		block1.addTransaction(walletA.sendFunds(publicKeyB, 1.1f));
		addBlock(block1);

		block1 = new Block(blockchain.get(blockchain.size() - 1).hash);
		block1.addTransaction(walletA.sendFunds(publicKeyB, 60f));
		addBlock(block1);

		System.out.println("\nBalances after multiple transactions");
		System.out.println("Miners's balance is : " + miner.getBalance());
		System.out.println("WalletA's balance is: " + walletA.getBalance());

		// walletB laden
		System.out.println("\nobjekt walletB geladen");
		walletB = (Wallet) FileUtil.loadObject(filenameWalletB);

		System.out.println("WalletB's balance is: " + walletB.getBalance());

		System.out.println("\nSend Coin to walletA");
		Block blockB1 = new Block(blockchain.get(blockchain.size() - 1).hash);
		blockB1.addTransaction(walletB.sendFunds(walletA.publicKey, 11.123f));
		addBlock(blockB1);
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		System.out.println("_miner's balance is : " + miner.getBalance());

		System.out.println("\nSend Coin from miner to walletA");
		Block blockMiner = new Block(blockchain.get(blockchain.size() - 1).hash);
		blockMiner.addTransaction(miner.sendFunds(walletA.publicKey, 3.99f));
		addBlock(blockMiner);
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		System.out.println("_miner's balance is : " + miner.getBalance());

		System.out.println("\nSend 1 Coin from miner to walletA, 1+2+3 b an a");
		blockMiner = new Block(blockchain.get(blockchain.size() - 1).hash);
		blockMiner.addTransaction(miner.sendFunds(walletA.publicKey, 1.00f));

		System.out.println(isChainValid());
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		System.out.println("_miner's balance is : " + miner.getBalance());
		blockMiner.addTransaction(walletA.sendFunds(publicKeyB, 44f));

		blockMiner.addTransaction(walletB.sendFunds(walletA.publicKey, 1.00f));
		// System.out.println("Transaction:\n" + StringUtil.getJson(blockMiner));
		blockMiner.addTransaction(walletB.sendFunds(walletA.publicKey, 2.00f));
		blockMiner.addTransaction(walletB.sendFunds(walletA.publicKey, 3.00f));

		addBlock(blockMiner);

		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		System.out.println("_miner's balance is : " + miner.getBalance());
		
		// save blockchain
		FileUtil.saveObject(filenameBlockchain, blockchain);
		System.out.println("\nBlockchain gespeichert in Datei:" + filenameBlockchain);
		FileUtil.saveJsonFile(filenameBlockchainJson, StringUtil.getJson(blockchain));
		System.out.println("\nBlockchain im JSON-Format gespeichert in Datei:" + filenameBlockchainJson);
		System.out.println("\nDie Blockchain ist gültig:" + isChainValid());

		// save wallet 
		FileUtil.saveObject(filenameWalletA, walletA);
		System.out.println("\nWalletA gespeichert in Datei:" + filenameWalletA);
		FileUtil.saveJsonFile(filenameWalletAJson, StringUtil.getJson(walletA));
		System.out.println("\nWalletA im JSON-Format gespeichert in Datei:" + filenameWalletAJson);
				
		System.out.println("\nProgramm beendet");
	}

	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("# Die Current Hashes sind nicht gleich");
				return false;
			}
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("# Die Previous Hashes sind nicht gleich");
				return false;
			}
			if (!currentBlock.mined) {
				System.out.println("# Dieser Block wurde noch nicht mined");
				return false;
			}
			TransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				if (!currentTransaction.verifySignature()) {
					System.out.println("# Die Signatur der Transaction(" + t + ") ist ungültig");
					return false;
				}
				if (!currentTransaction.asReward) {
					if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
						System.out.println("# Die Inputs sind nicht gleich den Outputs der Transaktion(" + t + ")");
						return false;
					}
					for (TransactionInput input : currentTransaction.inputs) {
						tempOutput = tempUTXOs.get(input.transactionOutputId);
						if (tempOutput == null) {
							System.out.println("# Der referenzierte Input der Transaktion(" + t + ") fehlt");
							return false;
						}
						if (input.UTXO.value != tempOutput.value) {
							System.out
									.println("# Der referenzierte Input der Transaktion(" + t + ") Wert ist ungültig");
							return false;
						}
						tempUTXOs.remove(input.transactionOutputId);
					}
				}
				for (TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}
				if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					System.out.println("# Transaktion(" + t + ") Der Output Empfänger ist nicht wer er sein sollte");
					return false;
				}
				if (!currentTransaction.asReward) {
					if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
						System.out.println("# Transaktion(" + t + ") Der Output 'change' ist nicht der Sender");
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
