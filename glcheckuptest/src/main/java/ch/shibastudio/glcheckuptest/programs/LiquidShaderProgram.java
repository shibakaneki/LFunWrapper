package ch.shibastudio.glcheckuptest.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import ch.shibastudio.glcheckuptest.R;

/**
 * Created by didier on 16.08.17.
 */

public class LiquidShaderProgram extends AbstractShaderProgram {
    public LiquidShaderProgram(@NonNull Context context) {
        super(context, R.raw.liquid_vertex_shader, R.raw.liquid_fragment_shader);
    }
}
