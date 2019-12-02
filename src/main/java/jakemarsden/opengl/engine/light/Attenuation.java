package jakemarsden.opengl.engine.light;

import static jakemarsden.opengl.engine.math.Math.pow;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class Attenuation {

  /** constant factor */
  public final float k;
  /** linear factor */
  public final float l;
  /** quadratic factor */
  public final float q;

  /** No attenuation, i.e. full intensity regardless of distance */
  public static @NonNull Attenuation none() {
    return Attenuation.of(1, 0, 0);
  }

  /**
   * Tends to give good values for lights which start at 100% intensity, but aren't immediately
   * noticeable at ~20% of the specified range
   *
   * @param range the distance from the light at which the intensity should be (very close to) zero
   * @see <a href="http://wiki.ogre3d.org/tiki-index.php?page=-Point+Light+Attenuation">-Point Light
   *     Attenuation | Ogre Wiki</a>
   * @see <a href="http://wiki.ogre3d.org/Light+Attenuation+Shortcut">Light Attenuation Shortcut |
   *     Ogre Wiki</a>
   */
  public static @NonNull Attenuation range(float range) {
    return Attenuation.of(1, 4.5f / range, 75.0f / pow(range, 2));
  }

  public static @NonNull Attenuation of(float k, float l, float q) {
    return new Attenuation(k, l, q);
  }

  private Attenuation(float k, float l, float q) {
    this.k = k;
    this.l = l;
    this.q = q;
  }

  /** How intense a light should be at a given distance from it */
  public float calculateIntensity(float distance) {
    return 1 / (this.k + this.l * distance + this.q * pow(distance, 2));
  }

  @Override
  public @NonNull String toString() {
    return String.format("Attenuation{k=%.2f, l=%.2f, q=%.2f}", this.k, this.l, this.q);
  }
}
