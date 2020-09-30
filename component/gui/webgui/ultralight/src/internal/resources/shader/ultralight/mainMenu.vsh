#version 330 core

layout (location = 0) in vec3 inPos;
layout (location = 1) in vec2 inTexCoord;

out vec2 TexCoord;

void main()
{
    TexCoord = inTexCoord;
    gl_Position = vec4(inPos, 1.);
}