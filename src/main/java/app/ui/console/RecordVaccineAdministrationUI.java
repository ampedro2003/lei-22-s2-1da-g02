package app.ui.console;

import app.controller.RecordVaccineAdministrationController;
import app.domain.shared.Constants;
import app.ui.console.utils.Utils;
import dto.SnsUserDto;

import java.util.List;
import java.util.Scanner;

/**
 * @author Guilherme Sousa <1211073@isep.ipp.pt>
 */
public class RecordVaccineAdministrationUI implements Runnable {

    private final RecordVaccineAdministrationController controller = new RecordVaccineAdministrationController();

    private final Scanner read = new Scanner(System.in);

    @Override
    public void run() {
        // Select Vaccination Center
        int vaccinationCenterIndexInList = Utils.selectVaccinationCenterIndex();
        controller.setVaccinationCenter(vaccinationCenterIndexInList);

        // Get all User information using Dto
        System.out.printf("%n" + controller.fillListWithUserSnsNumber().get(Constants.FIRST_USER_WAITING_ROOM) + "%n%n");
        // Select User from Waiting Room List
        int options = Utils.selectFromList(List.of(Constants.OPTIONS), "Consult Waiting Room List");
        int userIndexInList = fullListOfSnsUsers(options);

        // Select a Vaccine (Verifies if it matches the Vaccine Type)
        controller.setUserScheduledVaccineType(userIndexInList);
        int foundVaccine = controller.findLastDoseOfVaccineType();
        setDosageAndVaccine(foundVaccine);
    }

    private int fullListOfSnsUsers(int options) {
        if (options == Constants.FIRST_USER_WAITING_ROOM) {
            int selectedUser = Utils.selectFromList(controller.fillListWithUserSnsNumber(), "Users");
            SnsUserDto snsUserDto = controller.getSnsUserInformation(selectedUser);
            controller.setSnsUser(snsUserDto);
            return selectedUser;
        } else {
            SnsUserDto snsUserDto = controller.getSnsUserInformation(Constants.FIRST_USER_WAITING_ROOM);
            controller.setSnsUser(snsUserDto);
            return Constants.FIRST_USER_WAITING_ROOM;
        }
    }

    private int userFirstDose() {
        if (controller.findLastDoseOfVaccineType() == Constants.FIRST_DOSE) {
            int vaccineIndexInList;
            do {
                vaccineIndexInList = Utils.showAndSelectIndex(controller.vaccineTypeAvailableVaccines(), "Select a Vaccine: ");
            } while (controller.userSuitsAgeGroup(controller.findLastDoseOfVaccineType()) == Constants.INVALID_VALUE);
            return vaccineIndexInList;
        }
        //If the user doesn´t fit in any of the age groups.
        return Constants.FIT_AGE_GROUP;
    }

    private void setDosageAndVaccine(int foundVaccine) {
        if (foundVaccine != Constants.FIRST_DOSE) {
            int numberOfDoses = controller.getUserNumberOfDoses();
            int currentAppointment = controller.findLastDoseOfVaccineType();
            controller.setVaccine(currentAppointment);
            System.out.println(controller.vaccineAdministrationProcess(numberOfDoses, currentAppointment));
        } else {
            int vaccineIndex = userFirstDose();
            if (vaccineIndex != Constants.FIT_AGE_GROUP) {
                controller.setVaccine(vaccineIndex);
                System.out.println("Dosage: " + Constants.DOSAGE_FIRST_DOSE + "ml");
            }
        }
    }
}