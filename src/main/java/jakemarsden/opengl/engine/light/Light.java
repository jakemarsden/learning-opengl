package jakemarsden.opengl.engine.light;

import jakemarsden.opengl.engine.math.Color3;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class Light {

  private @NonNull Color3 ambient;
  private @NonNull Color3 diffuse;
  private @NonNull Color3 specular;

  protected Light(@NonNull Color3 ambient, @NonNull Color3 diffuse, @NonNull Color3 specular) {
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.specular = specular;
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
}
