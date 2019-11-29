package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.EPSILON;
import static jakemarsden.opengl.engine.math.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector4Test {

  @Test
  void createZero() {
    final var v1 = Vector4.zero();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
    assertEquals(0.0f, v1.z, 0);
    assertEquals(0.0f, v1.w, 0);
  }

  @Test
  void createUnit() {
    final var v1 = Vector4.unit(5.0f, 7.0f, 31.0f, 101.0f);
    assertEquals(0.04716981f, v1.x, EPSILON);
    assertEquals(0.06603773f, v1.y, EPSILON);
    assertEquals(0.29245283f, v1.z, EPSILON);
    assertEquals(0.95283018f, v1.w, EPSILON);
  }

  @Test
  void createXYZW() {
    final var v1 = Vector4.of(5.0f, 7.0f, 31.0f, 101.0f);
    assertEquals(5.0f, v1.x, 0);
    assertEquals(7.0f, v1.y, 0);
    assertEquals(31.0f, v1.z, 0);
    assertEquals(101.0f, v1.w, 0);
  }

  @Test
  void normalise() {
    final var v1 = Vector4.of(5, 7, 31, 101).normalise();
    assertEquals(0.04716981f, v1.x, EPSILON);
    assertEquals(0.06603773f, v1.y, EPSILON);
    assertEquals(0.29245283f, v1.z, EPSILON);
    assertEquals(0.95283018f, v1.w, EPSILON);
  }

  @Test
  void normalisedZeroIsZero() {
    final var v1 = Vector4.zero().normalise();
    assertEquals(0.0f, v1.x, 0);
    assertEquals(0.0f, v1.y, 0);
    assertEquals(0.0f, v1.z, 0);
    assertEquals(0.0f, v1.w, 0);
  }

  @Test
  void length() {
    assertEquals(106.0f, Vector4.of(5, 7, 31, 101).length(), EPSILON);
  }

  @Test
  void lengthOfZero() {
    assertEquals(0.0f, Vector4.zero().length(), 0);
  }

  @Test
  void length2() {
    assertEquals(11236.0f, Vector4.of(5, 7, 31, 101).length2(), EPSILON);
  }

  @Test
  void length2OfZero() {
    assertEquals(0.0f, Vector4.zero().length2(), 0);
  }

  @Test
  void dot() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    assertEquals(516.0f, v1.dot(v2));
  }

  @Test
  void negate() {
    final var v1 = Vector4.of(5, -7, 31, -101).negate();
    assertEquals(-5.0f, v1.x, 0);
    assertEquals(7.0f, v1.y, 0);
    assertEquals(-31.0f, v1.z, 0);
    assertEquals(101.0f, v1.w, 0);
  }

  @Test
  void reciprocal() {
    final var v1 = Vector4.of(5, 7, 31, 101).reciprocal();
    assertEquals(0.2f, v1.x, EPSILON);
    assertEquals(0.14285714f, v1.y, EPSILON);
    assertEquals(0.03225806f, v1.z, EPSILON);
    assertEquals(0.00990099f, v1.w, EPSILON);
  }

  @Test
  void plus() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    final var v3 = v1.plus(v2);
    assertEquals(6.0f, v3.x, EPSILON);
    assertEquals(9.0f, v3.y, EPSILON);
    assertEquals(34.0f, v3.z, EPSILON);
    assertEquals(105.0f, v3.w, EPSILON);
  }

  @Test
  void minus() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    final var v3 = v1.minus(v2);
    assertEquals(4.0f, v3.x, EPSILON);
    assertEquals(5.0f, v3.y, EPSILON);
    assertEquals(28.0f, v3.z, EPSILON);
    assertEquals(97.0f, v3.w, EPSILON);
  }

  @Test
  void times() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    final var v3 = v1.times(v2);
    assertEquals(5.0f, v3.x, EPSILON);
    assertEquals(14.0f, v3.y, EPSILON);
    assertEquals(93.0f, v3.z, EPSILON);
    assertEquals(404.0f, v3.w, EPSILON);
  }

  @Test
  void divide() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    final var v3 = v1.divide(v2);
    assertEquals(5f, v3.x, EPSILON);
    assertEquals(3.5f, v3.y, EPSILON);
    assertEquals(10.33333333f, v3.z, EPSILON);
    assertEquals(25.25f, v3.w, EPSILON);
  }

  @Test
  void asString() {
    final var v1 = Vector4.of(EPSILON, -PI, PI, -PI);
    assertEquals("Vector4{0.00, -3.14, 3.14, -3.14}", v1.toString());
  }

  @Test
  void hashCodesOfEqualObjectsAreSame() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(5, 7, 31, 101);
    assertEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void hashCodesOfDifferingObjectsAreUsuallyDifferent() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    assertNotEquals(v1.hashCode(), v2.hashCode());
  }

  @Test
  void equals() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(5, 7, 31, 101);
    assertEquals(v1, v2);
  }

  @Test
  void notEquals() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = Vector4.of(1, 2, 3, 4);
    assertNotEquals(v1, v2);
  }

  @Test
  void differentClassIsNotEqual() {
    final var v1 = Vector4.of(5, 7, 31, 101);
    assertNotEquals(new Object(), v1);
  }
}
