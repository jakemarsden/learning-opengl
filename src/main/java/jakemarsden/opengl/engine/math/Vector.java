package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.sqrt;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @param <TThis> must match the exact type of the subclass, e.g. {@code final class MyVector
 *     implements Vector<MyVector> {}}
 */
interface Vector<TThis extends Vector<TThis>> {

  default float length() {
    return sqrt(this.length2());
  }

  // assume `this` matches type `TThis`, which is true if the subclass used `<TThis>` correctly
  @SuppressWarnings("unchecked")
  default float length2() {
    return this.dot((TThis) this);
  }

  float dot(@NonNull TThis operand);

  // assume `this` matches type `TThis`, which is true if the subclass used `<TThis>` correctly
  @SuppressWarnings("unchecked")
  default @NonNull TThis normalise() {
    final var length2 = this.length2();
    if (length2 == 0 || length2 == 1) return (TThis) this;
    return this.divide(sqrt(length2));
  }

  default @NonNull TThis negate() {
    return this.times(-1);
  }

  @NonNull
  TThis reciprocal();

  @NonNull
  TThis plus(@NonNull TThis addend);

  @NonNull
  TThis minus(@NonNull TThis subtrahend);

  @NonNull
  TThis times(float factor);

  @NonNull
  TThis times(@NonNull TThis factor);

  @NonNull
  TThis divide(float divisor);

  @NonNull
  TThis divide(@NonNull TThis divisor);
}
