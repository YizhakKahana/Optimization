package main.dynamic;

import main.Item;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static Random rd = new Random(); // creating Random object

    public static int knapsackDyProg(Item[] items, int M) {
        int n = items.length;
        int[][] B = new int[n + 1][M + 1];

        for (int i=0; i<=n; i++)
            for (int j=0; j<=M; j++) {
                B[i][j] = 0;
            }

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= M; j++) {
                B[i][j] = B[i - 1][j];

                if ((j >= items[i-1].weight) && (B[i][j] < B[i - 1][j - items[i - 1].weight] + items[i - 1].value)) {
                    B[i][j] = B[i - 1][j - items[i - 1].weight] + items[i - 1].value;
                }

//                System.out.print(B[i][j] + " ");
            }
//            System.out.print("\n");
        }

//        System.out.println("Max Value Dynamic:\t" + B[n][M]);

//        System.out.println("Selected Items: ");

//        while (n != 0) {
//            if (B[n][M] != B[n - 1][M]) {
//                System.out.println("\tItem " + n + " with weight = " + W[n - 1] + " and value = " + V[n - 1]);
//
//                M = M - W[n-1];
//            }
//
//            n--;
//        }
        return B[n][M];
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("What is the bag size? ");
        int bagSize = sc.nextInt();
        System.out.print("How many items to generate? ");
        int itemsAmount = sc.nextInt();

        /*
         * Pack and Weight - Value
         */
        Item[] items = new Item[itemsAmount];

        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(rd.nextInt(bagSize) + 1, rd.nextInt(bagSize*5));
            System.out.println("Item " + i + ": weight - " + items[i].weight + ", value - " + items[i].value); // printing each array element
        }

        /*
         * Run the algorithm
         */
        knapsackDyProg(items, bagSize);
    }
}
