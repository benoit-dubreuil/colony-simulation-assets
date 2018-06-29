#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec3 g_CameraPosition;
uniform mat4 g_WorldMatrix;
uniform mat4 g_WorldViewProjectionMatrix;

uniform float m_FresnelOutlineBias;
uniform float m_FresnelOutlineScale;
uniform float m_FresnelOutlinePower;

attribute vec3 inPosition;
attribute vec3 inNormal;

varying float fresnelOutlineR;

void main() {
    vec3 worldPosition = (g_WorldMatrix * vec4(inPosition, 1.0)).xyz;
    vec4 worldNormal = normalize(g_WorldMatrix * vec4(inNormal, 0.0));

    vec3 fresnelI = normalize(worldPosition - g_CameraPosition);

    fresnelOutlineR = m_FresnelOutlineBias + m_FresnelOutlineScale * pow(1.0 + dot(fresnelI, worldNormal.xyz), m_FresnelOutlinePower);

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}