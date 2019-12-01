package jakemarsden.opengl.engine.entity;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Model;
import jakemarsden.opengl.engine.shader.Shader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.s;

public final class Entity {

  private final Model model;

  private Vector3 pos;
  private Vector3 rot;
  private Vector3 scale;

  private Vector3 vel;
  private Vector3 rotVel;

  private @Nullable Matrix4 transform;

  public static Entity.@NonNull Builder builder(@NonNull Model model) {
    return new Builder(model);
  }

  private Entity(
      @NonNull Model model,
      @NonNull Vector3 pos,
      @NonNull Vector3 rot,
      @NonNull Vector3 scale,
      @NonNull Vector3 vel,
      @NonNull Vector3 rotVel) {

    this.model = model;
    this.pos = pos;
    this.rot = rot;
    this.scale = scale;
    this.vel = vel;
    this.rotVel = rotVel;
  }

  public @NonNull Model getModel() {
    return model;
  }

  public @NonNull Vector3 getPosition() {
    return pos;
  }

  public void setPosition(@NonNull Vector3 pos) {
    this.pos = pos;
    this.dirty();
  }

  public @NonNull Vector3 getRotation() {
    return rot;
  }

  public void setRotation(@NonNull Vector3 rot) {
    this.rot = rot;
    this.dirty();
  }

  public @NonNull Vector3 getScale() {
    return scale;
  }

  public void setScale(@NonNull Vector3 scale) {
    this.scale = scale;
    this.dirty();
  }

  public @NonNull Vector3 getVelocity() {
    return this.vel;
  }

  public void setVelocity(@NonNull Vector3 vel) {
    this.vel = vel;
  }

  public @NonNull Vector3 getRotationalVelocity() {
    return this.rotVel;
  }

  public void setRotationalVelocity(@NonNull Vector3 rotVel) {
    this.rotVel = rotVel;
  }

  public void update(@s float deltaTime, @s float elapsedTime) {
    if (!this.vel.equals(Vector3.zero())) {
      this.pos = this.pos.plus(this.vel.times(deltaTime));
      this.dirty();
    }
    if (!this.rotVel.equals(Vector3.zero())) {
      this.rot = this.rot.plus(this.rotVel.times(deltaTime));
      this.dirty();
    }
  }

  public void draw(Shader shader) {
    shader.setModelTransform(this.calculateTransform());
    this.model.draw(shader);
  }

  public void destroy() {
    this.model.destroy();
  }

  private @NonNull Matrix4 calculateTransform() {
    if (this.transform == null) this.transform = Matrix4.transform(this.pos, this.rot, this.scale);
    return this.transform;
  }

  private void dirty() {
    this.transform = null;
  }

  public static final class Builder {

    private final @NonNull Model model;

    private @Nullable Vector3 pos;
    private @Nullable Vector3 rot;
    private @Nullable Vector3 scale;

    private @Nullable Vector3 vel;
    private @Nullable Vector3 rotVel;

    private Builder(@NonNull Model model) {
      this.model = model;
    }

    public @NonNull Builder withPosition(@NonNull Vector3 pos) {
      this.pos = pos;
      return this;
    }

    public @NonNull Builder withRotation(@NonNull Vector3 rot) {
      this.rot = rot;
      return this;
    }

    public @NonNull Builder withScale(@NonNull Vector3 scale) {
      this.scale = scale;
      return this;
    }

    public @NonNull Builder withVelocity(@NonNull Vector3 vel) {
      this.vel = vel;
      return this;
    }

    public @NonNull Builder withRotationalVelocity(@NonNull Vector3 rotVel) {
      this.rotVel = rotVel;
      return this;
    }

    public @NonNull Entity build() {
      return new Entity(
          this.model,
          this.pos != null ? this.pos : Vector3.zero(),
          this.rot != null ? this.rot : Vector3.zero(),
          this.scale != null ? this.scale : Vector3.zero(),
          this.vel != null ? this.vel : Vector3.zero(),
          this.rotVel != null ? this.rotVel : Vector3.zero());
    }
  }
}
