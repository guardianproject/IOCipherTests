package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.FileInputStream;
import info.guardianproject.iocipher.FileOutputStream;
import info.guardianproject.iocipher.FileReader;
import info.guardianproject.iocipher.FileWriter;
import info.guardianproject.iocipher.IOCipherFileChannel;
import info.guardianproject.iocipher.VirtualFileSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileTest extends AndroidTestCase {
	private final static String TAG = "FileTest";

	private VirtualFileSystem vfs;

	protected void setUp() {
		java.io.File db = new java.io.File(mContext.getDir("vfs",
				Context.MODE_PRIVATE).getAbsoluteFile(), TAG + ".db");
		if (db.exists())
			db.delete();
		Log.v(TAG, "database file: " + db.getAbsolutePath());
		if (db.exists())
			Log.v(TAG, "exists: " + db.getAbsolutePath());
		try {
			vfs = new VirtualFileSystem(db.getAbsolutePath());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		vfs.mount();
	}

	protected void tearDown() {
		vfs.unmount();
	}

	public void testExists() {
		File f = new File("");
		try {
			assertFalse(f.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testGetFreeSpace() {
		File f = new File("");
		try {
			long free = f.getFreeSpace();
			Log.v(TAG, "f.getFreeSpace: " + Long.toString(free));
			assertTrue(free > 0);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testGetUsableSpace() {
		File f = new File("");
		try {
			long total = f.getUsableSpace();
			Log.v(TAG, "f.getUsableSpace: " + Long.toString(total));
			assertTrue(total > 0);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testGetTotalSpace() {
		File f = new File("");
		try {
			long total = f.getTotalSpace();
			Log.v(TAG, "f.getTotalSpace: " + Long.toString(total));
			assertTrue(total > 0);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testMkdirExists() {
		File f = new File("/test.iocipher.dir."
				+ Integer.toString((int) (Math.random() * 1024)));
		try {
			assertFalse(f.exists());
			assertTrue(f.mkdir());
			assertTrue(f.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testMkdirs() {
		File f0 = new File("/"
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		File f1 = new File(f0,
				Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		File f2 = new File(f1,
				Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		Log.v(TAG, "f2: " + f2.getAbsolutePath());
		try {
			f2.mkdirs();
			for (String f : f0.list()) {
				Log.v(TAG, "file in f0: " + f);
			}
			for (String f : f1.list()) {
				Log.v(TAG, "file in f1: " + f);
			}
			assertTrue(f0.exists());
			assertTrue(f1.exists());
			assertTrue(f2.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testSlashIsDirectory() {
		File f = new File("/");
		try {
			assertTrue(f.isDirectory());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testCanReadSlash() {
		File f = new File("/");
		try {
			assertTrue(f.isDirectory());
			assertTrue(f.canRead());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testCanWriteSlash() {
		File f = new File("/");
		try {
			assertTrue(f.isDirectory());
			assertTrue(f.canWrite());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testSlashIsFile() {
		File f = new File("/");
		try {
			assertFalse(f.isFile());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testSlashIsAbsolute() {
		File f = new File("/");
		try {
			assertTrue(f.isAbsolute());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testMkdirRemove() {
		File f = new File("/mkdir-to-remove");
		try {
			assertTrue(f.mkdir());
			assertTrue(f.exists());
			assertTrue(f.delete());
			assertFalse(f.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testARenameDirFailure() {
		File d = new File("/dir-to-rename");
		try {
			d.mkdir();
			assertFalse(d.renameTo(new File("/somethingelse")));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

/*
// TODO libsqlfs does not support renaming directories
	public void testMkdirRename() {
		String dir = "/mkdir-to-rename";
		String newdir = "/renamed";
		String firstfile = "first-file";
		File root = new File("/");
		File d = new File(dir);
		File newd = new File(newdir);
		try {
			d.mkdir();
			File f1 = new File(d, firstfile);
			f1.createNewFile();
			assertTrue(f1.exists());
			String[] files = d.list();
			for (String filename : files) {
				Log.v(TAG, "testMkdirList " + dir + ": " + filename);
			}
			assertTrue(d.renameTo(newd));
			assertTrue(new File(newd, firstfile).exists());
			File f2 = new File(newd, "second-file");
			f2.createNewFile();
			files = root.list();
			for (String filename : files) {
				Log.v(TAG, "testMkdirList root: " + filename);
			}
			files = newd.list();
			for (String filename : files) {
				Log.v(TAG, "testMkdirList " + newdir + ": " + filename);
			}
			assertFalse(d.exists());
			assertTrue(newd.exists());
			assertTrue(f2.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
*/

	public void testNewFileRename() {
		File root = new File("/");
		File f = new File("/testNewFileRename-NEW."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		File newf = new File("/testNewFileRename-RENAMED."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			f.createNewFile();
			assertTrue(f.renameTo(newf));
			final String[] files = root.list();
			for (String filename : files) {
				Log.v(TAG, "testNewFileRename file: " + filename);
			}
			assertFalse(f.exists());
			assertTrue(newf.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testMkdirIsDirectory() {
		File f = new File("/mkdir-to-test");
		try {
			f.mkdir();
			assertTrue(f.isDirectory());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testMkdirList() {
		File root = new File("/");
		File f = new File("/mkdir-to-list");
		try {
			f.mkdir();
			final String[] files = root.list();
			for (String filename : files) {
				Log.v(TAG, "testMkdirList file: " + filename);
			}
			assertTrue(files.length > 2); // it should always give us "." and
			// ".."
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

/*
// TODO testMkdirLastModified fails
	public void testMkdirLastModified() {
		File root = new File("/");
		File f = new File("/test.iocipher.dir."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			long lasttime = root.lastModified();
			Log.v(TAG, "f.lastModified: " + Long.toString(lasttime));
			f.mkdir();
			long thistime = root.lastModified();
			Log.i(TAG,
					"f.lastModified after setting: " + Long.toString(thistime));
			assertTrue(thistime > lasttime);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

// TODO testMkdirMtime fails
	public void testMkdirMtime() {
		File f = new File("/mkdir-with-mtime");
		long faketime = 1000000000L;
		try {
			f.mkdir();
			Log.v(TAG, "f.lastModified: " + Long.toString(f.lastModified()));
			f.setLastModified(faketime);
			long time = f.lastModified();
			Log.v(TAG, "f.lastModified after setting: " + Long.toString(time));
			assertTrue(time == faketime);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
*/

	public void testCreateNewFile() {
		File root = new File("/");
		File f = new File("/testCreateNewFile."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			f.createNewFile();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			final String[] files = root.list();
			for (String filename : files) {
				Log.v(TAG, "testCreateNewFile file: " + filename);
			}
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteNewFile() {
		File root = new File("/");
		File f = new File("/testWriteNewFile."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertTrue(root.isDirectory());
			assertFalse(f.exists());
			FileOutputStream out = new FileOutputStream(f);
			out.write(123);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			final String[] files = root.list();
			for (String filename : files) {
				Log.v(TAG, "testWriteNewFile file: " + filename);
			}
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteByteInNewFileThenRead() {
		byte testValue = 43;
		File root = new File("/");
		File f = new File("/testWriteNewFile."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertTrue(root.isDirectory());
			assertFalse(f.exists());
			FileOutputStream out = new FileOutputStream(f);
			out.write(testValue);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			int b = in.read();
			Log.v(TAG, "read: " + Integer.toString(b));
			assertTrue(b == testValue);
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenReadByByte() {
		String testString = "this is a test of IOCipher!";
		File f = new File("/testWriteTextInNewFileThenReadByByte."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			byte[] data = new byte[testString.length()];
			in.read(data, 0, data.length);
			String dataString = new String(data);
			Log.v(TAG, "read: " + dataString);
			assertTrue(dataString.equals(testString));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenReadIntoByteArray() {
		String testString = "this is a test of IOCipher!";
		File f = new File("/testWriteTextInNewFileThenReadIntoByteArray."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			byte[] data = new byte[testString.length()];
			int ret = in.read(data);
			assertTrue(ret == data.length);
			String dataString = new String(data);
			assertTrue(dataString.equals(testString));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenReadOneByteByByte() {
		String testString = "01234567890abcdefgh";
		File f = new File("/testWriteTextInNewFileThenReadOneByteByByte."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			byte[] data = new byte[testString.length()];
			int ret = 0;
			int i = 0;
			while (i < data.length) {
				ret = in.read();
				if (ret != -1) {
					data[i] = (byte) ret;
					i++;
				} else {
					break;
				}
			}
			String dataString = new String(data);
			Log.v(TAG, "read: " + dataString);
			assertTrue(dataString.equals(testString));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenCheckSize() {
		String testString = "01234567890abcdefgh";
		File f = new File("/testWriteTextInNewFileThenCheckSize."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			IOCipherFileChannel channel = in.getChannel();
			assertTrue(channel.size() == testString.length());
			assertTrue(testString.length() == f.length());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenSkipAndRead() {
		String testString = "01234567890abcdefghijklmnopqrstuvxyz";
		File f = new File("/testWriteTextInNewFileThenSkipAndRead."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			FileInputStream in = new FileInputStream(f);
			char c = (char) in.read();
			assertTrue(c == testString.charAt(0));
			in.skip(5);
			c = (char) in.read();
			Log.v(TAG, "c: " + c + "  testString.charAt(6): "
							+ testString.charAt(6));
			assertTrue(c == testString.charAt(6));
			in.skip(20);
			c = (char) in.read();
			Log.v(TAG,"c: " + c + "  testString.charAt(27): "
							+ testString.charAt(27));
			assertTrue(c == testString.charAt(27));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteTextInNewFileThenFileInputStream() {
		String testString = "01234567890abcdefgh";
		File f = new File("/testWriteTextInNewFileThenFileInputStream."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(testString);
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			BufferedReader in = new BufferedReader(new FileReader(f));
			String tmp = in.readLine();
			Log.v(TAG, "in.readline(): " + tmp);
			assertTrue(testString.equals(tmp));
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

	public void testWriteManyLinesInNewFileThenFileInputStream() {
		String testString = "01234567890abcdefghijklmnopqrstuvwxyz";
		File f = new File("/testWriteManyLinesInNewFileThenFileInputStream."
				+ Integer.toString((int) (Math.random() * Integer.MAX_VALUE)));
		try {
			assertFalse(f.exists());
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			for (int i = 0; i < 25; i++)
				out.write(testString + "\n");
			out.close();
			assertTrue(f.exists());
			assertTrue(f.isFile());
			BufferedReader in = new BufferedReader(new FileReader(f));
			for (int i = 0; i < 25; i++) {
				String tmp = in.readLine();
				Log.v(TAG, "in.readline(): " + tmp);
				assertTrue(testString.equals(tmp));
			}
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		} catch (IOException e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}

}
