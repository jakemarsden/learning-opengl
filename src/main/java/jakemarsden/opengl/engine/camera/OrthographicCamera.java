package jakemarsden.opengl.engine.camera;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Projection;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class OrthographicCamera extends BaseCamera {

  private float left;
  private float right;
  private float bottom;
  private float top;
  private float near;
  private float far;

  public OrthographicCamera(@NonNull Vector3 pos, @NonNull Vector3 dir) {
    this(pos, dir, -1, 1, -1, 1, 0.1f, 100f);
  }

  public OrthographicCamera(
      @NonNull Vector3 pos,
      @NonNull Vector3 dir,
      float left,
      float right,
      float bottom,
      float top,
      float near,
      float far) {

    super(pos, dir);
    this.left = left;
    this.right = right;
    this.bottom = bottom;
    this.top = top;
    this.near = near;
    this.far = far;
  }

  public float getLeft() {
    return left;
  }

  public void setLeft(float left) {
    this.left = left;
    this.dirtyProjection();
  }

  public float getRight() {
    return right;
  }

  public void setRight(float right) {
    this.right = right;
    this.dirtyProjection();
  }

  public float getBottom() {
    return bottom;
  }

  public void setBottom(float bottom) {
    this.bottom = bottom;
    this.dirtyProjection();
  }

  public float getTop() {
    return top;
  }

  public void setTop(float top) {
    this.top = top;
    this.dirtyProjection();
  }

  public float getNear() {
    return near;
  }

  public void setNear(float near) {
    this.near = near;
    this.dirtyProjection();
  }

  public float getFar() {
    return far;
  }

  public void setFar(float far) {
    this.far = far;
    this.dirtyProjection();
  }

  @Override
  @NonNull
  Matrix4 calculateProjection() {
    return Projection.orthographic(
        this.left, this.right, this.bottom, this.top, this.near, this.far);
  }
}
