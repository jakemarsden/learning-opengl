package jakemarsden.opengl;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.model.Material;
import jakemarsden.opengl.engine.shader.Shader;
import jakemarsden.opengl.engine.shader.ShaderProgram;
import jakemarsden.opengl.engine.shader.ShaderProgramLoader;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;

final class MainShader implements Shader {

  private static final String NAME = "main";

  private static final String UNIFORM_CAMERA_TRANSFORM = "cameraTransform";
  private static final String UNIFORM_MODEL_TRANSFORM = "modelTransform";
  private static final String UNIFORM_MATERIAL_DIFFUSE = "material.diffuse";

  private final ShaderProgram prog;

  MainShader() {
    try {
      this.prog = ShaderProgramLoader.load(NAME, MainShader.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setCameraTransform(@NonNull Matrix4 camera) {
    this.prog.setUniformMat4(UNIFORM_CAMERA_TRANSFORM, camera);
  }

  @Override
  public void setModelTransform(@NonNull Matrix4 model) {
    this.prog.setUniformMat4(UNIFORM_MODEL_TRANSFORM, model);
  }

  @Override
  public void setMaterial(@NonNull Material mat) {
    this.prog.setUniformTexture(UNIFORM_MATERIAL_DIFFUSE, mat.diffuseTexUnit);
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
