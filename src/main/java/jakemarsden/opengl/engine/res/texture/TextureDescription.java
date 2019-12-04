package jakemarsden.opengl.engine.res.texture;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import jakemarsden.opengl.engine.math.Color4;
import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.NonNull;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(name = "image", value = TextureDescription.Image.class),
  @JsonSubTypes.Type(name = "color", value = TextureDescription.Color.class),
  @JsonSubTypes.Type(name = "none", value = TextureDescription.Empty.class)
})
public abstract class TextureDescription {

  TextureDescription() {}

  static final class Image extends TextureDescription {

    final @NonNull String name;
    final boolean useAlpha;

    @JsonCreator
    Image(
        @JsonProperty(required = true) @NonNull String name,
        @JsonProperty(defaultValue = "false") boolean useAlpha) {

      this.name = name;
      this.useAlpha = useAlpha;
    }
  }

  static final class Color extends TextureDescription {

    final @NonNull Color4 color;

    @JsonCreator(mode = Mode.PROPERTIES)
    Color(@JsonProperty(required = true) float @NonNull [] color) {
      this.color = parseColorValue(color);
    }

    private static @NonNull Color4 parseColorValue(float @NonNull [] color) {
      if (color.length != 4)
        throw new IllegalArgumentException("Illegal color value: " + Arrays.toString(color));
      return Color4.rgba(color[0], color[1], color[2], color[3]);
    }
  }

  static final class Empty extends TextureDescription {

    @JsonCreator
    Empty() {}
  }
}
