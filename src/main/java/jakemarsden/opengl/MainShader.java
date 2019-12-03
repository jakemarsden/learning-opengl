package jakemarsden.opengl;

import static java.lang.Math.min;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;

import jakemarsden.opengl.engine.light.DirectionalLight;
import jakemarsden.opengl.engine.light.Light;
import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.light.SpotLight;
import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Material;
import jakemarsden.opengl.engine.shader.Shader;
import jakemarsden.opengl.engine.shader.ShaderProgram;
import jakemarsden.opengl.engine.shader.ShaderProgramLoader;
import java.io.IOException;
import java.util.stream.IntStream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.fissore.slf4j.FluentLogger;

final class MainShader implements Shader {

  private static final FluentLogger LOGGER = getLogger(MainShader.class);
  private static final String NAME = "main";

  /**
   * Maximum number of point lights allowed in one frame. Must match the shader program's
   * corresponding pre-processor directive
   */
  private static final int MAX_POINT_LIGHTS = 4;
  /**
   * Maximum number of spotlights allowed in one frame. Must match the shader program's
   * corresponding pre-processor directive
   */
  private static final int MAX_SPOT_LIGHTS = 4;

  private static final String UNIFORM_CAMERA_POSITION = "cameraPosition";
  private static final String UNIFORM_CAMERA_TRANSFORM = "cameraTransform";
  private static final String UNIFORM_MODEL_TRANSFORM = "modelTransform";
  private static final String UNIFORM_MODEL_TRANSFORM_INVERSE = "invModelTransform";

  private static final String UNIFORM_DIRECTIONAL_LIGHT = "directionalLight";
  private static final String UNIFORM_DIRECTIONAL_LIGHT_DIRECTION = "directionalLight.direction";

  private static final String[] UNIFORM_POINT_LIGHT;
  private static final String[] UNIFORM_POINT_LIGHT_POSITION;
  private static final String[] UNIFORM_POINT_LIGHT_ATTENUATION;

  static {
    UNIFORM_POINT_LIGHT = arrUniNames("pointLights[%d]", MAX_POINT_LIGHTS);
    UNIFORM_POINT_LIGHT_POSITION = arrUniNames("pointLights[%d].position", MAX_POINT_LIGHTS);
    UNIFORM_POINT_LIGHT_ATTENUATION = arrUniNames("pointLights[%d].attenuation", MAX_POINT_LIGHTS);
  }

  private static final String[] UNIFORM_SPOT_LIGHT;
  private static final String[] UNIFORM_SPOT_LIGHT_POSITION;
  private static final String[] UNIFORM_SPOT_LIGHT_DIRECTION;
  private static final String[] UNIFORM_SPOT_LIGHT_APERTURE;
  private static final String[] UNIFORM_SPOT_LIGHT_OUTER_APERTURE;

  static {
    UNIFORM_SPOT_LIGHT = arrUniNames("spotLights[%d]", MAX_SPOT_LIGHTS);
    UNIFORM_SPOT_LIGHT_POSITION = arrUniNames("spotLights[%d].position", MAX_SPOT_LIGHTS);
    UNIFORM_SPOT_LIGHT_DIRECTION = arrUniNames("spotLights[%d].direction", MAX_SPOT_LIGHTS);
    UNIFORM_SPOT_LIGHT_APERTURE = arrUniNames("spotLights[%d].aperture", MAX_SPOT_LIGHTS);
    UNIFORM_SPOT_LIGHT_OUTER_APERTURE =
        arrUniNames("spotLights[%d].outerAperture", MAX_SPOT_LIGHTS);
  }

  private static final String UNIFORM_MATERIAL_AMBIENT = "material.ambientMap";
  private static final String UNIFORM_MATERIAL_DIFFUSE = "material.diffuseMap";
  private static final String UNIFORM_MATERIAL_SPECULAR = "material.specularMap";
  private static final String UNIFORM_MATERIAL_EMISSION = "material.emissionMap";
  private static final String UNIFORM_MATERIAL_SHININESS = "material.shininess";

  private static @NonNull String @NonNull [] arrUniNames(@NonNull String format, int count) {
    return IntStream.range(0, count)
        .mapToObj(idx -> String.format(format, idx))
        .toArray(String[]::new);
  }

  private final ShaderProgram prog;

