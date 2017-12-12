precision mediump float;

varying vec4 v_Color;

void main() {
  if (length(gl_PointCoord - vec2(0.5)) > 0.5) {
    discard;
  } else {
    gl_FragColor = v_Color;
  }
}