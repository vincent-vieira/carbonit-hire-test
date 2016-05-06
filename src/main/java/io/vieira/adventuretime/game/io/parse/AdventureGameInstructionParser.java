package io.vieira.adventuretime.game.io.parse;

import io.vieira.adventuretime.game.io.exception.GameInstructionParsingException;

import java.util.Arrays;
import java.util.List;

/**
 * Simple implementation of {@link GameInstructionParser}, providing all {@link io.vieira.adventuretime.game.elements.WorldElement}s
 * and {@link io.vieira.adventuretime.game.AdventureWorld} based on the {@link GameInstruction}s defined as constants and supplied through
 * constructor.
 *
 * @see GameInstruction#TREASURE
 * @see GameInstruction#MOUNTAIN
 * @see GameInstruction#ADVENTURE_WORLD
 * @see GameInstruction#ADVENTURER
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public class AdventureGameInstructionParser implements GameInstructionParser {

    private final List<GameInstruction> instructions;

    public AdventureGameInstructionParser(GameInstruction... registredInstructions){
        instructions = Arrays.asList(registredInstructions);
    }

    @Override
    public final Object fromLine(String line) throws GameInstructionParsingException {
        return instructions.stream()
                .filter(gameInstruction -> gameInstruction.instructionPattern().matcher(line).matches())
                .map(gameInstruction -> gameInstruction.transform(line))
                .findFirst()
                .orElse(null);
    }

}
