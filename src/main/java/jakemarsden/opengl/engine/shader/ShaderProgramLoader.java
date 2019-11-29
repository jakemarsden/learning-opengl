package jakemarsden.opengl.engine.shader;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL20.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.fissore.slf4j.FluentLogger;

public final class ShaderProgramLoader {

  private static final FluentLogger LOGGER = getLogger(ShaderProgramLoader.class);

  public static @NonNull ShaderProgram load(@NonNull String name, @NonNull Class<?> clazz)
      throws IOException {

    final var vertId = compile(GL_VERTEX_SHADER, name + ".vert.glsl", clazz);
    final var fragId = compile(GL_FRAGMENT_SHADER, name + ".frag.glsl", clazz);

    final var prettyName = clazz.getPackageName() + "." + name;
    final int id;
    try {
      id = link(vertId, fragId, prettyName);
    } finally {
      glDeleteShader(vertId);
      glDeleteShader(fragId);
    }
    return new ShaderProgram(id, prettyName);
  }

  static void destroy(int id) {
    glDeleteProgram(id);
  }

  private static int link(int vertId, int fragId, @NonNull String prettyName) throws IOException {
    final var id = glCreateProgram();
    glAttachShader(id, vertId);
    glAttachShader(id, fragId);

    glLinkProgram(id);
    if (glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE) {
      LOGGER.error().log(() -> glGetProgramInfoLog(id));
      throw new IOException("Shader linking failed: " + prettyName);
    }
    LOGGER.trace().log(() -> glGetProgramInfoLog(id));

    glValidateProgram(id);
    if (glGetProgrami(id, GL_VALIDATE_STATUS) != GL_TRUE) {
      LOGGER.error().log(() -> glGetProgramInfoLog(id));
      throw new IOException("Shader validation failed: " + prettyName);
    }
    LOGGER.trace().log(() -> glGetProgramInfoLog(id));

    return id;
  }

  private static int compile(int type, @NonNull String name, @NonNull Class<?> clazz)
      throws IOException {
    final var src = readFromClasspath(name, clazz);
    final var id = glCreateShader(type);
    glShaderSource(id, src);

    glCompileShader(id);
    if (glGetShaderi(id, GL_COMPILE_STATUS) != GL_TRUE) {
      LOGGER.error().log(() -> glGetShaderInfoLog(id));
      glDeleteShader(id);
      throw new IOException("Shader compilation failed: " + clazz.getPackageName() + "." + name);
    }
    LOGGER.trace().log(() -> glGetShaderInfoLog(id));

    return id;
  }

  private static @NonNull String readFromClasspath(@NonNull String name, @NonNull Class<?> clazz)
      throws IOException {

    final var in = clazz.getResourceAsStream(name);
    if (in == null)
      throw new FileNotFoundException(
          "Unable to find shader program: " + clazz.getPackageName() + "." + name);

    final byte[] data;
    try {
      data = in.readAllBytes();
    } catch (IOException e) {
      throw new IOException(
          "Unable to read shader program: " + clazz.getPackageName() + "." + name, e);
    }
    return new String(data);
  }

  private ShaderProgramLoader() {
    throw new UnsupportedOperationException();
  }
}
