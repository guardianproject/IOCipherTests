package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileTest extends AndroidTestCase {

	protected void setUp() {
		
	}
	protected void tearDown() {
		
	}
	public void testExists() {
		File f = new File("");
		try {
			assertFalse(f.exists());
		} catch (ExceptionInInitializerError e) {
			Log.e("IOCipherTests", e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testSlashIsDirectory() {
		File f = new File("/");
		try {
			assertTrue(f.isDirectory());
		} catch (ExceptionInInitializerError e) {
			Log.e("IOCipherTests", e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testSlashIsFile() {
		File f = new File("/");
		try {
			assertFalse(f.isFile());
		} catch (ExceptionInInitializerError e) {
			Log.e("IOCipherTests", e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testSlashIsAbsolute() {
		File f = new File("/");
		try {
			assertTrue(f.isAbsolute());
		} catch (ExceptionInInitializerError e) {
			Log.e("IOCipherTests", e.getCause().toString());
			assertFalse(true);
		}
	}
	public void testCreateDirInTmp() {
		//File f = new File("/tmp/test.iocipher.dir." + Integer.toString((int)(Math.random() * 1024)));
		File f = new File("/TEST");
		try {
			assertTrue(f.mkdir());
		} catch (ExceptionInInitializerError e) {
			Log.e("IOCipherTests", e.getCause().toString());
			assertFalse(true);
		}
	}
}