  MainShader() {
    try {
      this.prog = ShaderProgramLoader.load(NAME, MainShader.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte getMaxSupportedPointLights() {
    return MAX_POINT_LIGHTS;
  }

  @Override
  public void setCameraPosition(@NonNull Vector3 pos) {
    this.prog.setUniformVec3(UNIFORM_CAMERA_POSITION, pos);
  }

  @Override
  public void setCameraTransform(@NonNull Matrix4 camera) {
    this.prog.setUniformMat4(UNIFORM_CAMERA_TRANSFORM, camera);
  }

  @Override
  public void setModelTransform(@NonNull Matrix4 model) {
    this.prog.setUniformMat4(UNIFORM_MODEL_TRANSFORM, model);
    this.prog.setUniformMat4(UNIFORM_MODEL_TRANSFORM_INVERSE, model.invert().transpose());
  }

  @Override
  public void setDirectionalLight(@NonNull DirectionalLight light) {
    this.prog.setUniformVec3(UNIFORM_DIRECTIONAL_LIGHT_DIRECTION, light.getDirection());
    this.setLight(UNIFORM_DIRECTIONAL_LIGHT, light);
  }

  @Override
  public void setPointLights(@NonNull PointLight @NonNull [] lights) {
    if (lights.length > MAX_POINT_LIGHTS) {
      final var msgFormat =
          "Exceeded maximum number of point lights, expected: <={} but was: {}. Some won't have a "
              + "visual effect, consider using only the {} closest or increase the limit";
      LOGGER
          .warn()
          .every(60, SECONDS)
          .log(msgFormat, MAX_POINT_LIGHTS, lights.length, MAX_POINT_LIGHTS);
    }
    int idx = 0;
    for (; idx < min(lights.length, MAX_POINT_LIGHTS); idx++) this.setPointLight(idx, lights[idx]);
    for (; idx < MAX_POINT_LIGHTS; idx++) this.unsetPointLight(idx);
  }

  private void setPointLight(int idx, @NonNull PointLight light) {
    this.prog.setUniformVec3(UNIFORM_POINT_LIGHT_POSITION[idx], light.getPosition());
    this.prog.setUniformVec3(UNIFORM_POINT_LIGHT_ATTENUATION[idx], light.getAttenuation());
    this.setLight(UNIFORM_POINT_LIGHT[idx], light);
  }

  private void unsetPointLight(int idx) {
    this.unsetLight(UNIFORM_POINT_LIGHT[idx]);
  }

  @Override
  public void setSpotLights(@NonNull SpotLight @NonNull [] lights) {
    if (lights.length > MAX_SPOT_LIGHTS) {
      final var msgFormat =
          "Exceeded maximum number of spot lights, expected: <={} but was: {}. Some won't have a "
              + "visual effect, consider using only the {} closest or increase the limit";
      LOGGER
          .warn()
          .every(60, SECONDS)
          .log(msgFormat, MAX_SPOT_LIGHTS, lights.length, MAX_SPOT_LIGHTS);
    }
    int idx = 0;
    for (; idx < min(lights.length, MAX_SPOT_LIGHTS); idx++) this.setSpotLight(idx, lights[idx]);
    for (; idx < MAX_SPOT_LIGHTS; idx++) this.unsetSpotLight(idx);
  }

  private void setSpotLight(int idx, @NonNull SpotLight light) {
    this.prog.setUniformVec3(UNIFORM_SPOT_LIGHT_POSITION[idx], light.getPosition());
    this.prog.setUniformVec3(UNIFORM_SPOT_LIGHT_DIRECTION[idx], light.getDirection());
    this.prog.setUniformFloat(UNIFORM_SPOT_LIGHT_APERTURE[idx], light.getAperture());
    this.prog.setUniformFloat(UNIFORM_SPOT_LIGHT_OUTER_APERTURE[idx], light.getOuterAperture());
    this.setLight(UNIFORM_SPOT_LIGHT[idx], light);
  }

  private void unsetSpotLight(int idx) {
    this.unsetLight(UNIFORM_SPOT_LIGHT[idx]);
  }

  private void setLight(@NonNull String prefix, @NonNull Light light) {
    this.prog.setUniformVec3(prefix + ".ambient", light.getAmbient());
    this.prog.setUniformVec3(prefix + ".diffuse", light.getDiffuse());
    this.prog.setUniformVec3(prefix + ".specular", light.getSpecular());
  }

  private void unsetLight(@NonNull String prefix) {
    // disable by making it emit nothing
    this.prog.setUniformVec3(prefix + ".ambient", Color3.black());
    this.prog.setUniformVec3(prefix + ".diffuse", Color3.black());
    this.prog.setUniformVec3(prefix + ".specular", Color3.black());
  }

  @Override
  public void setMaterial(@NonNull Material mat) {
    this.prog.setUniformTexture(UNIFORM_MATERIAL_AMBIENT, mat.ambientMapTexUnit);
    this.prog.setUniformTexture(UNIFORM_MATERIAL_DIFFUSE, mat.diffuseMapTexUnit);
    this.prog.setUniformTexture(UNIFORM_MATERIAL_SPECULAR, mat.specularMapTexUnit);
    this.prog.setUniformTexture(UNIFORM_MATERIAL_EMISSION, mat.emissionMapTexUnit);
    this.prog.setUniformFloat(UNIFORM_MATERIAL_SHININESS, mat.shininess);
  }

  @Override
  public void start() {
    this.prog.start();
  }

  @Override
  public void stop() {
    this.prog.stop();
  }

  @Override
  public void destroy() {
    this.prog.destroy();
  }
}
