package jakemarsden.opengl.engine.util;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Lazy<T> implements Supplier<@NonNull T> {

  private @Nullable Supplier<@NonNull T> initialiser;
  private @MonotonicNonNull T value;

  public static <T> @NonNull Lazy<T> create(@NonNull Supplier<@NonNull T> initialiser) {
    return new Lazy<>(initialiser);
  }

  private Lazy(@NonNull Supplier<@NonNull T> initialiser) {
    this.initialiser = initialiser;
  }

  @Override
  public @NonNull T get() {
    if (this.value == null) {
      this.value = requireNonNull(this.initialiser).get();
      this.initialiser = null;
    }
    return this.value;
  }
}
