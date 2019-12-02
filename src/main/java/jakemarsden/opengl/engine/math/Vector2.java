package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Vector2 implements Vector<Vector2> {

  private static final Vector2 ZERO = new Vector2(0, 0);

  public final float x;
  public final float y;

  public static @NonNull Vector2 zero() {
    return ZERO;
  }

  public static @NonNull Vector2 unit(float x, float y) {
    return Vector2.of(x, y).normalise();
  }

  public static @NonNull Vector2 of(float xy) {
    return Vector2.of(xy, xy);
  }

  public static @NonNull Vector2 of(float x, float y) {
    return new Vector2(x, y);
  }

  private Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public float dot(@NonNull Vector2 operand) {
    return this.x * operand.x + this.y * operand.y;
  }

  @Override
  public @NonNull Vector2 reciprocal() {
    if (this.x == 0 || this.y == 0) throw new ArithmeticException("Division by zero: 1 / " + this);
    return Vector2.of(1 / this.x, 1 / this.y);
  }

  @Override
  public @NonNull Vector2 plus(@NonNull Vector2 addend) {
    return this.plus(addend.x, addend.y);
  }

  public @NonNull Vector2 plus(float addendX, float addendY) {
    return Vector2.of(this.x + addendX, this.y + addendY);
  }

  @Override
  public @NonNull Vector2 minus(@NonNull Vector2 subtrahend) {
    return this.minus(subtrahend.x, subtrahend.y);
  }

  public @NonNull Vector2 minus(float subtrahendX, float subtrahendY) {
    return Vector2.of(this.x - subtrahendX, this.y - subtrahendY);
  }

  @Override
  public @NonNull Vector2 times(float factor) {
    return this.times(factor, factor);
  }

  @Override
  public @NonNull Vector2 times(@NonNull Vector2 factor) {
    return this.times(factor.x, factor.y);
  }

  public @NonNull Vector2 times(float factorX, float factorY) {
    return Vector2.of(this.x * factorX, this.y * factorY);
  }

  @Override
  public @NonNull Vector2 divide(float divisor) {
    return this.divide(divisor, divisor);
  }

  @Override
  public @NonNull Vector2 divide(@NonNull Vector2 divisor) {
    return this.divide(divisor.x, divisor.y);
  }

  public @NonNull Vector2 divide(float divisorX, float divisorY) {
    if (divisorX == 0 || divisorY == 0)
      throw new ArithmeticException(
          "Division by zero: " + this + " / " + Vector2.of(divisorX, divisorY));
    return Vector2.of(this.x / divisorX, this.y / divisorY);
  }

  @Override
  public @NonNull String toString() {
    return String.format("Vector2{%.2f, %.2f}", this.x, this.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Vector2.class) return false;
    final var obj = (Vector2) o;
    return this.x == obj.x && this.y == obj.y;
  }
}
