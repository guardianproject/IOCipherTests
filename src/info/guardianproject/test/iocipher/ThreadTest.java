/**
 * Copyright 2012 Aaron Huttner <aaron@gryphn.co>
 */
package info.guardianproject.test.iocipher;

import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.IOCipherFileChannel;
import info.guardianproject.iocipher.VirtualFileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import info.guardianproject.test.iocipher.R;
import info.guardianproject.test.iocipher.R.id;
import info.guardianproject.test.iocipher.R.layout;
import info.guardianproject.test.iocipher.R.raw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ThreadTest extends Activity {
	private VirtualFileSystem vfs;
	private static final String TAG = "ThreadTest";

	private Button mRunButton;
	private ImageView mImageView;

	private Handler handler;
	private int IMG_NUM = 1;

	private static final int DELAY = 100; // delay in milliseconds for the
											// runnable below, the shorter the
											// delay the easier it is to cause
											// the problem

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mImageView = (ImageView) findViewById(R.id.imageView);

		mRunButton = (Button) findViewById(R.id.runButton);

		/*
		 * set up a button to try to save new info the the IOCipher DB while
		 * it's being accessed by the runnable. This mimics sending a new
		 * message when you already have a bunch of images displaying in your
		 * conversation
		 */
		mRunButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// we use an async task because it mimics the real app's
				// behavior
				new saveFileAsyncTask().execute();

			}
		});

		// mount the db and add some files to it
		mount();
		copyToCipherDb("/redbull.jpeg", getResources().openRawResource(R.raw.redbull));
		copyToCipherDb("/car.jpeg", getResources().openRawResource(R.raw.car));

		/*
		 * set up a runnable to constantly access the IOCipher DB. The point of
		 * this is to mimic what happens when I scroll around in a list view
		 * that is displaying images, just like in the MMS app when you're in a
		 * thread
		 */
		handler = new Handler();
		final Runnable switchR = new Runnable() {
			public void run() {

				String image = "";
				switch (IMG_NUM) {
				case 1:
					IMG_NUM = 2;
					image = "/redbull.jpeg";
					break;
				case 2:
					IMG_NUM = 1;
					image = "/car.jpeg";
					break;

				}

				info.guardianproject.iocipher.FileInputStream input;
				try {
					input = new info.guardianproject.iocipher.FileInputStream(image);
					Bitmap bm = BitmapFactory.decodeStream(input, null, null);
					mImageView.setImageBitmap(bm);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				handler.postDelayed(this, DELAY);
			}
		};

		handler.postDelayed(switchR, DELAY);

	}

	public void mount() {
		java.io.File db = new java.io.File(this.getApplicationContext().getDir("vfs", Context.MODE_PRIVATE).getAbsoluteFile(), "test.db");
		if (db.exists()) {
			db.delete();
			Log.v(TAG, "database file: " + db.getAbsolutePath());
		}
		if (db.exists()) {
			Log.v(TAG, "exists: " + db.getAbsolutePath());
		}
		if (!db.canRead()) {
			Log.v(TAG, "can't read: " + db.getAbsolutePath());
			// throw new Exception("can't read: " + db.getAbsolutePath());
		}
		if (!db.canWrite()) {
			Log.v(TAG, "can't write: " + db.getAbsolutePath());
			// throw new Exception("can't write: " + db.getAbsolutePath());
		}

		vfs = new VirtualFileSystem(db.getAbsolutePath());
		vfs.mount("password");


	}

	private void copyToCipherDb(String fileName, InputStream is) {
		try {

			info.guardianproject.iocipher.FileOutputStream fos = new info.guardianproject.iocipher.FileOutputStream(fileName);

			ReadableByteChannel sourceFileChannel = Channels.newChannel(is);
			IOCipherFileChannel destinationFileChannel = fos.getChannel();
			destinationFileChannel.transferFrom(sourceFileChannel, 0, is.available());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class saveFileAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			copyToCipherDb("/thumbs_up.jpeg", getResources().openRawResource(R.raw.thumbs_up));
			return null;
		}

	}

}
