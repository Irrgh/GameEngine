//type vertex
#version 460 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTex;
layout (location=2) in vec3 aNorm;



uniform mat4 uView;
uniform mat4 uProjection;

out vec2 uv;
out vec3 normal;


void main () {
    uv = aTex;
    gl_Position = uProjection * uView * vec4 (aPos, 1.0);
    normal = aNorm;
}

//type fragment
#version 460 core


in vec4 Position;
in vec2 uv;
in vec3 normal;


uniform sampler2D textureSampler;
uniform float uTime;

out vec4 color;

vec3 sun = vec3 (.1, .3, -.5);


void main () {
    vec3 white = vec3(1,1,1);
    //color = texture (textureSampler, uv);
    color = vec4(normal,1);
}


