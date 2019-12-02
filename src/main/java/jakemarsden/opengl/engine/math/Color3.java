package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Color3 implements Vector<Color3> {

  private static final Color3 BLACK = Color3.gray(0);
  private static final Color3 WHITE = Color3.gray(1);

  public final float r;
  public final float g;
  public final float b;

  public static @NonNull Color3 black() {
    return BLACK;
  }

  public static @NonNull Color3 white() {
    return WHITE;
  }

  public static @NonNull Color3 gray(float brightness) {
    return Color3.rgb(brightness, brightness, brightness);
  }

  public static @NonNull Color3 rgb(float r, float g, float b) {
    return new Color3(r, g, b);
  }

  private Color3(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public float dot(@NonNull Color3 operand) {
    return this.r * operand.r + this.g * operand.g + this.b * operand.b;
  }

  @Override
  public @NonNull Color3 reciprocal() {
    if (this.r == 0 || this.g == 0 || this.b == 0)
      throw new ArithmeticException("Division by zero: 1 / " + this);
    return Color3.rgb(1 / this.r, 1 / this.g, 1 / this.b);
  }

  @Override
  public @NonNull Color3 plus(@NonNull Color3 addend) {
    return this.plus(addend.r, addend.g, addend.b);
  }

  public @NonNull Color3 plus(float addendR, float addendG, float addendB) {
    return Color3.rgb(this.r + addendR, this.g + addendG, this.b + addendB);
  }

  @Override
  public @NonNull Color3 minus(@NonNull Color3 subtrahend) {
    return this.minus(subtrahend.r, subtrahend.g, subtrahend.b);
  }

  public @NonNull Color3 minus(float subtrahendR, float subtrahendG, float subtrahendB) {
    return Color3.rgb(this.r - subtrahendR, this.g - subtrahendG, this.b - subtrahendB);
  }

  @Override
  public @NonNull Color3 times(float factor) {
    return this.times(factor, factor, factor);
  }

  @Override
  public @NonNull Color3 times(@NonNull Color3 factor) {
    return this.times(factor.r, factor.g, factor.b);
  }

  public @NonNull Color3 times(float factorR, float factorG, float factorB) {
    return Color3.rgb(this.r * factorR, this.g * factorG, this.b * factorB);
  }

  @Override
  public @NonNull Color3 divide(float divisor) {
    return this.divide(divisor, divisor, divisor);
  }

  @Override
  public @NonNull Color3 divide(@NonNull Color3 divisor) {
    return this.divide(divisor.r, divisor.g, divisor.b);
  }

  public @NonNull Color3 divide(float divisorR, float divisorG, float divisorB) {
    return Color3.rgb(this.r / divisorR, this.g / divisorG, this.b / divisorB);
  }

  @Override
  public @NonNull String toString() {
    return String.format("Color3{%.2f, %.2f, %.2f}", this.r, this.g, this.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.r, this.g, this.b);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Color3.class) return false;
    final var obj = (Color3) o;
    return this.r == obj.r && this.g == obj.g && this.b == obj.b;
  }
}
