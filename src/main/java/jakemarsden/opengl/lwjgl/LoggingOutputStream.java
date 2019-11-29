package jakemarsden.opengl.lwjgl;

import java.io.OutputStream;
import java.util.function.Consumer;

final class LoggingOutputStream extends OutputStream {

  private final Consumer<String> logger;

  LoggingOutputStream(Consumer<String> logger) {
    this.logger = logger;
  }

  @Override
  public void write(int b) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(byte[] b, int off, int len) {
    final var msg = new String(b, off, len);
    this.logger.accept(msg);
  }
}
