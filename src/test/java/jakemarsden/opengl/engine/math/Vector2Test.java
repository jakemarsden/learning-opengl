package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.EPSILON;
import static jakemarsden.opengl.engine.math.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector2Test {

  @Test
  void createZero() {
    final var v1 = Vector2.zero();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
  }

  @Test
  void createUnit() {
    final var v1 = Vector2.unit(3.0f, 4.0f);
    assertEquals(0.6f, v1.x, EPSILON);
    assertEquals(0.8f, v1.y, EPSILON);
  }

  @Test
  void createXY() {
    final var v1 = Vector2.of(3.0f, 4.0f);
    assertEquals(3.0f, v1.x, 0);
    assertEquals(4.0f, v1.y, 0);
  }

  @Test
  void normalise() {
    final var v1 = Vector2.of(3, 4).normalise();
    assertEquals(0.6f, v1.x, EPSILON);
    assertEquals(0.8f, v1.y, EPSILON);
  }

  @Test
  void normalisedZeroIsZero() {
    final var v1 = Vector2.zero().normalise();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
  }

  @Test
  void length() {
    assertEquals(5.0f, Vector2.of(3, 4).length(), EPSILON);
    assertEquals(13.0f, Vector2.of(5, 12).length(), EPSILON);
    assertEquals(17.0f, Vector2.of(8, 15).length(), EPSILON);
    assertEquals(25.0f, Vector2.of(7, 24).length(), EPSILON);
  }

  @Test
  void lengthOfZero() {
    assertEquals(0.0f, Vector2.zero().length(), 0);
  }

  @Test
  void length2() {
    assertEquals(25.0f, Vector2.of(3, 4).length2(), EPSILON);
    assertEquals(169.0f, Vector2.of(5, 12).length2(), EPSILON);
    assertEquals(289.0f, Vector2.of(8, 15).length2(), EPSILON);
    assertEquals(625.0f, Vector2.of(7, 24).length2(), EPSILON);
  }

  @Test
  void length2OfZero() {
    assertEquals(0.0f, Vector2.zero().length2(), 0);
  }

  @Test
  void dot() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    assertEquals(84.0f, v1.dot(v2));
  }

  @Test
  void negate() {
    final var v1 = Vector2.of(3, -4).negate();
    assertEquals(-3.0f, v1.x, 0);
    assertEquals(4.0f, v1.y, 0);
  }

  @Test
  void reciprocal() {
    final var v1 = Vector2.of(3, 4).reciprocal();
    assertEquals(0.33333333f, v1.x, EPSILON);
    assertEquals(0.25f, v1.y, EPSILON);
  }

  @Test
  void plus() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    final var v3 = v1.plus(v2);
    assertEquals(11.0f, v3.x, EPSILON);
    assertEquals(19.0f, v3.y, EPSILON);
  }

  @Test
  void minus() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    final var v3 = v1.minus(v2);
    assertEquals(-5.0f, v3.x, EPSILON);
    assertEquals(-11.0f, v3.y, EPSILON);
  }

  @Test
  void times() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    final var v3 = v1.times(v2);
    assertEquals(24.0f, v3.x, EPSILON);
    assertEquals(60.0f, v3.y, EPSILON);
  }

  @Test
  void divide() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    final var v3 = v1.divide(v2);
    assertEquals(0.375f, v3.x, EPSILON);
    assertEquals(0.26666666f, v3.y, EPSILON);
  }

  @Test
  void asString() {
    final var v1 = Vector2.of(EPSILON, -PI);
    assertEquals("Vector2{0.00, -3.14}", v1.toString());
  }

  @Test
  void hashCodesOfEqualObjectsAreSame() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(3, 4);
    assertEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void hashCodesOfDifferingObjectsAreUsuallyDifferent() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    assertNotEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void equals() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(3, 4);
    assertEquals(v1, v2);
  }

  @Test
  void notEquals() {
    final var v1 = Vector2.of(3, 4);
    final var v2 = Vector2.of(8, 15);
    assertNotEquals(v1, v2);
  }

  @Test
  void differentClassIsNotEqual() {
    final var v1 = Vector2.of(3, 4);
    assertNotEquals(new Object(), v1);
  }
}
