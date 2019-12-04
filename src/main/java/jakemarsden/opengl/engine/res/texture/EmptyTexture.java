package jakemarsden.opengl.engine.res.texture;

import static org.lwjgl.opengl.GL11.GL_NONE;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EmptyTexture extends Texture {

  private static final EmptyTexture INSTANCE = new EmptyTexture();

  public static @NonNull EmptyTexture get() {
    return INSTANCE;
  }

  private EmptyTexture() {
    super(GL_NONE, 0, 0, 0);
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getFormat() {
    return 0;
  }

  @Override
  public void bindTo(int unit) {
    // no operation
  }

  @Override
  public void unbindFrom(int unit) {
    // no operation
  }

  @Override
  public void destroy() {
    // no operation
  }

  @Override
  public @NonNull String toString() {
    return "EmptyTexture{}";
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    return o.getClass() == this.getClass();
  }
}
