package info.guardianproject.iocipher.tests;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.VirtualFileSystem;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class VirtualFileSystemTest extends AndroidTestCase {
	private final static String TAG = "VirtualFileSystemTest";

	private VirtualFileSystem vfs;
	private String goodPassword = "this is the right password";
	private String badPassword = "this soooo not the right password, its wrong";

	protected void setUp() {
		java.io.File db = new java.io.File(mContext.getDir("vfs",
				Context.MODE_PRIVATE).getAbsoluteFile(), TAG + ".db");
		if (db.exists())
			db.delete();
		vfs = new VirtualFileSystem(db.getAbsolutePath());
	}

	protected void tearDown() {
	}

	public void testInitMountUnmount() {
		vfs.mount(goodPassword);
		if (vfs.isMounted()) {
			Log.i(TAG, "vfs is mounted");
		} else {
			Log.i(TAG, "vfs is NOT mounted");
		}
		assertTrue(vfs.isMounted());
		vfs.unmount();
	}

	public void testInitMountMkdirUnmount() {
		vfs.mount(goodPassword);
		if (vfs.isMounted()) {
			Log.i(TAG, "vfs is mounted");
		} else {
			Log.i(TAG, "vfs is NOT mounted");
		}
		File d = new File("/test");
		assertTrue(d.mkdir());
		vfs.unmount();
	}

	public void testMountCreateUnmountMountExists() {
		vfs.mount(goodPassword);
		File f = new File("/testMountCreateUnmountMountExists."
				+ Integer.toString((int) (Math.random() * 1024)));
		try {
			f.createNewFile();
		} catch (Exception e) {
			Log.e(TAG, "cannot create " + f.getPath());
			assertFalse(true);
		}
		vfs.unmount();
		vfs.mount(goodPassword);
		assertTrue(f.exists());
		vfs.unmount();
	}

	public void testMountKeyWithBadPassword() {
		vfs.mount(goodPassword);
		File d = new File("/");
		for (String f : d.list()) {
			Log.v(TAG, "file: " + f);
		}
		vfs.unmount();
		try {
			vfs.mount(badPassword);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			vfs.unmount();
		}
		fail();
	}
}
