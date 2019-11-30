# version 330 core

struct Material {
  sampler2D diffuse;
};

in vec2 TexCoord;

out vec4 FragColor;

uniform Material material;

void main() {
  FragColor = texture(material.diffuse, TexCoord);
}
