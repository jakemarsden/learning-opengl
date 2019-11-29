package jakemarsden.opengl.engine.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class StaticMesh implements Mesh {

  static final int ATTRIB_VERTICES = 0;

  private final int vaoId;
  private final int eboId;
  private final int vboId;
  private final int type;
  private final int size;

  /** @param type one of {@code GL_TRIANGLES}, {@code GL_TRIANGLE_STRIPS}, {@code GL_LINES}... */
  StaticMesh(int vaoId, int eboId, int vboId, int type, int size) {
    this.vaoId = vaoId;
    this.eboId = eboId;
    this.vboId = vboId;
    this.type = type;
    this.size = size;
  }

  @Override
  public void bind() {
    glBindVertexArray(this.vaoId);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId);
    glEnableVertexAttribArray(ATTRIB_VERTICES);
  }

  @Override
  public void unbind() {
    glDisableVertexAttribArray(ATTRIB_VERTICES);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);
    glBindVertexArray(GL_NONE);
  }

  @Override
  public void draw() {
    glDrawElements(this.type, this.size, GL_UNSIGNED_SHORT, 0);
  }

  @Override
  public void destroy() {
    StaticMeshLoader.destroy(this.vaoId, this.eboId, this.vboId);
  }
}
