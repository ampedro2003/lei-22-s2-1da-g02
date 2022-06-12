package app.controller;

import app.domain.model.*;
import app.domain.shared.Constants;
import app.stores.VaccinationCentersStore;
import app.ui.console.utils.Utils;
import app.dto.SnsUserDto;
import app.dto.VaccineBulletinDto;
import app.mapper.SnsUserMapper;
import app.mapper.VaccineBulletinMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Guilherme Sousa <1211073@isep.ipp.pt>
 */

public class RecordVaccineAdministrationController {

    private final Company company = App.getInstance().getCompany();

    private final VaccinationCentersStore vaccinationCentersStore = company.getVaccinationCentersStore();

    private VaccinationCenter vaccinationCenter;

    private VaccineType vaccineType;

    private Vaccine vaccine;

    private SnsUser snsUser;

    private LocalDateTime localDateTime;

    private String lotNumber;

    public RecordVaccineAdministrationController() {
    }

    /**
     * Sets vaccination center.
     *
     * @param index the index
     */
    public void setVaccinationCenter(int index) {
        VaccinationCentersStore vaccinationCentersStore = company.getVaccinationCentersStore();
        vaccinationCenter = vaccinationCentersStore.getVaccinationCenters().get(index);
    }

    /**
     * Sets sns user.
     *
     * @param snsUserDto the sns user dto
     */
    public void setSnsUser(SnsUserDto snsUserDto) {
        SnsUserMapper snsUserMapper = new SnsUserMapper();
        snsUser = snsUserMapper.SNSUserDtoToDomain(snsUserDto);
    }

    /**
     * Sets vaccine type.
     *
     * @param userIndexInList the user index in list
     */
    public void setVaccineType(int userIndexInList) {
        vaccineType = vaccinationCenter.getArrivalsList().get(userIndexInList).getVaccineType();
    }

    /**
     * Sets vaccine.
     *
     * @param currentAppointment the current appointment
     */
    public void setVaccine(int currentAppointment) {
        if (!snsUser.administratedVaccines().isEmpty())
            vaccine = snsUser.administratedVaccines().get(currentAppointment).getVaccine();
        else
            vaccine = vaccineTypeAvailableVaccines().get(currentAppointment);
    }

    /**
     * Sets lot number.
     *
     * @param setLotNumber the set lot number
     */
    public void setLotNumber(String setLotNumber) {
        lotNumber = setLotNumber;
    }


    /**
     * Sets local date time.
     */
    public void setLocalDateTime() {
        localDateTime = LocalDateTime.now();
    }

    // Functionalities

    /**
     * Vaccine type available vaccines list.
     *
     * @return the list with the available vaccines for vaccine type
     */
    public List<Vaccine> vaccineTypeAvailableVaccines() {
        ArrayList<Vaccine> vaccinesAvailable = new ArrayList<>();
        for (int index = 0; index < company.getVaccinesList().size(); index++) {
            if (vaccineType.equals(company.getVaccinesList().get(index).getVaccineType())) {
                vaccinesAvailable.add(company.getVaccinesList().get(index));
            }
        }
        return vaccinesAvailable;
    }


    /**
     * Vaccine available name list.
     *
     * @return the list of vaccine names for appointment vaccine type
     */
    public List<String> vaccineAvailableName() {
        ArrayList<String> vaccinesAvailable = new ArrayList<>();
        for (int index = 0; index < company.getVaccinesList().size(); index++) {
            if (vaccineType.equals(company.getVaccinesList().get(index).getVaccineType())) {
                vaccinesAvailable.add(company.getVaccinesList().get(index).getName());
            }
        }
        return vaccinesAvailable;
    }


    /**
     * Fill list with user sns number list.
     *
     * @return the list containing sns users sns number
     */
    public List<String> fillListWithUserSnsNumber() {
        ArrayList<String> userSnsNumber = new ArrayList<>();
        for (Arrival arrival : vaccinationCenter.getArrivalsList()) userSnsNumber.add("SNS Number - " + arrival.getSnsNumber());
        return userSnsNumber;
    }


