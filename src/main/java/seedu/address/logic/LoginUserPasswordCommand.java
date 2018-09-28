package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.Model;
import seedu.address.model.UserPasswordContainsKeywordsPredicate;

/**
 * Queries the login book to see if there is a password that matches input password. Used for the login process.
 * Keyword matching is case sensitive.
 */
public class LoginUserPasswordCommand extends FindCommand {

    private final UserPasswordContainsKeywordsPredicate predicate;

    public LoginUserPasswordCommand(UserPasswordContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

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
                || (other instanceof LoginUserPasswordCommand // instanceof handles nulls
                && predicate.equals(((LoginUserPasswordCommand) other).predicate)); // state check
    }
}
