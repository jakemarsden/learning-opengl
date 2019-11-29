package jakemarsden.opengl.engine.camera;

import static jakemarsden.opengl.engine.math.Math.PI;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Projection;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.UnitsTools;
import org.checkerframework.checker.units.qual.radians;

public final class PerspectiveCamera extends BaseCamera {

  private float nearZ;
  private float farZ;
  private @radians float fovY;
  private float ar;

  public PerspectiveCamera(@NonNull Vector3 pos, @NonNull Vector3 dir, float ar) {
    this(pos, dir, 0.1f, 100.0f, PI / 4 * (float) UnitsTools.rad, ar);
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
    if (nearZ == this.nearZ) return;
    this.dirtyProjection();
    this.nearZ = nearZ;
  }

  public float getFarZ() {
    return farZ;
  }

  public void setFarZ(float farZ) {
    if (farZ == this.farZ) return;
    this.dirtyProjection();
    this.farZ = farZ;
  }

  public @radians float getFovY() {
    return this.fovY;
  }

  public void setFovY(@radians float fovY) {
    if (fovY == this.fovY) return;
    this.dirtyProjection();
    this.fovY = fovY;
  }

  public float getAspectRatio() {
    return this.ar;
  }

  public void setAspectRatio(float ar) {
    if (ar == this.ar) return;
    this.dirtyProjection();
    this.ar = ar;
  }

  @Override
  protected @NonNull Matrix4 calculateProjection() {
    return Projection.perspective(this.fovY, this.ar, this.nearZ, this.farZ);
  }
}
