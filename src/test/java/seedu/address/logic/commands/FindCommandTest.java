package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAccounts.getTypicalLoginBook;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalLoginBook(), getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalLoginBook(), getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        TagContainsKeywordsPredicate thirdPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("third"));
        TagContainsKeywordsPredicate fourthPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("fourth"));

        FindCommand findFirstCommand = new FindPersonSubCommand(firstPredicate);
        FindCommand findSecondCommand = new FindPersonSubCommand(secondPredicate);
        FindCommand findThirdCommand = new FindTagSubCommand(thirdPredicate);
        FindCommand findFourthCommand = new FindTagSubCommand(fourthPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));
        assertTrue(findThirdCommand.equals(findThirdCommand));
        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindPersonSubCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        FindCommand findThirdCommandCopy = new FindTagSubCommand(thirdPredicate);
        assertTrue(findThirdCommand.equals(findThirdCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));
        assertFalse(findFourthCommand.equals(4));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));
        assertFalse(findThirdCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));

        //different tag -> returns false
        assertFalse(findFourthCommand.equals(findThirdCommand));

        //different object -> returns false
        assertFalse(findFirstCommand.equals(findThirdCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindPersonSubCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindPersonSubCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
