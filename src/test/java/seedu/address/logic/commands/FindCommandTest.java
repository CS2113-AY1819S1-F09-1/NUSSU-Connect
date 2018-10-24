package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAccounts.getTypicalLoginBook;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.JOHN;
import static seedu.address.testutil.TypicalPersons.KHOR;
import static seedu.address.testutil.TypicalPersons.SEGWIT;
import static seedu.address.testutil.TypicalPersons.getTypicalTaggedAddressBook;

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
    private Model model = new ModelManager(getTypicalLoginBook(), getTypicalTaggedAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalLoginBook(),
                                                   getTypicalTaggedAddressBook(), new UserPrefs());
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

        FindCommand findFirstCommand = new FindPersonSubCommand(firstPredicate, false);
        FindCommand findSecondCommand = new FindPersonSubCommand(secondPredicate, false);
        FindCommand findThirdCommand = new FindTagSubCommand(thirdPredicate);
        FindCommand findFourthCommand = new FindTagSubCommand(fourthPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));
        assertTrue(findThirdCommand.equals(findThirdCommand));
        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindPersonSubCommand(firstPredicate, false);
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
        NameContainsKeywordsPredicate predicate = prepareNameContainsKeywordsPredicate(" ");
        FindCommand command = new FindPersonSubCommand(predicate);
        expectedModel.executeSearch(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_excludeMultipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        NameContainsKeywordsPredicate predicate = prepareNameContainsKeywordsPredicate("Kurz Elle Kunz");
        FindCommand command = new FindPersonSubCommand(predicate, true);
        expectedModel.executeSearch(predicate.negate());
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL, GEORGE, JOHN, SEGWIT, KHOR), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = prepareNameContainsKeywordsPredicate("Kurz Elle Kunz");
        FindCommand command = new FindPersonSubCommand(predicate);
        expectedModel.executeSearch(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_searchByTag_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        TagContainsKeywordsPredicate predicate = prepareTagContainsKeywordsPredicate(VALID_TAG_FRIEND);
        FindCommand command = new FindTagSubCommand(predicate);
        expectedModel.executeSearch(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SEGWIT, KHOR), model.getFilteredPersonList());
    }

    @Test
    public void execute_excludeSearchByTag_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 9);
        TagContainsKeywordsPredicate predicate = prepareTagContainsKeywordsPredicate(VALID_TAG_HUSBAND);
        FindCommand command = new FindTagSubCommand(predicate, true);
        expectedModel.executeSearch(predicate.negate());
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE, JOHN, KHOR),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_searchByTag_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        TagContainsKeywordsPredicate predicate = prepareTagContainsKeywordsPredicate(VALID_TAG_HUSBAND);
        FindCommand command = new FindTagSubCommand(predicate);
        expectedModel.executeSearch(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(SEGWIT), model.getFilteredPersonList());
    }

    @Test
    public void execute_twoBackToBackFindCommand_onePersonFound() {
        String firstExpectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        TagContainsKeywordsPredicate firstPredicate = prepareTagContainsKeywordsPredicate(VALID_TAG_FRIEND);
        FindCommand firstCommand = new FindTagSubCommand(firstPredicate);
        expectedModel.executeSearch(firstPredicate);
        assertCommandSuccess(firstCommand, model, commandHistory, firstExpectedMessage, expectedModel);
        assertEquals(Arrays.asList(SEGWIT, KHOR), model.getFilteredPersonList());

        String secondExpectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate secondPredicate = prepareNameContainsKeywordsPredicate("Choo");
        FindCommand secondCommand = new FindPersonSubCommand(secondPredicate);
        expectedModel.executeSearch(secondPredicate);
        assertCommandSuccess(secondCommand, model, commandHistory, secondExpectedMessage, expectedModel);
        assertEquals(Arrays.asList(SEGWIT), model.getFilteredPersonList());
    }
    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate prepareNameContainsKeywordsPredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    /**
     * Parses {@code userInput} into a {@code TagContainsKeywordsPredicate}.
     */
    private TagContainsKeywordsPredicate prepareTagContainsKeywordsPredicate(String userInput) {
        return new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
