package jakemarsden.opengl.engine.math;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @param <TVec> the type of vector the subclass can operate on
 * @param <TThis> must match the exact type of the subclass, e.g. {@code final class MyMatrix
 *     implements Matrix<MyVector, MyMatrix> {}}
 */
interface Matrix<TVec extends Vector<TVec>, TThis extends Matrix<TVec, TThis>> {

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
