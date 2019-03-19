package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.epiggy.Budget;

/**
 * Panel containing the budget status.
 */
public class BudgetPanel extends UiPart<Region> {
    private static final String FXML = "BudgetPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(BudgetPanel.class);

    @FXML
    private ListView<Budget> budgetView;

    public BudgetPanel(SimpleObjectProperty<Budget> budget, Consumer<Budget> setBudget) {
        super(FXML);
        System.out.println(budget);
        // Can only sense when the ObservableList changes but cannot sense when the Object inside the List changes
        // TODO Fix this
        List<Budget> listOfBudget = new ArrayList<>();
        ObservableList<Budget> observableListOfBudget = FXCollections.observableList(listOfBudget);
        listOfBudget.add(budget.getValue());
        budgetView.setItems(observableListOfBudget);
        budgetView.setCellFactory(listView -> new BudgetListViewCell());
        budgetView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Budget changed to : '" + newValue + "'");
            setBudget.accept(newValue);
        });
        budget.addListener((observable, oldValue, newValue) -> {
            logger.fine("Budget changed to: " + newValue);
            // Don't modify selection if we are already selecting the selected expense,
            // otherwise we would have an infinite loop.
            if (Objects.equals(budgetView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                budgetView.getSelectionModel().clearSelection();
            } else {
                int index = budgetView.getItems().indexOf(newValue);
                budgetView.scrollTo(index);
                budgetView.getSelectionModel().clearAndSelect(index);
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Budget} using a {@code ExpenseCard}.
     */
    class BudgetListViewCell extends ListCell<Budget> {
        @Override
        protected void updateItem(Budget budget, boolean empty) {
            super.updateItem(budget, empty);

            if (empty || budget == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new BudgetCard(budget).getRoot());
            }
        }
    }

}
