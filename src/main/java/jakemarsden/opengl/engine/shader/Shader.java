package jakemarsden.opengl.engine.shader;

import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.tex.Texture;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Shader {

  void setCameraTransform(@NonNull Matrix4 camera);

  void setModelTransform(@NonNull Matrix4 model);

  void setTexture(@NonNull Texture tex);

  void start();

  void stop();

  void destroy();
}
