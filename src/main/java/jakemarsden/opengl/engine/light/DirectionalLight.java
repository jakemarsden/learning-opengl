package jakemarsden.opengl.engine.light;

import jakemarsden.opengl.engine.math.Color3;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class DirectionalLight extends Light {

  private @NonNull Vector3 dir;

  public DirectionalLight(
      @NonNull Vector3 dir,
      @NonNull Color3 ambient,
      @NonNull Color3 diffuse,
      @NonNull Color3 specular) {

    super(ambient, diffuse, specular);
    this.dir = dir;
  }

  public @NonNull Vector3 getDirection() {
    return this.dir;
  }

  public void setDirection(@NonNull Vector3 dir) {
    this.dir = dir;
  }
}
