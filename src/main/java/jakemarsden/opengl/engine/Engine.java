package jakemarsden.opengl.engine;

import static org.checkerframework.checker.units.UnitsTools.s;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwSetTime;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.s;

public final class Engine implements Runnable {

  private final Game game;

  public Engine(@NonNull Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    final var game = this.game;

    @s float elapsedTime = 0 * s;
    @s float lastUpdateTime = 0 * s;

    // reset clock so frame 0 is at 0 elapsed time
    glfwSetTime(glfwGetTime());

    while (game.shouldContinue()) {
      game.processInput();
      game.update(elapsedTime - lastUpdateTime, elapsedTime);
      game.render();

      lastUpdateTime = elapsedTime;
      elapsedTime = (float) glfwGetTime() * s;
    }
  }
}
