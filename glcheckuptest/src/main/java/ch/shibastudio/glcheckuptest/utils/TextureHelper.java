package ch.shibastudio.glcheckuptest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by shibakaneki on 19.01.18.
 */

public class TextureHelper {
	private final static String LOG_TAG = "TEXTURE_HELPER";

	/**
	 * Loads a texture.
	 * @param context as the given context.
	 * @param resourceId as the given texture resource id.
	 * @return a texture handle.
	 */
	public static int loadTexture(@NonNull Context context, int resourceId){
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);

		if(0 == textureObjectIds[0]){
			Log.w(LOG_TAG, "Couldn't generate a new OpenGL texture object!");
			return 0;
		}

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

		if(null == bitmap){
			Log.w(LOG_TAG, "Resource id " +resourceId +" cannot be decoded!");
			glDeleteTextures(1, textureObjectIds, 0);
		}

		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);

		return textureObjectIds[0];
	}
}
