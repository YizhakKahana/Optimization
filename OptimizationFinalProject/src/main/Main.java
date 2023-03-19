package main;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

import static main.dynamic.Main.knapsackDyProg;
import static main.genetic.Main.calculateBestValue;
import static main.greedy.Main.getMaxValue;
import static main.naive.Main.calculateNaive;

public class Main {

    static Random rd = new Random(); // creating Random object

    public static long[] generateScenario(int bagSize, int itemsAmount) {
        /*
         * Pack and Weight - Value
         */
        Item[] items = new Item[itemsAmount];

        for (int i = 0; i < items.length; i++) {
            int distance = 0;
            int v = rd.nextInt((bagSize - distance)) + 1;
            items[i] = new Item(v + rd.nextInt(distance + 1), v + rd.nextInt(distance + 1));

//            items[i] = new Item(v, 4 * v);

//            items[i] = new Item(rd.nextInt(bagSize) + 1, rd.nextInt(bagSize) + 1);

//            System.out.println("Item " + i + ": weight - " + items[i].weight + ", value - " + items[i].value); // printing each array element
        }

//        System.out.println("### value");
//        for (Item item : items) {
//            System.out.println(item.value);
//        }
//
//        System.out.println("### weight");
//        for (Item item : items) {
//            System.out.println(item.weight);
//        }

        long startTime = 0;

//        startTime = System.nanoTime();
//        int naiveMaxValue = calculateNaive(bagSize, items, items.length);
//        long naiveTimeNano = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int dynamicMaxValue = knapsackDyProg(items, bagSize);
        long dynamicTimeNano = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int populationNumber = items.length;
        int[] geneticResult = calculateBestValue(items, bagSize, populationNumber);
        int geneticMaxValue = geneticResult[0];
        int numberOfGenerations = geneticResult[1];
        long geneticTimeNano = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        int greedyMaxValue = getMaxValue(items, bagSize);
        long greedyTimeNano = System.nanoTime() - startTime;

        return new long[] {
                dynamicMaxValue, dynamicTimeNano,
                greedyMaxValue, greedyTimeNano,
                geneticMaxValue, geneticTimeNano, numberOfGenerations, populationNumber,
//                naiveMaxValue, naiveTimeNano
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("What is the bag size? ");
        int bagSize = sc.nextInt();
        System.out.print("How many items to generate? ");
        int itemsAmount = sc.nextInt();
        System.out.print("How many scenarios to simulate? ");
        int scenarios = sc.nextInt();

        long dynamicMaxValue;
        long dynamicTimeNano = 0;
        long greedyMaxValue;
        long greedyTimeNano = 0;
        long geneticMaxValue;
        long geneticTimeNano = 0;
        long numberOfGenerations = 0;
        long populationNumber = 0;
//        long naiveMaxValue;
//        long naiveTimeNano = 0;
        double greedyAccuracy = 0;
        double geneticAccuracy = 0;
        double naiveAccuracy = 0;

        for (int i = 1; i <= scenarios; i++) {
            long[] results = generateScenario(bagSize, itemsAmount);

            dynamicMaxValue = results[0];
            dynamicTimeNano += results[1];
            greedyMaxValue = results[2];
            greedyTimeNano += results[3];
            geneticMaxValue = results[4];
            geneticTimeNano += results[5];
            numberOfGenerations += results[6];
            populationNumber += results[7];
//            naiveMaxValue = results[8];
//            naiveTimeNano += results[9];
            greedyAccuracy += (double) greedyMaxValue*100/dynamicMaxValue;
            geneticAccuracy += (double) geneticMaxValue*100/dynamicMaxValue;
//            naiveAccuracy += (double) naiveMaxValue*100/dynamicMaxValue;
        }

        dynamicTimeNano /= scenarios;
        greedyTimeNano /= scenarios;
        geneticTimeNano /= scenarios;
        greedyAccuracy /= scenarios;
        geneticAccuracy /= scenarios;
//        naiveAccuracy /= scenarios;
        populationNumber /= scenarios;
        numberOfGenerations /= scenarios;

        System.out.println("Number of scenarios: " + scenarios);
        System.out.println("Dynamic Time AVG (millis): " + dynamicTimeNano / 1000000 + " | Accuracy: 100%");
//        System.out.println("Naive Time AVG (millis): " + naiveTimeNano / 1000000 + " | Accuracy: " + new DecimalFormat("##.##").format(naiveAccuracy) + "%");
        System.out.println("Greedy Time AVG (millis): " + greedyTimeNano / 1000000 + " | Accuracy: " + new DecimalFormat("##.##").format(greedyAccuracy) + "%");
        System.out.println("Genetic Time AVG (millis): " + geneticTimeNano / 1000000 + " | Accuracy: " + new DecimalFormat("##.##").format(geneticAccuracy) + "%");
        System.out.println("Number of generations: " + numberOfGenerations);
        System.out.println("Number of populations: " + populationNumber);
    }
}
