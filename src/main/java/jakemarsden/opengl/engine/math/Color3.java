package jakemarsden.opengl.engine.math;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Color3 {

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
