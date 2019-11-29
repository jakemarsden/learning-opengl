package jakemarsden.opengl.engine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.UnitsTools;
import org.checkerframework.checker.units.qual.s;

public final class Engine implements Runnable {

  private final Game game;

  public Engine(@NonNull Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    final var game = this.game;

    @s double lastUpdateTime = currentTime();
    while (game.shouldContinue()) {
      final @s double currUpdateTime = currentTime();
      final @s double deltaTime = currUpdateTime - lastUpdateTime;
      lastUpdateTime = currUpdateTime;

      game.processInput();
      game.update(deltaTime);
      game.render();
    }
  }

  private static @s double currentTime() {
    return glfwGetTime() * UnitsTools.s;
  }
}
