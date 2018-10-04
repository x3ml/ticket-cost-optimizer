package pl.kubiciel.tco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.util.Pair;

public class Main {

    private final static int WEEK_DURATION = 7;
    private final static int MONTH_DURATION = 30;

    public static List<Integer> travelDays = new ArrayList<Integer>(
            Arrays.asList(1, 3, 8, 9, 10, 12, 15, 19, 45, 46, 47, 78, 81, 89, 100, 101, 103, 105, 107, 108, 109, 110, 131, 132, 133, 134, 134, 136, 137, 138, 139, 140, 145, 146, 147, 152, 190, 192,
                    204, 211, 220, 222, 224, 227, 229, 231, 234, 236, 238, 241, 243, 245, 248, 250
            ));

//    public static List<Integer> travelDays =
//            Arrays.asList(220, 222, 224, 227, 229, 231, 234, 236, 238, 241, 243, 245, 248, 250);

    public static List<Pair<Integer, List<Integer>>> travelWeeks = new ArrayList<Pair<Integer, List<Integer>>>();
    public static List<Pair<Integer, List<Integer>>> travelMonths = new ArrayList<Pair<Integer, List<Integer>>>();

    public final static int oneDayPrice = 2;
    public final static int weekPrice = 7;
    public final static int monthPrice = 25;


    public static void main(String[] args) {
        // write your code here

        System.out.println("Ticket Cost Optimizer");
        Main main = new Main();
        main.print();
        System.out.println("\ncost: " + main.calculateCost());

        travelMonths = main.calculatePeriods(MONTH_DURATION, monthPrice, travelDays);
        travelWeeks = main.calculatePeriods(WEEK_DURATION, weekPrice, travelDays);
        main.print();
        System.out.println("\ncost: " + main.calculateCost());
    }

    public List<Pair<Integer, List<Integer>>> calculatePeriods(int periodLength, int periodPrice, List<Integer> travelDaysToProcess) {
        Integer day;
        List<Pair<Integer, List<Integer>>> travelPeriods = new ArrayList<Pair<Integer, List<Integer>>>();
        List<Integer> travelDaysOutput = new ArrayList<Integer>(travelDays);
        Collections.copy(travelDaysOutput, travelDaysToProcess);

        for (int i = 0; i < travelDaysToProcess.size(); i++) {
            int cost;
            int index = i;
            day = travelDaysToProcess.get(i);
            List<Integer> period = new ArrayList<Integer>();

            int lastIndexInPeriod;
            int[] res = calculatePeriod(period, index, periodLength);
            cost = res[1];
            lastIndexInPeriod = res[0];

            if (cost > periodPrice) {
                for (Integer periodDay : period) {
                    System.out.print("\nremoving: " + periodDay);
                    travelDaysOutput.remove(periodDay);
                }
                travelPeriods.add(new Pair(day, period));
                System.out.print("\nadded " + periodLength + " day period: " + printPeriod(period));
                i = lastIndexInPeriod;
            }
        }
        travelDaysToProcess.retainAll(travelDaysOutput);
        return travelPeriods;
    }

    int[] calculatePeriod(List<Integer> period, int initialIndex, int offset) {
        int lastIndexInWeek = 0;
        int cost = 0;
        Integer day = travelDays.get(initialIndex);
        while (initialIndex < travelDays.size()) {
            Integer weekDay = travelDays.get(initialIndex);
            if (weekDay < day + offset) {
                period.add(weekDay);
                cost += oneDayPrice;
                lastIndexInWeek = initialIndex;
            } else {
                break;
            }
            initialIndex++;
        }
        return new int[] {lastIndexInWeek, cost};
    }

    public int calculateCost() {
        return travelDays.size() * oneDayPrice + travelWeeks.size() * weekPrice + travelMonths.size() * monthPrice;
    }

    public void print() {
        Iterator<Integer> itTD = travelDays.iterator();
        Iterator<Pair<Integer, List<Integer>>> itTW = travelWeeks.iterator();
        System.out.print("\nTD: " + travelDays.size() + " TW: " + travelWeeks.size() + " TM: " + travelMonths.size() + " ||| ");

        for (int i = 0, j = 0, k = 0; i < travelDays.size() || j < travelWeeks.size() || k < travelMonths.size(); ) {

            Integer nextDay = null;
            Pair<Integer, List<Integer>> nextWeek = null;
            Pair<Integer, List<Integer>> nextMonth = null;

            if (i < travelDays.size()) {
                nextDay = travelDays.get(i);
            }

            if (j < travelWeeks.size()) {
                nextWeek = travelWeeks.get(j);
            }

            if (k < travelMonths.size()) {
                nextMonth = travelMonths.get(k);
            }

            if (shouldPrintValue(nextDay, nextWeek, nextMonth)) {
                System.out.print(" " + nextDay);
                i++;
            }

            if (shouldPrintValue(nextWeek, nextDay, nextMonth)) {
                System.out.print(" " + printPeriod(nextWeek.getValue()));
                j++;
            }

            if (shouldPrintValue(nextMonth, nextDay, nextWeek)) {
                System.out.print(" [" + printPeriod(nextMonth.getValue()) + "]");
                k++;
            }
        }

        System.out.print(" END");
    }

    private boolean shouldPrintValue(Object value, Object against1, Object against2) {
        Integer valueInt = getIntegerValue(value);
        Integer against1Int = getIntegerValue(against1);
        Integer against2Int = getIntegerValue(against2);

        return valueInt != null && ((against1Int == null && against2Int == null) || ((against1Int == null || valueInt < against1Int) && (against2Int == null || valueInt < against2Int)));
    }

    private Integer getIntegerValue(Object value) {
        if (value == null) {
            return null;
        }
        return value instanceof Pair ? ((Pair<Integer, List<Integer>>)value).getKey() : (Integer) value;
    }

    private static String printPeriod(List<Integer> week) {
        StringBuilder sb = new StringBuilder("[");
        for (Integer day : week) {
            sb.append(day);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

}
