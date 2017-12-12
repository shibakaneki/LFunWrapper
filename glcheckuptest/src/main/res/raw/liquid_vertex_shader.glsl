uniform mat4 u_Matrix;

attribute vec3 a_Position;
attribute vec4 a_Color;
attribute vec2 a_PointSize;

varying vec4 v_Color;

void main() {
    v_Color = a_Color;
    gl_Position = u_Matrix * vec4(a_Position, 1.0);
    gl_PointSize = a_PointSize[0] * a_PointSize[1];
}
