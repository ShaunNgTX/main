package seedu.revision.model.quiz;

import static seedu.revision.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GraphListTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GraphList(null));
    }

}
