package jakemarsden.opengl.engine.shader;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL20.*;

import jakemarsden.opengl.engine.light.Attenuation;
import jakemarsden.opengl.engine.math.*;
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

  public void start() {
    glUseProgram(this.id);
  }

  public void stop() {
    glUseProgram(GL_NONE);
  }

  public void setUniformBool(@NonNull String name, boolean value) {
    this.setUniformInt(name, value ? GL_TRUE : GL_FALSE);
  }

  public void setUniformInt(@NonNull String name, int value) {
    glUniform1i(this.findUniform(name), value);
  }

  public void setUniformFloat(@NonNull String name, float value) {
    glUniform1f(this.findUniform(name), value);
  }

  public void setUniformVec2(@NonNull String name, @NonNull Vector2 value) {
    glUniform2f(this.findUniform(name), value.x, value.y);
  }

  public void setUniformVec3(@NonNull String name, @NonNull Vector3 value) {
    glUniform3f(this.findUniform(name), value.x, value.y, value.z);
  }

  public void setUniformVec3(@NonNull String name, @NonNull Color3 value) {
    glUniform3f(this.findUniform(name), value.r, value.g, value.b);
  }

  public void setUniformVec3(@NonNull String name, @NonNull Attenuation value) {
    glUniform3f(this.findUniform(name), value.k, value.l, value.q);
  }

  public void setUniformVec4(@NonNull String name, @NonNull Vector4 value) {
    glUniform4f(this.findUniform(name), value.x, value.y, value.z, value.w);
  }

  public void setUniformVec4(@NonNull String name, @NonNull Color4 value) {
    glUniform4f(this.findUniform(name), value.r, value.g, value.b, value.a);
  }

  public void setUniformMat4(@NonNull String name, @NonNull Matrix4 value) {
    glUniformMatrix4fv(this.findUniform(name), true, value.toArray());
  }

  /** @param value one of {@code GL_TEXTURE0}, {@code GL_TEXTURE1}, {@code GL_TEXTURE2}... */
  public void setUniformTexture(@NonNull String name, int value) {
    this.setUniformInt(name, value - GL_TEXTURE0);
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
