package app.domain.model;

import app.controller.App;
import app.domain.shared.Constants;
import app.domain.shared.GenericClass;
import app.stores.VaccinationCentersStore;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Departure {

    private final int snsNumber;

    private final LocalDateTime departureTime;

    GenericClass<Departure> generics = new GenericClass<>();
    VaccinationCentersStore vaccinationCentersStore = App.getInstance().getCompany().getVaccinationCentersStore();

    public Departure(int snsNumber, LocalDateTime departureTime) {
        this.snsNumber = snsNumber;
        this.departureTime = departureTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return "departureTime=" + departureTime;
    }

    /**
     * Exports the list of Users that arrived at a vaccination center to a binary file.
     * @throws NotSerializableException
     */
    public void exportDataToFile() throws NotSerializableException {
        for (VaccinationCenter vaccinationCenter : vaccinationCentersStore.getVaccinationCenters()) {
            generics.binaryFileWrite(Constants.FILE_PATH_DEPARTURES, vaccinationCenter.getDepartureList());
        }
    }
}
