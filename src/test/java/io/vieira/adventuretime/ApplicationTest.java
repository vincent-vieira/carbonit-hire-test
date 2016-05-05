package io.vieira.adventuretime;

import io.vieira.adventuretime.game.AdventureWorldTest;
import io.vieira.adventuretime.game.AdventurerTest;
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
        AdventurerTest.class
})
public class ApplicationTest {}
