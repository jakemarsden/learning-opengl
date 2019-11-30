package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class StaticMeshLoader {

  /** @param type one of {@code GL_TRIANGLE_STRIP}, {@code GL_TRIANGLES}, {@code GL_POINTS}... */
  public static StaticMesh load(
      int type,
      @NonNull Vertex @NonNull [] vertices,
      short @NonNull [] indices,
      @NonNull Material mat) {

    final var positions = new float[3 * vertices.length];
    for (var idx = 0; idx < vertices.length; idx++) {
      positions[0 + 3 * idx] = vertices[idx].position.x;
      positions[1 + 3 * idx] = vertices[idx].position.y;
      positions[2 + 3 * idx] = vertices[idx].position.z;
    }

    final var texCoords = new float[2 * vertices.length];
    for (var idx = 0; idx < vertices.length; idx++) {
      texCoords[0 + 2 * idx] = vertices[idx].texCoord.x;
      texCoords[1 + 2 * idx] = vertices[idx].texCoord.y;
    }

    return StaticMeshLoader.load(type, positions, texCoords, indices, mat);
  }

  /** @param type one of {@code GL_TRIANGLE_STRIP}, {@code GL_TRIANGLES}, {@code GL_POINTS}... */
  public static @NonNull StaticMesh load(
      int type,
      float @NonNull [] positions,
      float @NonNull [] texCoords,
      short @NonNull [] indices,
      @NonNull Material mat) {

    final var vao = glGenVertexArrays();
    final var vbo = glGenBuffers();
    final var tbo = glGenBuffers();
    final var ebo = glGenBuffers();

    glBindVertexArray(vao);
    StaticMeshLoader.populateAttribArrayBuffer(vbo, StaticMesh.ATTRIB_POSITION, positions, 3);
    StaticMeshLoader.populateAttribArrayBuffer(tbo, StaticMesh.ATTRIB_TEX_COORD, texCoords, 2);
    glBindVertexArray(GL_NONE);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);

    return new StaticMesh(vao, vbo, tbo, ebo, indices.length, type, mat);
  }

  private static void populateAttribArrayBuffer(
      int id, int attribIdx, float[] data, int dimensions) {

    glBindBuffer(GL_ARRAY_BUFFER, id);
    glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    glVertexAttribPointer(attribIdx, dimensions, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
  }

  private StaticMeshLoader() {
    throw new UnsupportedOperationException();
  }
}
