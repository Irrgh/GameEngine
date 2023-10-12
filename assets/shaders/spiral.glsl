//type vertex
#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

out vec4 fColor;
out vec2 uv;

void main () {
    fColor =  aColor;
    uv = aPos.xy;
    gl_Position = vec4 (aPos, 1.0);
}

//type fragment
#version 330 core

in vec4 fColor;
in vec2 uv;
out vec4 color;

uniform float u_time;


float absSin(float x); // Function declaration
float smallerThan (float a, float b);


void main () {

    vec3 white = vec3(1,1,1);

    vec2 coords = uv;

    float d = distance(coords,vec2(0.0,0.0)) * 10;
    float angle = atan(coords.x / coords.y);
    float spiral =  (1- absSin((d + angle - u_time*5)) )  ;

    /*for (int i = 0; i < 4; i++) {

        d = distance(coords,vec2(0.0,0.0)) * 10;
        angle = atan(coords.x / coords.y);
        spiral +=  ( absSin((d + angle - u_time)*res) )  ;


        int res = res * 2 ;
        coords = vec2(mod(uv.x*res,1) - 0.5, mod(uv.y*res,1)-0.5);


    }
    */


    color = vec4 (fColor *  spiral * spiral * spiral);
}

float smallerThan (float a, float b) {
    if (a < b) {
        return 1;
    }
    return 0;
}


float absSin (float x) {
    return abs(sin(x));
}







