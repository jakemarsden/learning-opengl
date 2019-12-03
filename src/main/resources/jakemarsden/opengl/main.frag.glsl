# version 330 core

/**
 * Maximum number of point lights allowed in one frame. Must match the shader class' corresponding
 * constant
 */
# define MAX_POINT_LIGHTS 4
/**
 * Maximum number of spotlights allowed in one frame. Must match the shader class' corresponding
 * constant
 */
# define MAX_SPOT_LIGHTS 4

struct DirectionalLight {
  vec3 direction;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
};

struct PointLight {
  vec3 position;
  vec3 attenuation;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
};

struct SpotLight {
  vec3 position;
  vec3 direction;
  float aperture;
  float outerAperture;
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
uniform Material material;
uniform DirectionalLight directionalLight;
uniform PointLight[MAX_POINT_LIGHTS] pointLights;
uniform SpotLight[MAX_SPOT_LIGHTS] spotLights;

float clampedDot(vec3 a, vec3 b) {
  return max(dot(a, b), 0.0);
}

float calcLightIntensity(vec3 attn, float distance) {
  return 1 / (attn.x + attn.y * distance + attn.z * pow(distance, 2));
}

vec3 calcDirectionalLight(vec3 cameraDir, vec3 matAmb, vec3 matDiff, vec3 matSpec, float matShiny) {
  DirectionalLight light = directionalLight;

  vec3 lightDir = -light.direction;
  vec3 reflectionDir = reflect(-lightDir, Normal);

  vec3 amb = matAmb * light.ambient;
  vec3 diff = matDiff * light.diffuse * clampedDot(Normal, lightDir);
  vec3 spec = matSpec * light.specular * pow(clampedDot(cameraDir, reflectionDir), matShiny);
  return amb + diff + spec;
}

vec3 calcPointLight(vec3 cameraDir, vec3 matAmb, vec3 matDiff, vec3 matSpec, float matShiny) {
  vec3 total;
  for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
    PointLight light = pointLights[i];

    vec3 lightDir = normalize(light.position - Position);
    vec3 reflectionDir = reflect(-lightDir, Normal);
    float lightDist = length(light.position - Position);
    float lightIntensity = calcLightIntensity(light.attenuation, lightDist);

    vec3 amb = matAmb * light.ambient;
    vec3 diff = matDiff * light.diffuse * clampedDot(Normal, lightDir);
    vec3 spec = matSpec * light.specular * pow(clampedDot(cameraDir, reflectionDir), matShiny);
    total += lightIntensity * (amb + diff + spec);
  }
  return total;
}

vec3 calcSpotLight(vec3 cameraDir, vec3 matAmb, vec3 matDiff, vec3 matSpec, float matShiny) {
  vec3 total;
  for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
    SpotLight light = spotLights[i];

    vec3 lightDir = normalize(light.position - Position);
    vec3 reflectionDir = reflect(-lightDir, Normal);

    // phi = angle between a cone's axis and its sides
    // aperture = angle between a cone's sides
    float cosInnerPhi = cos(light.aperture / 2);
    float cosOuterPhi = cos(light.outerAperture / 2);
    // 100% intensity within `aperture`
    // diminishing intensity between `aperture` and `outerAperture`
    // zero intensity beyond `outerAperture`
    float theta = dot(lightDir, -light.direction);
    float intensity = clamp((theta - cosOuterPhi) / (cosInnerPhi - cosOuterPhi), 0, 1);

    vec3 amb = matAmb * light.ambient;
    vec3 diff = intensity * matDiff * light.diffuse * clampedDot(Normal, lightDir);
    vec3 spec = intensity * matSpec * light.specular * pow(clampedDot(cameraDir, reflectionDir), matShiny);
    total += amb + diff + spec;
  }
  return total;
}

void main() {
  vec3 matAmb = texture(material.ambientMap, TexCoord).rgb;
  vec3 matDiff = texture(material.diffuseMap, TexCoord).rgb;
  vec3 matSpec = texture(material.specularMap, TexCoord).rgb;
  vec3 matEmis = texture(material.emissionMap, TexCoord).rgb;
  float matOpacity = texture(material.diffuseMap, TexCoord).a;

  vec3 cameraDir = normalize(cameraPosition - Position);
  vec3 totalLight;
  totalLight += calcDirectionalLight(cameraDir, matAmb, matDiff, matSpec, material.shininess);
  totalLight += calcPointLight(cameraDir, matAmb, matDiff, matSpec, material.shininess);
  totalLight += calcSpotLight(cameraDir, matAmb, matDiff, matSpec, material.shininess);
  totalLight += matEmis;

  FragColor = vec4(totalLight, matOpacity);
}
