package jakemarsden.opengl.engine.shader;

import jakemarsden.opengl.engine.math.Matrix4;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Shader {

  void setCameraTransform(@NonNull Matrix4 camera);

  void setModelTransform(@NonNull Matrix4 model);

  void start();

  void stop();

  void destroy();
}
