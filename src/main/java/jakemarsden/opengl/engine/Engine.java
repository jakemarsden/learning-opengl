package jakemarsden.opengl.engine;

import static java.lang.System.currentTimeMillis;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class Engine implements Runnable {

  private final Game game;

  public Engine(@NonNull Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    final var game = this.game;
    game.init();

    long lastUpdateTime = currentTimeMillis();
    while (game.shouldContinue()) {
      final long currUpdateTime = currentTimeMillis();
      final long deltaTime = currUpdateTime - lastUpdateTime;
      lastUpdateTime = currUpdateTime;

      game.processInput();
      game.update(deltaTime);
      game.render();
    }

    game.destroy();
  }
}
