package seedu.revision.ui;

import org.junit.jupiter.api.Test;

import static seedu.revision.testutil.Assert.assertThrows;

/**
 * @@author khiangleon
 */
public class UiManagerTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UiManager(null));
    }

}
