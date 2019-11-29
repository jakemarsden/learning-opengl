package jakemarsden.opengl.engine.math;

import static jakemarsden.opengl.engine.math.Math.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Matrix4Test {

  @Test
  void createIdentity() {
    final float[] expected = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    final var m1 = Matrix4.identity();
    assertArrayEquals(expected, m1.toArray(), 0);
  }

  @Test
  void createTranslate() {
    final float[] expected = {
      1, 0, 0, 2,
      0, 1, 0, 3,
      0, 0, 1, 4,
      0, 0, 0, 1
    };
    final var m1 = Matrix4.translate(Vector3.of(2, 3, 4));
    assertArrayEquals(expected, m1.toArray(), 0);
  }

  @Test
  void createRotateX() {
    final var t = PI / 4;
    final float[] expected = {
      1, 0, 0, 0, //
      0, cos(t), -sin(t), 0, //
      0, sin(t), cos(t), 0, //
      0, 0, 0, 1
    };
    final var m1 = Matrix4.rotate(Vector3.of(t, 0, 0));
    assertArrayEquals(expected, m1.toArray(), EPSILON);
  }

  @Test
  void createRotateY() {
    final var t = PI / 4;
    final float[] expected = {
      cos(t), 0, sin(t), 0, //
      0, 1, 0, 0, //
      -sin(t), 0, cos(t), 0, //
      0, 0, 0, 1
    };
    final var m1 = Matrix4.rotate(Vector3.of(0, t, 0));
    assertArrayEquals(expected, m1.toArray(), EPSILON);
  }

  @Test
  void createRotateZ() {
    final var t = PI / 4;
    final float[] expected = {
      cos(t), -sin(t), 0, 0, //
      sin(t), cos(t), 0, 0, //
      0, 0, 1, 0, //
      0, 0, 0, 1
    };
    final var m1 = Matrix4.rotate(Vector3.of(0, 0, t));
    assertArrayEquals(expected, m1.toArray(), EPSILON);
  }

  @Test
  void createScale() {
    final float[] expected = {
      2, 0, 0, 0,
      0, 3, 0, 0,
      0, 0, 4, 0,
      0, 0, 0, 1
    };
    final var m1 = Matrix4.scale(Vector3.of(2, 3, 4));
    assertArrayEquals(expected, m1.toArray(), 0);
  }

  @Test
  void identityInvertedIsIdentity() {
    final float[] expected = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    final var m1 = Matrix4.identity().invert();
    assertArrayEquals(expected, m1.toArray(), EPSILON);
  }

  @Test
  void invertTranslation() {
    final float[] expected = {
      1, 0, 0, -2,
      0, 1, 0, 3,
      0, 0, 1, -4,
      0, 0, 0, 1
    };
    final float[] m1 = {
      1, 0, 0, 2,
      0, 1, 0, -3,
      0, 0, 1, 4,
      0, 0, 0, 1
    };
    final var m2 = Matrix4.of(m1).invert();
    assertArrayEquals(expected, m2.toArray(), EPSILON);
  }

  @Test
  void invertRotation() {
    // TODO: implement test
  }

  @Test
  void invertScale() {
    final float[] expected = {
      -0.5f, 0, 0, 0, //
      0, 2, 0, 0, //
      0, 0, 0.25f, 0, //
      0, 0, 0, 1
    };
    final float[] m1 = {
      -2, 0, 0, 0,
      0, 0.5f, 0, 0,
      0, 0, 4, 0,
      0, 0, 0, 1
    };
    final var m2 = Matrix4.of(m1).invert();
    assertArrayEquals(expected, m2.toArray(), EPSILON);
  }

  @Test
  void transpose() {
    final float[] expected = {
      11, 12, 13, 14,
      21, 22, 23, 24,
      31, 32, 33, 34,
      41, 42, 43, 44
    };
    final float[] m1 = {
      11, 21, 31, 41,
      12, 22, 32, 42,
      13, 23, 33, 43,
      14, 24, 34, 44
    };
    final var m2 = Matrix4.of(m1).transpose();
    assertArrayEquals(expected, m2.toArray(), 0);
  }

  @Test
  void vectorTimesIdentityIsUnchanged() {
    final var m1 = Matrix4.identity();
    final var v1 = Vector4.of(5, 7, 31, 101);
    final var v2 = m1.times(v1);
    assertEquals(5.0f, v2.x, 0);
    assertEquals(7.0f, v2.y, 0);
    assertEquals(31.0f, v2.z, 0);
    assertEquals(101.0f, v2.w, 0);
  }

  @Test
  void identityTimesIdentityIsIdentity() {
    final float[] expected = {
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    };
    final var m1 = Matrix4.identity();
    final var m2 = Matrix4.identity();
    final var m3 = m1.times(m2);
    assertArrayEquals(expected, m3.toArray(), 0);
  }

  @Test
  void identityAsString() {
    assertEquals(
        "Matrix4{{1.00, 0.00, 0.00, 0.00}, "
            + "{0.00, 1.00, 0.00, 0.00}, "
            + "{0.00, 0.00, 1.00, 0.00}, "
            + "{0.00, 0.00, 0.00, 1.00}}",
        Matrix4.identity().toString());
  }
}
