package info.guardianproject.iocipher.tests;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.util.Formatter;
import java.util.Random;

import android.util.Log;

public class Util {
	private final static String TAG = "Util";

	static String randomFileName(File root, String testName) {
		String name = null;
		do {
			name = root.getAbsolutePath()
					+ "/"
					+ testName
					+ "."
					+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
		} while ((new File(name)).exists());
		return name;
	}

	static String toHex(byte[] digest) {
		Formatter formatter = new Formatter();
		for (byte b : digest) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

	static boolean nativeWriteRandomBytes(int bytes, String filename) {
		try {
			File f = new File(filename);
			FileOutputStream out = new FileOutputStream(f);

			Random prng = new Random();
			byte[] random_buf = new byte[bytes];
			prng.nextBytes(random_buf);

			out.write(random_buf);
			out.close();

		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			return false;
		}
		return true;
	}

	static boolean cipherWriteRandomBytes(int bytes, String filename) {
		try {
			info.guardianproject.iocipher.File f = new info.guardianproject.iocipher.File(filename);
			info.guardianproject.iocipher.FileOutputStream out = new info.guardianproject.iocipher.FileOutputStream(f);

			Random prng = new Random();
			byte[] random_buf = new byte[bytes];
			prng.nextBytes(random_buf);

			out.write(random_buf);
			out.close();

		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			return false;
		}
		return true;
	}

	static void deleteDirectory(File dir) {
		for (File child : dir.listFiles()) {
			if (child.isDirectory()) {
				deleteDirectory(child);
				child.delete();
			} else
				child.delete();
		}
		dir.delete();
	}
}
