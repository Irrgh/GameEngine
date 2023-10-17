//type vertex
#version 460 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTex;
layout (location=2) in vec3 aNorm;
layout (location=3) in mat4 instanceTransform;



uniform mat4 uView;
uniform mat4 uProjection;

out vec2 uv;
out vec3 normal;
out vec3 pos;

void main () {
    uv = aTex;
    gl_Position = uProjection * uView * instanceTransform * vec4 (aPos, 1.0);
    normal =  (instanceTransform *vec4(aNorm,0)).xyz ;
    pos = (instanceTransform * vec4(aPos,1)).xyz;
}

//type fragment
#version 460 core




in vec2 uv;
in vec3 normal;
in vec3 pos;


uniform sampler2D textureSampler;
uniform float uTime;

out vec4 color;


vec3 sunPos = vec3 (10,10, 15);
vec3 suncolor = normalize (vec3 (255, 235, 179));
float dist = distance (pos,sunPos);
float lightpower = 100;




void main () {
    vec4 col = texture(textureSampler, uv);
    vec3 white = vec3 (1,1,1);
    float cosTheta = clamp(dot(normal,normalize(sunPos- pos)),0,1);
    color = vec4 ( normal * lightpower * cosTheta / (dist*dist),1);

}


