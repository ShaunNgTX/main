package seedu.revision.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.revision.commons.core.GuiSettings;
import seedu.revision.commons.core.LogsCenter;
import seedu.revision.logic.Logic;
import seedu.revision.logic.commands.CommandResult;
import seedu.revision.logic.commands.exceptions.CommandException;
import seedu.revision.logic.parser.exceptions.ParseException;
import seedu.revision.model.answerable.Answer;
import seedu.revision.model.answerable.Answerable;
import seedu.revision.model.category.Category;
import seedu.revision.model.answerable.Difficulty;
import seedu.revision.model.answerable.Mcq;
import seedu.revision.model.answerable.Question;


/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class StartQuizWindow extends UiPart<Stage> {

    Answer correctAnswerStub = new Answer("CORRECT");
    Set<Answer> correctAnswerSetStub = new HashSet<>(Arrays.asList(correctAnswerStub));
    Answer[] wrongAnswerStub = {new Answer("WRONG A"), new Answer("WRONG B"), new Answer("WRONG C"),};
    Set<Answer> wrongAnswerSetStub = new HashSet<>(Arrays.asList(wrongAnswerStub));
    Category categoryStub = new Category("math");
    Set<Category> categoriesStub = new HashSet<>(Arrays.asList(categoryStub));

    public MainWindow mainWindow;
    private final Mcq DEFAULT_QUESTION =
            new Mcq(new Question("what is 10 + 10?"), correctAnswerSetStub, wrongAnswerSetStub,
                    new Difficulty("1"), categoriesStub);


    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private AnswerableListPanel answerableListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    private AnswersGridPane answersGridPane;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane answerableListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public StartQuizWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Logic getLogic() { return logic; }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

//        answerableListPanel = new AnswerableListPanel(logic.getFilteredAnswerableList());
//        answerableListPanelPlaceholder.getChildren().add(answerableListPanel.getRoot());
        ObservableList<Answerable> filteredList = logic.getFilteredAnswerableList();

        answersGridPane = new AnswersGridPane(DEFAULT_QUESTION);
        answerableListPanelPlaceholder.getChildren().add(answersGridPane.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        mainWindow = new MainWindow(getPrimaryStage(), getLogic());
        mainWindow.show();
        mainWindow.fillInnerParts();
    }

    public AnswerableListPanel getAnswerableListPanel() {
        return answerableListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.revision.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
