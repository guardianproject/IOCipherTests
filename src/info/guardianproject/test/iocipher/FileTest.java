package info.guardianproject.test.iocipher;

import android.test.AndroidTestCase;
import info.guardianproject.iocipher.File;

public class FileTest extends AndroidTestCase {

	protected void setUp() {
		
	}
	protected void tearDown() {
		
	}
	public void testExists() {
		File f = new File("");
		assertFalse(f.exists());
	}
}
