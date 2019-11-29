package jakemarsden.opengl.engine.shader;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.fissore.slf4j.FluentLogger;

public final class ShaderProgram {

  private final Map<String, Integer> uniformLocationCache = new HashMap<>();

  private final FluentLogger LOGGER;
  private final int id;

  ShaderProgram(int id, @NonNull String name) {
    this.LOGGER = getLogger(name);
    this.id = id;
  }

  public void setUniformInt(@NonNull String name, int value) {
    glUniform1i(this.findUniform(name), value);
  }

  /** @param value one of {@code GL_TEXTURE0}, {@code GL_TEXTURE1}, {@code GL_TEXTURE2}... */
  public void setUniformTexture(@NonNull String name, int value) {
    this.setUniformInt(name, value - GL_TEXTURE0);
  }

  public void start() {
    glUseProgram(this.id);
  }

  public void stop() {
    glUseProgram(GL_NONE);
  }

  public void destroy() {
    ShaderProgramLoader.destroy(this.id);
    this.uniformLocationCache.clear();
  }

  private int findUniform(@NonNull String name) {
    return this.uniformLocationCache.computeIfAbsent(name, this::calculateUniformLocation);
  }

  private int calculateUniformLocation(@NonNull String name) {
    final int loc = glGetUniformLocation(this.id, name);
    if (loc == -1) LOGGER.warn().log("No such uniform: " + name);
    return loc;
  }
}
