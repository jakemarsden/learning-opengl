package jakemarsden.opengl.engine.res.material;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakemarsden.opengl.engine.res.texture.TextureDescription;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class MaterialDescription {

  final @NonNull String name;
  final @NonNull TextureDescription ambient;
  final @NonNull TextureDescription diffuse;
  final @NonNull TextureDescription specular;
  final @NonNull TextureDescription emission;
  final float shininess;

  @JsonCreator
  MaterialDescription(
      @JsonProperty(required = true) @NonNull String name,
      @JsonProperty(required = true) @NonNull TextureDescription ambient,
      @JsonProperty(required = true) @NonNull TextureDescription diffuse,
      @JsonProperty(required = true) @NonNull TextureDescription specular,
      @JsonProperty(required = true) @NonNull TextureDescription emission,
      @JsonProperty(required = true) float shininess) {

    this.name = name;
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.specular = specular;
    this.emission = emission;
    this.shininess = shininess;
  }
}
