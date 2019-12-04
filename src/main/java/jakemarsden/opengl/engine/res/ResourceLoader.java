package jakemarsden.opengl.engine.res;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ResourceLoader {

  private final @NonNull ClassLoader clazzLoader;
  private final @NonNull String basePath;

  public static @NonNull ResourceLoader create(@NonNull Class<?> clazz, @NonNull String basePath) {
    final var clazzLoader = requireNonNull(clazz.getClassLoader());
    return new ResourceLoader(requireNonNull(clazzLoader), basePath);
  }

  private ResourceLoader(@NonNull ClassLoader clazzLoader, @NonNull String basePath) {
    this.clazzLoader = clazzLoader;
    this.basePath = basePath;
  }

  public @NonNull BufferedImage loadImageResource(@NonNull String name) throws IOException {
    final var path = this.getPath(name);
    final BufferedImage img;
    try (final var in = this.loadResource(path)) {
      img = ImageIO.read(in);
    }

    if (img == null) throw new IOException("Unsupported image format: " + this.getPath(path));
    return img;
  }

  private @NonNull InputStream loadResource(@NonNull String path) throws IOException {
    final var res = this.clazzLoader.getResourceAsStream(path);
    if (res == null)
      throw new FileNotFoundException(
          "Resource not found: \"" + path + "\" using: " + this.clazzLoader.getName());

    return res;
  }

  private @NonNull String getPath(@NonNull String name) {
    if (this.basePath.isEmpty()) return name;
    return this.basePath + "/" + name;
  }
}
