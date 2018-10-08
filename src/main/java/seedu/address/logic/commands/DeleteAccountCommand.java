package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_USERPASSWORD;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.login.LoginDetails;

/**
 * Deletes an account identified using it's associated user ID and password from the login book.
 */
public class DeleteAccountCommand extends Command {

    public static final String COMMAND_WORD = "deleteaccount";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the account identified by it's user ID and password from the login book.\n"
            + "Parameters: "
            + PREFIX_USERID + "USERID "
            + PREFIX_USERPASSWORD + "PASSWORD "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_USERID + "A1234567M "
            + PREFIX_USERPASSWORD + "zaq1xsw2cde3";

    public static final String MESSAGE_DELETE_ACCOUNT_SUCCESS = "Deleted account: %1$s";

    private final LoginDetails toDelete;

    public DeleteAccountCommand(LoginDetails loginDetails) {
        requireNonNull(loginDetails);
        toDelete = loginDetails;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.deleteAccount(toDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_ACCOUNT_SUCCESS, toDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteAccountCommand // instanceof handles nulls
                && toDelete.equals(((DeleteAccountCommand) other).toDelete)); // state check
    }
}
