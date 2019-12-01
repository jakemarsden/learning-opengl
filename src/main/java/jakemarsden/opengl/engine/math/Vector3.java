package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.sqrt;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Vector3 implements Vector<Vector3> {

  private static final Vector3 ZERO = new Vector3(0, 0, 0);
  private static final Vector3 ONE = new Vector3(1, 1, 1);

  public final float x;
  public final float y;
  public final float z;

  public static @NonNull Vector3 zero() {
    return ZERO;
  }

  public static @NonNull Vector3 one() {
    return ONE;
  }

  public static @NonNull Vector3 unit(float x, float y, float z) {
    return Vector3.of(x, y, z).normalise();
  }

  public static @NonNull Vector3 of(float xyz) {
    return Vector3.of(xyz, xyz, xyz);
  }

  public static @NonNull Vector3 of(float x, float y, float z) {
    return new Vector3(x, y, z);
  }

  private Vector3(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public @NonNull Vector3 normalise() {
    final var length2 = this.length2();
    if (length2 == 0 || length2 == 1) return this;
    return this.divide(sqrt(length2));
  }

  @Override
  public float length() {
    return sqrt(this.length2());
  }

  @Override
  public float length2() {
    return this.dot(this);
  }

  @Override
  public float dot(@NonNull Vector3 operand) {
    return this.x * operand.x + this.y * operand.y + this.z * operand.z;
  }

  /** Order is important! ({@code a.cross(b) != b.cross(a)} */
  public @NonNull Vector3 cross(@NonNull Vector3 v) {
    return Vector3.of(
        this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
  }

  @Override
  public @NonNull Vector3 negate() {
    return this.times(-1);
  }

  @Override
  public @NonNull Vector3 reciprocal() {
    if (this.x == 0 || this.y == 0 || this.z == 0)
      throw new ArithmeticException("Division by zero: 1 / " + this);
    return Vector3.of(1.0f / this.x, 1.0f / this.y, 1.0f / this.z);
  }

  @Override
  public @NonNull Vector3 plus(@NonNull Vector3 addend) {
    return this.plus(addend.x, addend.y, addend.z);
  }

  public @NonNull Vector3 plus(float addendX, float addendY, float addendZ) {
    return Vector3.of(this.x + addendX, this.y + addendY, this.z + addendZ);
  }

  @Override
  public @NonNull Vector3 minus(@NonNull Vector3 subtrahend) {
    return this.minus(subtrahend.x, subtrahend.y, subtrahend.z);
  }

  public @NonNull Vector3 minus(float subtrahendX, float subtrahendY, float subtrahendZ) {
    return Vector3.of(this.x - subtrahendX, this.y - subtrahendY, this.z - subtrahendZ);
  }

  @Override
  public @NonNull Vector3 times(float factor) {
    return this.times(factor, factor, factor);
  }

  @Override
  public @NonNull Vector3 times(@NonNull Vector3 factor) {
    return this.times(factor.x, factor.y, factor.z);
  }

  public @NonNull Vector3 times(float factorX, float factorY, float factorZ) {
    return Vector3.of(this.x * factorX, this.y * factorY, this.z * factorZ);
  }

  @Override
  public @NonNull Vector3 divide(float divisor) {
    return this.divide(divisor, divisor, divisor);
  }

  @Override
  public @NonNull Vector3 divide(@NonNull Vector3 divisor) {
    return this.divide(divisor.x, divisor.y, divisor.z);
  }

  public @NonNull Vector3 divide(float divisorX, float divisorY, float divisorZ) {
    if (divisorX == 0 || divisorY == 0 || divisorZ == 0)
      throw new ArithmeticException(
          "Division by zero: " + this + " / " + Vector3.of(divisorX, divisorY, divisorZ));
    return Vector3.of(this.x / divisorX, this.y / divisorY, this.z / divisorZ);
  }

  @Override
  public @NonNull String toString() {
    return String.format("Vector3{%.2f, %.2f, %.2f}", this.x, this.y, this.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.z);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Vector3.class) return false;
    final var obj = (Vector3) o;
    return this.x == obj.x && this.y == obj.y && this.z == obj.z;
  }
}
