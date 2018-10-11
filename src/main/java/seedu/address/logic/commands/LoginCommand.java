package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.SearchHistoryManager;

/**
 * Queries the login book to see if there is a user ID and password that matches input
 * user ID and password. Used for the login process.
 * Keyword matching is case insensitive for user ID and case sensitive for user Password.
 */
public abstract class LoginCommand extends Command {

    public static final String COMMAND_WORD = "login";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":Login into addressbook with input user ID and password."
            + "Parameters: USERID PASSWORD\n"
            + "Example: " + COMMAND_WORD + " A3583758X passphrase";

    /**
     * Ensures that Search History only remains valid for back-to-back find commands
     */
    protected void ensureSearchHistoryValidity(CommandHistory history) {
        String lastExecutedCommand = history.getLastExecutedCommand();
        String lastExecutedCommandWord = "";
        if (lastExecutedCommand != null) {
            lastExecutedCommandWord = AddressBookParser.basicParseCommand(lastExecutedCommand);
        }
        if (!lastExecutedCommandWord.equals(COMMAND_WORD)) {
            SearchHistoryManager.getInstance().clearSearchHistory();
        }
    }

    protected Predicate getMostUpdatedPredicate(Predicate predicate) {
        return SearchHistoryManager.getInstance().updateNewSearch(predicate);
    }
}
