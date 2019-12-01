# version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texCoord;

out vec3 Position;
out vec3 Normal;
out vec2 TexCoord;

uniform mat4 cameraTransform;
uniform mat4 modelTransform;
uniform mat4 invModelTransform;

void main() {
  vec4 worldPosition = modelTransform * vec4(position, 1.0);
  vec3 worldNormal = normalize(mat3(invModelTransform) * normal);

  gl_Position = cameraTransform * worldPosition;

  Position = worldPosition.xyz;
  Normal = worldNormal;
  TexCoord = texCoord;
}
