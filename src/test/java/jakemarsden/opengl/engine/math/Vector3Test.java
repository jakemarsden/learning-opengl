package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.EPSILON;
import static jakemarsden.opengl.engine.math.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector3Test {

  @Test
  void createZero() {
    final var v1 = Vector3.zero();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
    assertEquals(0.0f, v1.z, 0);
  }

  @Test
  void createUnit() {
    final var v1 = Vector3.unit(2.0f, 3.0f, 6.0f);
    assertEquals(0.28571428f, v1.x, EPSILON);
    assertEquals(0.42857142f, v1.y, EPSILON);
    assertEquals(0.85714285f, v1.z, EPSILON);
  }

  @Test
  void createXYZ() {
    final var v1 = Vector3.of(2.0f, 3.0f, 6.0f);
    assertEquals(2.0f, v1.x, 0);
    assertEquals(3.0f, v1.y, 0);
    assertEquals(6.0f, v1.z, 0);
  }

  @Test
  void normalise() {
    final var v1 = Vector3.of(2, 3, 6).normalise();
    assertEquals(0.28571428f, v1.x, EPSILON);
    assertEquals(0.42857142f, v1.y, EPSILON);
    assertEquals(0.85714285f, v1.z, EPSILON);
  }

  @Test
  void normalisedZeroIsZero() {
    final var v1 = Vector3.zero().normalise();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
    assertEquals(0.0f, v1.z, 0);
  }

  @Test
  void length() {
    assertEquals(7.0f, Vector3.of(2, 3, 6).length(), EPSILON);
    assertEquals(15.0f, Vector3.of(2, 10, 11).length(), EPSILON);
    assertEquals(21.0f, Vector3.of(4, 13, 16).length(), EPSILON);
    assertEquals(27.0f, Vector3.of(2, 10, 25).length(), EPSILON);
  }

  @Test
  void lengthOfZero() {
    assertEquals(0.0f, Vector3.zero().length(), 0);
  }

  @Test
  void length2() {
    assertEquals(49.0f, Vector3.of(2, 3, 6).length2(), EPSILON);
    assertEquals(225.0f, Vector3.of(2, 10, 11).length2(), EPSILON);
    assertEquals(441.0f, Vector3.of(4, 13, 16).length2(), EPSILON);
    assertEquals(729.0f, Vector3.of(2, 10, 25).length2(), EPSILON);
  }

  @Test
  void length2OfZero() {
    assertEquals(0.0f, Vector3.zero().length2(), 0);
  }

  @Test
  void dot() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    assertEquals(143.0f, v1.dot(v2));
  }

  @Test
  void cross() {
    // TODO implement test
  }

  @Test
  void negate() {
    final var v1 = Vector3.of(2, -3, 6).negate();
    assertEquals(-2.0f, v1.x, 0);
    assertEquals(3.0f, v1.y, 0);
    assertEquals(-6.0f, v1.z, 0);
  }

  @Test
  void reciprocal() {
    final var v1 = Vector3.of(2, 3, 6).reciprocal();
    assertEquals(0.5f, v1.x, EPSILON);
    assertEquals(0.33333333f, v1.y, EPSILON);
    assertEquals(0.16666666f, v1.z, EPSILON);
  }

  @Test
  void plus() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    final var v3 = v1.plus(v2);
    assertEquals(6.0f, v3.x, EPSILON);
    assertEquals(16.0f, v3.y, EPSILON);
    assertEquals(22.0f, v3.z, EPSILON);
  }

  @Test
  void minus() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    final var v3 = v1.minus(v2);
    assertEquals(-2.0f, v3.x, EPSILON);
    assertEquals(-10.0f, v3.y, EPSILON);
    assertEquals(-10.0f, v3.z, EPSILON);
  }

  @Test
  void times() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    final var v3 = v1.times(v2);
    assertEquals(8.0f, v3.x, EPSILON);
    assertEquals(39.0f, v3.y, EPSILON);
    assertEquals(96.0f, v3.z, EPSILON);
  }

  @Test
  void divide() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    final var v3 = v1.divide(v2);
    assertEquals(0.5f, v3.x, EPSILON);
    assertEquals(0.23076923f, v3.y, EPSILON);
    assertEquals(0.375f, v3.z, EPSILON);
  }

  @Test
  void asString() {
    final var v1 = Vector3.of(EPSILON, -PI, PI);
    assertEquals("Vector3{0.00, -3.14, 3.14}", v1.toString());
  }

  @Test
  void hashCodesOfEqualObjectsAreSame() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(2, 3, 6);
    assertEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void hashCodesOfDifferingObjectsAreUsuallyDifferent() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    assertNotEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void equals() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(2, 3, 6);
    assertEquals(v1, v2);
  }

  @Test
  void notEquals() {
    final var v1 = Vector3.of(2, 3, 6);
    final var v2 = Vector3.of(4, 13, 16);
    assertNotEquals(v1, v2);
  }

  @Test
  void differentClassIsNotEqual() {
    final var v1 = Vector3.of(2, 3, 6);
    assertNotEquals(new Object(), v1);
  }
}
