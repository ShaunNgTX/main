package seedu.revision.ui;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import seedu.revision.logic.Logic;
import seedu.revision.logic.LogicManager;
import seedu.revision.model.Model;
import seedu.revision.model.ModelManager;
import seedu.revision.model.RevisionTool;
import seedu.revision.model.UserPrefs;
import seedu.revision.storage.Storage;
import seedu.revision.stubs.HistoryStub;
import seedu.revision.stubs.StorageStub;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author khiangleon
 */
@ExtendWith(ApplicationExtension.class)
public class HistoryWindowTest {

    private HistoryWindow HistoryWindow;
    private static final String COMMAND_EXIT = "exit";

    @BeforeAll
    public static void runHeadless() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    /**
     * Will be called with {@code @Before} semantics, i.e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    public void start(Stage stage) {
        Storage storageStub = new StorageStub();
        RevisionTool testRevisionTool = new RevisionTool();
        Model model = new ModelManager(testRevisionTool, new UserPrefs(), new HistoryStub());
        Logic logic = new LogicManager(model, storageStub);
        HistoryWindow = new HistoryWindow(stage, logic);
        HistoryWindow.fillInnerParts();
        stage.show();
    }

    /**
     * Enters the exit command and expects the HistoryWindow to close.
     * @param robot robot used to simulate user actions. Will be injected by the test runner.
     */
    @Test
    public void typeExit_shouldReturnFromQuizModeToConfigurationMode(FxRobot robot) {
        robot.clickOn(".commandTextField");
        robot.write(COMMAND_EXIT);
        robot.type(KeyCode.ENTER);
        int numberOfMainWindows = getNumberOfWindowsShown(robot, "CS2103 Revision Tool");
        assertEquals(0, numberOfMainWindows);
    }

    private int getNumberOfWindowsShown(FxRobot fxRobot, String stageTitle) {
        return (int) fxRobot.listTargetWindows().stream()
                .filter(window -> window instanceof Stage && ((Stage) window).getTitle().equals(stageTitle))
                .count();
    }

}
