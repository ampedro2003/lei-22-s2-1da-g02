package app.domain.model;

import app.controller.App;
import app.controller.ScheduleVaccineController;
import app.ui.console.utils.Utils;
import dto.ScheduledVaccineDto;
import mapper.ScheduledVaccineMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

class ScheduledVaccineTest {

    private final ScheduledVaccineMapper mapper = new ScheduledVaccineMapper();
    private final ScheduleVaccineController controller = new ScheduleVaccineController();
    private final Company company = App.getInstance().getCompany();


    @Test
    void addTwoAppointmentForTheSameVaccine() {
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        ScheduledVaccineDto scheduledVaccineDto2 = new ScheduledVaccineDto();
        final int snsUserNumber = 111111111;
        final VaccineType vaccineType = new VaccineType("TEST1", "test", "test1");
        scheduledVaccineDto1.vaccineType = vaccineType;
        scheduledVaccineDto1.date = LocalDateTime.of(2022, 6, 22, 10, 30);
        scheduledVaccineDto1.snsNumber = snsUserNumber;

        ScheduledVaccine appointment1 = mapper.dtoToDomain(scheduledVaccineDto1);
        ScheduledVaccine.addAppointment(appointment1);

        scheduledVaccineDto2.vaccineType = vaccineType;
        scheduledVaccineDto2.date = LocalDateTime.of(2022, 6, 23, 10, 30);
        scheduledVaccineDto2.snsNumber = snsUserNumber;

        assertFalse(ScheduledVaccine.userIsEligibleForTheAppointment(scheduledVaccineDto2));
    }

    @Test
    void addNullAppointment() {
        Utils.bootstrap();
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        VaccinationCenter vaccinationCenter = company.getVaccinationCenters().get(0);

        assertFalse(controller.validateAppointment(scheduledVaccineDto1, vaccinationCenter));
    }

    @Test
    void addAppointmentWithNullSnsNumber() {
        Utils.bootstrap();
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        VaccinationCenter vaccinationCenter = company.getVaccinationCenters().get(0);
        scheduledVaccineDto1.vaccineType = company.getVaccineTypes().get(0);
        scheduledVaccineDto1.date = LocalDateTime.of(2022, 6, 22, 10, 0);
        assertFalse(controller.validateAppointment(scheduledVaccineDto1, vaccinationCenter));
    }

    @Test
    void addAppointmentWithNullVaccineType() {
        Utils.bootstrap();
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        VaccinationCenter vaccinationCenter = company.getVaccinationCenters().get(0);
        scheduledVaccineDto1.snsNumber = 111111111;
        scheduledVaccineDto1.date = LocalDateTime.of(2022, 6, 22, 10, 0);
        assertFalse(controller.validateAppointment(scheduledVaccineDto1, vaccinationCenter));
    }

    @Test
    void addAppointmentWithNullDate() {
        Utils.bootstrap();
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        VaccinationCenter vaccinationCenter = company.getVaccinationCenters().get(0);
        scheduledVaccineDto1.snsNumber = 111111111;
        scheduledVaccineDto1.vaccineType = company.getVaccineTypes().get(0);
        assertFalse(controller.validateAppointment(scheduledVaccineDto1, vaccinationCenter));
    }

    @Test
    void addAppointmentToDayWithNoAvailability() {
        Utils.bootstrap();
        ScheduledVaccineDto scheduledVaccineDto1 = new ScheduledVaccineDto();
        ScheduledVaccineDto scheduledVaccineDto2 = new ScheduledVaccineDto();
        VaccinationCenter vaccinationCenter = new VaccinationCenter("test", "test", "911111111", "test@gmail.com", "911111111", "www.test.com", "9", "16", "420", "1", "test", "4470-111", "test", company.getCentreCoordinatorList().get(0).getId());


        final VaccineType vaccineType = new VaccineType("TEST1", "test", "test1");
        scheduledVaccineDto1.vaccineType = vaccineType;
        scheduledVaccineDto1.date = LocalDateTime.of(2022, 6, 22, 9, 0);
        scheduledVaccineDto1.snsNumber = 111111111;

        ScheduledVaccine appointment1 = mapper.dtoToDomain(scheduledVaccineDto1);
        controller.scheduleVaccine(scheduledVaccineDto1, vaccinationCenter);
        ScheduledVaccine.addAppointment(appointment1);

        scheduledVaccineDto2.vaccineType = vaccineType;
        scheduledVaccineDto2.date = LocalDateTime.of(2022, 6, 22, 10, 30);
        scheduledVaccineDto2.snsNumber = 222222222;

        assertFalse(controller.scheduleVaccine(scheduledVaccineDto2, vaccinationCenter));
    }
}