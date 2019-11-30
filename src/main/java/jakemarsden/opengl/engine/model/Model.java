package jakemarsden.opengl.engine.model;

import jakemarsden.opengl.engine.shader.Shader;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Model {

  private final @NonNull Mesh @NonNull [] meshes;

  Model(@NonNull Mesh @NonNull [] meshes) {
    this.meshes = meshes;
  }

  public void destroy() {
    for (final var mesh : this.meshes) mesh.destroy();
  }

  public void draw(@NonNull Shader shader) {
    for (final var mesh : this.meshes) {
      mesh.bind();
      mesh.draw(shader);
      mesh.unbind();
    }
  }
}
