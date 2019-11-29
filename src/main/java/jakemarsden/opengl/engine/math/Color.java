package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Color {

  private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

  public final float r;
  public final float g;
  public final float b;
  public final float a;

  public static @NonNull Color transparent() {
    return TRANSPARENT;
  }

  public static @NonNull Color gray(float brightness) {
    return Color.rgb(brightness, brightness, brightness);
  }

  public static @NonNull Color rgb(float r, float g, float b) {
    return Color.rgba(r, g, b, 1);
  }

  public static @NonNull Color rgba(float r, float g, float b, float a) {
    return new Color(r, g, b, a);
  }

  private Color(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  @Override
  public @NonNull String toString() {
    return String.format("Color{%.2f, %.2f, %.2f, %.2f}", this.r, this.g, this.b, this.a);
  }

  @Override
  public int hashCode() {
    if (this.a == 0) return 0;
    return Objects.hash(this.r, this.g, this.b, this.a);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != Color.class) return false;
    final var obj = (Color) o;
    if (this.a != obj.a) return false;
    if (this.a == 0) return true;
    return this.r == obj.r && this.g == obj.g && this.b == obj.b;
  }
}
