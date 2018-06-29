#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec4 m_FresnelOutlineColor;

varying float fresnelOutlineR;

void main() {
    gl_FragColor = mix(vec4(0.0, 0.0, 0.0, 1.0), m_FresnelOutlineColor, fresnelOutlineR);
}