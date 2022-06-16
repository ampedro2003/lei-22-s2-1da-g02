package app.controller;

import app.domain.model.Company;
import app.domain.model.SnsUser;
import app.domain.model.VaccinationCenter;
import app.domain.model.Vaccine;
import app.domain.shared.Constants;
import app.domain.shared.GenericClass;
import app.miscellaneous.ReadLegacyDataFile;
import app.stores.VaccinationCentersStore;
import app.ui.console.utils.Utils;
import app.ui.gui.SortedListGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Data from legacy system controller.
 */
public class DataFromLegacySystemController {

    private final Company company = App.getInstance().getCompany();
    private List<String> sortedList;
    private VaccinationCenter center;
    private final ReadLegacyDataFile readLegacyDataFile;

    private DataFromLegacySystemController controllerInfo;

    private int optionArrivalOrDeparture;
    private int optionAscendingOrDescending;


    public void setControllerInfo(DataFromLegacySystemController controllerInfo) {
        this.controllerInfo = controllerInfo;
    }


    /**
     * Instantiates a new Data from legacy system controller.
     */
    public DataFromLegacySystemController() {
        final Company company = App.getInstance().getCompany();
        final VaccinationCentersStore store = company.getVaccinationCentersStore();
        String id = Utils.getLoggedCoordinatorId();
        center = store.getVaccinationCenterAssociatedToCoordinator(id);
        readLegacyDataFile = new ReadLegacyDataFile(center);
    }




    public String getSortingAlgorithm() {
        return App.getInstance().getSortingAlgorithm();
    }


    /**
     * Gets sns user list.
     *
     * @return the sns user list
     */
    public ArrayList<SnsUser> getSNSUserList() {
        return company.getSnsUsersStore().getSnsUserList();
    }

    /**
     * Gets vaccines.
     *
     * @return the vaccines
     */
    public List<Vaccine> getVaccines() {
        return company.getVaccinesList();
    }

    /**
     * Export data to file.
     *
     * @param csvLegacyData the csv legacy data
     * @throws NotSerializableException the not serializable exception
     */
    public void exportDataToFile(List<String> csvLegacyData) throws NotSerializableException {
        GenericClass<String> generics = new GenericClass<>();
        generics.binaryFileWrite(Constants.FILE_PATH_UPDATEDLEGACY, csvLegacyData);
    }

    /**
     * Read file.
     *
     * @param path the path
     * @throws Exception the exception
     */
    public void readFile(String path) throws Exception {
        readLegacyDataFile.readFile(path);
    }

    /**
     * Update file boolean.
     *
     * @return the boolean
     * @throws NotSerializableException the not serializable exception
     */
    public List<String> updateFile() throws NotSerializableException {
       return readLegacyDataFile.updateLegacyFile();
    }

    /**
     * Set list.
     *
     * @param position the position
     */
    public void setList(int position){
        readLegacyDataFile.setList(position);
    }

    /**
     * Merge ascending list.
     *
     * @param list  the list
     * @param begin the beggining
     * @param end   the end
     * @return the list
     */
    public List<String> mergeAscending(List<LocalTime> list, int begin, int end){
       return readLegacyDataFile.mergeSortAscending(list,begin,end);
    }

    /**
     * Merge descending list.
     *
     * @param list  the list
     * @param begin the beggining
     * @param end   the end
     * @return the list
     */
    public List<String> mergeDescending(List<LocalTime> list, int begin, int end){
         return readLegacyDataFile.mergeSortDescending(list,begin,end);
    }

    /**
     * Get list to sort list.
     *
     * @return the list
     */
    public List<LocalTime> getListToSort(){
        return readLegacyDataFile.listToSort;
    }

    /**
     * Heap ascending list.
     *
     * @return the list
     */
    public List<String> heapAscending(){
     return    readLegacyDataFile.heapSortAscending();
    }

    /**
     * Heap descending list.
     *
     * @return the list
     */
    public List<String> heapDescending(){
      return readLegacyDataFile.heapSortDescending();
    }


    public void chooseCriteriaToSort(int option) {
        final int ArrivalOption = 6;
        final int LeaveOption = 8;
        switch (option) {
            case 0:
                setList(ArrivalOption);
                break;
            case 1:
                setList(LeaveOption);
                break;
            case 2:
                break;
        }
    }

    public List<String> sortListWithAlgo(String algorithm, int option) {
        switch (algorithm) {
            case "HeapSort":
                switch (option) {
                    case 0:
                        sortedList = heapAscending();
                        return sortedList;
                    case 1:
                        sortedList = heapDescending();
                        return sortedList;
                    case 2:
                        break;
                }
                break;
            case "MergeSort":
                switch (option) {
                    case 0:
                        sortedList = mergeAscending(getListToSort(), 0, getListToSort().size() - 1);
                        return sortedList;
                    case 1:
                        sortedList =  mergeDescending(getListToSort(), 0, getListToSort().size() - 1);
                        return sortedList;
                    case 2:
                        break;
                }
                break;
        }
        return null;
    }


    public int getOptionArrivalOrDeparture() {
        return optionArrivalOrDeparture;
    }

    public void setOptionArrivalOrDeparture(int option) {
        optionArrivalOrDeparture = option;
    }


    public int getOptionAscendingOrDescending() {
        return optionAscendingOrDescending;
    }

    public void setOptionAscendingOrDescending(int option) {
        optionAscendingOrDescending = option;
    }





    /**
     * To check vaccination stats scene.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    public void toSortedListScene(ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sorted-list.fxml"));
        root = loader.load();

        SortedListGUI nextSceneUi = loader.getController();
        nextSceneUi.setController(controllerInfo);
        nextSceneUi.setStatsTable();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
