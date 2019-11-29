package jakemarsden.opengl.engine.camera;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Camera {

  @NonNull
  Vector3 getPosition();

  void setPosition(@NonNull Vector3 pos);

  @NonNull
  Vector3 getDirection();

  void setDirection(@NonNull Vector3 rot);

  /** @return combination of the projection and view transformations representing this camera */
  @NonNull
  Matrix4 calculatePvTransform();
}
