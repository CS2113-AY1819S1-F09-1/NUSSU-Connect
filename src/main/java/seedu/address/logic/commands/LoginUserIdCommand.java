package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.login.UserIdContainsKeywordsPredicate;

/**
 * Queries the login book to see if there is a user ID that matches input user ID. Used for the login process.
 * Keyword matching is case insensitive.
 */
public class LoginUserIdCommand extends FindCommand {

    private final UserIdContainsKeywordsPredicate predicate;

    public LoginUserIdCommand(UserIdContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    //if returned list of login details list is empty, means account not exist, else, account exists
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredLoginDetailsList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_LOGINDETAILS_LISTED_OVERVIEW, model.getFilteredLoginDetailsList().size()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LoginUserIdCommand // instanceof handles nulls
                && predicate.equals(((LoginUserIdCommand) other).predicate)); // state check
    }
}
