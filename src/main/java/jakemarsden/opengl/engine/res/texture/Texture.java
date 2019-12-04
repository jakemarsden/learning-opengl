package jakemarsden.opengl.engine.res.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Texture {

  final int id;
  private final int width;
  private final int height;
  private final int format;

  Texture(int id, int width, int height, int format) {
    this.id = id;
    this.width = width;
    this.height = height;
    this.format = format;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  /** @return one of {@code GL_RGB}, {@code GL_RGBA} */
  public int getFormat() {
    return format;
  }

  public void bindTo(int unit) {
    glActiveTexture(unit);
    glBindTexture(GL_TEXTURE_2D, this.id);
  }

  public void unbindFrom(int unit) {
    glActiveTexture(unit);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);
  }

  public abstract void destroy();

  @Override
  public abstract @NonNull String toString();

  @Override
  public int hashCode() {
    return this.id;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != this.getClass()) return false;
    final var obj = (Texture) o;
    return obj.id == this.id;
  }
}
