package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.LoginBook;
import seedu.address.testutil.TypicalAccounts;

public class XmlSerializableLoginBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "XmlSerializableLoginBookTest");
    private static final Path TYPICAL_ACCOUNTS_FILE = TEST_DATA_FOLDER.resolve("typicalAccountsLoginBook.xml");
    private static final Path INVALID_ACCOUNT_FILE = TEST_DATA_FOLDER.resolve("invalidAccountsLoginBook.xml");
    private static final Path DUPLICATE_ACCOUNT_FILE = TEST_DATA_FOLDER.resolve("duplicateAccountLoginBook.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalAccountsFile_success() throws Exception {
        XmlSerializableLoginBook dataFromFile = XmlUtil.getDataFromFile(TYPICAL_ACCOUNTS_FILE,
                XmlSerializableLoginBook.class);
        LoginBook loginBookFromFile = dataFromFile.toModelType();
        LoginBook typicalAccountsLoginBook = TypicalAccounts.getTypicalLoginBook();
        assertEquals(loginBookFromFile, typicalAccountsLoginBook);
    }

    @Test
    public void toModelType_invalidAccountFile_throwsIllegalValueException() throws Exception {
        XmlSerializableLoginBook dataFromFile = XmlUtil.getDataFromFile(INVALID_ACCOUNT_FILE,
                XmlSerializableLoginBook.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateAccounts_throwsIllegalValueException() throws Exception {
        XmlSerializableLoginBook dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_ACCOUNT_FILE,
                XmlSerializableLoginBook.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableLoginBook.MESSAGE_DUPLICATE_ACCOUNT);
        dataFromFile.toModelType();
    }
}
