package jakemarsden.opengl.engine.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CountedRefTest {

  @Test
  void takeRefReturnsCorrectRef() {
    final var instance = new Object();
    final var obj = CountedRef.create(instance);

    assertSame(instance, obj.takeRef());
    assertSame(instance, obj.takeRef());
  }

  @Test
  void trueReturnedOnlyWhenLastRefReturned() {
    final var instance = new Object();
    final var obj = CountedRef.create(instance);

    obj.takeRef();
    obj.takeRef();

    assertFalse(obj.returnRef(instance));
    assertTrue(obj.returnRef(instance));
  }

  @Test
  void returningTooManyRefsThrows() {
    final var instance = new Object();
    final var obj = CountedRef.create(instance);

    obj.takeRef();
    obj.returnRef(instance);

    assertThrows(IllegalStateException.class, () -> obj.returnRef(instance));
  }

  @Test
  void returningRefImmediatelyAfterCreationThrows() {
    final var instance = new Object();
    final var obj = CountedRef.create(instance);

    assertThrows(IllegalStateException.class, () -> obj.returnRef(instance));
  }

  @Test
  void returningWrongRefThrows() {
    final var instance = new Object();
    final var obj = CountedRef.create(instance);

    obj.takeRef();

    assertThrows(IllegalArgumentException.class, () -> obj.returnRef(new Object()));
  }
}
