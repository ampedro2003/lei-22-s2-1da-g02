package app.miscellaneous;

import app.controller.App;
import app.controller.DataFromLegacySystemController;
import app.domain.model.Company;
import app.ui.console.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.NotSerializableException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadLegacyDataFile {

    private final DataFromLegacySystemController ctrl = new DataFromLegacySystemController();

    private List<String> csvLegacyData = new ArrayList<>();
    private List<LocalDateTime> listOfArrivalDates = new ArrayList<>();

    public void readFile(String path) throws Exception {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("\"", "");
            String[] values = line.split(";");
            csvLegacyData.add(values[0] + "|" + values[1] + "|" + values[2] + "|" + values[3] + "|" + values[4] + "|" + values[5] + "|"
                    + values[6] + "|" + values[7]);
        }
    }

    public void sortListWithAlgo() {
        String algorithmToBeUsed = App.getInstance().getSortingAlgorithm();
        switch (algorithmToBeUsed) {
            case "BubbleSort":
                Scanner sc = new Scanner(System.in);
                System.out.println();
                System.out.println("Choose the way you want to sort.");
                System.out.println("0 - Ascending");
                System.out.println("1 - Descending");
                int option = sc.nextInt();
                switch (option){
                    case 0: bubbleSortAscending();
                    case 1: bubbleSortDescending();
                }
                break;
            case "OtherSort":
                System.out.println("Not implemented yet");
                break;
        }
    }

    public void chooseOrderToSort(){
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println();
            System.out.println("Choose the way you want to sort.");
            System.out.println("0 - Ascending");
            System.out.println("1 - Descending");
            int option = sc.nextInt();
            switch (option){
                case 0: bubbleSortAscending();
                case 1: bubbleSortDescending();
            }
        }catch (Exception e){
            e.printStackTrace();
            chooseOrderToSort();
        }
    }

    public void updateLegacyFile() throws NotSerializableException {
        if (!ctrl.getSNSUserList().isEmpty() && !ctrl.getVaccines().isEmpty()) {
            for (int i = 0; i < csvLegacyData.size(); i++) {
                String[] values;
                float percentage = (float) i * 100 / csvLegacyData.size();
                boolean flag;
                int j;
                int k;

                System.out.printf("\n%.1f%% complete...", percentage);
                values = csvLegacyData.get(i).split("\\|");

                for (j = 0; j < ctrl.getSNSUserList().size(); j++) {
                    if (ctrl.getSNSUserList().get(j).getSnsUserNumber() == Integer.parseInt(values[0])) {
                        break;
                    }
                }
                csvLegacyData.set(i, ctrl.getSNSUserList().get(j).getStrName() + "|" + csvLegacyData.get(i));

                for (k = 0; k < ctrl.getVaccines().size(); k++) {
                    if (ctrl.getVaccines().get(k).getName().equals(values[1])) {
                        break;
                    }
                }
                csvLegacyData.set(i, csvLegacyData.get(i) + "|" + ctrl.getVaccines().get(k).getVaccineType().getDescription());
            }
            //System.out.println();
            //printUpdatedLegacy(csvLegacyData);
            ctrl.exportDataToFile(csvLegacyData);
        } else {
            System.out.println("Either the SNS User list is empty or the Vaccine list is," +
                    " this makes it impossible to update the legacy " +
                    "data with the SNS User number and the Vaccine's description.");
        }
    }

    public void printUpdatedLegacy(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public void bubbleSortAscending(){

        for (int i = 0; i < listOfArrivalDates.size() - 1; ++i) {

            for (int j = 0; j < listOfArrivalDates.size() - i - 1; ++j) {

                if (listOfArrivalDates.get(j + 1).isBefore(listOfArrivalDates.get(j))) {

                    LocalDateTime swap = listOfArrivalDates.get(j);
                    listOfArrivalDates.set(j,listOfArrivalDates.get(j + 1));
                    listOfArrivalDates.set(j+1,swap);

                    String swapString = csvLegacyData.get(j);
                    csvLegacyData.set(j,csvLegacyData.get(j+1));
                    csvLegacyData.set(j+1,swapString);
                }
            }
        }
        printUpdatedLegacy(csvLegacyData);
        }



    public void bubbleSortDescending(){

        for (int i = 0; i < listOfArrivalDates.size() - 1; ++i) {

            for (int j = 0; j < listOfArrivalDates.size() - i - 1; ++j) {

                if (listOfArrivalDates.get(j + 1).isAfter(listOfArrivalDates.get(j))) {

                    LocalDateTime swap = listOfArrivalDates.get(j);
                    listOfArrivalDates.set(j,listOfArrivalDates.get(j + 1));
                    listOfArrivalDates.set(j+1,swap);

                    String swapString = csvLegacyData.get(j);
                    csvLegacyData.set(j,csvLegacyData.get(j+1));
                    csvLegacyData.set(j+1,swapString);
                }
            }
        }
        printUpdatedLegacy(csvLegacyData);
    }


    public void choosePositionToSort(){
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println();
            System.out.println("Choose the option you want to sort.");
            System.out.println("0 - Arrival Date Time");
            System.out.println("1 - Leaving Date Time");
            int option = sc.nextInt();
            switch (option){
                case 0: setList(6);
                case 1: setList(8);
            }
        }catch (Exception e){
            e.printStackTrace();
            choosePositionToSort();
        }
    }



    public void setList(int position) {
        String[] values;
        listOfArrivalDates.clear();
        for (String csvLegacyDatum : csvLegacyData) {
            values = csvLegacyDatum.split("\\|");
            String date = values[position];
            String[] dateAndHour = date.split(" ");
            String[] dayMonthYear = dateAndHour[0].split("/");
            int year = Integer.parseInt(dayMonthYear[2]);
            int month = Integer.parseInt(dayMonthYear[0]);
            int day = Integer.parseInt(dayMonthYear[1]);
            String[] hourAndMinute = dateAndHour[1].split(":");
            int hour = Integer.parseInt(hourAndMinute[0]);
            int minute = Integer.parseInt(hourAndMinute[1]);

            LocalDateTime dataToBeAdded = LocalDateTime.of(year, month, day, hour, minute);
            listOfArrivalDates.add(dataToBeAdded);
        }
    }


}
