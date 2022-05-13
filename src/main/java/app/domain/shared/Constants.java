package app.domain.shared;


import app.domain.model.Company;

/**
 *
 * @author Paulo Maio <pam@isep.ipp.pt>
 */
public class Constants {

    private Company company;
    public static final String ROLE_ADMIN = "ADMINISTRATOR";

    public static final String ROLE_RECEPTIONIST = "RECEPTIONIST";

    public static final String ROLE_NURSE = "NURSE";
    public static final String ROLE_SNS_USER = "SNS_USER";
    public static final String ROLE_CENTRE_COORDINATOR = "CENTRE_COORDINATOR";
    public static final String PARAMS_FILENAME = "config.properties";
    public static final String PARAMS_COMPANY_DESIGNATION = "Company.Designation";

    public static final int PASSWORD_LENGTH = 7;
    public static final int ID_LENGTH = 5;

    public static final int NUMBER_OF_PHONE_NUMBER_DIGITS = 9;

    public static final int STARTING_NUMBER_PORTUGUESE_PHONE = 9;

    public static final int FIRST_SECOND_NUMBER_PORTUGUESE_PHONE = 1;

    public static final int SECOND_SECOND_NUMBER_PORTUGUESE_PHONE = 2;

    public static final int THIRD_SECOND_NUMBER_PORTUGUESE_PHONE = 3;

    public static final int FOURTH_SECOND_NUMBER_PORTUGUESE_PHONE = 6;

    public static final int NUMBER_OF_CITIZEN_CARD_DIGITS = 12;

    public static final int FIRST_SECOND_DIGIT_CC = 10;
    }
