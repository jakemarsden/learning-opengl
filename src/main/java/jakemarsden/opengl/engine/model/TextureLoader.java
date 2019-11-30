package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.BufferUtils;

public final class TextureLoader {

  public static @NonNull Texture loadImage(
      @NonNull String fileName, @NonNull Class<?> clazz, boolean enableAlpha) {

    final BufferedImage img;
    try {
      img = TextureLoader.readFromClasspath(fileName, clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final int width = img.getWidth();
    final int height = img.getHeight();

    final var buf = TextureLoader.imageToRgbaBuffer(img, width, height, enableAlpha);
    return TextureLoader.load(buf, width, height, enableAlpha);
  }

  private static @NonNull Texture load(
      @NonNull ByteBuffer pixels, int width, int height, boolean enableAlpha) {

    final int id = glGenTextures();

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, id);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        enableAlpha ? GL_RGBA : GL_RGB,
        width,
        height,
        0,
        enableAlpha ? GL_RGBA : GL_RGB,
        GL_UNSIGNED_BYTE,
        pixels);
    glGenerateMipmap(GL_TEXTURE_2D);

    glBindTexture(GL_TEXTURE_2D, GL_NONE);

    return new Texture(id);
  }

  /**
   * Converts a {@link BufferedImage}, which stores pixels formatted as {@code ARGB}, into a {@link
   * ByteBuffer}, formatted as {@code RGBA} or {@code RGB}
   */
  private static @NonNull ByteBuffer imageToRgbaBuffer(
      @NonNull BufferedImage img, int width, int height, boolean useAlphaChannel) {

    final int channels = useAlphaChannel ? 4 : 3;
    final var buf = BufferUtils.createByteBuffer(width * height * channels);

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        final int argb = img.getRGB(x, y);
        final byte a = (byte) (0xff & (argb >> 24));
        final byte r = (byte) (0xff & (argb >> 16));
        final byte g = (byte) (0xff & (argb >> 8));
        final byte b = (byte) (0xff & argb);
        buf.put(r);
        buf.put(g);
        buf.put(b);
        if (useAlphaChannel) buf.put(a);
      }
    }

    buf.flip();
    return buf;
  }

  private static @NonNull BufferedImage readFromClasspath(
      @NonNull String name, @NonNull Class<?> clazz) throws IOException {

    final var in = clazz.getResourceAsStream(name);
    if (in == null)
      throw new FileNotFoundException(
          "Unable to find texture image: " + clazz.getPackageName() + "/" + name);

    try {
      return ImageIO.read(in);
    } catch (IOException e) {
      throw new IOException(
          "Unable to read texture image: " + clazz.getPackageName() + "/" + name, e);
    }
  }

  private TextureLoader() {
    throw new UnsupportedOperationException();
  }
}
