package jakemarsden.opengl.engine.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class StaticMesh implements Mesh {

  private static final int ATTRIB_VERTICES = 0;
  private static final int ATTRIB_TEX_COORDS = 1;

  /** Vertex Array Object ID */
  private final int vaoId;
  /** Element Buffer Object ID (holds indices) */
  private final int eboId;
  /** Vertex Buffer Object ID (holds vertices) */
  private final int vboId;
  /** Texture coordinate Buffer Object ID (holds texture coords) */
  private final int tboId;

  private final int type;
  private final int size;

  public static @NonNull StaticMesh triangles(
      short[] indices, float[] vertices, float[] texCoords) {
    return StaticMesh.load(indices, vertices, texCoords, GL_TRIANGLES);
  }

  /** @param type one of {@code GL_TRIANGLES}, {@code GL_TRIANGLE_STRIPS}, {@code GL_LINES}... */
  private static @NonNull StaticMesh load(
      short[] indices, float[] vertices, float[] texCoords, int type) {

    final int vaoId = glGenVertexArrays();
    glBindVertexArray(vaoId);

    final int vboId = StaticMesh.loadArrayBuffer(ATTRIB_VERTICES, 3, vertices);
    final int tboId = StaticMesh.loadArrayBuffer(ATTRIB_TEX_COORDS, 2, texCoords);

    glBindVertexArray(GL_NONE);

    final int eboId = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);

    return new StaticMesh(vaoId, eboId, vboId, tboId, type, indices.length);
  }

  private static int loadArrayBuffer(int attribIndex, int dimensions, float[] data) {
    final int id = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, id);
    glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    glVertexAttribPointer(attribIndex, dimensions, GL_FLOAT, false, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
    return id;
  }

  /** @param type one of {@code GL_TRIANGLES}, {@code GL_TRIANGLE_STRIPS}, {@code GL_LINES}... */
  private StaticMesh(int vaoId, int eboId, int vboId, int tboId, int type, int size) {
    this.vaoId = vaoId;
    this.eboId = eboId;
    this.vboId = vboId;
    this.tboId = tboId;
    this.type = type;
    this.size = size;
  }

  @Override
  public void destroy() {
    glDeleteBuffers(this.eboId);
    glDeleteBuffers(this.tboId);
    glDeleteBuffers(this.vboId);
    glDeleteVertexArrays(this.vaoId);
  }

  @Override
  public void bind() {
    glBindVertexArray(this.vaoId);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId);
    glEnableVertexAttribArray(ATTRIB_VERTICES);
    glEnableVertexAttribArray(ATTRIB_TEX_COORDS);
  }

  @Override
  public void unbind() {
    glDisableVertexAttribArray(ATTRIB_TEX_COORDS);
    glDisableVertexAttribArray(ATTRIB_VERTICES);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);
    glBindVertexArray(GL_NONE);
  }

  @Override
  public void draw() {
    glDrawElements(this.type, this.size, GL_UNSIGNED_SHORT, 0);
  }
}
