package jakemarsden.opengl;

import static jakemarsden.opengl.engine.math.Math.PI;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.opengl.GL11.*;

import jakemarsden.opengl.engine.Game;
import jakemarsden.opengl.engine.camera.PerspectiveCamera;
import jakemarsden.opengl.engine.display.Display;
import jakemarsden.opengl.engine.entity.Entity;
import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Vector2;
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

  private final Entity crate;
  private final Entity lamp;
  private final PointLight lampLight;

  MainGame(@NonNull Display display) {
    LOGGER.info().log("#<init>");
    this.display = display;

    GL.createCapabilities();
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearColor(0.4f, 0.4f, 0.5f, 1);

    this.camera =
        new PerspectiveCamera(
            Vector3.of(0, 1, 4),
            Vector3.unit(0, -0.2f, -1),
            display.getWidth() / (float) display.getHeight());

    this.shader = new MainShader();

    this.crate =
        MainGame.createCrateEntity()
            .withPosition(Vector3.zero())
            .withScale(Vector3.of(0.5f))
            .withRotationalVelocity(Vector3.of(0, -PI / 8, 0))
            .build();
    this.lamp =
        MainGame.createLampEntity()
            .withPosition(Vector3.of(0.75f, 0, 2))
            .withScale(Vector3.of(0.05f))
            .build();
    this.lampLight = new PointLight(Color3.gray(0.1f), Color3.white(), Color3.white());

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

    this.crate.destroy();
    this.lamp.destroy();

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
  public void update(@s float deltaTime, @s float elapsedTime) {
    this.crate.update(deltaTime, elapsedTime);
    this.lamp.update(deltaTime, elapsedTime);
  }

  @Override
  public void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    this.shader.start();
    this.shader.setCameraPosition(this.camera.getPosition());
    this.shader.setCameraTransform(this.camera.calculatePvTransform());
    this.shader.setLight(this.lamp.getPosition(), this.lampLight);

    this.crate.draw(shader);
    this.lamp.draw(shader);

    this.shader.stop();

    this.display.swapDrawBuffers();
    this.display.processPendingInputEvents();
  }

  private static Entity.@NonNull Builder createCrateEntity() {
    final var model =
        MainGame.createCubeModel(
            Material.builder()
                .withDiffuseLighting(TextureLoader.loadImage("crate.diff.png", MainGame.class))
                .withSpecularLighting(TextureLoader.loadImage("crate.spec.png", MainGame.class), 32)
                .build());
    return Entity.builder(model);
  }

  private static Entity.@NonNull Builder createLampEntity() {
    final var model =
        MainGame.createCubeModel(
            Material.builder()
                .withEmissionLighting(TextureLoader.flatColor(Color3.white()))
                .build());
    return Entity.builder(model);
  }

  private static @NonNull Model createCubeModel(@NonNull Material mat) {
    final Vertex[] vertices = {
      // back
      Vertex.of(Vector3.of(-1, -1, -1), Vector3.of(0, 0, -1), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(1, -1, -1), Vector3.of(0, 0, -1), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(1, 1, -1), Vector3.of(0, 0, -1), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(-1, 1, -1), Vector3.of(0, 0, -1), Vector2.of(0, 0)),
      // front
      Vertex.of(Vector3.of(-1, -1, 1), Vector3.of(0, 0, 1), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(1, -1, 1), Vector3.of(0, 0, 1), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(1, 1, 1), Vector3.of(0, 0, 1), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(-1, 1, 1), Vector3.of(0, 0, 1), Vector2.of(0, 0)),
      // left
      Vertex.of(Vector3.of(-1, -1, -1), Vector3.of(-1, 0, 0), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(-1, -1, 1), Vector3.of(-1, 0, 0), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(-1, 1, 1), Vector3.of(-1, 0, 0), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(-1, 1, -1), Vector3.of(-1, 0, 0), Vector2.of(0, 0)),
      // right
      Vertex.of(Vector3.of(1, -1, -1), Vector3.of(1, 0, 0), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(1, -1, 1), Vector3.of(1, 0, 0), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(1, 1, 1), Vector3.of(1, 0, 0), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(1, 1, -1), Vector3.of(1, 0, 0), Vector2.of(0, 0)),
      // top
      Vertex.of(Vector3.of(-1, 1, -1), Vector3.of(0, 1, 0), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(-1, 1, 1), Vector3.of(0, 1, 0), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(1, 1, 1), Vector3.of(0, 1, 0), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(1, 1, -1), Vector3.of(0, 1, 0), Vector2.of(0, 0)),
      // bottom
      Vertex.of(Vector3.of(-1, -1, -1), Vector3.of(0, -1, 0), Vector2.of(0, 1)),
      Vertex.of(Vector3.of(-1, -1, 1), Vector3.of(0, -1, 0), Vector2.of(1, 1)),
      Vertex.of(Vector3.of(1, -1, 1), Vector3.of(0, -1, 0), Vector2.of(1, 0)),
      Vertex.of(Vector3.of(1, -1, -1), Vector3.of(0, -1, 0), Vector2.of(0, 0)),
    };

    final short[] indices = {
      0, 1, 2, 2, 0, 3, // back
      4, 5, 6, 6, 4, 7, // front
      8, 9, 10, 10, 8, 11, // left
      12, 13, 14, 14, 12, 15, // right
      16, 17, 18, 18, 16, 19, // top
      20, 21, 22, 22, 20, 23, // bottom
    };

    final Mesh[] meshes = {StaticMeshLoader.load(GL_TRIANGLES, vertices, indices, mat)};
    return ModelLoader.load(meshes);
  }
}
