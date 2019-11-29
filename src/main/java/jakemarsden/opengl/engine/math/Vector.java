package jakemarsden.opengl.engine.math;

import org.checkerframework.checker.nullness.qual.NonNull;

interface Vector<TThis extends Vector<TThis>> {

  TThis normalise();

  float length();

  float length2();

  float dot(@NonNull TThis operand);

  @NonNull
  TThis negate();

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
