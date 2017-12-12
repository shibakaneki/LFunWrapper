package ch.shibastudio.glcheckuptest.renderers;

/**
 * Created by shibakaneki on 06.10.17.
 */

public interface ITextureRenderer {

	/**
	 * Initializes the rendering.
	 */
	void initRendering();

	/**
	 * Renders the scene.
	 */
	void render();

	/**
	 * Sets the frame per second
	 * @param fps as the wanted FPS.
	 */
	void setFps(float fps);

	/**
	 * Gets the fps.
	 * @return the frame per second.
	 */
	float getFps();

	/**
	 * Gets the surface width.
	 * @return the surface width.
	 */
	int getWidth();

	/**
	 * Gets the surface height.
	 * @return the surface height.
	 */
	int getHeight();

	/**
	 * Notifies a slow rendering.
	 * @param exceedingMs as the exceeding time used for the rendering (total time - desired fps)
	 */
	void notifySlowRendering(long exceedingMs);
}
