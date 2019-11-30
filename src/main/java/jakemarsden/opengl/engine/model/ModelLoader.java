package jakemarsden.opengl.engine.model;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ModelLoader {

  public static @NonNull Model load(@NonNull Mesh @NonNull [] meshes) {
    return new Model(meshes);
  }

  private ModelLoader() {
    throw new UnsupportedOperationException();
  }
}
