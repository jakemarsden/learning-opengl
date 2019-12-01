package jakemarsden.opengl.engine.model;

import static org.lwjgl.opengl.GL13.*;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Material {

  private static final int TEX_UNIT_AMBIENT_MAP = GL_TEXTURE0;
  private static final int TEX_UNIT_DIFFUSE_MAP = GL_TEXTURE1;
  private static final int TEX_UNIT_SPECULAR_MAP = GL_TEXTURE2;
  private static final int TEX_UNIT_EMISSION_MAP = GL_TEXTURE3;

  public final @Nullable Texture ambientMap;
  public final @Nullable Texture diffuseMap;
  public final @Nullable Texture specularMap;
  public final @Nullable Texture emissionMap;
  public final float shininess;

  public final int ambientMapTexUnit = TEX_UNIT_AMBIENT_MAP;
  public final int diffuseMapTexUnit = TEX_UNIT_DIFFUSE_MAP;
  public final int specularMapTexUnit = TEX_UNIT_SPECULAR_MAP;
  public final int emissionMapTexUnit = TEX_UNIT_EMISSION_MAP;

  public static Material.@NonNull Builder builder() {
    return new Builder();
  }

  private Material(
      @Nullable Texture ambientMap,
      @Nullable Texture diffuseMap,
      @Nullable Texture specularMap,
      @Nullable Texture emissionMap,
      float shininess) {

    this.ambientMap = ambientMap;
    this.diffuseMap = diffuseMap;
    this.specularMap = specularMap;
    this.emissionMap = emissionMap;
    this.shininess = shininess;
  }

  public void bind() {
    if (this.ambientMap != null) this.ambientMap.bindTo(this.ambientMapTexUnit);
    if (this.diffuseMap != null) this.diffuseMap.bindTo(this.diffuseMapTexUnit);
    if (this.specularMap != null) this.specularMap.bindTo(this.specularMapTexUnit);
    if (this.emissionMap != null) this.emissionMap.bindTo(this.emissionMapTexUnit);
  }

  public void unbind() {
    if (this.ambientMap != null) this.ambientMap.unbindFrom(this.ambientMapTexUnit);
    if (this.diffuseMap != null) this.diffuseMap.unbindFrom(this.diffuseMapTexUnit);
    if (this.specularMap != null) this.specularMap.unbindFrom(this.specularMapTexUnit);
    if (this.emissionMap != null) this.emissionMap.unbindFrom(this.emissionMapTexUnit);
  }

  public void destroy() {
    if (this.ambientMap != null) this.ambientMap.destroy();
    if (this.diffuseMap != null) this.diffuseMap.destroy();
    if (this.specularMap != null) this.specularMap.destroy();
    if (this.emissionMap != null) this.emissionMap.destroy();
  }

  public static final class Builder {

    private @Nullable Texture ambientMap = null;
    private @Nullable Texture diffuseMap = null;
    private @Nullable Texture specularMap = null;
    private @Nullable Texture emissionMap = null;
    private float shininess = 0;

    private Builder() {}

    public @NonNull Builder withAmbientLighting(@NonNull Texture ambientMap) {
      this.ambientMap = ambientMap;
      return this;
    }

    public @NonNull Builder withDiffuseLighting(@NonNull Texture diffuseMap) {
      this.diffuseMap = diffuseMap;
      return this;
    }

    public @NonNull Builder withSpecularLighting(@NonNull Texture specularMap, float shininess) {
      this.specularMap = specularMap;
      this.shininess = shininess;
      return this;
    }

    public @NonNull Builder withEmissionLighting(@NonNull Texture emissionMap) {
      this.emissionMap = emissionMap;
      return this;
    }

    public @NonNull Material build() {
      return new Material(
          this.ambientMap != null ? this.ambientMap : this.diffuseMap,
          this.diffuseMap,
          this.specularMap,
          this.emissionMap,
          this.shininess);
    }
  }
}
