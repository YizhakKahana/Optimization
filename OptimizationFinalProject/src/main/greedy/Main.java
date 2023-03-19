package main.greedy;

import main.Item;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Random rd = new Random(); // creating Random object

    public static Item[] calculateBest(Item[] items, int bagSize) {
        Item[] sortedItems = items.clone();
        Arrays.sort(sortedItems, Comparator.comparing(a -> - (double) a.value / a.weight));

        int i;
        for (i = 0; i < sortedItems.length && bagSize >= 0; i++) {
            bagSize -= sortedItems[i].weight;
        }

        return Arrays.stream(sortedItems).limit(i - 1).collect(Collectors.toList()).toArray(new Item[i - 1]);
    }

    public static int getMaxValue(Item[] items, int bagSize) {
        return Arrays.stream(calculateBest(items, bagSize)).map(a -> a.value).reduce(Integer::sum).orElse(0);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("What is the bag size? ");
        int bagSize = sc.nextInt();
        System.out.print("How many items to generate? ");
        int itemsAmount = sc.nextInt();

        Item[] items = new Item[itemsAmount];

        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(rd.nextInt(bagSize) + 1, rd.nextInt((int) Math.pow(bagSize, 0.5)));
//            System.out.println("Item " + i + ": weight - " + items[i].weight + ", value - " + items[i].value); // printing each array element
        }

        System.out.println("Max Value:\t" + getMaxValue(items, bagSize));
    }
}
