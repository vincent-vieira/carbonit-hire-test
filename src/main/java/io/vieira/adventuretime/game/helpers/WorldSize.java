package io.vieira.adventuretime.game.helpers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * World size model wrapper.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WorldSize {

    private final int height;

    private final int width;

    public WorldSize(int height, int width) {
        this.height = height;
        this.width = width;
    }
}
