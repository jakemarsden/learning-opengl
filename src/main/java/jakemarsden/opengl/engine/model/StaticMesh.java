package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import jakemarsden.opengl.engine.shader.Shader;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class StaticMesh implements Mesh {

  static final int ATTRIB_POSITION = 0;
  static final int ATTRIB_TEX_COORD = 1;

  private final int vao;
  private final int vbo;
  private final int tbo;
  private final int ebo;
  private final int size;

  private final int type;
  private final Material mat;

  /** @param type one of {@code GL_TRIANGLE_STRIP}, {@code GL_TRIANGLES}, {@code GL_POINTS}... */
  StaticMesh(int vao, int vbo, int tbo, int ebo, int size, int type, @NonNull Material mat) {

    this.vao = vao;
    this.vbo = vbo;
    this.tbo = tbo;
    this.ebo = ebo;
    this.size = size;

    this.type = type;
    this.mat = mat;
  }

  @Override
  public void destroy() {
    glDeleteBuffers(this.ebo);
    glDeleteBuffers(this.vbo);
    glDeleteBuffers(this.tbo);
    glDeleteVertexArrays(this.vao);

    this.mat.destroy();
  }

  @Override
  public void bind() {
    glBindVertexArray(this.vao);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
    glEnableVertexAttribArray(StaticMesh.ATTRIB_POSITION);
    glEnableVertexAttribArray(StaticMesh.ATTRIB_TEX_COORD);

    this.mat.bind();
  }

  @Override
  public void unbind() {
    this.mat.unbind();

    glDisableVertexAttribArray(StaticMesh.ATTRIB_POSITION);
    glDisableVertexAttribArray(StaticMesh.ATTRIB_TEX_COORD);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_NONE);
    glBindVertexArray(GL_NONE);
  }

  @Override
  public void draw(@NonNull Shader shader) {
    shader.setMaterial(this.mat);
    glDrawElements(this.type, this.size, GL_UNSIGNED_SHORT, 0);
  }
}
