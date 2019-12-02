# version 330 core

/**
 * Maximum number of point lights allowed per frame. Name and value must match the shader class'
 * corresponding constant
 */
# define MAX_POINT_LIGHTS 8

struct PointLight {
  bool enabled;
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
  vec3 attenuation;
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

float clampedDot(vec3 a, vec3 b) {
  return max(dot(a, b), 0.0);
}

float calcLightIntensity(vec3 attn, float distance) {
  return 1 / (attn.x + attn.y * distance + attn.z * pow(distance, 2));
}

vec3 calcPointLight(vec3 pos, vec3 normal, vec3 cameraPos, vec3 matAmb, vec3 matDiff, vec3 matSpec, float matShiny) {

  vec3 cameraDir = normalize(cameraPos - pos);

  vec3 total;
  for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
    PointLight light = pointLights[i];
    if (!light.enabled) continue;

    float lightDist = length(light.position - pos);
    vec3 lightDir = normalize(light.position - pos);
    vec3 reflectionDir = reflect(-lightDir, normal);
    float intensity = calcLightIntensity(light.attenuation, lightDist);

    vec3 amb = matAmb * light.ambient;
    vec3 diff = matDiff * light.diffuse * clampedDot(normal, lightDir);
    vec3 spec = matSpec * light.specular * pow(clampedDot(cameraDir, reflectionDir), matShiny);
    total += intensity * (amb + diff + spec);
  }
  return total;
}

void main() {
  vec3 materialAmbient = texture(material.ambientMap, TexCoord).rgb;
  vec3 materialDiffuse = texture(material.diffuseMap, TexCoord).rgb;
  vec3 materialSpecular = texture(material.specularMap, TexCoord).rgb;
  vec3 materialEmission = texture(material.emissionMap, TexCoord).rgb;
  float materialOpacity = texture(material.diffuseMap, TexCoord).a;

  vec3 totalLight;
  totalLight += calcPointLight(Position, Normal, cameraPosition, materialAmbient, materialDiffuse, materialSpecular, material.shininess);
  totalLight += materialEmission;

  FragColor = vec4(totalLight, materialOpacity);
}
