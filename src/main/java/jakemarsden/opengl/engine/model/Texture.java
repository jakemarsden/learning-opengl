package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public final class Texture {

  private final int id;

  Texture(int id) {
    this.id = id;
  }

  public void bindTo(int unit) {
    glActiveTexture(unit);
    glBindTexture(GL_TEXTURE_2D, this.id);
  }

  public void unbindFrom(int unit) {
    glActiveTexture(unit);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);
  }

  public void destroy() {
    glDeleteTextures(this.id);
  }
}
