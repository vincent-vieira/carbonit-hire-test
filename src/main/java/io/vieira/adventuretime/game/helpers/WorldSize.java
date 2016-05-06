package io.vieira.adventuretime.game.helpers;

import io.vieira.adventuretime.game.io.write.Savable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.StringJoiner;

/**
 * World size model wrapper.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WorldSize implements Savable {

    private final int height;

    private final int width;

    public WorldSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public String getSavableRepresentation() {
        return new StringJoiner(" ").add("C").add(Integer.toString(width)).add(Integer.toString(height)).toString();
    }
}
