package jakemarsden.opengl.engine.res.texture;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Color4;
import jakemarsden.opengl.engine.res.ResourceLoader;
import jakemarsden.opengl.engine.util.CountedRef;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class TextureLoader {

  /** The texture unit to use for loading textures */
  private static final int TEXTURE_UNIT = GL_TEXTURE15;

  private static final int COLOR_TEXTURE_WIDTH = 4;
  private static final int COLOR_TEXTURE_HEIGHT = 4;

  private final @NonNull Map<String, CountedRef<ImageTexture>> imageCache = new HashMap<>();
  private final @NonNull Map<Color4, CountedRef<ColorTexture>> colorCache = new HashMap<>();

  private final @NonNull ResourceLoader resLoader;

  public static @NonNull TextureLoader create(@NonNull ResourceLoader resLoader) {
    return new TextureLoader(resLoader);
  }

  private TextureLoader(@NonNull ResourceLoader resLoader) {
    this.resLoader = resLoader;
  }

  public @NonNull ImageTexture loadImage(@NonNull String name) {
    return loadImage(name, false);
  }

  public @NonNull ImageTexture loadImage(@NonNull String name, boolean useAlpha) {
    return this.imageCache
        .computeIfAbsent(name, it -> CountedRef.create(this.loadImageTexture(name, useAlpha)))
        .takeRef();
  }

  public @NonNull ColorTexture loadColor(@NonNull Color3 color) {
    return this.loadColor(Color4.rgba(color, 1), false);
  }

  public @NonNull ColorTexture loadColor(@NonNull Color4 color) {
    return this.loadColor(color, color.a != 1);
  }

  private @NonNull ColorTexture loadColor(@NonNull Color4 color, boolean useAlpha) {
    return this.colorCache
        .computeIfAbsent(color, it -> CountedRef.create(this.loadColorTexture(color, useAlpha)))
        .takeRef();
  }

  void destroyImage(@NonNull ImageTexture tex) {
    this.imageCache.compute(
        tex.path,
        (path, countedTex) -> {
          if (countedTex == null) throw new IllegalStateException();
          final var shouldDestroy = countedTex.returnRef(tex);
          if (shouldDestroy) this.unloadTexture(tex);
          return shouldDestroy ? null : countedTex;
        });
  }

  void destroyColor(@NonNull ColorTexture tex) {
    this.colorCache.compute(
        tex.color,
        (color, countedTex) -> {
          if (countedTex == null) throw new IllegalStateException();
          final var shouldDestroy = countedTex.returnRef(tex);
          if (shouldDestroy) this.unloadTexture(tex);
          return shouldDestroy ? null : countedTex;
        });
  }

  private @NonNull ImageTexture loadImageTexture(@NonNull String name, boolean useAlpha) {
    final BufferedImage img;
    try {
      img = this.resLoader.loadImageResource(name);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load texture: \"" + name + "\"", e);
    }

    final var width = img.getWidth();
    final var height = img.getHeight();
    final var format = useAlpha ? GL_RGBA : GL_RGB;

    final var pixBuf = this.asRgbaPixelBuffer(img, width, height, useAlpha);
    final var texId = this.loadPixels(pixBuf, width, height, format);
    return new ImageTexture(texId, format, width, height, name, this);
  }

  private @NonNull ColorTexture loadColorTexture(@NonNull Color4 color, boolean useAlpha) {
    final var width = COLOR_TEXTURE_WIDTH;
    final var height = COLOR_TEXTURE_HEIGHT;
    final var format = useAlpha ? GL_RGBA : GL_RGB;

    final var pixBuf = this.asRgbaPixelBuffer(color, width, height, useAlpha);
    final var texId = this.loadPixels(pixBuf, width, height, format);
    return new ColorTexture(texId, format, width, height, color, this);
  }

  private void unloadTexture(@NonNull Texture tex) {
    this.unloadPixels(tex.id);
  }

  /** @param format one of {@code GL_RGB}, {@code GL_RGBA}... */
  private int loadPixels(@NonNull ByteBuffer pixBuf, int width, int height, int format) {

    final int texId = glGenTextures();
    glActiveTexture(TEXTURE_UNIT);
    glBindTexture(GL_TEXTURE_2D, texId);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, pixBuf);
    glGenerateMipmap(GL_TEXTURE_2D);

    glBindTexture(GL_TEXTURE_2D, GL_NONE);
    return texId;
  }

  private void unloadPixels(int texId) {
    glDeleteTextures(texId);
  }

  private @NonNull ByteBuffer asRgbaPixelBuffer(
      @NonNull BufferedImage img, int width, int height, boolean useAlpha) {

    final var pixSize = useAlpha ? 4 : 3;
    final var pixBuf = createByteBuffer(width * height * pixSize);

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        final var pix = img.getRGB(x, y);

        final var a = (byte) (0xff & (pix >> 24));
        final var r = (byte) (0xff & (pix >> 16));
        final var g = (byte) (0xff & (pix >> 8));
        final var b = (byte) (0xff & pix);

        pixBuf.put(r);
        pixBuf.put(g);
        pixBuf.put(b);
        if (useAlpha) pixBuf.put(a);
      }
    }

    pixBuf.flip();
    return pixBuf;
  }

  private @NonNull ByteBuffer asRgbaPixelBuffer(
      @NonNull Color4 color, int width, int height, boolean useAlpha) {

    final var pixSize = useAlpha ? 4 : 3;
    final var pix = new byte[pixSize];
    pix[0] = (byte) (0xff * color.r);
    pix[1] = (byte) (0xff * color.g);
    pix[2] = (byte) (0xff * color.b);
    if (useAlpha) pix[3] = (byte) (0xff * color.a);

    final var pixBuf = createByteBuffer(width * height * pixSize);
    for (var idx = 0; idx < width * height; idx++) pixBuf.put(pix);

    pixBuf.flip();
    return pixBuf;
  }
}
