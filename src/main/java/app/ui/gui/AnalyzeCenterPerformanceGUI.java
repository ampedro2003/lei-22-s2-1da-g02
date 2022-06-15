package app.ui.gui;

import app.controller.AnalyzeCenterPerformanceController;
import app.miscellaneous.VaccinationCenterDailyPerformance;
import app.miscellaneous.MaxSumSubListStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AnalyzeCenterPerformanceGUI {

    private final AnalyzeCenterPerformanceController controller = new AnalyzeCenterPerformanceController();

    @FXML
    private Label lbSelectTimeInterval;

    @FXML
    private javafx.scene.control.TextField txtTimeInterval;

    @FXML
    private Button analyzeBtn;

    @FXML
    private DatePicker datePicker;


    @FXML
    private TableView<VaccinationCenterDailyPerformance> TimeTableView;

    @FXML
    private TableColumn<VaccinationCenterDailyPerformance, String> timeIntervalCollumn;

    @FXML
    private TableColumn<VaccinationCenterDailyPerformance, String> differenceBetweenArrivalsAndDeparturesCollumn;


    @FXML
    private TableView<MaxSumSubListStats> sumListTableView;

    @FXML
    private TableColumn<MaxSumSubListStats, String> maxSumList;


    @FXML
    private Label lbSum;


    @FXML
    void analyzeCenterPerformance(ActionEvent event) {
        if (checkIfTimeIntervalIsValid()){
            controller.setTimeInterval(Integer.parseInt(txtTimeInterval.getText()));
            int[] statisticsDailyList = controller.getTheStatisticsDailyList();
            List<String> timeIntervals = controller.getTimeIntervals();
            ObservableList<VaccinationCenterDailyPerformance> timeObservableList = FXCollections.observableArrayList();
            timeIntervalCollumn.setCellValueFactory(new PropertyValueFactory<>("timeInterval"));
            differenceBetweenArrivalsAndDeparturesCollumn.setCellValueFactory(new PropertyValueFactory<>("differenceBetweenArrivalsAndDepartures"));

            for (int position = 0; position < statisticsDailyList.length; position++)
                timeObservableList.add(new VaccinationCenterDailyPerformance(timeIntervals.get(position), String.valueOf(statisticsDailyList[position])));
            TimeTableView.setItems(timeObservableList);


            int[] maxSumSubList = controller.getMaxSumSubList();
            ObservableList<MaxSumSubListStats> sumObservableList =FXCollections.observableArrayList();
            maxSumList.setCellValueFactory(new PropertyValueFactory<>("number"));
            for (int i : maxSumSubList) sumObservableList.add(new MaxSumSubListStats(String.valueOf(i)));
            sumListTableView.setItems(sumObservableList);

            lbSum.setText(String.valueOf(controller.getMaxSum()));


        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid time interval");
            alert.setContentText("Please enter a valid time interval. It has to be a positive integer, and it also has be to a smaller interval than the working hours of the Vaccination Center");
            alert.showAndWait();
        }

    }

    /**
     * Gets the first date.
     *
     * @param event the event
     */
    @FXML
    void setSelectedDate(ActionEvent event) {
        controller.setSelectedDate(datePicker.getValue());
        lbSelectTimeInterval.setVisible(true);
        txtTimeInterval.setVisible(true);
        analyzeBtn.setVisible(true);
    }

    /**
     * Goes to the previous scene.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    public void back(ActionEvent event) throws IOException {
        toCenterCoordinatorMenu(event);
    }


    private void toCenterCoordinatorMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/center-coordinator-menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private boolean checkIfTimeIntervalIsValid() {
        return controller.checkIfTimeIntervalIsValid(txtTimeInterval.getText());
    }

}
