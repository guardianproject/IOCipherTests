package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.VirtualFileSystem;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class VirtualFileSystemTest extends AndroidTestCase {
	private final static String TAG = "VirtualFileSystemTest";

	private java.io.File app_vfs;
	private VirtualFileSystem vfs;

	protected void setUp() {
		java.io.File db = new java.io.File(app_vfs, "sqlcipherfs.db");
		if (db.exists())
			db.delete();
		app_vfs = mContext.getDir("vfs", Context.MODE_PRIVATE)
				.getAbsoluteFile();
		vfs = new VirtualFileSystem(app_vfs.getAbsolutePath());
	}

	protected void tearDown() {
	}

	public void testInitMountUnmount() {
		vfs.mount();
		if (vfs.isOpen()) {
			Log.i(TAG, "vfs is open");
		} else {
			Log.i(TAG, "vfs is NOT open");
		}
		assertTrue(vfs.isOpen());
		vfs.unmount();
	}

	public void testInitMountMkdirUnmount() {
		vfs.mount();
		if (vfs.isOpen()) {
			Log.i(TAG, "vfs is open");
		} else {
			Log.i(TAG, "vfs is NOT open");
		}
		File f = new File("/test");
		assertTrue(f.mkdir());
		vfs.unmount();
	}
}
