package jakemarsden.opengl.engine.res.texture;

import jakemarsden.opengl.engine.math.Color4;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ColorTexture extends Texture {

  private final @NonNull TextureLoader loader;

  final @NonNull Color4 color;

  ColorTexture(
      int id,
      int format,
      int width,
      int height,
      @NonNull Color4 color,
      @NonNull TextureLoader loader) {

    super(id, width, height, format);
    this.color = color;
    this.loader = loader;
  }

  @Override
  public void destroy() {
    this.loader.destroyColor(this);
  }

  @Override
  public @NonNull String toString() {
    return "ColorTexture{" + this.color + "}";
  }
}
