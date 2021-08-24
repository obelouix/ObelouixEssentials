package fr.obelouix.essentials.utils;

public class NumericValue {

    public static boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException exception) {
//            exception.printStackTrace();
        }
        return false;
    }

}
