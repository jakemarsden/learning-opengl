package jakemarsden.opengl.engine.model;

import jakemarsden.opengl.engine.shader.Shader;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Mesh {

  void bind();

  void unbind();

  void draw(@NonNull Shader shader);

  void destroy();
}
