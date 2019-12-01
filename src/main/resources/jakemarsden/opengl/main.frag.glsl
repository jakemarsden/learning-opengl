# version 330 core

struct PointLight {
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
};

struct Material {
  sampler2D ambientMap;
  sampler2D diffuseMap;
  sampler2D specularMap;
  sampler2D emissionMap;
  float shininess;
};

in vec3 Position;
in vec3 Normal;
in vec2 TexCoord;

out vec4 FragColor;

uniform vec3 cameraPosition;
uniform PointLight light;
uniform Material material;

void main() {
  vec3 cameraDirection = normalize(cameraPosition - Position);
  vec3 lightDirection = normalize(light.position - Position);
  vec3 reflectionDirection= reflect(-lightDirection, Normal);

  float opacity = texture(material.diffuseMap, TexCoord).a;

  vec3 ambientColor = texture(material.ambientMap, TexCoord).rgb;
  vec3 diffuseColor = texture(material.diffuseMap, TexCoord).rgb;
  vec3 specularColor = texture(material.specularMap, TexCoord).rgb;
  vec3 emissionColor = texture(material.emissionMap, TexCoord).rgb;

  vec3 ambient = light.ambient * ambientColor;
  vec3 diffuse = light.diffuse * diffuseColor * max(dot(Normal, lightDirection), 0.0);
  vec3 specular = light.specular * specularColor * pow(max(dot(cameraDirection, reflectionDirection), 0.0), material.shininess);
  vec3 emission = emissionColor;
  FragColor = vec4(ambient + diffuse + specular + emission, opacity);
}
