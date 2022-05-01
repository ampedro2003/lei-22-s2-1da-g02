package app.ui.console;

import app.domain.model.Address;
import app.domain.model.Employee;
/**
 *
 * @author João Castro <1210816@isep.ipp.pt>
 */
public class VaccinationCenterDto {

    public VaccinationCenterDto() {
    }

    public int intID;
    public String strName;
    public String strPhoneNumber;
    public String strEmail;
    public String strFax;
    public String strWebsite;
    public String strOpeningHour;
    public String strClosingHour;
    public String strSlotDuration;
    public String strVaccinesPerSlot;
    public String strRoad;
    public String strZipCode;
    public String strLocal;
    public String id;


    public VaccinationCenterDto(int intID, String strName, String strPhoneNumber, String strEmail, String strFax, String strWebsite,
                                String strOpeningHour, String strClosingHour, String strSlotDuration, String strVaccinesPerSlot,
                                String strRoad, String strZipCode, String strLocal, String id) {
        this.intID = intID;
        this.strName = strName;
        this.strPhoneNumber = strPhoneNumber;
        this.strEmail = strEmail;
        this.strFax = strFax;
        this.strWebsite = strWebsite;
        this.strOpeningHour = strOpeningHour;
        this.strClosingHour = strClosingHour;
        this.strSlotDuration = strSlotDuration;
        this.strVaccinesPerSlot = strVaccinesPerSlot;
        this.strRoad=strRoad;
        this.strZipCode=strZipCode;
        this.strLocal=strLocal;
        this.id=id;
    }

    @Override
    public String toString() {
        return "VaccinationCenterDto{" +
                "intID=" + intID +
                ", strName='" + strName + '\'' +
                ", strPhoneNumber='" + strPhoneNumber + '\'' +
                ", strEmail='" + strEmail + '\'' +
                ", strFax='" + strFax + '\'' +
                ", strWebsite='" + strWebsite + '\'' +
                ", strOpeningHour='" + strOpeningHour + '\'' +
                ", strClosingHour='" + strClosingHour + '\'' +
                ", strSlotDuration='" + strSlotDuration + '\'' +
                ", strVaccinesPerSlot='" + strVaccinesPerSlot + '\'' +
                ", strRoad='" + strRoad + '\'' +
                ", strZipCode='" + strZipCode + '\'' +
                ", strLocal='" + strLocal + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
