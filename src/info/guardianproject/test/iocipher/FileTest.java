package info.guardianproject.test.iocipher;

import android.test.AndroidTestCase;
import android.util.Log;
import info.guardianproject.iocipher.File;

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
}
