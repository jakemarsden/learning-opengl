package jakemarsden.opengl.engine;

import jakemarsden.opengl.engine.display.Display;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Game {

  @NonNull
  Display getDisplay();

  void init();

  void destroy();

  boolean shouldContinue();

  void processInput();

  void update(long deltaTime);

  void render();
}
