package jakemarsden.opengl.engine.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Keeps track of how many times a given instance is currently being "used", and tells you when
 * everyone's stopped using it and it's time to clean up
 */
public final class CountedRef<T> {

  private @Nullable T instance;
  private int refCount = 0;

  public static <T> @NonNull CountedRef<T> create(@NonNull T instance) {
    return new CountedRef<>(instance);
  }

  private CountedRef(@NonNull T instance) {
    this.instance = instance;
  }

  public @NonNull T takeRef() {
    if (this.instance == null) throw new IllegalStateException();
    this.refCount++;
    return this.instance;
  }

  public boolean returnRef(@NonNull T instance) {
    if (this.refCount <= 0) throw new IllegalStateException();
    if (instance != this.instance) throw new IllegalArgumentException();

    final var shouldDestroy = --this.refCount == 0;
    if (shouldDestroy) this.instance = null;
    return shouldDestroy;
  }
}
