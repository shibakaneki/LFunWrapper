package ch.shibastudio.glcheckuptest.programs;

/**
 * Created by shibakaneki on 29.01.18.
 */

public interface IShaderProgram {
	/**
	 * Uses the program.
	 */
	void useProgram();

	/**
	 * Sets the uniforms.
	 * @param matrix as the given matrix.
	 */
	void setUniforms(float[] matrix);

	/**
	 * Sets the testure.
	 * @param textureId as the id of the texture to set.
	 */
	void setTexture(int textureId);

	/**
	 * Gets the location of the position attribute.
	 * @return the location of the position attribute.
	 */
	int getPositionAttributeLocation();

	/**
	 * Gets the location of the color attribute.
	 * @return the location of the color attribute.
	 */
	int getColorAttributeLocation();

	/**
	 * Gets the location of the point size attribute.
	 * @return the location of the point size attribute.
	 */
	int getPointSizeAttributeLocation();

	/**
	 * Gets the location of the texture coordinate location.
	 * @return
	 */
	int getTextureCoordinatesAttributeLocation();

	/**
	 * Gets an indication whether this program uses a textures.
	 * @return true if a texture is used; otherwise false.
	 */
	boolean hasTexture();
}
