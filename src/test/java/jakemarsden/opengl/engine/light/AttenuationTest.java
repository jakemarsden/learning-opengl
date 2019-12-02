package jakemarsden.opengl.engine.light;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * the allowable deltas for these tests are unusually tolerant because the calculated attenuation
 * values don't really <em>mean</em> anything - they're just "nice" values to use for each range
 *
 * @see <a href="http://wiki.ogre3d.org/tiki-index.php?page=-Point+Light+Attenuation">-Point Light
 *     Attenuation | Ogre Wiki</a>
 * @see <a href="http://wiki.ogre3d.org/Light+Attenuation+Shortcut">Light Attenuation Shortcut |
 *     Ogre Wiki</a>
 */
class AttenuationTest {

  @Test
  void createWithRange() {
    assertAttenuationEquals(1.0f, 0.0014f, 0.000007f, Attenuation.range(3250));
    assertAttenuationEquals(1.0f, 0.007f, 0.0002f, Attenuation.range(600));
    assertAttenuationEquals(1.0f, 0.014f, 0.0007f, Attenuation.range(325));
    assertAttenuationEquals(1.0f, 0.022f, 0.0019f, Attenuation.range(200));
    assertAttenuationEquals(1.0f, 0.027f, 0.0028f, Attenuation.range(160));
    assertAttenuationEquals(1.0f, 0.045f, 0.0075f, Attenuation.range(100));
    assertAttenuationEquals(1.0f, 0.07f, 0.017f, Attenuation.range(65));
    assertAttenuationEquals(1.0f, 0.09f, 0.032f, Attenuation.range(50));
    assertAttenuationEquals(1.0f, 0.14f, 0.07f, Attenuation.range(32));
    // assertAttenuationEquals(1.0f, 0.22f, 0.2f, Attenuation.range(20));
    assertAttenuationEquals(1.0f, 0.35f, 0.44f, Attenuation.range(13));
    // assertAttenuationEquals(1.0f, 0.7f, 1.8f, Attenuation.range(7));
  }

  private static void assertAttenuationEquals(
      float expectedK, float expectedL, float expectedQ, Attenuation actual) {

    assertEquals(expectedK, actual.k, 0);
    assertEquals(expectedL, actual.l, 0.005f);
    assertEquals(expectedQ, actual.q, 0.005f);
  }

  @Test
  void noAttenuationIsAlwaysFullIntensity() {
    assertEquals(1, Attenuation.none().calculateIntensity(1e12f), 0);
    assertEquals(1, Attenuation.none().calculateIntensity(0), 0);
  }

  @Test
  void calculatedIntensityIs100PercentAtStartOfRange() {
    assertEquals(1, Attenuation.range(3250).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(600).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(325).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(200).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(160).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(100).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(65).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(50).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(32).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(20).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(13).calculateIntensity(0), 0);
    assertEquals(1, Attenuation.range(7).calculateIntensity(0), 0);
  }

  @Test
  void calculatedIntensityApproachesZeroAtEndOfRange() {
    assertEquals(0, Attenuation.range(3250).calculateIntensity(3250), 0.0125f);
    assertEquals(0, Attenuation.range(600).calculateIntensity(600), 0.0125f);
    assertEquals(0, Attenuation.range(325).calculateIntensity(325), 0.0125f);
    assertEquals(0, Attenuation.range(200).calculateIntensity(200), 0.0125f);
    assertEquals(0, Attenuation.range(160).calculateIntensity(160), 0.0125f);
    assertEquals(0, Attenuation.range(100).calculateIntensity(100), 0.0125f);
    assertEquals(0, Attenuation.range(65).calculateIntensity(65), 0.0125f);
    assertEquals(0, Attenuation.range(50).calculateIntensity(50), 0.0125f);
    assertEquals(0, Attenuation.range(32).calculateIntensity(32), 0.0125f);
    assertEquals(0, Attenuation.range(20).calculateIntensity(20), 0.0125f);
    assertEquals(0, Attenuation.range(13).calculateIntensity(13), 0.0125f);
    assertEquals(0, Attenuation.range(7).calculateIntensity(7), 0.0125f);
  }
}
