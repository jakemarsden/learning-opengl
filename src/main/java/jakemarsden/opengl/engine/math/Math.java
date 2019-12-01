package jakemarsden.opengl.engine.math;

/**
 * Simple wrapper around Java's {@link java.lang.Math math utils}. The main reason is to reduce the
 * noise from {@code double -> float} casts littered everywhere, although a lesser advantage is that
 * later optimisations will be easier (e.g. lookup tables for trig functions). I'm doubtful any
 * optimisation will be necessary, let alone manage to beat modern silicon, but it's nice to keep
 * the option open
 */
public final class Math {

  public static final float EPSILON = 1e-6f;
  public static final float PI = (float) java.lang.Math.PI;

  public static float pow(float base, float exponent) {
    return (float) java.lang.Math.pow(base, exponent);
  }

  public static float sqrt(float value) {
    return (float) java.lang.Math.sqrt(value);
  }

  public static float sin(float theta) {
    return (float) java.lang.Math.sin(theta);
  }

  public static float cos(float theta) {
    return (float) java.lang.Math.cos(theta);
  }

  public static float tan(float theta) {
    return (float) java.lang.Math.tan(theta);
  }

  public static float cotan(float theta) {
    return 1 / (float) java.lang.Math.tan(theta);
  }

  private Math() {
    throw new UnsupportedOperationException();
  }
}
