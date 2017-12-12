package ch.shibastudio.glcheckuptest.renderers;

import android.graphics.SurfaceTexture;
import android.opengl.GLUtils;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.TextureView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;

import static android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION;
import static android.opengl.EGL14.EGL_OPENGL_ES2_BIT;
import static javax.microedition.khronos.egl.EGL10.EGL_BAD_NATIVE_WINDOW;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE;

/**
 * Created by shibakaneki on 06.10.17.
 */

public abstract class AbstractTextureRenderer implements ITextureRenderer, TextureView.SurfaceTextureListener {
	private final static String LOG_TAG = "TEXTURERENDERER";

	private RenderThread renderThread;
	private int width;
	private int height;

	private float fps = 30.0f;
	private long lastUpdateTime = -1l;

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		this.width = width;
		this.height = height;
		this.renderThread = new RenderThread(surface, this);
		this.renderThread.setFps(this.fps);
		this.renderThread.start();
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		if(null != this.renderThread){
			this.renderThread.stopRendering();
		}
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		long t = System.currentTimeMillis();

		if(this.lastUpdateTime > 0){
			float dt = (float)(t - this.lastUpdateTime);
			this.fps = 1000.0f / dt;
			Log.i(LOG_TAG, "FPS: " +fps);
		}

		this.lastUpdateTime = t;
	}

	@Override
	public int getWidth(){ return this.width; }

	@Override
	public int getHeight(){ return this.height; }

	@Override
	public float getFps(){ return this.fps; }

	@Override
	public void setFps(float fps){
		this.fps = fps;
		if(null != this.renderThread){
			this.renderThread.setFps(fps);
		}
	}

	@Override
	public void notifySlowRendering(long exceedingMs){
		Log.w(LOG_TAG, "Slow rendering! (overtime of " +exceedingMs +" ms)");
	}

	private static class RenderThread extends Thread{
		private SurfaceTexture surface;

		private EGL10 egl;
		private EGLDisplay eglDisplay;
		private EGLContext eglContext;
		private EGLConfig eglConfig;
		private EGLSurface eglSurface;
		private GL gl;
		private ITextureRenderer textureRenderer;

		private long targetFpsDelay = 2000l;
		private boolean isRunning = true;
		private boolean isRenderingInitialized = false;

		public RenderThread(SurfaceTexture surface, @NonNull ITextureRenderer renderer){
			this.surface = surface;
			this.textureRenderer = renderer;
		}

		/**
		 * Sets the fps.
		 * @param fps as the given fps.
		 */
		public void setFps(float fps){
			this.targetFpsDelay = (long)(1000.0f / fps);
		}

		/**
		 * Stops the rendering.
		 */
		public void stopRendering(){
			this.isRunning = false;
		}

		@Override
		public void run(){
			this.initGL();

			while(this.isRunning){
				long t0 = System.currentTimeMillis();
				this.checkCurrent();

				if(!this.isRenderingInitialized){
					this.textureRenderer.initRendering();
					this.isRenderingInitialized = true;
				}

				this.textureRenderer.render();

				// swap the buffers
				if (!this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface)) {
					Log.e(LOG_TAG, "Cannot swap buffers");
					//throw new RuntimeException("Cannot swap buffers");
				}

				long t1 = System.currentTimeMillis();
				long dt = t1 - t0;

				if(dt < this.targetFpsDelay){
					long timeToWait = this.targetFpsDelay - dt;
					// Sleep
					try {
						Thread.sleep(timeToWait);
					} catch (InterruptedException e) {
						// Ignore
					}
				}else{
					this.textureRenderer.notifySlowRendering(dt - this.targetFpsDelay);
				}
			}

			this.finishGL();
		}

		/**
		 * Initializes OpenGL.
		 */
		private void initGL(){
			this.egl = (EGL10)EGLContext.getEGL();

			// Get the display
			this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
			if(EGL_NO_DISPLAY == this.eglDisplay){
				throw new RuntimeException("eglGetDisplay failed " + GLUtils.getEGLErrorString(this.egl.eglGetError()));
			}

			int[] version = new int[2];
			if(!this.egl.eglInitialize(this.eglDisplay, version)){
				throw new RuntimeException("eglInitialize failed " + GLUtils.getEGLErrorString(this.egl.eglGetError()));
			}

			// Get the config
			this.eglConfig = this.chooseGLConfig();
			if(null == this.eglConfig){
				throw new RuntimeException("eglConfig not initialized");
			}

			this.eglContext = this.createContext(this.egl, this.eglDisplay, this.eglConfig);

			// Create the surface
			this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, this.surface, null);
			if(null == this.eglSurface || EGL_NO_SURFACE == this.eglSurface){
				int error = this.egl.eglGetError();
				if(EGL_BAD_NATIVE_WINDOW == error){
					Log.e(LOG_TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
					return;
				}
				throw new RuntimeException("createWindowSurface failed " + GLUtils.getEGLErrorString(error));
			}

			if (!this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
				throw new RuntimeException("eglMakeCurrent failed " + GLUtils.getEGLErrorString(this.egl.eglGetError()));
			}

			this.gl = this.eglContext.getGL();
		}

		/**
		 * Checks the current EGL context.
		 */
		private void checkCurrent() {
			if (!this.eglContext.equals(this.egl.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl.eglGetCurrentSurface(EGL10.EGL_DRAW))) {
				if (!this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
					throw new RuntimeException("eglMakeCurrent failed " + GLUtils.getEGLErrorString(this.egl.eglGetError()));
				}
			}
		}

		/**
		 * Creates the OpenGL context.
		 * @param egl as the egl.
		 * @param eglDisplay as the egl display.
		 * @param eglConfig as the egl config.
		 * @return the egl context.
		 */
		private EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig){
			int[] attribList = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE};
			return egl.eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, attribList);
		}

		/**
		 * Finishes OpenGL.
		 */
		private void finishGL(){
			this.egl.eglDestroyContext(this.eglDisplay, this.eglContext);
			this.egl.eglDestroySurface(this.eglDisplay, this.eglSurface);
		}

		/**
		 * Choose the OpenGL config.
		 * @return the OpenGL config.
		 */
		private EGLConfig chooseGLConfig(){
			int[] configsCount = new int[1];
			EGLConfig [] configs = new EGLConfig[1];
			int[] configSpec = this.getConfig();
			if(!this.egl.eglChooseConfig(this.eglDisplay, configSpec, configs, 1, configsCount)){
				throw new IllegalArgumentException("eglChooseConfig failed " + GLUtils.getEGLErrorString(this.egl.eglGetError()));
			}else if(configsCount[0] > 0){
				return configs[0];
			}
			return null;
		}

		/**
		 * Gets the config.
		 * @return the config.
		 */
		private int[] getConfig(){
			return new int[] {
					EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
					EGL10.EGL_RED_SIZE, 8,
					EGL10.EGL_GREEN_SIZE, 8,
					EGL10.EGL_BLUE_SIZE, 8,
					EGL10.EGL_ALPHA_SIZE, 8,
					EGL10.EGL_DEPTH_SIZE, 0,
					EGL10.EGL_STENCIL_SIZE, 0,
					EGL10.EGL_NONE
			};
		}
	}
}
