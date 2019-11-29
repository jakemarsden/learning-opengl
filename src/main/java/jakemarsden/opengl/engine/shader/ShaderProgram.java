package jakemarsden.opengl.engine.shader;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.fissore.slf4j.FluentLogger;

public final class ShaderProgram {

  private final FluentLogger LOGGER;
  private final int id;

  ShaderProgram(int id, @NonNull String name) {
    this.LOGGER = getLogger(name);
    this.id = id;
  }

  public void start() {
    glUseProgram(this.id);
  }

  public void stop() {
    glUseProgram(GL_NONE);
  }

  public void destroy() {
    ShaderProgramLoader.destroy(this.id);
  }
}
