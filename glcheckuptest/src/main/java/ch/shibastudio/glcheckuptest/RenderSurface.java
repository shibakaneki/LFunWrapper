package ch.shibastudio.glcheckuptest;

import android.graphics.Color;
import android.opengl.GLES20;

import ch.shibastudio.glcheckuptest.shaders.Texture;

/**
 * Created by shibakaneki on 12.07.17.
 */

public class RenderSurface {
	private int[] mFrameBuffer = new int[1];
	private Texture mTexture;
	private int mWidth;
	private int mHeight;
	private int mClearColor = Color.TRANSPARENT;

	public RenderSurface(int width, int height) {
		mWidth = width;
		mHeight = height;

		GLES20.glGenFramebuffers(1, mFrameBuffer, 0);
		mTexture = new Texture();

		// Bind the texture object
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.getTextureId());

		// Set default filtering modes
		// We could have them pass in through the parameters in the future.
		GLES20.glTexParameteri(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER,
				GLES20.GL_LINEAR);
		GLES20.glTexParameteri(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR);
		GLES20.glTexParameteri(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		// Generate the texture
		GLES20.glTexImage2D(
				GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mWidth, mHeight, 0,
				GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
		GLES20.glFramebufferTexture2D(
				GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
				GLES20.GL_TEXTURE_2D, mTexture.getTextureId(), 0);

		final int status =
				GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
		if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException(
					"Failed to initialize framebuffer object " + status);
		}

		// Bind the screen frame buffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	public void beginRender(int clearMask) {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
		GLES20.glViewport(0, 0, mWidth, mHeight);
		if (clearMask != 0) {
			GLES20.glClearColor(
					Color.red(mClearColor), Color.blue(mClearColor),
					Color.green(mClearColor), Color.alpha(mClearColor));
			GLES20.glClear(clearMask);
		}
	}

	public void endRender() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	}

	public Texture getTexture() {
		return mTexture;
	}

	public void setClearColor(int color) {
		mClearColor = color;
	}
}
