package ch.shibastudio.glcheckuptest.utils;

/**
 * Created by didier on 16.08.17.
 */

public class MatrixHelper {
    /**
     * Builds a perspective matrix.
     * @param m as the array that will contain the matrix.
     * @param yFovInDegree as the field of vision angle.
     * @param aspect as the screen aspect ratio.
     * @param n as the distance to the near plane.
     * @param f as the distance to the far plane.
     */
    public static void perspectiveM(float[] m, float yFovInDegree, float aspect, float n, float f){
        // Compute the focal length.
        final float angleRad = (float)Math.toRadians(yFovInDegree);
        final float a = (float)(1.0f / Math.tan(angleRad / 2f));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f+n)/(f-n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
