//type vertex
#version 460 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTex;
layout (location=2) in vec3 aNorm;



uniform mat4 uView;
uniform mat4 uProjection;

out vec2 uv;
out vec3 normal;
out vec3 pos;

void main () {
    uv = aTex;
    gl_Position = uProjection * uView * vec4 (aPos, 1.0);
    normal = aNorm;
    pos = aPos;
}

//type fragment
#version 460 core




in vec2 uv;
in vec3 normal;
in vec3 pos;


uniform sampler2D textureSampler;
uniform float uTime;

out vec4 color;

vec3 sunDir = normalize (vec3 (.21, .43, .5));
vec3 sunPos = vec3 (1.4,1.8, 1.3);
vec3 suncolor = normalize (vec3 (255, 235, 179));
float dist = distance (pos,sunPos);
float lightpower = 10;




void main () {
    vec3 col = vec3(1,0.4,.8);
    //vec4 col = texture (textureSampler, uv);
    float cosTheta = clamp(dot(normal,sunDir),0,1);
    color = vec4 ((normal * 0.15) + normal * lightpower * col * cosTheta / (dist*dist),1);
}


