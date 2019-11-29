package jakemarsden.opengl.engine.mesh;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class StaticMeshLoader {

  public static @NonNull StaticMesh triangles(short[] indices, float[] vertices) {
    return StaticMeshLoader.load(indices, vertices, GL_TRIANGLES);
  }

  public static @NonNull StaticMesh triangleStrip(short[] indices, float[] vertices) {
    return StaticMeshLoader.load(indices, vertices, GL_TRIANGLE_STRIP);
  }

  /** @param type one of {@code GL_TRIANGLES}, {@code GL_TRIANGLE_STRIPS}, {@code GL_LINES}... */
  private static @NonNull StaticMesh load(short[] indices, float[] vertices, int type) {
    final int vaoId = glGenVertexArrays();
    glBindVertexArray(vaoId);

    final int vboId = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, vboId);
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    glVertexAttribPointer(StaticMesh.ATTRIB_VERTICES, 3, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);

    glBindVertexArray(GL_NONE);

    final int eboId = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);

    return new StaticMesh(vaoId, eboId, vboId, type, indices.length);
  }

  static void destroy(int vaoId, int eboId, int vboId) {
    glDeleteBuffers(eboId);
    glDeleteBuffers(vboId);
    glDeleteVertexArrays(vaoId);
  }

  private StaticMeshLoader() {
    throw new UnsupportedOperationException();
  }
}
