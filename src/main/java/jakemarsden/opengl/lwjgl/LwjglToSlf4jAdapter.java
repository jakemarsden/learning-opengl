package jakemarsden.opengl.lwjgl;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.PrintStream;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.event.Level;

public final class LwjglToSlf4jAdapter {

  public static PrintStream create() {
    final var logger = getLogger("org.lwjgl");
    final var logFn = logFnForLevel(logger, Level.DEBUG);
    return new PrintStream(new LoggingOutputStream(logFn));
  }

  private static Consumer<String> logFnForLevel(Logger logger, Level level) {
    switch (level) {
      case ERROR:
        return logger::error;
      case WARN:
        return logger::warn;
      case INFO:
        return logger::info;
      case DEBUG:
        return logger::debug;
      case TRACE:
        return logger::trace;
      default:
        throw new IllegalArgumentException("Unsupported level: " + level);
    }
  }

  private LwjglToSlf4jAdapter() {
    throw new UnsupportedOperationException();
  }
}
