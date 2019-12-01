package jakemarsden.opengl.engine.model;

import jakemarsden.opengl.engine.math.Vector2;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Vertex {

  public final @NonNull Vector3 position;
  public final @NonNull Vector3 normal;
  public final @NonNull Vector2 texCoord;

  public static @NonNull Vertex of(
      @NonNull Vector3 position, @NonNull Vector3 normal, @NonNull Vector2 texCoord) {

    return new Vertex(position, normal, texCoord);
  }

  private Vertex(@NonNull Vector3 position, @NonNull Vector3 normal, @NonNull Vector2 texCoord) {
    this.position = position;
    this.normal = normal;
    this.texCoord = texCoord;
  }
}
