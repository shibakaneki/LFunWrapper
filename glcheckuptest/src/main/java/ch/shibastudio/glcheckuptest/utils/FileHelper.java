package ch.shibastudio.glcheckuptest.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by shibakaneki on 12.07.17.
 */

public class FileHelper {
	public static String loadAsset(AssetManager assetMgr, String fileName) {
		String fileContent = null;
		try {
			InputStream is = assetMgr.open(fileName);

			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			fileContent = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return fileContent;
	}

	public static Bitmap loadBitmap(AssetManager assetMgr, String fileName) {
		Bitmap bitmap = null;
		try {
			InputStream is = assetMgr.open(fileName);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

		return bitmap;
	}

	public static String readTextFileFromResource(@NonNull Context context, int resId){
		StringBuilder body = new StringBuilder();
		try {
			InputStream inputStream = context.getResources().openRawResource(resId); InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String nextLine;
			while ((nextLine = bufferedReader.readLine()) != null) {
				body.append(nextLine);
				body.append('\n');
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not open resource: " + resId, e);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Resource not found: " + resId, nfe);
		}
		return body.toString();
	}
}
