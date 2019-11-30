package jakemarsden.opengl.engine.camera;

import static jakemarsden.opengl.engine.math.Math.PI;
import static org.checkerframework.checker.units.UnitsTools.rad;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Projection;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.radians;

public final class PerspectiveCamera extends BaseCamera {

  private float nearZ;
  private float farZ;
  private @radians float fovY;
  private float ar;

  public PerspectiveCamera(@NonNull Vector3 pos, @NonNull Vector3 dir, float ar) {
    this(pos, dir, 0.1f, 100.0f, (PI / 4) * (float) rad, ar);
  }

  public PerspectiveCamera(
      @NonNull Vector3 pos,
      @NonNull Vector3 dir,
      float nearZ,
      float farZ,
      @radians float fovY,
      float ar) {

    super(pos, dir);
    this.nearZ = nearZ;
    this.farZ = farZ;
    this.fovY = fovY;
    this.ar = ar;
  }

  public float getNearZ() {
    return nearZ;
  }

  public void setNearZ(float nearZ) {
    this.nearZ = nearZ;
    this.dirtyProjection();
  }

  public float getFarZ() {
    return farZ;
  }

  public void setFarZ(float farZ) {
    this.farZ = farZ;
    this.dirtyProjection();
  }

  public @radians float getFovY() {
    return this.fovY;
  }

  public void setFovY(@radians float fovY) {
    this.fovY = fovY;
    this.dirtyProjection();
  }

  public float getAspectRatio() {
    return this.ar;
  }

  public void setAspectRatio(float ar) {
    this.ar = ar;
    this.dirtyProjection();
  }

  @Override
  @NonNull
  Matrix4 calculateProjection() {
    return Projection.perspective(this.fovY, this.ar, this.nearZ, this.farZ);
  }
}
