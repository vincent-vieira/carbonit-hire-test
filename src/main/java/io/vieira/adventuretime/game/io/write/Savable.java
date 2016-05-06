package io.vieira.adventuretime.game.io.write;

/**
 * Interface defining the behavior of savable elements, meaning any element writable to a report file.
 *
 * @see AdventureReporter
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
public interface Savable {
    String getSavableRepresentation();
}
