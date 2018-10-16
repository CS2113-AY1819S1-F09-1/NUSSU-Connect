package seedu.address.model.searchhistory;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Predicate;

import seedu.address.model.searchhistory.exceptions.EmptyHistoryException;

/**
 * Represents in-memory model for Predicates containing system search logic.
 */
public class SearchHistoryManager {

    private Stack<Predicate> searchHistoryStack = new Stack<>();
    /** Returns predicate at top of search history stack if stack is non-empty.
     * If search history stack is empty, null is returned.
     * @return a Predicate containing the system search logic.
     */
    private Predicate retrievePredicateAtTopOfStack() {
        if (!searchHistoryStack.empty()) {
            return searchHistoryStack.peek();
        } else {
            return null;
        }
    }

    /** Returns system search logic after reverting to its previous state.
     * If search history is empty after revert, null is returned.
     * @return a Predicate containing the system search logic after reverting.
     * @throws EmptyStackException If search history is empty after revert.
     */
    public Predicate revertLastSearch() throws EmptyStackException {
        try {
            removeLastPredicateFromStack();
        } catch (EmptyStackException e) {
            throw new EmptyHistoryException();
        }
        return retrievePredicateAtTopOfStack();
    }
    /** Returns system search logic given a user-defined search logic.
     * @param predicate a Predicate containing the user-defined search logic.
     * @return a Predicate containing the system search logic.
     **/
    public Predicate executeNewSearch(Predicate predicate) {
        addNewPredicateToStack(predicate);
        assert !searchHistoryStack.empty();
        return retrievePredicateAtTopOfStack();
    }

    /** Updates system search logic to its next state given a user-defined search logic
     * @param newPredicate a Predicate containing the user-defined search logic
     **/
    private void addNewPredicateToStack(Predicate newPredicate) {
        Predicate predicate = retrievePredicateAtTopOfStack();
        if (predicate == null) {
            searchHistoryStack.push(newPredicate);
        } else {
            assert !searchHistoryStack.empty();
            Predicate updatedPredicate = predicate.and(newPredicate);
            searchHistoryStack.push(updatedPredicate);
        }
    }

    private void removeLastPredicateFromStack() {
        searchHistoryStack.pop();
    }

    public void clearSearchHistory() {
        searchHistoryStack.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SearchHistoryManager)) {
            return false;
        }

        SearchHistoryManager other = (SearchHistoryManager) obj;
        return searchHistoryStack.size() == other.searchHistoryStack.size();
    }
}
