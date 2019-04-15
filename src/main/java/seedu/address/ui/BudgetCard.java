package seedu.address.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.epiggy.Budget;

//@@author tehwenyi

/**
 * A UI component that displays information of a {@code Budget}.
 */
public class BudgetCard extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(BudgetCard.class);

    private static final String FXML = "BudgetListCard.fxml";

    public final Budget budget;

    @FXML
    private Label title;
    @FXML
    private Label amount;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;
    @FXML
    private Label period;
    @FXML
    private Label status;
    @FXML
    private Label remainingAmount;
    @FXML
    private Label remainingDays;
    @FXML
    private Label notification;

    public BudgetCard(int displayedIndex, Budget budget) {
        super(FXML);
        this.budget = budget;
        title.setText(displayedIndex + ". " + budget.getStatus() + " Budget");
        amount.setText("Amount: $" + budget.getBudgetedAmount().toString());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy (E)");
        startDate.setText("Start Date: " + dateFormat.format(budget.getStartDate()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(budget.getEndDate());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        endDate.setText("End Date: " + dateFormat.format(calendar.getTime()));
        period.setText("Period of Budget: " + budget.getPeriod().toString() + " days");

        status.setText("Status");
        if (budget.getRemainingAmount().getAmount() >= 0) {
            remainingAmount.setText("Amount remaining: $" + budget.getRemainingAmount().toString());
        } else {
            remainingAmount.setText("Amount exceeded: $" + budget.getPositiveRemainingAmount().toString());
        }
        remainingDays.setText("Days remaining: " + budget.getRemainingDays().toString() + " days");

        if (budget.getRemainingAmount().getAmount() < 0) {
            notification.setText("You have exceeded your budget!");
            notification.setStyle("-fx-font-weight: bold; -fx-border-color: firebrick;"
                    + "-fx-text-fill: white; -fx-background-color: crimson;");
        } else if (budget.getRemainingAmount().getAmount() == 0) {
            notification.setText("You have $0 left of your budget!");
            notification.setStyle("-fx-font-weight: bold; -fx-border-color: orchid; "
                    + "-fx-text-fill: white; -fx-background-color: mediumorchid;");
        } else if (budget.getRemainingAmount().getAmount() < (budget.getBudgetedAmount().getAmount() / 5)) {
            notification.setText("You have spent more than 80% of your budget. \n"
                    + "Please control your expenses!");
            notification.setStyle("-fx-font-weight: bold; -fx-border-color: tomato; "
                    + "-fx-text-fill: white; -fx-background-color: coral;");
        } else {
            notification.setText("“Save money and money will save you.”\n"
                    + "Remember to spend wisely!");
            notification.setStyle("-fx-font-weight: bold; -fx-border-color: thistle;"
                    + "-fx-text-fill: white; -fx-background-color: plum;");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        return (other == this);
    }
}
