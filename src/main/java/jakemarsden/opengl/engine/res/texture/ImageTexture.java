package jakemarsden.opengl.engine.res.texture;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class ImageTexture extends Texture {

  private final @NonNull TextureLoader loader;

  final @NonNull String path;

  ImageTexture(
      int id,
      int format,
      int width,
      int height,
      @NonNull String path,
      @NonNull TextureLoader loader) {

    super(id, width, height, format);
    this.path = path;
    this.loader = loader;
  }

  @Override
  public void destroy() {
    this.loader.destroyImage(this);
  }

  @Override
  public @NonNull String toString() {
    return "ImageTexture{" + this.path + "}";
  }
}
