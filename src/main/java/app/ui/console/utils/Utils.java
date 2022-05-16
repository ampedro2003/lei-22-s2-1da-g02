package app.ui.console.utils;

import app.controller.App;
import app.controller.CreateVaccinationCenterController;
import app.controller.RegisterNewEmployeeController;
import app.controller.SpecifyNewVaccineTypeController;
import app.domain.model.*;
import app.domain.shared.Constants;
import dto.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paulo Maio <pam@isep.ipp.pt>
 * @author Pedro Monteiro <1211076@isep.ipp.pt>
 * @author Gustavo Jorge <1211061@isep.ipp.pt>
 * @author Guilherme Sousa <1211073@isep.ipp.pt>
 */
public class Utils {

    private static final int MAXNUMBEROFCHARSSNSUSERNUMBER = 9;

    /**
     * Reads a String from the console
     *
     * @param prompt Read String
     * @return String
     */
    static public String readLineFromConsole(String prompt) {
        try {
            System.out.print("\n" + prompt);

            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);

            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Validates emails
     * Checks if there is a "@" and is it ends with a valid domain
     *
     * @param strEmail The email to be validated
     * @return returnes true if the email is valid
     */
    static public boolean validateEmail(String strEmail) {
        if (!strEmail.contains("@") && !strEmail.contains("."))
            return false;

        String[] emailSplitter = strEmail.split("@");
        String[] validEmailDomain = {"gmail.com", "hotmail.com", "isep.ipp.pt", "sapo.pt", "outlook.com"};

        for (String s : validEmailDomain) {
            if (Objects.equals(emailSplitter[1], s))
                return true;
        }
        return false;
    }

    /**
     * Reads a String from the console and converts it into an integer
     *
     * @param prompt Read integer
     * @return Integer
     */
    static public int readIntegerFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }

