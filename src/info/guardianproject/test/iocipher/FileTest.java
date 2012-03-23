package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileTest extends AndroidTestCase {
	private final static String TAG = "AndroidTestCase";
	
	private java.io.File app_tmp;
	private String app_tmp_dir;

	protected void setUp() {
		app_tmp = mContext.getDir("tmp", Context.MODE_PRIVATE).getAbsoluteFile();
		app_tmp_dir = app_tmp.getAbsolutePath();
	}
	protected void tearDown() {
		
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
	public void testSlashList() {
		File f = new File("/");
		try {
			for (String file : f.list()) {
				Log.i(TAG, "file: " + file);
			}
			assertTrue(true);
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
	public void testCreateDirInRootNoPerms() {
		File f = new File("/NonExistant");
		try {
			assertFalse(f.mkdir());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
	
	public void testDataLocalExists() {
		File f = new File("/data/local");
		try {
			assertTrue(f.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testDataLocalIsDirectory() {
		File f = new File("/data/local");
		try {
			assertTrue(f.isDirectory());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testAppTmpMkdir() {
		File f = new File(app_tmp_dir + "/test.iocipher.dir." + Integer.toString((int)(Math.random() * 1024)));
		try {
			assertTrue(f.mkdir());
		} catch (ExceptionInInitializerError e) {
			Log.e(TAG, e.getCause().toString());
			assertFalse(true);
		}
	}
}
