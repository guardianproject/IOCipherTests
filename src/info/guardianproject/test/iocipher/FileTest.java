package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.VirtualFileSystem;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileTest extends AndroidTestCase {
	private final static String TAG = "FileTest";

	private java.io.File app_vfs;
	private VirtualFileSystem vfs;

	protected void setUp() {
		java.io.File db = new java.io.File(app_vfs, "sqlcipherfs.db");
		if (db.exists())
			db.delete();
		app_vfs = mContext.getDir("vfs", Context.MODE_PRIVATE)
				.getAbsoluteFile();
		vfs = new VirtualFileSystem(app_vfs.getAbsolutePath());
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

	public void testSlashIsDirectory() {
		File f = new File("/");
		try {
			assertTrue(f.isDirectory());
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

	public void testMkdirRename() {
		File f = new File("/mkdir-to-rename");
		try {
			f.mkdir();
			f.renameTo(new File("/renamed"));
			assertFalse(new File("/mkdir-to-rename").exists());
			assertTrue(new File("/renamed").exists());
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
				Log.i(TAG, "testMkdirList file: " + filename);
			}
			assertTrue(files.length > 2); // it should always give us "." and
											// ".."
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
}
