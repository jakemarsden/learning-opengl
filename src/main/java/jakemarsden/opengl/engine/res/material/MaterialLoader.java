package jakemarsden.opengl.engine.res.material;

import jakemarsden.opengl.engine.res.ResourceLoader;
import jakemarsden.opengl.engine.res.texture.TextureLoader;
import jakemarsden.opengl.engine.util.CountedRef;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class MaterialLoader {

  private final Map<String, CountedRef<Material>> matCache = new HashMap<>();

  private final @NonNull ResourceLoader resLoader;
  private final @NonNull TextureLoader texLoader;

  public static @NonNull MaterialLoader create(
      @NonNull ResourceLoader resLoader, @NonNull TextureLoader texLoader) {
    return new MaterialLoader(resLoader, texLoader);
  }

  private MaterialLoader(@NonNull ResourceLoader resLoader, @NonNull TextureLoader texLoader) {
    this.resLoader = resLoader;
    this.texLoader = texLoader;
  }

  public @NonNull Material load(@NonNull String name) {
    return this.matCache
        .computeIfAbsent(name, it -> CountedRef.create(this.loadMaterial(it)))
        .takeRef();
  }

  void destroyMaterial(@NonNull Material mat) {
    this.matCache.compute(
        mat.name,
        (name, countedMat) -> {
          if (countedMat == null) throw new IllegalStateException();
          final var shouldDestroy = countedMat.returnRef(mat);
          if (shouldDestroy) this.unloadMaterial(mat);
          return shouldDestroy ? null : countedMat;
        });
  }

  private @NonNull Material loadMaterial(@NonNull String name) {
    final MaterialDescription desc;
    try {
      desc = this.resLoader.loadYaml(name, MaterialDescription.class);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load material: " + name, e);
    }

    if (!desc.name.equals(name))
      throw new IllegalArgumentException("Expected: " + desc.name + " but was: " + name);

    return new Material(
        this,
        desc.name,
        this.texLoader.loadFromDescription(desc.ambient),
        this.texLoader.loadFromDescription(desc.diffuse),
        this.texLoader.loadFromDescription(desc.specular),
        this.texLoader.loadFromDescription(desc.emission),
        desc.shininess);
  }

  private void unloadMaterial(@NonNull Material mat) {
    mat.ambientMap.destroy();
    mat.diffuseMap.destroy();
    mat.specularMap.destroy();
    mat.emissionMap.destroy();
  }
}
