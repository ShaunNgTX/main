package seedu.revision.ui;

import static seedu.revision.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * @@author khiangleon
 */
public class UiManagerTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UiManager(null));
    }

}
