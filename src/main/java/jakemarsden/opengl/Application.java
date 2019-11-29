package jakemarsden.opengl;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;

import jakemarsden.opengl.lwjgl.LwjglToSlf4jAdapter;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.system.Configuration;

public class Application implements Runnable {

  private static final FluentLogger LOGGER = getLogger(Application.class);

  public static void main(String[] args) {
    LOGGER.info().log("#main: args={}", (Object) args);

    Configuration.DEBUG.set(true);
    Configuration.DEBUG_STREAM.set(LwjglToSlf4jAdapter.create());
    new Application().run();
  }

  @Override
  public void run() {
    LOGGER.info().log("Application startup");
    // TODO
    LOGGER.info().log("Application shutdown");
  }

  private Application() {}
}