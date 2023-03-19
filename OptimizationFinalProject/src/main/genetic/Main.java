package main.genetic;

import main.Item;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static Random rd = new Random(); // creating Random object
    static double mutationDivider = 1;
    static double mutation = 0;
//    static int populationNumber = 1000;
    static int maxValue = 0;
    static int stopNotImprovingGenetic = 100;
    static int maxGeneration = 500;

    public static void calculateFitness(Item[] items, int bagSize, DNA dna) {
        int weight = 0;
        int value = 0;
        for (int i = 0; i < items.length && weight <= bagSize; i++) {
            if (dna.bits[i] == 1) {
                weight += items[i].weight;
                value += items[i].value;
            }
        }

        int fitness = weight <= bagSize ? value : 0;

        if (maxValue < fitness) {
            maxValue = fitness;
        }

        dna.fitness = (int) Math.pow(fitness, 2);
//        dna.fitness = fitness;
    }

    public static DNA crossover(DNA parent1, DNA parent2) {
        DNA crossovered = new DNA(parent1.bits.length);
        int pivot = rd.nextInt(parent1.bits.length);
//        int pivot = parent1.bits.length / 2;

        System.arraycopy(parent1.bits, 0, crossovered.bits, 0, pivot);

        if (parent2.bits.length - pivot >= 0)
            System.arraycopy(parent2.bits, pivot, crossovered.bits, pivot, parent2.bits.length - pivot);

        return crossovered;
    }

    public static void mutate(DNA dna) {
        for (int i = 0; i < dna.bits.length; i++) {
            if (rd.nextDouble() <= mutation) {
                dna.bits[i] = dna.bits[i] == 0 ? (byte) 1 : 0;
            }
        }
    }

    public static DNA pickParent(DNA[] oldPopulation, int fitnessesSum) {
        if (fitnessesSum <= 0) {
            return oldPopulation[0];
        }

        int randomIndex = rd.nextInt(fitnessesSum);
        int index = 0;
        for (int i = 0; i < oldPopulation.length && randomIndex >= 0; i++) {
            randomIndex -= oldPopulation[index].fitness;
            index++;
        }

        return oldPopulation[index - 1];
    }

    public static DNA generateChild(DNA[] oldPopulation, int fitnessesSum) {
        DNA randomParent1 = pickParent(oldPopulation, fitnessesSum);
        DNA randomParent2 = pickParent(oldPopulation, fitnessesSum);

        DNA child = crossover(randomParent1, randomParent2);
        mutate(child);
        return child;
    }

    public static void randomizeBytes(DNA dna, int ons) {
        for (int i = 0; i < ons; i++) {
            int index = rd.nextInt(dna.bits.length);
            dna.bits[index] = 1;
        }
    }

    public static void fullRandomizeBytes(DNA dna) {
        for (int i = 0; i < dna.bits.length; i++) {
            int value = rd.nextInt(2);
            dna.bits[i] = (byte) value;
        }
    }

    public static DNA[] generateFirstPopulation(Item[] items, int bagSize, int populationNumber) {
        DNA[] population = new DNA[populationNumber];

        for (int i = 0; i < population.length; i++) {
            population[i] = new DNA(items.length);
//            fullRandomizeBytes(population[i]);
            randomizeBytes(population[i], String.valueOf(populationNumber).length());
//            population[i].bits[i % items.length] = 1;
            calculateFitness(items, bagSize, population[i]);
        }

        return population;
    }

    public static DNA[] generateNewPopulation(Item[] items, DNA[] oldPopulation, int bagSize) {
        DNA[] population = new DNA[oldPopulation[0].bits.length];
        int fitnessesSum = Arrays.stream(oldPopulation).map(a -> a.fitness).reduce(0, Integer::sum);

        for (int i = 0; i < population.length; i++) {
            population[i] = generateChild(oldPopulation, fitnessesSum);
            calculateFitness(items, bagSize, population[i]);
        }

        return population;
    }

    public static int[] calculateBestValue(Item[] items, int bagSize, int populationNumber) {
        maxValue = 0;
        int maxV = 0;
        mutation = mutationDivider / items.length;
        DNA[] population = generateFirstPopulation(items, bagSize, populationNumber);

//        for (int i = 1; i < generations; i++) {
//            System.out.println("Generation number:\t" + i);
//            population = generateNewPopulation(items, population, bagSize);
//        }

//        System.out.println("Max Value Genetic:\t" + maxValue);

        int generations = 1;
        int notImproving = 0;
//        while (notImproving < stopNotImprovingGenetic && generations <= maxGeneration) {
        while (generations <= maxGeneration) {
            generations++;
//            System.out.println("Generation number:\t" + generationsToDynamic);
            population = generateNewPopulation(items, population, bagSize);
            if (maxV >= maxValue) {
                notImproving ++;
            } else {
                notImproving = 0;
            }
            maxV = maxValue;
        }

        return new int[] {maxValue, generations};
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
            items[i] = new Item(rd.nextInt(bagSize) + 1, rd.nextInt(100));
//            System.out.println("Item " + i + ": weight - " + items[i].weight + ", value - " + items[i].value); // printing each array element
        }

        int populationNumber = (int) Math.pow(items.length, 2);
        int[] geneticResult = calculateBestValue(items, bagSize, populationNumber);
        int geneticMaxValue = geneticResult[0];
        int numberOfGenerations = geneticResult[1];

        System.out.println("Genetic Max Value:\t" + geneticMaxValue + " | Time (millis):\t");
        System.out.println("Number of generations:\t" + numberOfGenerations);
        System.out.println("Number of populations:\t" + populationNumber);
    }
}
