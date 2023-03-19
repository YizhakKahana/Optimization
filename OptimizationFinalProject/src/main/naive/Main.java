package main.naive;

import main.Item;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Random rd = new Random(); // creating Random object

    public static int calculateNaive(int bagSize, Item[] items, int n)
    {
        // Base Case
        if (n == 0 || bagSize == 0)
            return 0;

        // If weight of the nth item is
        // more than Knapsack capacity W,
        // then this item cannot be included
        // in the optimal solution
        if (items[n - 1].weight > bagSize)
            return calculateNaive(bagSize, items, n - 1);

            // Return the maximum of two cases:
            // (1) nth item included
            // (2) not included
        else
            return Math.max(items[n - 1].value
                            + calculateNaive(bagSize - items[n - 1].weight, items, n - 1),
                    calculateNaive(bagSize, items, n - 1));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("What is the bag size? ");
        int bagSize = sc.nextInt();
        System.out.print("How many items to generate? ");
        int itemsAmount = sc.nextInt();

        Item[] items = new Item[itemsAmount];

        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(rd.nextInt((int) Math.pow(bagSize, 0.5)), rd.nextInt((int) Math.pow(bagSize, 0.5)));
//            items[i] = new Item(rd.nextInt(bagSize) + 1, rd.nextInt(bagSize) + 1);
//            System.out.println("Item " + i + ": weight - " + items[i].weight + ", value - " + items[i].value); // printing each array element
        }
        long startTime;

        startTime = System.currentTimeMillis();
        System.out.println("Max Value:\t" + calculateNaive(bagSize, items, items.length));
        System.out.println("Time in millis:\t" + (System.currentTimeMillis() - startTime));
    }
}
