package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.cos;
import static jakemarsden.opengl.engine.math.Math.sin;

import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Matrix4 implements Matrix<Vector4, Matrix4> {

  private static final int COLS = 4;
  private static final int ROWS = 4;
  private static final int SIZE = COLS * ROWS;

  private static final Matrix4 IDENTITY;

  static {
    final float[] m = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    IDENTITY = new Matrix4(m);
  }

  private final float[] m;

  public static @NonNull Matrix4 identity() {
    return IDENTITY;
  }

  public static @NonNull Matrix4 transform(
      @NonNull Vector3 translation, @NonNull Vector3 rotation) {
    final var t = Matrix4.translate(translation);
    final var r = Matrix4.rotate(rotation);
    return t.times(r);
  }

  public static @NonNull Matrix4 transform(
      @NonNull Vector3 translation, @NonNull Vector3 rotation, float scale) {
    final var tr = Matrix4.transform(translation, rotation);
    final var s = Matrix4.scale(scale);
    return tr.times(s);
  }

  public static @NonNull Matrix4 transform(
      @NonNull Vector3 translation, @NonNull Vector3 rotation, @NonNull Vector3 scale) {
    final var tr = Matrix4.transform(translation, rotation);
    final var s = Matrix4.scale(scale);
    return tr.times(s);
  }

  public static @NonNull Matrix4 translate(@NonNull Vector3 v) {
    return Matrix4.translate(v.x, v.y, v.z);
  }

  public static @NonNull Matrix4 translate(float x, float y, float z) {
    final float[] m = {
      1, 0, 0, x,
      0, 1, 0, y,
      0, 0, 1, z,
      0, 0, 0, 1
    };
    return new Matrix4(m);
  }

  public static @NonNull Matrix4 rotate(@NonNull Vector3 v) {
    return Matrix4.rotate(v.x, v.y, v.z);
  }

  public static @NonNull Matrix4 rotate(float x, float y, float z) {
    final var a = cos(x);
    final var b = sin(x);
    final var c = cos(y);
    final var d = sin(y);
    final var e = cos(z);
    final var f = sin(z);
    final var m = new float[SIZE];
    m[0] = c * e;
    m[1] = -c * f;
    m[2] = d;
    m[4] = b * d * e + a * f;
    m[5] = -b * d * f + a * e;
    m[6] = -b * c;
    m[8] = -a * d * e + b * f;
    m[9] = a * d * f + b * e;
    m[10] = a * c;
    m[15] = 1;
    return new Matrix4(m);
  }

  public static @NonNull Matrix4 scale(float s) {
    return Matrix4.scale(s, s, s);
  }

  public static @NonNull Matrix4 scale(@NonNull Vector3 v) {
    return Matrix4.scale(v.x, v.y, v.z);
  }

  public static @NonNull Matrix4 scale(float x, float y, float z) {
    final float[] m = {
      x, 0, 0, 0, //
      0, y, 0, 0, //
      0, 0, z, 0, //
      0, 0, 0, 1
    };
    return new Matrix4(m);
  }

  public static @NonNull Matrix4 of(float[] m) {
    if (m.length != SIZE)
      throw new IllegalArgumentException("Expected length: " + SIZE + " but was: " + m.length);
    return new Matrix4(Arrays.copyOf(m, SIZE));
  }

  private Matrix4(float[] m) {
    this.m = m;
  }

  @Override
  public @NonNull Matrix4 invert() {
    final var inverse = new float[SIZE];
    Matrix4.scaryInvert(this.m, inverse);
    return new Matrix4(inverse);
  }

  @Override
  public @NonNull Matrix4 transpose() {
    final float[] transposed = {
      this.m[0], this.m[4], this.m[8], this.m[12],
      this.m[1], this.m[5], this.m[9], this.m[13],
      this.m[2], this.m[6], this.m[10], this.m[14],
      this.m[3], this.m[7], this.m[11], this.m[15]
    };
    return new Matrix4(transposed);
  }

  @Override
  public @NonNull Vector4 times(@NonNull Vector4 v) {
    return Vector4.of(
        this.m[0] * v.x + this.m[1] * v.y + this.m[2] * v.z + this.m[3] * v.w,
        this.m[4] * v.x + this.m[5] * v.y + this.m[6] * v.z + this.m[7] * v.w,
        this.m[8] * v.x + this.m[9] * v.y + this.m[10] * v.z + this.m[11] * v.w,
        this.m[12] * v.x + this.m[13] * v.y + this.m[14] * v.z + this.m[15] * v.w);
  }

  @Override
  public @NonNull Matrix4 times(@NonNull Matrix4 factor) {
    final var m = new float[SIZE];
    for (var row = 0; row < ROWS; row++) {
      for (var col = 0; col < COLS; col++) {
        for (var i = 0; i < COLS; i++)
          m[col + row * COLS] += this.m[i + row * COLS] * factor.m[col + i * COLS];
      }
    }
    return new Matrix4(m);
  }

  @Override
  public float[] toArray() {
    return Arrays.copyOf(this.m, SIZE);
  }

  @Override
  public @NonNull String toString() {
    final var sb = new StringBuilder(128);
    sb.append("Matrix4{");
    for (var row = 0; row < ROWS; row++) {
      if (row != 0) sb.append(", ");
      sb.append("{");
      for (var col = 0; col < COLS; col++) {
        if (col != 0) sb.append(", ");
        sb.append(String.format("%.2f", this.m[col + row * COLS]));
      }
      sb.append("}");
    }
    sb.append("}");
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.m);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Matrix4.class) return false;
    final var obj = (Matrix4) o;
    return Arrays.equals(this.m, obj.m);
  }

  /**
   * I don't know how this works. I don't <em>want</em> to know how this works. I shamelessly stole
   * it from <a href="https://stackoverflow.com/a/1148405/6314470">here</a> and now I'm going to go
   * bleach my eyes.
   *
   * @see <a href="https://stackoverflow.com/a/1148405/6314470">c++ - inverting a 4x4 matrix - Stack
   *     Overflow</a>
   * @see <a href="https://cgit.freedesktop.org/mesa/glu/tree/src/libutil/project.c#n163"></a>
   */
  private static void scaryInvert(float[] m, float[] inv) {
    inv[0] =
        m[5] * m[10] * m[15]
            - m[5] * m[11] * m[14]
            - m[9] * m[6] * m[15]
            + m[9] * m[7] * m[14]
            + m[13] * m[6] * m[11]
            - m[13] * m[7] * m[10];
    inv[4] =
        -m[4] * m[10] * m[15]
            + m[4] * m[11] * m[14]
            + m[8] * m[6] * m[15]
            - m[8] * m[7] * m[14]
            - m[12] * m[6] * m[11]
            + m[12] * m[7] * m[10];
    inv[8] =
        m[4] * m[9] * m[15]
            - m[4] * m[11] * m[13]
            - m[8] * m[5] * m[15]
            + m[8] * m[7] * m[13]
            + m[12] * m[5] * m[11]
            - m[12] * m[7] * m[9];
    inv[12] =
        -m[4] * m[9] * m[14]
            + m[4] * m[10] * m[13]
            + m[8] * m[5] * m[14]
            - m[8] * m[6] * m[13]
            - m[12] * m[5] * m[10]
            + m[12] * m[6] * m[9];
    inv[1] =
        -m[1] * m[10] * m[15]
            + m[1] * m[11] * m[14]
            + m[9] * m[2] * m[15]
            - m[9] * m[3] * m[14]
            - m[13] * m[2] * m[11]
            + m[13] * m[3] * m[10];
    inv[5] =
        m[0] * m[10] * m[15]
            - m[0] * m[11] * m[14]
            - m[8] * m[2] * m[15]
            + m[8] * m[3] * m[14]
            + m[12] * m[2] * m[11]
            - m[12] * m[3] * m[10];
    inv[9] =
        -m[0] * m[9] * m[15]
            + m[0] * m[11] * m[13]
            + m[8] * m[1] * m[15]
            - m[8] * m[3] * m[13]
            - m[12] * m[1] * m[11]
            + m[12] * m[3] * m[9];
    inv[13] =
        m[0] * m[9] * m[14]
            - m[0] * m[10] * m[13]
            - m[8] * m[1] * m[14]
            + m[8] * m[2] * m[13]
            + m[12] * m[1] * m[10]
            - m[12] * m[2] * m[9];

    inv[2] =
        m[1] * m[6] * m[15]
            - m[1] * m[7] * m[14]
            - m[5] * m[2] * m[15]
            + m[5] * m[3] * m[14]
            + m[13] * m[2] * m[7]
            - m[13] * m[3] * m[6];
    inv[6] =
        -m[0] * m[6] * m[15]
            + m[0] * m[7] * m[14]
            + m[4] * m[2] * m[15]
            - m[4] * m[3] * m[14]
            - m[12] * m[2] * m[7]
            + m[12] * m[3] * m[6];
    inv[10] =
        m[0] * m[5] * m[15]
            - m[0] * m[7] * m[13]
            - m[4] * m[1] * m[15]
            + m[4] * m[3] * m[13]
            + m[12] * m[1] * m[7]
            - m[12] * m[3] * m[5];

    inv[14] =
        -m[0] * m[5] * m[14]
            + m[0] * m[6] * m[13]
            + m[4] * m[1] * m[14]
            - m[4] * m[2] * m[13]
            - m[12] * m[1] * m[6]
            + m[12] * m[2] * m[5];
    inv[3] =
        -m[1] * m[6] * m[11]
            + m[1] * m[7] * m[10]
            + m[5] * m[2] * m[11]
            - m[5] * m[3] * m[10]
            - m[9] * m[2] * m[7]
            + m[9] * m[3] * m[6];

    inv[7] =
        m[0] * m[6] * m[11]
            - m[0] * m[7] * m[10]
            - m[4] * m[2] * m[11]
            + m[4] * m[3] * m[10]
            + m[8] * m[2] * m[7]
            - m[8] * m[3] * m[6];
    inv[11] =
        -m[0] * m[5] * m[11]
            + m[0] * m[7] * m[9]
            + m[4] * m[1] * m[11]
            - m[4] * m[3] * m[9]
            - m[8] * m[1] * m[7]
            + m[8] * m[3] * m[5];
    inv[15] =
        m[0] * m[5] * m[10]
            - m[0] * m[6] * m[9]
            - m[4] * m[1] * m[10]
            + m[4] * m[2] * m[9]
            + m[8] * m[1] * m[6]
            - m[8] * m[2] * m[5];

    float determinant = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
    if (determinant == 0)
      throw new ArithmeticException(
          "Division by zero: cannot compute the inverse of a matrix who's determinant is 0");

    determinant = 1.0f / determinant;
    for (var i = 0; i < SIZE; i++) inv[i] = inv[i] * determinant;
  }
}
