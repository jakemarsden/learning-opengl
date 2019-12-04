package jakemarsden.opengl.engine.res.material;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE2;

import jakemarsden.opengl.engine.res.texture.Texture;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Material {

  private static final int TEX_UNIT_AMBIENT = GL_TEXTURE0;
  private static final int TEX_UNIT_DIFFUSE = GL_TEXTURE1;
  private static final int TEX_UNIT_SPECULAR = GL_TEXTURE2;
  private static final int TEX_UNIT_EMISSION = GL_TEXTURE3;

  private final @NonNull MaterialLoader loader;
  final @NonNull String name;

  final @NonNull Texture ambientMap;
  final @NonNull Texture diffuseMap;
  final @NonNull Texture specularMap;
  final @NonNull Texture emissionMap;
  final float shininess;

  Material(
      @NonNull MaterialLoader loader,
      @NonNull String name,
      @NonNull Texture ambientMap,
      @NonNull Texture diffuseMap,
      @NonNull Texture specularMap,
      @NonNull Texture emissionMap,
      float shininess) {

    this.loader = loader;
    this.name = name;

    this.ambientMap = ambientMap;
    this.diffuseMap = diffuseMap;
    this.specularMap = specularMap;
    this.emissionMap = emissionMap;
    this.shininess = shininess;
  }

  public @NonNull Texture getAmbientMap() {
    return ambientMap;
  }

  public @NonNull Texture getDiffuseMap() {
    return diffuseMap;
  }

  public @NonNull Texture getSpecularMap() {
    return specularMap;
  }

  public @NonNull Texture getEmissionMap() {
    return emissionMap;
  }

  public float getShininess() {
    return shininess;
  }

  public int getAmbientTexUnit() {
    return TEX_UNIT_AMBIENT;
  }

  public int getDiffuseTexUnit() {
    return TEX_UNIT_DIFFUSE;
  }

  public int getSpecularTexUnit() {
    return TEX_UNIT_SPECULAR;
  }

  public int getEmissionTexUnit() {
    return TEX_UNIT_EMISSION;
  }

  public void bind() {
    this.ambientMap.bindTo(TEX_UNIT_AMBIENT);
    this.diffuseMap.bindTo(TEX_UNIT_DIFFUSE);
    this.specularMap.bindTo(TEX_UNIT_SPECULAR);
    this.emissionMap.bindTo(TEX_UNIT_EMISSION);
  }

  public void unbind() {
    this.ambientMap.unbindFrom(TEX_UNIT_AMBIENT);
    this.diffuseMap.unbindFrom(TEX_UNIT_DIFFUSE);
    this.specularMap.unbindFrom(TEX_UNIT_SPECULAR);
    this.emissionMap.unbindFrom(TEX_UNIT_EMISSION);
  }

  public void destroy() {
    this.loader.destroyMaterial(this);
  }

  @Override
  public @NonNull String toString() {
    return "Material{" + this.name + "}";
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass() != this.getClass()) return false;
    final var obj = (Material) o;
    return obj.name.equals(this.name);
  }
}