    /**
     * Gets sns user information.
     *
     * @param selectedUser the selected user in the waiting room list
     * @return the sns user information as Dto
     */
    public SnsUserDto getSnsUserInformation(int selectedUser) {
        SnsUserMapper snsUserMapper = new SnsUserMapper();
        return snsUserMapper.domainToSNSUserDto(company.getSnsUserList().get(snsUserIndexInList(selectedUser)));
    }

    private int snsUserIndexInList(int selectedUser) {
        for (int index = 0; index < company.getSnsUserList().size(); index++) {
            if (vaccinationCenter.getArrivalsList().get(selectedUser).getSnsNumber() == company.getSnsUserList().get(index).getSnsUserNumber()) {
                return index;
            }
        }
        return Constants.INVALID_VALUE;
    }

    /**
     * Check if user fits into any of the available vaccines age group
     *
     * @param indexVaccine selected vaccine index in vaccines list
     * @return the age grouop he fits
     */
    public int userFirstDoseAgeGroup(int indexVaccine) {
        int userAge = snsUser.getUserAge();
        ArrayList<Vaccine> vaccines = (ArrayList<Vaccine>) vaccineTypeAvailableVaccines();
        for (int columns = 0; columns < vaccines.get(indexVaccine).getAdminProcess().getAgeGroups().get(0).size(); columns++) {
            for (int rows = 0; rows < vaccines.get(indexVaccine).getAdminProcess().getAgeGroups().size() - 1; rows++) {
                if ((userAge > vaccines.get(indexVaccine).getAdminProcess().getAgeGroups().get(columns).get(rows)) && userAge < vaccines.get(indexVaccine).getAdminProcess().getAgeGroups().get(columns).get(rows + 1) && rows == 0) {
                    return columns;
                }
            }
        }
        return -1;
    }


    /**
     * Remove selected user from list.
     *
     * @param index the index of the user in the waiting room list
     */
    public void removeUserFromList(int index) {
        vaccinationCenter.getArrivalsList().remove(index);
    }

    /**
     * Gets user number of doses, for a given vaccine type.
     *
     * @return the number of doses
     */
    public int getUserNumberOfDoses() {
        if (!snsUser.administratedVaccines().isEmpty())
            return (snsUser.administratedVaccines().get(findLastDoseOfVaccineType()).getDose());
        return Constants.FIRST_DOSE;
    }


    /**
     * Find last dose of the vaccine type of the appointment.
     *
     * @return the index of the last taken vaccine, since it contains the number of doses taken by each vaccine type
     */
    public int findLastDoseOfVaccineType() {
        for (int index = snsUser.administratedVaccines().size() - 1; index >= 0; index--) {
            if (vaccineType.equals(snsUser.administratedVaccines().get(index).getVaccine().getVaccineType()))
                return index;
        }
        return Constants.FIRST_DOSE;
    }

    public Double dosageForDose(int numberOfDoses, int indexVaccine) {
        if (numberOfDoses == Constants.FIRST_DOSE)
            return vaccineTypeAvailableVaccines().get(indexVaccine).getAdminProcess().getDosage().get(Constants.FIRST_DOSE + 1);
        else {
            int userAge = snsUser.getUserAge();
            int userAgeGroupIndex = snsUser.administratedVaccines().get(indexVaccine).getVaccine().getUserAgeGroupIndex(userAge);
            return snsUser.administratedVaccines().get(indexVaccine).getVaccine().getAdminProcess().getDosage().get(userAgeGroupIndex);
        }

    }


    /**
     * Get dosage for respective dose vaccine administration process.
     *
     * @param numberOfDoses the number of doses taken already by the user
     * @param indexVaccine  the index of the selected vaccine in vaccines list
     * @return dosage for respective dose.
     */
    public String vaccineAdministrationProcess(int numberOfDoses, int indexVaccine) {
        return "Dosage: " + dosageForDose(numberOfDoses, indexVaccine) + "ml";
    }


    /**
     * Vaccine type Code for appointment vaccine type.
     *
     * @return the vaccine type code
     */
    public String vaccineTypeInfo() {
        return "Vaccine Type: " + vaccineType.getCode();
    }


