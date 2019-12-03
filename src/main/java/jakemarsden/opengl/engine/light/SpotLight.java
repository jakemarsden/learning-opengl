package jakemarsden.opengl.engine.light;

import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SpotLight extends Light {

  /** The apex of the cone of light cast by this spotlight */
  private @NonNull Vector3 pos;
  /** Points along the axis of the cone of light cast by this spotlight */
  private @NonNull Vector3 dir;
  /**
   * The aperture (angle between opposite "sides") of the <em>inner</em> cone of light cast by this
   * spotlight. A spotlight casts light at 100% intensity within this inner cone
   */
  private float aperture;
  /**
   * The aperture (angle between opposite "sides") of the <em>outer</em> cone of light cast by this
   * spotlight. The light cast by a spotlight diminishes in intensity between its inner (100%
   * intensity) and outer (0% intensity) cones. A spotlight casts no light outside of its outer cone
   */
  private float outerAperture;

  public SpotLight(
      @NonNull Vector3 pos,
      @NonNull Vector3 dir,
      float aperture,
      float outerAperture,
      @NonNull Color3 ambient,
      @NonNull Color3 diffuse,
      @NonNull Color3 specular) {

    super(ambient, diffuse, specular);
    this.pos = pos;
    this.dir = dir;
    this.aperture = aperture;
    this.outerAperture = outerAperture;
  }

  public @NonNull Vector3 getPosition() {
    return this.pos;
  }

  public void setPosition(@NonNull Vector3 pos) {
    this.pos = pos;
  }

  public @NonNull Vector3 getDirection() {
    return this.dir;
  }

  public void setDirection(@NonNull Vector3 dir) {
    this.dir = dir;
  }

  public float getAperture() {
    return aperture;
  }

  public void setAperture(float aperture) {
    this.aperture = aperture;
  }

  public float getOuterAperture() {
    return outerAperture;
  }

  public void setOuterAperture(float outerAperture) {
    this.outerAperture = outerAperture;
  }
}
