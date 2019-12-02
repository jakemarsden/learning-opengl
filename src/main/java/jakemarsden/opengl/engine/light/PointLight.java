package jakemarsden.opengl.engine.light;

import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class PointLight {

  private Vector3 pos;
  private Color3 ambient;
  private Color3 diffuse;
  private Color3 specular;
  private Attenuation attenuation;

  public PointLight(
      @NonNull Vector3 pos,
      @NonNull Color3 ambient,
      @NonNull Color3 diffuse,
      @NonNull Color3 specular,
      @NonNull Attenuation attenuation) {

    this.pos = pos;
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.specular = specular;
    this.attenuation = attenuation;
  }

  public @NonNull Vector3 getPosition() {
    return this.pos;
  }

  public void setPosition(@NonNull Vector3 pos) {
    this.pos = pos;
  }

  public @NonNull Color3 getAmbient() {
    return this.ambient;
  }

  public void setAmbient(@NonNull Color3 ambient) {
    this.ambient = ambient;
  }

  public @NonNull Color3 getDiffuse() {
    return this.diffuse;
  }

  public void setDiffuse(@NonNull Color3 diffuse) {
    this.diffuse = diffuse;
  }

  public @NonNull Color3 getSpecular() {
    return this.specular;
  }

  public void setSpecular(@NonNull Color3 specular) {
    this.specular = specular;
  }

  public @NonNull Attenuation getAttenuation() {
    return this.attenuation;
  }

  public void setAttenuation(@NonNull Attenuation attenuation) {
    this.attenuation = attenuation;
  }
}
