//type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fColor;
out float zDepth;
out vec4 Position;

void main () {
    fColor =  aColor;
    gl_Position = uProjection * uView * vec4 (aPos, 1.0);
    Position = gl_Position;
}

//type fragment
#version 330 core

in vec4 fColor;
in vec4 Position;
out vec4 color;

uniform float uTime;



void main () {
    vec3 white = vec3(1,1,1);
    color = vec4(fColor.rgb, 1);

}


