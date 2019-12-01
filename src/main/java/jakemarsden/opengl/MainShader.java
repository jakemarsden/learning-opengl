package jakemarsden.opengl;

import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Material;
import jakemarsden.opengl.engine.shader.Shader;
import jakemarsden.opengl.engine.shader.ShaderProgram;
import jakemarsden.opengl.engine.shader.ShaderProgramLoader;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;

final class MainShader implements Shader {

  private static final String NAME = "main";

  private static final String UNIFORM_CAMERA_POSITION = "cameraPosition";
  private static final String UNIFORM_CAMERA_TRANSFORM = "cameraTransform";
  private static final String UNIFORM_MODEL_TRANSFORM = "modelTransform";
  private static final String UNIFORM_MODEL_TRANSFORM_INVERSE = "invModelTransform";

  private static final String UNIFORM_LIGHT_POSITION = "light.position";
  private static final String UNIFORM_LIGHT_AMBIENT = "light.ambient";
  private static final String UNIFORM_LIGHT_DIFFUSE = "light.diffuse";
  private static final String UNIFORM_LIGHT_SPECULAR = "light.specular";

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
  public void setLight(@NonNull Vector3 pos, @NonNull PointLight light) {
    this.prog.setUniformVec3(UNIFORM_LIGHT_POSITION, pos);
    this.prog.setUniformVec3(UNIFORM_LIGHT_AMBIENT, light.getAmbient());
    this.prog.setUniformVec3(UNIFORM_LIGHT_DIFFUSE, light.getDiffuse());
    this.prog.setUniformVec3(UNIFORM_LIGHT_SPECULAR, light.getSpecular());
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
