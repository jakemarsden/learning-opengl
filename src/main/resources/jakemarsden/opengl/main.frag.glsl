# version 330 core

/**
 * Maximum number of point lights allowed per frame. Name and value must match the shader class'
 * corresponding constant
 */
# define MAX_POINT_LIGHTS 4

struct Attenuation {
  float k;
  float l;
  float q;
};

struct PointLight {
  bool enabled;
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
uniform Material material;
uniform PointLight[MAX_POINT_LIGHTS] pointLights;

float dotClamp(vec3 a, vec3 b) {
  return max(dot(a, b), 0.0);
}

float calcLightIntensity(Attenuation attenuation, float distance) {
  float invIntensity = 0;
  invIntensity += attenuation.k;
  invIntensity += attenuation.l * distance;
  invIntensity += attenuation.q * pow(distance, 2);
  return 1 / invIntensity;
}

vec3 calcPointLight(PointLight light, Material mat) {
  if (!light.enabled) return vec3(0);

  vec3 cameraDirection = normalize(cameraPosition - Position);
  float lightDistance = length(light.position - Position);
  vec3 lightDirection = normalize(light.position - Position);
  vec3 reflectionDirection = reflect(-lightDirection, Normal);

  vec3 ambient = texture(mat.ambientMap, TexCoord).rgb;
  vec3 diffuse = texture(mat.diffuseMap, TexCoord).rgb;
  vec3 specular = texture(mat.specularMap, TexCoord).rgb;
  vec3 emission = texture(mat.emissionMap, TexCoord).rgb;

  ambient *= light.ambient;
  diffuse *= light.diffuse * dotClamp(Normal, lightDirection);
  specular *= light.specular * pow(dotClamp(cameraDirection, reflectionDirection), mat.shininess);

  float intensity = calcLightIntensity(light.attenuation, lightDistance);
  return intensity * (ambient + diffuse + specular + emission);
}

void main() {
  float opacity = texture(material.diffuseMap, TexCoord).a;

  vec3 light = vec3(0);
  for (int i = 0; i < MAX_POINT_LIGHTS; i++) light += calcPointLight(pointLights[i], material);
  FragColor = vec4(light, opacity);
}