    /**
     * Reads a String from the console and converts it into a double
     *
     * @param prompt Read double
     * @return Double
     */
    static public double readDoubleFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);


                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }


    /**
     * Validate phone numbers
     * Checks if the first number is a 9 and if the second is whether a 1, 2, 3 or 6
     * Checks if there are only 9 numbers
     *
     * @param strPhoneNumber The email to be validated
     * @return returnes true if the email is valid
     */
    public static boolean validatePhoneNumber(String strPhoneNumber) {
        final int NUMBER_OF_PHONE_NUMBER_DIGITS = 9;
        final int STARTING_NUMBER_PORTUGUESE_PHONE = 9;
        final int FIRST_SECOND_NUMBER_PORTUGUESE_PHONE = 1;
        final int SECOND_SECOND_NUMBER_PORTUGUESE_PHONE = 2;
        final int THIRD_SECOND_NUMBER_PORTUGUESE_PHONE = 3;
        final int FOURTH_SECOND_NUMBER_PORTUGUESE_PHONE = 6;

        if (strPhoneNumber.length() == NUMBER_OF_PHONE_NUMBER_DIGITS && Integer.parseInt(strPhoneNumber) % 1 == 0) {
            int ch1 = Integer.parseInt(String.valueOf(strPhoneNumber.charAt(0)));
            if (ch1 != STARTING_NUMBER_PORTUGUESE_PHONE)
                return false;

            int ch2 = Integer.parseInt(String.valueOf(strPhoneNumber.charAt(1)));
            if (ch2 != FIRST_SECOND_NUMBER_PORTUGUESE_PHONE && ch2 != SECOND_SECOND_NUMBER_PORTUGUESE_PHONE && ch2 != THIRD_SECOND_NUMBER_PORTUGUESE_PHONE && ch2 != FOURTH_SECOND_NUMBER_PORTUGUESE_PHONE) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean validateCitizenCardNumber(String strCitizenCardNumber) {
        final int NUMBER_OF_CITIZEN_CARD_DIGITS = 12;
        final int FIRST_SECOND_DIGIT_CC = 10;
        String noBlankSpotsCitizenCardNumber = strCitizenCardNumber.replaceAll("\\s", "");
        int sum = 0;
        if (noBlankSpotsCitizenCardNumber.length() != NUMBER_OF_CITIZEN_CARD_DIGITS)
            return false;

        boolean secondDigit = true;

        for (int digit = 0; digit < noBlankSpotsCitizenCardNumber.length(); digit++) {
            String toUpperCase = String.valueOf(noBlankSpotsCitizenCardNumber.charAt(digit)).toUpperCase();
            int value = getValueFromCitizenCardNumberDigit(toUpperCase);

            if (secondDigit) {
                value *= 2;

                if (value >= 10)
                    value -= 9;
            }
            sum += value;
            secondDigit = !secondDigit;
        }
        return (sum % FIRST_SECOND_DIGIT_CC) == 0;
    }

    static public boolean validateAddress(String strAddress) {
        String[] splitAddress = strAddress.split("/");
        if (splitAddress.length != 3)
            return false;

        String zipCode = splitAddress[1].trim();
        if (zipCode.length() != 8 || zipCode.charAt(4) != '-')
            return false;

        return true;
    }

    static public boolean validateSex(String strSex) {
        return strSex.equals("Male") || strSex.equals("Female") || strSex.isEmpty();
    }

    static public boolean validateBirthDate(String strBirthDate) {
        String dateFormat = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(strBirthDate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    static public int getValueFromCitizenCardNumberDigit(String letter) {
        switch (letter) {
            case "0":
                return 0;
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "A":
                return 10;
            case "B":
                return 11;
            case "C":
                return 12;
            case "D":
                return 13;
            case "E":
                return 14;
            case "F":
                return 15;
            case "G":
                return 16;
            case "H":
                return 17;
            case "I":
                return 18;
            case "J":
                return 19;
            case "K":
                return 20;
            case "L":
                return 21;
            case "M":
                return 22;
            case "N":
                return 23;
            case "O":
                return 24;
            case "P":
                return 25;
            case "Q":
                return 26;
            case "R":
                return 27;
            case "S":
                return 28;
            case "T":
                return 29;
            case "U":
                return 30;
            case "V":
                return 31;
            case "W":
                return 32;
            case "X":
                return 33;
            case "Y":
                return 34;
            case "Z":
                return 35;
        }
        throw new IllegalArgumentException("Invalid Value in the Document.");
    }

    /**
     * Read a date from the console
     *
     * @param prompt Read dates from console
     * @return Read date
     */
    static public Date readDateFromConsole(String prompt) {
        do {
            try {
                String strDate = readLineFromConsole(prompt);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                return df.parse(strDate);
            } catch (ParseException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }


    /**
     * Generates a random password with 7 alphanumeric characters, being 3 capital Letters and 2 numbers
     *
     * @return String - a random password
     */
    static public String passwordGenerator() {
        final int PASSWORD_LENGTH = 7;
        final String alphabetLetters = "abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789";
        StringBuilder password = new StringBuilder();
        Random generate = new Random();

        StringBuilder employeePassword = new StringBuilder();

        for (int position = 0; position < PASSWORD_LENGTH; position++) {
            if (position <= 2)
                password.append(Character.toUpperCase(alphabetLetters.charAt(generate.nextInt(25))));
            else if (position <= 4)
                password.append((generate.nextInt(9)));
            else
                password.append(alphabetLetters.charAt(generate.nextInt(alphabetLetters.length())));
        }

        for (int position = 0; position < PASSWORD_LENGTH; position++) {
            int index = (generate.nextInt(password.length()));
            char passwordAux = password.charAt(index);
            employeePassword.append(passwordAux);
            password.deleteCharAt(index);
        }
        return String.valueOf(employeePassword);
    }

    /**
     * Let the user confirm the data that he introduced before
     *
     * @param message A choosen message
     * @return Boolean - true if the user confirm the data
     */
    static public boolean confirm(String message) {
        String input;
        do {
            input = Utils.readLineFromConsole("\n" + message + "\n");
        } while (!input.equalsIgnoreCase("s") && !input.equalsIgnoreCase("n"));

        return input.equalsIgnoreCase("s");
    }

    /**
     * Shows the choosen list (showList) and let the user choose an option
     *
     * @param list A choosen list
     * @param header A choosen header
     * @return Object - the option choosen by the user
     */
    static public Object showAndSelectOne(List list, String header) {
        showList(list, header);
        return selectsObject(list);
    }

    /**
     * Shows the choosen list (showList) and let the user choose an option
     *
     * @param list A choosen list
     * @param header A choosen header
     * @return Integer - the option choosen by the user
     */
    static public int showAndSelectIndex(List list, String header) {
        showList(list, header);
        return selectsIndex(list);
    }

    /**
     * Prints the chossen list with a header and options
     *
     * @param list - A choosen list
     * @param header - A choosen header
     */
    static public void showList(List list, String header) {
        System.out.println(header);
        System.out.println();
        int index = 0;
        for (Object o : list) {
            index++;

            System.out.println(index + ". " + o.toString());
        }
        System.out.println("");
        System.out.println("0 - Cancel");
    }

    /**
     * Prints the choosen list with options and let the user choose one
     *
     * @param list A choosen list
     * @return Object - the choosen option from a list
     */
    static public Object selectsObject(List list) {
        String input;
        int value;
        do {
            input = Utils.readLineFromConsole("Type your option: ");
            value = Integer.parseInt(input);
        } while (value < 0 || value > list.size());

        if (value == 0) {
            return null;
        } else {
            return list.get(value - 1);
        }
    }

    /**
     * Prints the choosen list with options and let the user choose one
     *
     * @param list A choosen list
     * @return Integer - The choosen option from the list
     */
    static public int selectsIndex(List list) {
        String input = "";
        int value = -1;
        do {
            try {
                input = Utils.readLineFromConsole("Type your option: ");
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
            }
        } while (value < 0 || value > list.size());

        return value - 1;
    }

    /**
     * Asks the User for its confirmation.
     *
     * @return true if the User confirms the creation of the Vaccine.
     */
    public static boolean confirmCreation() {
        System.out.println("Do you confirm this data?");
        System.out.println("1 - Yes");
        System.out.println("0 - No");
        Scanner sc = new Scanner(System.in);
        System.out.printf("%nType your option: ");
        int check = 0;
        int option = 0;
        do {
            try {
                option = sc.nextInt();
                sc.nextLine();
                check = 1;
            } catch (InputMismatchException e) {
                System.out.println("Insert a valid option.");
                sc.nextLine();
            }
        } while (check == 0);

        return option == 1;
    }


    public static Vaccine createVaccine(String name, int id, String brand, double dosage, int minAge, int maxAge, int timeBetweenDoses) {
        Company c = App.getInstance().getCompany();
        bootstrapOptional();
        AdministrationProcess aP = new AdministrationProcess(new ArrayList<>(Arrays.asList(new ArrayList<>(List.of(minAge)), new ArrayList<>(List.of(maxAge)))), new ArrayList<>(List.of(2)), new ArrayList<>(List.of(dosage)), new ArrayList<>(Arrays.asList(new ArrayList<>(List.of(timeBetweenDoses)))));

        Vaccine v = new Vaccine(name, id, brand, aP, c.getVaccineTypes().get(0));


        return v;
    }



    public static int insertInt(String errorMessage) {
        int check = 0;
        Scanner sc = new Scanner(System.in);
        int input = -1;
        do {
            try {
                input = sc.nextInt();
                sc.nextLine();

                if (input >= 0) {
                    check = 1;
                } else {
                    System.out.print(errorMessage);
                }
            } catch (InputMismatchException e) {
                System.out.print(errorMessage);
                sc.nextLine();
            }
        } while (check == 0);
        return input;
    }



    public static boolean validateSNSUserNumber(String strSNSUserNumber) {
        return strSNSUserNumber.trim().matches("^[0-9]*$") && strSNSUserNumber.length() == MAXNUMBEROFCHARSSNSUSERNUMBER;
    }

    public static ArrayList<String> getVaccinationCenterList() {
        ArrayList<String> getVaccinationCenterNameList = new ArrayList<>();
        Company company = App.getInstance().getCompany();

        for (int position = 0; position < company.getVaccinationCenters().size(); position++) {
            getVaccinationCenterNameList.add(company.getVaccinationCenters().get(position).getStrName());
        }

        return getVaccinationCenterNameList;
    }

    public static int selectFromList(List list, String header) {

        System.out.println(header + ":");
        System.out.println();
        int optionNumber = 1;
        for (Object o : list) {
            System.out.println(optionNumber + " - " + o);
            optionNumber++;
        }

        do {
            System.out.println();
            System.out.print("Insert your option: ");
            int option = Utils.insertInt("Insert a valid option: ");

            if ((option >= 0) && (option < list.size() + 1))
                return option - 1 ;
            else
                System.out.println("Invalid option.");

        } while (true);

    }

    public static boolean arrayListIsEmpty(List<VaccineType> getVaccineTypes, List<VaccinationCenter> getVaccinationCenters, List<SNSUser> getSNSUser) {
        return !getSNSUser.isEmpty() && !getVaccinationCenters.isEmpty() && !getVaccineTypes.isEmpty();
    }

    public static void bootstrapOptional() {

        SpecifyNewVaccineTypeController ctrlVt = new SpecifyNewVaccineTypeController();

        ctrlVt.saveVaccineType("COVID", "A vaccine to prevent serious infections of the Covid-19 Virus", VaccineType.vaccineTechnologies[5]);
        ctrlVt.saveVaccineType("FLU22", "A vaccine to prevent serious infections of the Flu virus related to 2022 variant", VaccineType.vaccineTechnologies[1]);
        ctrlVt.saveVaccineType("CCCCC", "Vaccine Type 3", VaccineType.vaccineTechnologies[2]);

        RegisterNewEmployeeController ctrlEmp = new RegisterNewEmployeeController();

        RegisterNewEmployeeDto dtoEmp = new RegisterNewEmployeeDto();
        dtoEmp.id = "CC-95634";
        dtoEmp.name = "João";
        dtoEmp.password = "AAA22vx";
        dtoEmp.phoneNumber = "915604427";
        dtoEmp.citizenCardNumber = "11960343 8 ZW1";
        dtoEmp.email = "joao@gmail.com";
        dtoEmp.address = "Via Diagonal / 4475-079 / Porto";
        ctrlEmp.saveCreatedEmployee(dtoEmp, "Center Coordinator");

        RegisterNewEmployeeDto dtoEmp1 = new RegisterNewEmployeeDto();
        dtoEmp1.id = "CC-92634";
        dtoEmp1.name = "Francisca";
        dtoEmp1.password = "ah56BCC";
        dtoEmp1.phoneNumber = "919700873";
        dtoEmp1.citizenCardNumber = "14268862 2 ZX8";
        dtoEmp1.email = "francisca@gmail.com";
        dtoEmp1.address = "Rua de São Tomé / 4200-489 / Porto";
        ctrlEmp.saveCreatedEmployee(dtoEmp1, "Center Coordinator");

        RegisterNewEmployeeDto dtoEmp2 = new RegisterNewEmployeeDto();
        dtoEmp2.id = "NR-91272";
        dtoEmp2.name = "Joana";
        dtoEmp2.password = "fv93ACK";
        dtoEmp2.phoneNumber = "919880654";
        dtoEmp2.citizenCardNumber = "38002291 5 ZY5";
        dtoEmp2.email = "joana@gmail.com";
        dtoEmp2.address = "Rua De Azevedo De Albuquerque / 4050-076 / Porto";
        ctrlEmp.saveCreatedEmployee(dtoEmp2, "Nurse");

        RegisterNewEmployeeDto dtoEmp3 = new RegisterNewEmployeeDto();
        dtoEmp3.id = "CC-12345";
        dtoEmp3.name = "Carla";
        dtoEmp3.password = "AAA12aa";
        dtoEmp3.phoneNumber = "912345678";
        dtoEmp3.citizenCardNumber = "15925823 5 ZX3";
        dtoEmp3.email = "carla@gmail.com";
        dtoEmp3.address = "Rua Carlos Kimbo Slice / 4440-123 / Porto";
        ctrlEmp.saveCreatedEmployee(dtoEmp3, "Receptionist");

        CreateVaccinationCenterController ctrlVc = new CreateVaccinationCenterController();
        MassVaccinationCenterDto mvcDto = new MassVaccinationCenterDto();
        mvcDto.strID = "1234";
        mvcDto.strName = "CVC Matosinhos";
        mvcDto.strPhoneNumber = "915607321";
        mvcDto.strFax = "915607321";
        mvcDto.strEmail = "cvcmatosinhos@gmail.com";
        mvcDto.strClosingHour = "16";
        mvcDto.strOpeningHour = "9";
        mvcDto.strVaccinesPerSlot = "1";
        mvcDto.strSlotDuration = "30";
        mvcDto.strWebsite = "www.cvcmatosinhos.com";
        mvcDto.strRoad = "Rua do Amial";
        mvcDto.strZipCode = "4460-098";
        mvcDto.strLocal = "Matosinhos";
        mvcDto.strCenterCoordinatorID = "CC-95634";
        mvcDto.vaccineType = new VaccineType("COVID", "To prevent serious COVID-19 infections", VaccineType.vaccineTechnologies[5]);
        MassVaccinationCenter massVaccinationCenter1 = new MassVaccinationCenter(mvcDto.strID,mvcDto.strName,mvcDto.strPhoneNumber,mvcDto.strEmail, mvcDto.strFax, mvcDto.strWebsite,mvcDto.strOpeningHour,mvcDto.strClosingHour,mvcDto.strSlotDuration, mvcDto.strVaccinesPerSlot,mvcDto.strRoad,mvcDto.strZipCode,mvcDto.strLocal,mvcDto.strCenterCoordinatorID,mvcDto.vaccineType);
        ctrlVc.saveMassVaccinationCenter(mvcDto);

        Company c = App.getInstance().getCompany();


        SNSUserDto snsUserDto = new SNSUserDto("User Default", "243989890", "user@gmail.com", "16/03/2003", "915604428", "Male", "Rua da Telheira / 4560-098 / Porto", "14698413 7 ZY7", "AAA22aa");
        c.saveSNSUser(snsUserDto);
        c.getAuthFacade().addUserWithRole("UserDefault", "user@gmail.com", "123", Constants.ROLE_SNS_USER);

        SNSUserDto snsUserDto1 = new SNSUserDto("User Default", "111111111", "u1@gmail.com", "16/03/2003", "915604429", "Male", "Rua da Telheira / 4560-098 / Porto", "16068893 0 ZX7", "AAA22aa");
        c.saveSNSUser(snsUserDto1);
        c.getAuthFacade().addUserWithRole("UserDefault1", "u1@gmail.com", "123", Constants.ROLE_SNS_USER);



        HealthcareCenterDto hCcDto = new HealthcareCenterDto("1236", "Centro de Saude da Maia", "915372312", "csmaia@gmail.com", "915372312", "www.csmaia.com", "9", "17", "15", "3", "Rua da Escola", "4470-073", "Maia", "CC-92634", "Norte", "SNS", new ArrayList<>(List.of(new VaccineType("COVID", "To prevent serious COVID-19 infections", VaccineType.vaccineTechnologies[5]), new VaccineType("FLU22", "To prevent serious Flu infections", VaccineType.vaccineTechnologies[5]))));
        c.saveHealthcareCenter(hCcDto);


        ScheduledVaccine scheduledVaccine1 = new ScheduledVaccine(243989890, c.getVaccineTypes().get(0), LocalDateTime.of(2022,5,18, 10, 0) );
        ScheduledVaccine scheduledVaccine2 = new ScheduledVaccine(111111111, c.getVaccineTypes().get(0), LocalDateTime.of(2022,5,18, 11, 0) );


        c.getVaccinationCenters().get(0).addAppointment(scheduledVaccine1);
        c.getVaccinationCenters().get(0).addAppointment(scheduledVaccine2);

        //ADICIONAR UMA VACINA A UM USER:
        //AdministrationProcess administrationProcess = new AdministrationProcess();
        //TakenVaccine takenVaccine = new TakenVaccine( scheduledVaccine1.getDate(new Vaccine("Test", "12", "Brand", "20.0", "12", "15", "16"), ));
        //c.getSNSUserList().get(0).registerVaccine(a);
    }
}