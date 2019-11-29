package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.sqrt;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Vector4 implements Vector<Vector4> {

  private static final Vector4 ZERO = new Vector4(0, 0, 0, 0);

  public final float x;
  public final float y;
  public final float z;
  public final float w;

  public static @NonNull Vector4 zero() {
    return ZERO;
  }

  public static @NonNull Vector4 of(float x, float y, float z, float w) {
    return new Vector4(x, y, z, w);
  }

  public static @NonNull Vector4 unit(float x, float y, float z, float w) {
    return Vector4.of(x, y, z, w).normalise();
  }

  private Vector4(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  @Override
  public @NonNull Vector4 normalise() {
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
  public float dot(@NonNull Vector4 operand) {
    return this.x * operand.x + this.y * operand.y + this.z * operand.z + this.w * operand.w;
  }

  @Override
  public @NonNull Vector4 negate() {
    return this.times(-1);
  }

  @Override
  public @NonNull Vector4 reciprocal() {
    if (this.x == 0 || this.y == 0 || this.z == 0 || this.w == 0)
      throw new ArithmeticException("Division by zero: 1 / " + this);
    return Vector4.of(1.0f / this.x, 1.0f / this.y, 1.0f / this.z, 1.0f / this.w);
  }

  @Override
  public @NonNull Vector4 plus(@NonNull Vector4 addend) {
    return this.plus(addend.x, addend.y, addend.z, addend.w);
  }

  public @NonNull Vector4 plus(float addendX, float addendY, float addendZ, float addendW) {
    return Vector4.of(this.x + addendX, this.y + addendY, this.z + addendZ, this.w + addendW);
  }

  @Override
  public @NonNull Vector4 minus(@NonNull Vector4 subtrahend) {
    return this.minus(subtrahend.x, subtrahend.y, subtrahend.z, subtrahend.w);
  }

  public @NonNull Vector4 minus(
      float subtrahendX, float subtrahendY, float subtrahendZ, float subtrahendW) {
    return Vector4.of(
        this.x - subtrahendX, this.y - subtrahendY, this.z - subtrahendZ, this.w - subtrahendW);
  }

  @Override
  public @NonNull Vector4 times(float factor) {
    return this.times(factor, factor, factor, factor);
  }

  @Override
  public @NonNull Vector4 times(@NonNull Vector4 factor) {
    return this.times(factor.x, factor.y, factor.z, factor.w);
  }

  public Vector4 times(float factorX, float factorY, float factorZ, float factorW) {
    return Vector4.of(this.x * factorX, this.y * factorY, this.z * factorZ, this.w * factorW);
  }

  @Override
  public @NonNull Vector4 divide(float divisor) {
    return this.divide(divisor, divisor, divisor, divisor);
  }

  @Override
  public @NonNull Vector4 divide(@NonNull Vector4 divisor) {
    return this.divide(divisor.x, divisor.y, divisor.z, divisor.w);
  }

  public @NonNull Vector4 divide(float divisorX, float divisorY, float divisorZ, float divisorW) {
    if (divisorX == 0 || divisorY == 0 || divisorZ == 0 || divisorW == 0)
      throw new ArithmeticException(
          "Division by zero: " + this + " / " + Vector4.of(divisorX, divisorY, divisorZ, divisorW));
    return Vector4.of(this.x / divisorX, this.y / divisorY, this.z / divisorZ, this.w / divisorW);
  }

  @Override
  public @NonNull String toString() {
    return String.format("Vector4{%.2f, %.2f, %.2f, %.2f}", this.x, this.y, this.z, this.w);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.z, this.w);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Vector4.class) return false;
    final var obj = (Vector4) o;
    return this.x == obj.x && this.y == obj.y && this.z == obj.z && this.w == obj.w;
  }
}
