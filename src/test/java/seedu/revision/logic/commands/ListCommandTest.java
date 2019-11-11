package seedu.revision.logic.commands;

import static seedu.revision.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.revision.model.Model.PREDICATE_SHOW_ALL_ANSWERABLE;
import static seedu.revision.testutil.TypicalMcqs.getTypicalMcqs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.revision.commons.core.Messages;
import seedu.revision.logic.commands.main.ListCommand;
import seedu.revision.logic.parser.exceptions.ParseException;
import seedu.revision.model.History;
import seedu.revision.model.Model;
import seedu.revision.model.ModelManager;
import seedu.revision.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalMcqs(), new UserPrefs(), new History());
        expectedModel = new ModelManager(model.getRevisionTool(), new UserPrefs(), new History());
    }

    @Test
    public void execute_listIsNotFiltered_showsAllAnswerables() throws ParseException {
        assertCommandSuccess(new ListCommand(PREDICATE_SHOW_ALL_ANSWERABLE),
                model, String.format(Messages.MESSAGE_ANSWERABLES_LISTED_OVERVIEW,
                model.getFilteredAnswerableList().size()), expectedModel);
    }

}
