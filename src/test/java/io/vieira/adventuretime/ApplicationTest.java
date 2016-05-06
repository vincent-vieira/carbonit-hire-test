package io.vieira.adventuretime;

import io.vieira.adventuretime.game.AdventureWorldTest;
import io.vieira.adventuretime.game.AdventurerTest;
import io.vieira.adventuretime.io.AdventureReporterTest;
import io.vieira.adventuretime.io.MultipleAdventureGameFileLoaderTest;
import io.vieira.adventuretime.io.UniqueAdventureGameFileLoaderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Main app unit test.
 *
 * @author <a href="mailto:vincent.vieira@supinfo.com">Vincent Vieira</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdventureWorldTest.class,
        AdventurerTest.class,
        UniqueAdventureGameFileLoaderTest.class,
        MultipleAdventureGameFileLoaderTest.class,
        AdventureReporterTest.class
})
public class ApplicationTest {}
