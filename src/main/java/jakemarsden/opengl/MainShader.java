package jakemarsden.opengl;

import jakemarsden.opengl.engine.shader.Shader;
import jakemarsden.opengl.engine.shader.ShaderProgram;
import jakemarsden.opengl.engine.shader.ShaderProgramLoader;
import jakemarsden.opengl.engine.tex.Texture;
import java.io.IOException;

final class MainShader implements Shader {

  private static final String NAME = "main";

  private static final String UNIFORM_TEXTURE = "texSampler";

  private final ShaderProgram prog;

  MainShader() {
    try {
      this.prog = ShaderProgramLoader.load(NAME, MainShader.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setTexture(Texture tex) {
    this.prog.setUniformTexture(UNIFORM_TEXTURE, tex.getUnit());
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
