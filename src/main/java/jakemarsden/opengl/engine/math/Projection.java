package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.cotan;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.radians;

public final class Projection {

  /**
   * @param fovY field of view (in radians)
   * @param ar aspect ratio ({@code width / height})
   * @param nearZ distance to the near clipping plane
   * @param farZ distance to the far clipping plane
   * @see <a
   *     href="https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/opengl-perspective-projection-matrix">The
   *     Perspective and Orthographic Projection Matrix (The OpenGL Perspective Projection
   *     Matrix)</a>
   * @see <a href="https://cgit.freedesktop.org/mesa/glu/tree/src/libutil/project.c#n64"></a>
   */
  public static @NonNull Matrix4 perspective(
      @radians float fovY, float ar, float nearZ, float farZ) {

    final var f = cotan(fovY / 2.0f);
    final var depth = farZ - nearZ;

    final var m = new float[16];
    m[0 + 0 * 4] = f / ar;
    m[1 + 1 * 4] = f;
    m[2 + 2 * 4] = -(nearZ + farZ) / depth;
    m[3 + 2 * 4] = -2 * nearZ * farZ / depth;
    m[2 + 3 * 4] = -1;
    return Matrix4.of(m);
  }

  /**
   * @param left distance to the left clipping plane
   * @param right distance to the right clipping plane
   * @param bottom distance to the bottom clipping plane
   * @param top distance to the top clipping plane
   * @param near distance to the near clipping plane
   * @param far distance to the far clipping plane
   * @see <a
   *     href="https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/orthographic-projection-matrix">The
   *     Perspective and Orthographic Projection Matrix (The OpenGL Orthographic Projection
   *     Matrix)</a>
   */
  public static @NonNull Matrix4 orthographic(
      float left, float right, float bottom, float top, float near, float far) {

    final var data = new float[4 * 4];
    data[0 + 0 * 4] = 2.0f / (right - left);
    data[1 + 1 * 4] = 2.0f / (top - bottom);
    data[2 + 2 * 4] = 2.0f / (near - far);
    data[0 + 3 * 4] = (left + right) / (left - right);
    data[1 + 3 * 4] = (bottom + top) / (bottom - top);
    data[2 + 3 * 4] = (near + far) / (near - far);
    data[3 + 3 * 4] = 1.0f;
    return Matrix4.of(data);
  }

  /**
   * @param eye origin of the camera
   * @param centre a point in space to look at
   * @param up which direction is up (usually {@code [0, 1, 0]}, {@code [0, -1, 0]} is upside-down)
   * @see <a href="https://gamedev.stackexchange.com/a/90209">mathematics - How to calculate a
   *     direction vector for camera? - Game Development Stack Exchange</a>
   * @see <a href="https://cgit.freedesktop.org/mesa/glu/tree/src/libutil/project.c#n107"></a>
   */
  public static @NonNull Matrix4 lookAt(
      @NonNull Vector3 eye, @NonNull Vector3 centre, @NonNull Vector3 up) {

    final var forward = centre.minus(eye).normalise();
    final var left = forward.cross(up).normalise();
    up = left.cross(forward).normalise();

    final var m = new float[16];
    m[0 + 0 * 4] = left.x;
    m[1 + 0 * 4] = left.y;
    m[2 + 0 * 4] = left.z;
    m[0 + 1 * 4] = up.x;
    m[1 + 1 * 4] = up.y;
    m[2 + 1 * 4] = up.z;
    m[0 + 2 * 4] = -forward.x;
    m[1 + 2 * 4] = -forward.y;
    m[2 + 2 * 4] = -forward.z;
    m[3 + 3 * 4] = 1;
    return Matrix4.of(m).times(Matrix4.translate(eye.negate()));
  }

  private Projection() {
    throw new UnsupportedOperationException();
  }
}
