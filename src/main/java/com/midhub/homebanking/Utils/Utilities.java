package com.midhub.homebanking.Utils;

import java.util.Random;

public class Utilities {
    public static String generateAccountNumber() {
        int accountNumber = getRandomNumberUsingNextInt(10000000, 99999999);
        return "VIN-" + accountNumber;
    }

    public static String generateCardNumber() {
        return getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999) + "-" +
                getRandomNumberUsingNextInt(1000, 9999);
    }

    public static int generateRandomCVV() {
        int randomCVV = getRandomNumberUsingNextInt(100, 999);
        return randomCVV;
    }

    private static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
