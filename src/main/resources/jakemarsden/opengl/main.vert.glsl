# version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 TexCoord;

uniform mat4 cameraTransform;
uniform mat4 modelTransform;

void main() {
  gl_Position = cameraTransform * modelTransform * vec4(position, 1.0);
  TexCoord = texCoord;
}
