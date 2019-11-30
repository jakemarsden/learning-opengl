package jakemarsden.opengl.engine;

import org.checkerframework.checker.units.qual.s;

public interface Game {

  boolean shouldContinue();

  void processInput();

  void update(@s float deltaTime, @s float elapsedTime);

  void render();

  void destroy();
}
