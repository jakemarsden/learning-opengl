package jakemarsden.opengl.engine.math;

import org.checkerframework.checker.nullness.qual.NonNull;

interface Matrix<TVec extends Vector, TThis extends Matrix<TVec, TThis>> {

  @NonNull
  TThis invert();

  /** Mirrors the values along the major diagonal */
  @NonNull
  TThis transpose();

  @NonNull
  TVec times(@NonNull TVec vector);

  @NonNull
  TThis times(@NonNull TThis factor);

  float[] toArray();
}
