package jakemarsden.opengl;

import static java.lang.Math.min;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;

import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Material;
import jakemarsden.opengl.engine.shader.Shader;
import jakemarsden.opengl.engine.shader.ShaderProgram;
import jakemarsden.opengl.engine.shader.ShaderProgramLoader;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fissore.slf4j.FluentLogger;

final class MainShader implements Shader {

  private static final FluentLogger LOGGER = getLogger(MainShader.class);
  private static final String NAME = "main";

  /**
   * Maximum number of point lights allowed per frame. Name and value must match the shader
   * program's corresponding pre-processor directive
   */
  public static final int MAX_POINT_LIGHTS = 4;

  private static final String UNIFORM_CAMERA_POSITION = "cameraPosition";
  private static final String UNIFORM_CAMERA_TRANSFORM = "cameraTransform";
  private static final String UNIFORM_MODEL_TRANSFORM = "modelTransform";
  private static final String UNIFORM_MODEL_TRANSFORM_INVERSE = "invModelTransform";

  private static final String UNIFORM_POINT_LIGHT_ENABLED = "pointLights[%d].enabled";
  private static final String UNIFORM_POINT_LIGHT_POSITION = "pointLights[%d].position";
  private static final String UNIFORM_POINT_LIGHT_AMBIENT = "pointLights[%d].ambient";
  private static final String UNIFORM_POINT_LIGHT_DIFFUSE = "pointLights[%d].diffuse";
  private static final String UNIFORM_POINT_LIGHT_SPECULAR = "pointLights[%d].specular";
  private static final String UNIFORM_POINT_LIGHT_ATTN_K = "pointLights[%d].attenuation.k";
  private static final String UNIFORM_POINT_LIGHT_ATTN_L = "pointLights[%d].attenuation.l";
  private static final String UNIFORM_POINT_LIGHT_ATTN_Q = "pointLights[%d].attenuation.q";

  private static final String UNIFORM_MATERIAL_AMBIENT = "material.ambientMap";
  private static final String UNIFORM_MATERIAL_DIFFUSE = "material.diffuseMap";
  private static final String UNIFORM_MATERIAL_SPECULAR = "material.specularMap";
  private static final String UNIFORM_MATERIAL_EMISSION = "material.emissionMap";
  private static final String UNIFORM_MATERIAL_SHININESS = "material.shininess";

  private final ShaderProgram prog;

  MainShader() {
    try {
      this.prog = ShaderProgramLoader.load(NAME, MainShader.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
  public void setPointLights(@Nullable PointLight @NonNull [] lights) {
    if (lights.length > MAX_POINT_LIGHTS) {
      LOGGER
          .warn()
          .every(60, SECONDS)
          .log(
              "Exceeded maximum number of point lights: expected <={} but was {}: some lights may not be rendered, consider increasing the limit or using less lights",
              MAX_POINT_LIGHTS,
              lights.length);
    }
    int idx = 0;
    for (; idx < min(lights.length, MAX_POINT_LIGHTS); idx++) this.setPointLight(idx, lights[idx]);
    for (; idx < MAX_POINT_LIGHTS; idx++) this.setPointLight(idx, null);
  }

  private void setPointLight(int idx, @Nullable PointLight light) {
    this.prog.setUniformBool(String.format(UNIFORM_POINT_LIGHT_ENABLED, idx), light != null);
    if (light == null) return;
    final var attn = light.getAttenuation();
    this.prog.setUniformVec3(String.format(UNIFORM_POINT_LIGHT_POSITION, idx), light.getPosition());
    this.prog.setUniformVec3(String.format(UNIFORM_POINT_LIGHT_AMBIENT, idx), light.getAmbient());
    this.prog.setUniformVec3(String.format(UNIFORM_POINT_LIGHT_DIFFUSE, idx), light.getDiffuse());
    this.prog.setUniformVec3(String.format(UNIFORM_POINT_LIGHT_SPECULAR, idx), light.getSpecular());
    this.prog.setUniformFloat(String.format(UNIFORM_POINT_LIGHT_ATTN_K, idx), attn.k);
    this.prog.setUniformFloat(String.format(UNIFORM_POINT_LIGHT_ATTN_L, idx), attn.l);
    this.prog.setUniformFloat(String.format(UNIFORM_POINT_LIGHT_ATTN_Q, idx), attn.q);
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
