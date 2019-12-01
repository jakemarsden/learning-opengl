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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.s;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.opengl.GL;

final class MainGame implements Game {

  private static final FluentLogger LOGGER = getLogger(MainGame.class);

  private final Display display;
  private final Random rnd;

  private final PerspectiveCamera camera;
  private final MainShader shader;

  private final List<Entity> crates;
  private final List<Entity> lamps;
  private final List<PointLight> lampLights;

  MainGame(@NonNull Display display, @NonNull Random rnd) {
    LOGGER.info().log("#<init>");
    this.display = display;
    this.rnd = rnd;

    GL.createCapabilities();
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearColor(0.4f, 0.4f, 0.5f, 1);

    this.camera =
        new PerspectiveCamera(
            Vector3.of(0, 0, 6),
            Vector3.unit(0, 0, -1),
            display.getWidth() / (float) display.getHeight());

    this.shader = new MainShader();

    final Vector3[] cratePositions = {
      Vector3.of(0.0f, 0.0f, 0.0f),
      Vector3.of(2.0f, 5.0f, -15.0f),
      Vector3.of(-1.5f, -2.2f, -2.5f),
      Vector3.of(-3.8f, -2.0f, -12.3f),
      Vector3.of(2.4f, -0.4f, -3.5f),
      Vector3.of(-1.7f, 3.0f, -7.5f),
      Vector3.of(1.3f, -2.0f, -2.5f),
      Vector3.of(1.5f, 2.0f, -2.5f),
      Vector3.of(1.5f, 0.2f, -1.5f),
      Vector3.of(-1.3f, 1.0f, -1.5f)
    };
    this.crates = new ArrayList<>(cratePositions.length);
    for (Vector3 pos : cratePositions) {
      final var rot =
          Vector3.of(
              2 * PI * this.rnd.nextFloat(),
              2 * PI * this.rnd.nextFloat(),
              2 * PI * this.rnd.nextFloat());
      this.crates.add(
          MainGame.createCrate()
              .withPosition(pos)
              .withRotation(rot)
              .withRotationalVelocity(Vector3.of(0, -PI / 8, 0))
              .withScale(Vector3.of(0.375f))
              .build());
    }

    final Vector3[] lampPositions = {
      Vector3.of(0.7f, 0.2f, 2.0f),
      Vector3.of(2.3f, -3.3f, -4.0f),
      Vector3.of(-5.0f, 2.0f, -12.0f),
      Vector3.of(0.0f, 0.0f, 3.0f)
    };
    this.lamps = new ArrayList<>(lampPositions.length);
    this.lampLights = new ArrayList<>(lampPositions.length);
    for (final Vector3 pos : lampPositions) {
      final var rot =
          Vector3.of(
              2 * PI * this.rnd.nextFloat(),
              2 * PI * this.rnd.nextFloat(),
              2 * PI * this.rnd.nextFloat());
      final var color =
          Color3.rgb(this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat());
      this.lamps.add(
          MainGame.createLamp(color)
              .withPosition(pos)
              .withRotation(rot)
              .withScale(Vector3.of(0.075f))
              .build());
      this.lampLights.add(new PointLight(pos, Color3.gray(0.1f), color, color, 50));
    }

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

    this.crates.forEach(Entity::destroy);
    this.lamps.forEach(Entity::destroy);

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
    this.crates.forEach(crate -> crate.update(deltaTime, elapsedTime));

    for (var i = 0; i < this.lamps.size(); i++) {
      final var lamp = this.lamps.get(i);
      lamp.update(deltaTime, elapsedTime);
      this.lampLights.get(i).setPosition(lamp.getPosition());
    }
  }

  @Override
  public void render() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    this.shader.start();
    this.shader.setCameraPosition(this.camera.getPosition());
    this.shader.setCameraTransform(this.camera.calculatePvTransform());
    this.shader.setPointLights(this.lampLights.toArray(new PointLight[0]));

    this.crates.forEach(crate -> crate.draw(shader));
    this.lamps.forEach(lamp -> lamp.draw(shader));

    this.shader.stop();

    this.display.swapDrawBuffers();
    this.display.processPendingInputEvents();
  }

  private static Entity.@NonNull Builder createCrate() {
    final var model =
        MainGame.createCubeModel(
            Material.builder()
                .withDiffuseLighting(TextureLoader.loadImage("crate.diff.png", MainGame.class))
                .withSpecularLighting(TextureLoader.loadImage("crate.spec.png", MainGame.class), 32)
                .build());
    return Entity.builder(model);
  }

  private static Entity.@NonNull Builder createLamp(Color3 color) {
    final var model =
        MainGame.createCubeModel(
            Material.builder().withEmissionLighting(TextureLoader.flatColor(color)).build());
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
