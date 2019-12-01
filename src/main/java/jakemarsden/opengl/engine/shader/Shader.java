package jakemarsden.opengl.engine.shader;

import jakemarsden.opengl.engine.light.PointLight;
import jakemarsden.opengl.engine.math.Matrix4;
import jakemarsden.opengl.engine.math.Vector3;
import jakemarsden.opengl.engine.model.Material;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Shader {

  void setCameraTransform(@NonNull Matrix4 camera);

  void setModelTransform(@NonNull Matrix4 model);

  void setCameraPosition(@NonNull Vector3 pos);

  void setLight(@NonNull Vector3 pos, @NonNull PointLight light);

  void setMaterial(@NonNull Material mat);

  void start();

  void stop();

  void destroy();
}
