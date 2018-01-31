package ch.shibastudio.glcheckuptest.renderers;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ch.shibastudio.glcheckuptest.entities.LiquidEntity;
import ch.shibastudio.glcheckuptest.entities.RectEntity;
import ch.shibastudio.glcheckuptest.geometry.Point;
import ch.shibastudio.glcheckuptest.programs.LiquidShaderProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by didier on 16.08.17.
 */

public class LParticleRenderer implements GLSurfaceView.Renderer {
    private final static float WORLD_TOP = 10f;
    private final static float WORLD_BOTTOM = 0f;
    private final static float WORLD_HEIGHT = WORLD_TOP - WORLD_BOTTOM;

    private final static float WALL1_X = 177f/360f;
    private final static float WALL1_Y = 208f/640f;
    private final static float WALL1_W = 176f/360f;
    private final static float WALL1_H = 20f/640f;

    private Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    private LiquidShaderProgram liquidShaderProgram;
    private LiquidEntity liquidEntity;

    private RectEntity rectEntity;
    private RectEntity wall1Entity;

    public LParticleRenderer(@NonNull Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1f, 1f, 1f, 1f);

        this.liquidShaderProgram = new LiquidShaderProgram(this.context);
        this.liquidEntity = new LiquidEntity(10000, null);

        // Test particle
        //this.liquidEntity.addParticle(new Point(0f, 5f, 0f), Color.rgb(0, 172, 231));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // Test rect
        this.rectEntity = new RectEntity(
                0f,
                5f,
                WORLD_HEIGHT*ratio,
                WORLD_HEIGHT,
                new float[]{1f,1f,1f, 1f});

        float wall1WidthW = WALL1_W * WORLD_HEIGHT*ratio;
        float wall1HeightW = WALL1_H * WORLD_HEIGHT;
        float wall1XW = -WORLD_HEIGHT*ratio/2f + WALL1_X * WORLD_HEIGHT*ratio;
        float wall1YW = WORLD_TOP - WALL1_Y * WORLD_HEIGHT;

        this.wall1Entity = new RectEntity(
                wall1XW,
                wall1YW,
                wall1WidthW,
                wall1HeightW);

        this.wall1Entity.setAngle(-30f);  // !!!!!!!!!!! SHIT!!!

        orthoM(this.projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);

        float scaleFactor = 2f / WORLD_HEIGHT;  // OpenGL height (2f) / world height

        float[] transformationMatrix = new float[16];
        float[] translationMatrix = new float[16];
        float[] scaleMatrix = new float[16];

        setIdentityM(translationMatrix, 0);
        setIdentityM(scaleMatrix, 0);
        setIdentityM(transformationMatrix, 0);

        translateM(translationMatrix, 0, 0f, -5f, 0f);
        scaleM(scaleMatrix, 0, scaleFactor, scaleFactor, 1f);
        multiplyMM(transformationMatrix, 0, scaleMatrix, 0, translationMatrix, 0);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, transformationMatrix, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        this.rectEntity.render(viewProjectionMatrix);
        this.wall1Entity.render(viewProjectionMatrix);

        this.liquidShaderProgram.useProgram();
        this.liquidShaderProgram.setUniforms(this.viewProjectionMatrix);
        //this.liquidEntity.bindData(this.liquidShaderProgram);
        this.liquidEntity.draw();
    }
}
