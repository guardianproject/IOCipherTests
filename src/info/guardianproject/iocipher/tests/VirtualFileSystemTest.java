
package info.guardianproject.iocipher.tests;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.VirtualFileSystem;

public class VirtualFileSystemTest extends AndroidTestCase {
    private final static String TAG = "VirtualFileSystemTest";

    private VirtualFileSystem vfs;
    private String goodPassword = "this is the right password";
    private String badPassword = "this soooo not the right password, its wrong";
    private byte[] goodKey = {
            (byte) 0x2a, (byte) 0xfc, (byte) 0x69, (byte) 0xa1, (byte) 0x16, (byte) 0x40,
            (byte) 0x4f, (byte) 0x7d, (byte) 0x7f, (byte) 0x1b, (byte) 0x1d, (byte) 0xb9,
            (byte) 0x5e, (byte) 0x18, (byte) 0x11, (byte) 0x2e, (byte) 0x6b, (byte) 0x3c,
            (byte) 0xf7, (byte) 0x1e, (byte) 0x78, (byte) 0xaf, (byte) 0x88, (byte) 0x3c,
            (byte) 0xb1, (byte) 0x90, (byte) 0x51, (byte) 0x15, (byte) 0xbf, (byte) 0xc3,
            (byte) 0xb2, (byte) 0x8d,
    };
    private byte[] longKey = {
            (byte) 0x2a, (byte) 0xfc, (byte) 0x69, (byte) 0xa1, (byte) 0x16, (byte) 0x40,
            (byte) 0x4f, (byte) 0x7d, (byte) 0x7f, (byte) 0x1b, (byte) 0x1d, (byte) 0xb9,
            (byte) 0x5e, (byte) 0x18, (byte) 0x11, (byte) 0x2e, (byte) 0x6b, (byte) 0x3c,
            (byte) 0xf7, (byte) 0x1e, (byte) 0x78, (byte) 0xaf, (byte) 0x88, (byte) 0x3c,
            (byte) 0xb1, (byte) 0x90, (byte) 0x51, (byte) 0x15, (byte) 0xbf, (byte) 0xc3,
            (byte) 0xb2, (byte) 0x8d, (byte) 0x00
    };
    private byte[] shortKey = {
            (byte) 0x2a, (byte) 0xfc, (byte) 0x69, (byte) 0xa1, (byte) 0x16, (byte) 0x40,
            (byte) 0x4f, (byte) 0x7d, (byte) 0x7f, (byte) 0x1b, (byte) 0x1d, (byte) 0xb9,
            (byte) 0x5e, (byte) 0x18, (byte) 0x11, (byte) 0x2e, (byte) 0x6b, (byte) 0x3c,
            (byte) 0xf7, (byte) 0x1e, (byte) 0x78, (byte) 0xaf, (byte) 0x88, (byte) 0x3c,
            (byte) 0xb1, (byte) 0x90, (byte) 0x51, (byte) 0x15, (byte) 0xbf, (byte) 0xc3,
    };
    private byte[] badKey = {
            'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B',
            'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B',
    };

    @Override
    protected void setUp() {
        java.io.File db = new java.io.File(mContext.getDir("vfs",
                Context.MODE_PRIVATE).getAbsoluteFile(), TAG + ".db");
        if (db.exists())
            db.delete();
        vfs = VirtualFileSystem.get();
        vfs.setContainer(db);
    }

    @Override
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

    public void testMountPasswordWithBadPassword() {
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

    public void testMountKeyWithBadKey() {
        Log.i(TAG, "goodKey length: " + goodKey.length);
        Log.i(TAG, "badKey length: " + badKey.length);
        vfs.mount(goodKey);
        File d = new File("/");
        for (String f : d.list()) {
            Log.v(TAG, "file: " + f);
        }
        vfs.unmount();
        try {
            vfs.mount(badKey);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            vfs.unmount();
        }
        fail();
    }

    public void testMountKeyWithLongKey() {
        Log.i(TAG, "goodKey length: " + goodKey.length);
        Log.i(TAG, "longKey length: " + longKey.length);
        vfs.mount(goodKey);
        File d = new File("/");
        for (String f : d.list()) {
            Log.v(TAG, "file: " + f);
        }
        vfs.unmount();
        try {
            vfs.mount(longKey);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            vfs.unmount();
        }
        fail();
    }

    public void testMountKeyWithShortKey() {
        Log.i(TAG, "goodKey length: " + goodKey.length);
        Log.i(TAG, "shortKey length: " + shortKey.length);
        vfs.mount(goodKey);
        File d = new File("/");
        for (String f : d.list()) {
            Log.v(TAG, "file: " + f);
        }
        vfs.unmount();
        try {
            vfs.mount(shortKey);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            vfs.unmount();
        }
        fail();
    }

    public void testMountKeyWithZeroedKey() {
        byte[] keyCopy = new byte[goodKey.length];
        for (int i = 0; i < goodKey.length; i++)
            keyCopy[i] = goodKey[i];
        vfs.mount(keyCopy);
        File d = new File("/");
        for (String f : d.list()) {
            Log.v(TAG, "file: " + f);
        }
        vfs.unmount();
        for (int i = 0; i < keyCopy.length; i++)
            keyCopy[i] = 0;
        try {
            vfs.mount(keyCopy);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            vfs.unmount();
        }
        fail();
    }
}
