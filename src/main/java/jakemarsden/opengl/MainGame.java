package jakemarsden.opengl;

import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.*;

import jakemarsden.opengl.engine.Game;
import jakemarsden.opengl.engine.display.Display;
import jakemarsden.opengl.engine.mesh.StaticMesh;
import jakemarsden.opengl.engine.mesh.StaticMeshLoader;
import jakemarsden.opengl.engine.shader.Shader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.s;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.opengl.GL;

final class MainGame implements Game {

  private static final FluentLogger LOGGER = getLogger(MainGame.class);

  private final Display display;

  private final Shader shader;
  private final StaticMesh quadMesh;

  MainGame(@NonNull Display display) {
    LOGGER.info().log("#<init>");
    this.display = display;

    GL.createCapabilities();
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearColor(0.4f, 0.4f, 0.5f, 1);

    final short[] quadIndices = {
      0, 1, 3,
      1, 3, 2
    };
    final float[] quadVertices = {
      0.5f, 0.5f, 0.0f, //
      0.5f, -0.5f, 0.0f, //
      -0.5f, -0.5f, 0.0f, //
      -0.5f, 0.5f, 0.0f
    };
    this.shader = new MainShader();
    this.quadMesh = StaticMeshLoader.triangles(quadIndices, quadVertices);

    glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
    this.display.setResizeCallback((newWidth, newHeight) -> glViewport(0, 0, newWidth, newHeight));
    this.display.setVisible(true);
  }

  @Override
  public void destroy() {
    LOGGER.info().log("#destroy");
    this.display.setVisible(false);
    this.display.setResizeCallback(null);

    this.quadMesh.destroy();
    this.shader.destroy();

    GL.destroy();
  }

  @Override
  public boolean shouldContinue() {
    return !this.display.isCloseRequested();
  }

  @Override
  public void processInput() {}

  @Override
  public void update(@s double deltaTime) {}

  @Override
  public void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    this.shader.start();
    this.quadMesh.bind();
    this.quadMesh.draw();
    this.quadMesh.unbind();
    this.shader.stop();

    this.display.swapDrawBuffers();
    this.display.processPendingInputEvents();
  }
}
