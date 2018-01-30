package ch.shibastudio.glcheckuptest.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.shibastudio.glcheckuptest.R;

/**
 * Created by didier on 16.08.17.
 */

public class LiquidTextureShaderProgram extends AbstractShaderProgram {
    public LiquidTextureShaderProgram(@NonNull Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
    }

}