    /**
     * Vaccine name for selected vaccine or for previously took vaccine.
     *
     * @return the vaccine name
     */
    public String vaccineInfo() {
        return "Vaccine: " + vaccine.getName();
    }


    /**
     * Validate vaccine´s lot number.
     *
     * @param lotNumber the vaccine´s lot number
     * @return true if vaccine´s lot number is validated
     */
    public boolean validateLotNumber(String lotNumber) {
        String numbers = "(.*\\d.*)";
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        int counter = 0;
        if (lotNumber.length() == Constants.LOT_NUMBER_LENGTH) {
            for (int index = 0; index < lotNumber.length(); index++) {
                if (index <= 4 && (lotNumber.matches(numbers) || lotNumber.matches(upperCaseChars) || lotNumber.matches(lowerCaseChars)))
                    counter++;
                else if (index == 5 && lotNumber.charAt(index) == '-')
                    counter++;
                else if (index >= 6 && (lotNumber.matches(numbers)))
                    counter++;
            }
        }
        return counter == Constants.LOT_NUMBER_LENGHT;
    }

    private VaccineBulletinDto snsUserAddVaccineBulletin() {
        VaccineBulletinDto vaccineBulletinDto = new VaccineBulletinDto();
        vaccineBulletinDto.vaccine = vaccine;
        vaccineBulletinDto.doseNumber = getUserNumberOfDoses() + Constants.ADD_DOSE;
        vaccineBulletinDto.dateTimeOfLastDose = localDateTime;
        vaccineBulletinDto.lotNumber = lotNumber;
        return vaccineBulletinDto;
    }


    /**
     * Register vaccine in vaccine bulletin.
     */
    public void registerVaccineInVaccineBulletin() {
        VaccineBulletinMapper vaccineBulletinMapper = new VaccineBulletinMapper();

        if (vaccineBulletinMapper.VaccineBulletinDtoToDomain(snsUserAddVaccineBulletin()).isLastDose(vaccine.getUserAgeGroupIndex(snsUser.getUserAge()))) {
            vaccinationCenter.addFullyVaccinated(vaccineBulletinMapper.VaccineBulletinDtoToDomain(snsUserAddVaccineBulletin()));
        }
        vaccinationCenter.addAdministeredVaccine(vaccineBulletinMapper.VaccineBulletinDtoToDomain(snsUserAddVaccineBulletin()));
        snsUser.registerVaccine(vaccineBulletinMapper.VaccineBulletinDtoToDomain(snsUserAddVaccineBulletin()));
    }

    /**
     * Check if arrivals list is or not empty.
     *
     * @return true if the arrivals list is not empty
     */
    public boolean checkIfArrivalsListEmpty() {
        return !vaccinationCenter.getArrivalsList().isEmpty();
    }

    /**
     * Print Recovery Time is finished, since the UI layer can be replaced by an FX layer, this was the best way of acting.
     *
     * @throws IOException the io exception
     */
    public void printRecoveryTime() throws IOException {
        PrintWriter printWriter = new PrintWriter(Constants.PATH_RECOVERY_TIME_MESSAGE);
        printWriter.printf("Received at: " + Utils.formatDateToPrint(LocalDate.now()) + "%n%nYour Recovery Time is now finished, stay safe.");
        printWriter.close();
    }

    public List<String> vaccinationCentersAvailable() {
        List<String> vaccinationCenterName = new ArrayList<>();
        for (int index = 0; index < vaccinationCentersStore.getVaccinationCenters().size(); index++) {
            vaccinationCenterName.add(vaccinationCentersStore.getVaccinationCenters().get(index).getStrName());
        }
        return vaccinationCenterName;
    }

    public String getSnsUserName() {
        return snsUser.getStrName();
    }

    public int getUserAge() {
        return snsUser.getUserAge();
    }

    public List<String> users() {
        ArrayList<String> ola = new ArrayList<>();
        for (int i = 0; i < company.getSnsUserList().size(); i++) {
            ola.add(String.valueOf(company.getSnsUserList().get(i).getSnsUserNumber()));
        }
        return ola;
    }

    public String getVaccineName() {
        return vaccine.getName();
    }

    public String getVaccineTypeName() {
        return vaccineType.getCode();
    }
}