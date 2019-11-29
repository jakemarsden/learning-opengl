package jakemarsden.opengl.engine.tex;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.BufferUtils;

public final class Texture {

  private final int id;
  private final int textureUnit;

  /** @param textureUnit one of {@code GL_TEXTURE0}, {@code GL_TEXTURE1}, {@code GL_TEXTURE2}... */
  public static @NonNull Texture loadImage(
      @NonNull String fileName, @NonNull Class<?> clazz, int textureUnit, boolean enableAlpha)
      throws IOException {

    final var img = Texture.loadImageFromClasspath(fileName, clazz);
    final int width = img.getWidth();
    final int height = img.getHeight();

    final var buf = BufferUtils.createByteBuffer(width * height * 4);
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
        if (enableAlpha) buf.put(a);
      }
    }
    return Texture.load(buf.flip(), width, height, textureUnit, enableAlpha);
  }

  private static @NonNull Texture load(
      @NonNull ByteBuffer texels, int width, int height, int textureUnit, boolean enableAlpha) {

    final int id = glGenTextures();
    glActiveTexture(textureUnit);
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
        texels);
    glGenerateMipmap(GL_TEXTURE_2D);

    glBindTexture(GL_TEXTURE_2D, GL_NONE);

    return new Texture(id, textureUnit);
  }

  private static @NonNull BufferedImage loadImageFromClasspath(
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

  private Texture(int id, int textureUnit) {
    this.id = id;
    this.textureUnit = textureUnit;
  }

  /** @return one of {@code GL_TEXTURE0}, {@code GL_TEXTURE1}, {@code GL_TEXTURE2}... */
  public int getUnit() {
    return this.textureUnit;
  }

  public void destroy() {
    glDeleteTextures(this.id);
  }

  public void bind() {
    glActiveTexture(this.textureUnit);
    glBindTexture(GL_TEXTURE_2D, this.id);
  }

  public void unbind() {
    glActiveTexture(this.textureUnit);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);
  }
}
