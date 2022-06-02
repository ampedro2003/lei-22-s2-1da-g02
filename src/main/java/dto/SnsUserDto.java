package dto;

import app.domain.model.VaccineBulletin;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is just a DTO (Data Transfer Object), responsible for helping to transfer data from the UI to the Domain.
 * DTO related to the US014
 *
 * @author João Castro <1210816@isep.ipp.pt>
 */
public class SnsUserDto {

    public String strName;

    public int snsUserNumber;

    public String strEmail;
    public String strBirthDate;

    public String strPhoneNumber;
    public String strSex;
    public String strAddress;
    public String strCitizenCardNumber;
    public String strPassword;

    public List<VaccineBulletin> vaccineBulletins = new ArrayList<>();

    public SnsUserDto(String strName, int snsUserNumber, String strEmail, String strBirthDate, String strPhoneNumber, String strSex, String strAddress, String strCitizenCardNumber, String strPassword) {
        this.strName = strName;
        this.snsUserNumber = snsUserNumber;
        this.strEmail = strEmail;
        this.strBirthDate = strBirthDate;
        this.strPhoneNumber = strPhoneNumber;
        this.strSex = strSex;
        this.strAddress = strAddress;
        this.strCitizenCardNumber = strCitizenCardNumber;
        this.strPassword = strPassword;
    }

    public SnsUserDto() {
    }
}
