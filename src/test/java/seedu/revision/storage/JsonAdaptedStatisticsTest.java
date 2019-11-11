package seedu.revision.storage;

import static seedu.revision.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.revision.model.quiz.Statistics;

/**
 * @@author khiangleon
 */
public class JsonAdaptedStatisticsTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JsonAdaptedStatistics(new Statistics(null)));
    }

}

