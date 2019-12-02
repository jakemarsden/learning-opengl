package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Color4 implements Vector<Color4> {

  private static final Color4 TRANSPARENT = Color4.gray(0, 0);

  public final float r;
  public final float g;
  public final float b;
  public final float a;

  public static @NonNull Color4 transparent() {
    return TRANSPARENT;
  }

  public static @NonNull Color4 black(float alpha) {
    return Color4.gray(0, alpha);
  }

  public static @NonNull Color4 white(float alpha) {
    return Color4.gray(1, alpha);
  }

  public static @NonNull Color4 gray(float brightness, float alpha) {
    return Color4.rgba(brightness, brightness, brightness, alpha);
  }

  public static @NonNull Color4 rgba(@NonNull Color3 rgb, float a) {
    return Color4.rgba(rgb.r, rgb.g, rgb.b, a);
  }

  public static @NonNull Color4 rgba(float r, float g, float b, float a) {
    return new Color4(r, g, b, a);
  }

  private Color4(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  public @NonNull Color3 rgb() {
    return Color3.rgb(this.r, this.g, this.b);
  }

  @Override
  public float dot(@NonNull Color4 operand) {
    return this.r * operand.r + this.g * operand.g + this.b * operand.b + this.a * operand.a;
  }

  @Override
  public @NonNull Color4 reciprocal() {
    if (this.r == 0 || this.g == 0 || this.b == 0 || this.a == 0)
      throw new ArithmeticException("Division by zero: 1 / " + this);
    return Color4.rgba(1 / this.r, 1 / this.g, 1 / this.b, 1 / this.a);
  }

  @Override
  public @NonNull Color4 plus(@NonNull Color4 addend) {
    return this.plus(addend.r, addend.g, addend.b, addend.a);
  }

  public @NonNull Color4 plus(float addendR, float addendG, float addendB, float addendA) {
    return Color4.rgba(this.r + addendR, this.g + addendG, this.b + addendB, this.a + addendA);
  }

  @Override
  public @NonNull Color4 minus(@NonNull Color4 subtrahend) {
    return this.minus(subtrahend.r, subtrahend.g, subtrahend.b, subtrahend.a);
  }

  public @NonNull Color4 minus(
      float subtrahendR, float subtrahendG, float subtrahendB, float subtrahendA) {

    return Color4.rgba(
        this.r - subtrahendR, this.g - subtrahendG, this.b - subtrahendB, this.a - subtrahendA);
  }

  @Override
  public @NonNull Color4 times(float factor) {
    return this.times(factor, factor, factor, factor);
  }

  @Override
  public @NonNull Color4 times(@NonNull Color4 factor) {
    return this.times(factor.r, factor.g, factor.b, factor.a);
  }

  public @NonNull Color4 times(float factorR, float factorG, float factorB, float factorA) {
    return Color4.rgba(this.r * factorR, this.g * factorG, this.b * factorB, this.a * factorA);
  }

  @Override
  public @NonNull Color4 divide(float divisor) {
    return this.divide(divisor, divisor, divisor, divisor);
  }

  @Override
  public @NonNull Color4 divide(@NonNull Color4 divisor) {
    return this.divide(divisor.r, divisor.g, divisor.b, divisor.a);
  }

  public @NonNull Color4 divide(float divisorR, float divisorG, float divisorB, float divisorA) {
    return Color4.rgba(this.r / divisorR, this.g / divisorG, this.b / divisorB, this.a / divisorA);
  }

  @Override
  public @NonNull String toString() {
    return String.format("Color4{%.2f, %.2f, %.2f, %.2f}", this.r, this.g, this.b, this.a);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.r, this.g, this.b, this.a);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Color4.class) return false;
    final var obj = (Color4) o;
    return this.r == obj.r && this.g == obj.g && this.b == obj.b && this.a == obj.a;
  }
}
