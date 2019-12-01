# version 330 core

struct Attenuation {
  float k;
  float l;
  float q;
};

struct PointLight {
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
  Attenuation attenuation;
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

float dotClamp(vec3 a, vec3 b) {
  return max(dot(a, b), 0.0);
}

float calcLightIntensity(Attenuation attenuation, float distance) {
  return 1 / (
      attenuation.k * pow(distance, 0) +
      attenuation.l * pow(distance, 1) +
      attenuation.q * pow(distance, 2));
}

vec3 calcPointLight(PointLight light, Material mat) {
  vec3 cameraDirection = normalize(cameraPosition - Position);
  float lightDistance = length(light.position - Position);
  vec3 lightDirection = normalize(light.position - Position);
  vec3 reflectionDirection = reflect(-lightDirection, Normal);

  vec3 ambient = light.ambient;
  vec3 diffuse = light.diffuse * dotClamp(Normal, lightDirection);
  vec3 specular = light.specular * pow(dotClamp(cameraDirection, reflectionDirection), mat.shininess);
  vec3 emission = vec3(1);

  ambient *= texture(mat.ambientMap, TexCoord).rgb;
  diffuse *= texture(mat.diffuseMap, TexCoord).rgb;
  specular *= texture(mat.specularMap, TexCoord).rgb;
  emission *= texture(mat.emissionMap, TexCoord).rgb;

  float intensity = calcLightIntensity(light.attenuation, lightDistance);
  return intensity * (ambient + diffuse + specular + emission);
}

void main() {
  float opacity = texture(material.diffuseMap, TexCoord).a;
  vec3 light = calcPointLight(light, material);
  FragColor = vec4(light, opacity);
}
