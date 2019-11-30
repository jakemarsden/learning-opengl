package jakemarsden.opengl;

import static jakemarsden.opengl.engine.math.Math.PI;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.*;

import jakemarsden.opengl.engine.Game;
import jakemarsden.opengl.engine.camera.PerspectiveCamera;
import jakemarsden.opengl.engine.display.Display;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.s;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.opengl.GL;

final class MainGame implements Game {

  private static final FluentLogger LOGGER = getLogger(MainGame.class);

  private final Display display;

  private final PerspectiveCamera camera;
  private final MainShader shader;

  private final Model quad;

  private Vector3 quadPos = Vector3.zero();
  private Vector3 quadRot = Vector3.of(-0.3f * PI, 0, 0);
  private Vector3 quadScale = Vector3.one();

  MainGame(@NonNull Display display) {
    LOGGER.info().log("#<init>");
    this.display = display;

    GL.createCapabilities();
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearColor(0.4f, 0.4f, 0.5f, 1);

    this.camera =
        new PerspectiveCamera(
            Vector3.of(0, 0, 3),
            Vector3.of(0, 0, -1),
            display.getWidth() / (float) display.getHeight());
    this.shader = new MainShader();
    this.quad = MainGame.loadQuad();

    glViewport(0, 0, this.display.getWidth(), this.display.getHeight());
    this.display.setResizeCallback(
        (newWidth, newHeight) -> {
          glViewport(0, 0, newWidth, newHeight);
          this.camera.setAspectRatio(newWidth / (float) newHeight);
        });
    this.display.setVisible(true);
  }

  @Override
  public void destroy() {
    LOGGER.info().log("#destroy");
    this.display.setVisible(false);
    this.display.setResizeCallback(null);

    this.quad.destroy();
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
    this.shader.setCameraTransform(this.camera.calculatePvTransform());

    this.shader.setModelTransform(Matrix4.transform(quadPos, quadRot, quadScale));
    this.quad.draw(shader);
    this.shader.stop();

    this.display.swapDrawBuffers();
    this.display.processPendingInputEvents();
  }

  private static @NonNull Model loadQuad() {
    final Mesh[] meshes = {
      StaticMeshLoader.load(
          GL_TRIANGLES,
          new float[] {
            0.5f, 0.5f, 0.0f, //
            0.5f, -0.5f, 0.0f, //
            -0.5f, -0.5f, 0.0f, //
            -0.5f, 0.5f, 0.0f
          },
          new float[] {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f
          },
          new short[] {
            0, 1, 3,
            1, 3, 2
          },
          Material.of(TextureLoader.loadImage("test.jpg", MainGame.class, false)))
    };

    return ModelLoader.load(meshes);
  }
}
