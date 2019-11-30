package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL13.*;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class Material {

  private static final int TEX_UNIT_DIFFUSE = GL_TEXTURE2;

  public final @NonNull Texture diffuse;
  public final int diffuseTexUnit;

  public static @NonNull Material of(@NonNull Texture diffuse) {
    return new Material(diffuse);
  }

  private Material(@NonNull Texture diffuse) {
    this.diffuse = diffuse;
    this.diffuseTexUnit = TEX_UNIT_DIFFUSE;
  }

  public void bind() {
    this.diffuse.bindTo(this.diffuseTexUnit);
  }

  public void unbind() {
    this.diffuse.unbindFrom(this.diffuseTexUnit);
  }

  public void destroy() {
    this.diffuse.destroy();
  }
}
