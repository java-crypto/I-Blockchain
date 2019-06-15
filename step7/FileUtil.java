package net.bplaced.javacrypto.blockchain.step8;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtil {

	public static void saveObject(String filenameString, Object object) {
		try {
			FileOutputStream fo = new FileOutputStream(new File(filenameString));
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(object);
			oo.close();
			fo.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei nicht gefunden");
		} catch (IOException e) {
			System.out.println("Fehler bei der Initialisierung des Streams");
		}
	}

	public static Object loadObject(String filenameString) {
		Object object = null;
		try {
			FileInputStream fi = new FileInputStream(new File(filenameString));
			ObjectInputStream oi = new ObjectInputStream(fi);
			object = (Object) oi.readObject();
			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("Datei nicht gefunden");
		} catch (IOException e) {
			System.out.println("Fehler bei der Initialisierung des Streams");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static void saveJsonFile(String filenameString, String jsonString) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filenameString));
		writer.write(jsonString);
		writer.close();

	}
}
