package jakemarsden.opengl;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.*;

import jakemarsden.opengl.engine.Game;
import jakemarsden.opengl.engine.display.Display;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.opengl.GL;

final class MainGame implements Game {

  private static final FluentLogger LOGGER = getLogger(MainGame.class);

  private final Display display;

  MainGame(@NonNull Display display) {
    this.display = display;
  }

  @Override
  public @NonNull Display getDisplay() {
    return this.display;
  }

  @Override
  public void init() {
    LOGGER.info().log("#init");
    GL.createCapabilities();
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearColor(0.4f, 0.4f, 0.5f, 1);

    glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
    this.display.setResizeCallback((newWidth, newHeight) -> glViewport(0, 0, newWidth, newHeight));
    this.display.setVisible(true);
  }

  @Override
  public void destroy() {
    LOGGER.info().log("#destroy");
    this.display.setVisible(false);
    this.display.setResizeCallback(null);

    GL.destroy();
  }

  @Override
  public boolean shouldContinue() {
    return !this.display.isCloseRequested();
  }

  @Override
  public void processInput() {}

  @Override
  public void update(long deltaTime) {}

  @Override
  public void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    this.display.swapDrawBuffers();
    this.display.processPendingInputEvents();
  }
}
