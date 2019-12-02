package jakemarsden.opengl.engine.shader;

import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Shader {

  byte getMaxSupportedPointLights();

  void setCameraPosition(@NonNull Vector3 pos);

  void setCameraTransform(@NonNull Matrix4 camera);

  void setModelTransform(@NonNull Matrix4 model);

  void setPointLights(@Nullable PointLight @NonNull [] lights);

  void setMaterial(@NonNull Material mat);

  void start();

  void stop();

  void destroy();
}
