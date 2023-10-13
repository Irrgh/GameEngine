//type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aUv;
layout (location=2) in vec3 aNorm;




uniform mat4 uView;
uniform mat4 uProjection;

out vec2 uv;


void main () {
    uv = aUv;
    gl_Position = uProjection * uView * vec4 (aPos, 1.0);
}

//type fragment
#version 330 core


in vec4 Position;
in vec2 uv;


uniform sampler2D textureSampler;
uniform float uTime;

out vec4 color;

void main () {
    vec3 white = vec3(1,1,1);
    color = texture (textureSampler, uv);
}


