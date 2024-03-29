package jakemarsden.opengl.engine.camera;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Projection;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class BaseCamera implements Camera {

  private static final Vector3 UP = Vector3.of(0, 1, 0);

  private Vector3 pos;
  private Vector3 dir;

  private @Nullable Matrix4 projection = null;
  private @Nullable Matrix4 view = null;
  private @Nullable Matrix4 pv = null;

  BaseCamera(Vector3 pos, Vector3 dir) {
    this.pos = pos;
    this.dir = dir;
  }

  @Override
  public final @NonNull Vector3 getPosition() {
    return this.pos;
  }

  @Override
  public final void setPosition(@NonNull Vector3 pos) {
    this.pos = pos;
    this.dirtyView();
  }

  @Override
  public final @NonNull Vector3 getDirection() {
    return this.dir;
  }

  @Override
  public final void setDirection(@NonNull Vector3 dir) {
    this.dir = dir;
    this.dirtyView();
  }

  @Override
  public final @NonNull Matrix4 calculatePvTransform() {
    if (this.pv == null) {
      var p = this.projection;
      if (p == null) p = this.projection = this.calculateProjection();

      var v = this.view;
      if (v == null) v = this.view = this.calculateView();

      this.pv = p.times(v);
    }
    return this.pv;
  }

  abstract @NonNull Matrix4 calculateProjection();

  @NonNull
  Matrix4 calculateView() {
    return Projection.lookAt(this.pos, this.pos.plus(this.dir), UP);
  }

  final void dirtyProjection() {
    this.projection = null;
    this.pv = null;
  }

  final void dirtyView() {
    this.view = null;
    this.pv = null;
  }
}
