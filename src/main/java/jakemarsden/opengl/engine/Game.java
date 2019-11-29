package jakemarsden.opengl.engine;

import org.checkerframework.checker.units.qual.s;

public interface Game {

  boolean shouldContinue();

  void processInput();

  void update(@s double deltaTime);

  void render();

  void destroy();
}
