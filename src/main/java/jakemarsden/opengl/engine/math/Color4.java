package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Color4 {

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
