package app.ui.console;

import app.controller.ScheduleVaccineController;
import app.domain.model.*;
import app.domain.shared.Constants;
import app.ui.console.utils.Utils;
import dto.ScheduledVaccineDto;
import dto.VaccinationCenterDto;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ScheduleVaccineUI implements Runnable {

    private final Scanner sc = new Scanner(System.in);
    private final ScheduleVaccineController controller = new ScheduleVaccineController();

    /**
     * When the run() method calls, the code specified in the run() method is executed.
     */
    @Override
    public void run() {
        if (controller.companyHasNecessaryInfo()) {
            System.out.println();
            ScheduledVaccineDto scheduledVaccineDto = new ScheduledVaccineDto();
            int snsNumber;
            if (controller.loggedUserIsReceptionist()) {
                snsNumber = introduceSnsNumberUI();
            } else {
                snsNumber = controller.getSnsUserNumber();
            }

            int vaccinationCenterIndex = Utils.selectVaccinationCenterIndex();
            controller.setVaccinationCenter(vaccinationCenterIndex);

            VaccinationCenterDto vaccinationCenterInfo = controller.getVaccinationCenterInfo();

            if (!selectVaccineTypeUI(vaccinationCenterInfo, scheduledVaccineDto)) return;

            LocalDateTime date = selectDateUIok(vaccinationCenterInfo);

            scheduledVaccineDto.snsNumber = snsNumber;
            scheduledVaccineDto.date = date;

            if (controller.validateAppointment(scheduledVaccineDto)) {
                printAppointmentInfo(scheduledVaccineDto, vaccinationCenterInfo);
                if (Utils.confirmCreation()) {
                    if (controller.scheduleVaccine(scheduledVaccineDto)) {
                        printValidAppointmentInfo(scheduledVaccineDto, vaccinationCenterInfo);
                        try {
                            printAppointmentToFile(scheduledVaccineDto, vaccinationCenterInfo);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else
                        System.out.printf("%n-------------------------------%n|No appointment was registered|%n-------------------------------n");
                } else
                    System.out.printf("%n-------------------------------%n|No appointment was registered|%n-------------------------------%n");
            } else {
                System.out.printf("%nOops, something went wrong. Please try again!%nCommon causes: You already have an appointment for that vaccine; Your slot is not available anymore.%nYour age doesn't meet any of the existing age groups or the waiting time since the last dose isn´t finished.%n");
            }

        } else
            printNotEnoughData();
    }

    private int introduceSnsNumberUI() {
        int SNSNumber = 0;
        boolean check = false;
        do {
            System.out.print("Introduce the SNS Number: ");
            try {
                SNSNumber = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Insert a valid option.");
                sc.nextLine();
            }
            System.out.println();
            if (Utils.validateSNSUserNumber(SNSNumber)) {

                for (SnsUser snsUser : controller.getSnsUsersList()) {
                    if (snsUser.getSnsUserNumber() == (SNSNumber)) {
                        return SNSNumber;
                    }

                }
                System.out.println("There is no such SNS number.");


            } else {
                System.out.println("Invalid number.");
                System.out.println();
            }

        }
        while (!check);
        return SNSNumber;
    }

    private int selectVaccineTypeHealthCareCenterIndex(ArrayList<String> vaccineTypesList) {
        System.out.printf("%n----Suggested: " + Constants.SUGGESTED_VACCINE_TYPE_ONGOING_OUTBREAK + "----%n");
        return Utils.selectFromList(vaccineTypesList, "Select one Vaccine Type");
    }

    private boolean selectVaccineTypeUI(VaccinationCenterDto vaccinationCenter, ScheduledVaccineDto scheduledVaccineDto) {

        if (controller.isMassVaccinationCenter()) {

            System.out.println();
            System.out.println("The available Vaccine Type for " + vaccinationCenter.strName + " is: " + controller.getVaccineType().get(0));
            System.out.printf("%nDo you want to proceed?%n1 - Yes%n2 - No%n%nType your option: ");
            boolean check;
            do {
                int proceedVerification = Utils.insertInt("Insert a valid option: ");
                if (proceedVerification == 1) {
                    check = true;
                } else if (proceedVerification == 2) {
                    return false;
                } else {
                    System.out.println("Insert a valid option: ");
                    check = false;
                }
            } while (!check);

            controller.setVaccineType(1, scheduledVaccineDto);

        } else {
            controller.setVaccineType(selectVaccineTypeHealthCareCenterIndex(controller.getVaccineType()), scheduledVaccineDto);
        }
        return true;
    }

    private void printAppointmentInfo(ScheduledVaccineDto scheduledVaccineDto, VaccinationCenterDto vaccinationCenter) {
        System.out.printf("%n-------------------------%n|Appointment Information|%n-------------------------%n%n");
        System.out.printf("Given SNS Number: " + scheduledVaccineDto.snsNumber + "%n%nSelected Vaccination Center: " + vaccinationCenter.strName + "%n%nSelected Vaccine Type: " + scheduledVaccineDto.vaccineType + "%n%nDate: " + Utils.formatDateToPrint(scheduledVaccineDto.date.toLocalDate()) + "%n%nTime: " + scheduledVaccineDto.date.toLocalTime() + "%n");
        System.out.println("-------------------------");
    }

    private void printValidAppointmentInfo(ScheduledVaccineDto scheduledVaccineDto, VaccinationCenterDto vaccinationCenter) {
        System.out.printf("%n----------------------%n|Scheduling completed|%n----------------------%n%nYou have an appointment to take a %s vaccine, at %s in %s, on %s.%n%n", scheduledVaccineDto.vaccineType, scheduledVaccineDto.date.toLocalTime(), Utils.formatDateToPrint(scheduledVaccineDto.date.toLocalDate()), vaccinationCenter.strName);
    }

    private void printNotEnoughData() {
        System.out.printf("System is unable to schedule a vaccination without at least:%n- One Vaccination Center;%n- One Vaccine Type;%n- One Know System User.");
    }

    private void printAppointmentToFile(ScheduledVaccineDto scheduledVaccineDto, VaccinationCenterDto vaccinationCenter) throws IOException {
        System.out.printf("-----%n|SMS|%n-----%n%n");
        int options = Utils.selectFromList(List.of(Constants.OPTIONS), "Do you want to receive an SMS with the appointment information");
        if (options == 0) {
            PrintWriter printWriter = new PrintWriter(Constants.PATH_SMS_MESSAGE);
            printWriter.printf("Received at: " + Utils.formatDateToPrint(LocalDate.now()) + "%n%nYou have an appointment to take a %s vaccine, at %s in %s, on %s.", scheduledVaccineDto.vaccineType, scheduledVaccineDto.date.toLocalTime(), Utils.formatDateToPrint(scheduledVaccineDto.date.toLocalDate()), vaccinationCenter);
            printWriter.close();
            System.out.printf("%nA message with the information was sent to " + controller.getUserPhoneNumber() + ".");
        }
    }

    private LocalDateTime selectDateUIok(VaccinationCenterDto vaccinationCenter) {
        int openingHour = Integer.parseInt(vaccinationCenter.strOpeningHour);
        int closingHour = Integer.parseInt(vaccinationCenter.strClosingHour);
        int slotDuration = Integer.parseInt(vaccinationCenter.strSlotDuration);
        int slotsPerDay = vaccinationCenter.slotsPerDay;
        LocalDate dateWhenScheduling = LocalDate.now();
        ArrayList<String> availableDaysCurrentMonth = new ArrayList<>();
        ArrayList<String> availableDaysNextMonth = new ArrayList<>();
        ArrayList<LocalTime> availableHours = new ArrayList<>();

        System.out.printf("%nChoose the Date for the appointment:%n%n");
        boolean check = false;

        LocalDate selectedDate;
        int selectedDay;

        availableDaysCurrentMonth = controller.availableDaysListCurrentMonth(availableDaysCurrentMonth, dateWhenScheduling);
        availableDaysNextMonth = controller.availableDaysListNextMonth(availableDaysNextMonth);

        do {
            selectedDay = Utils.showAndSelectFromList(availableDaysCurrentMonth, 0) + dateWhenScheduling.getDayOfMonth() + 1;
            selectedDate = LocalDate.of(LocalDate.now().getYear(), dateWhenScheduling.getMonthValue(), selectedDay);

            if (selectedDate.equals(dateWhenScheduling)) {
                System.out.println();
                selectedDay = Utils.showAndSelectFromList(availableDaysNextMonth, 1) + 1;
                if (selectedDay != 0)
                    selectedDate = LocalDate.of(LocalDate.now().getYear(), dateWhenScheduling.getMonthValue() + 1, selectedDay);
                else {
                    selectedDay = 1;
                    selectedDate = LocalDate.of(LocalDate.now().getYear(), dateWhenScheduling.getMonthValue(), selectedDay);
                }
            } else check = true;

            if (!(selectedDate.getMonth() == dateWhenScheduling.getMonth())) {
                check = true;
            }
        } while (!check);

        System.out.printf("%nChoose the time");
        LocalTime timeOfTheSlot = LocalTime.of(openingHour, 0);

        availableHours = controller.availableHoursList(availableHours, slotsPerDay, selectedDate, timeOfTheSlot, slotDuration);
        int selectedOption;
        boolean flag;
        LocalTime timeSelected;
        do {
            selectedOption = Utils.selectFromList(availableHours, "") + 1;
            LocalTime openingHourCenter = LocalTime.of(openingHour, 0);
            LocalTime closingHourCenter = LocalTime.of(closingHour, 0);
            int minutesToBeAdded = 0;
            flag = true;
            if (selectedOption > 0) {
                minutesToBeAdded = controller.timeSelected(selectedOption, slotDuration);
            } else {
                flag = false;
                System.out.println("Invalid option.");
            }
            timeSelected = openingHourCenter.plusMinutes(minutesToBeAdded);
            if (timeSelected.isBefore(openingHourCenter) || timeSelected.isAfter(closingHourCenter) || selectedOption > slotsPerDay) {
                flag = false;
                System.out.println("Invalid option.");
            }
        } while (!flag);

        return LocalDateTime.of(selectedDate, timeSelected);
    }
}