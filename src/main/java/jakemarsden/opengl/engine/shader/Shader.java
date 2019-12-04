package jakemarsden.opengl.engine.shader;

import jakemarsden.opengl.engine.light.DirectionalLight;
import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.light.SpotLight;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.res.material.Material;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Shader {

  byte getMaxSupportedPointLights();

  void setCameraPosition(@NonNull Vector3 pos);

  void setCameraTransform(@NonNull Matrix4 camera);

  void setModelTransform(@NonNull Matrix4 model);

  void setDirectionalLight(@NonNull DirectionalLight light);

  void setPointLights(@NonNull PointLight @NonNull [] lights);

  void setSpotLights(@NonNull SpotLight @NonNull [] lights);

  void setMaterial(@NonNull Material mat);

  void start();

  void stop();

  void destroy();
}
