package jakemarsden.opengl.engine.light;

import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PointLight extends Light {

  private @NonNull Vector3 pos;
  private @NonNull Attenuation attenuation;

  public PointLight(
      @NonNull Vector3 pos,
      @NonNull Attenuation attenuation,
      @NonNull Color3 ambient,
      @NonNull Color3 diffuse,
      @NonNull Color3 specular) {

    super(ambient, diffuse, specular);
    this.pos = pos;
    this.attenuation = attenuation;
  }

  public @NonNull Vector3 getPosition() {
    return this.pos;
  }

  public void setPosition(@NonNull Vector3 pos) {
    this.pos = pos;
  }

  public @NonNull Attenuation getAttenuation() {
    return this.attenuation;
  }

  public void setAttenuation(@NonNull Attenuation attenuation) {
    this.attenuation = attenuation;
  }
}
