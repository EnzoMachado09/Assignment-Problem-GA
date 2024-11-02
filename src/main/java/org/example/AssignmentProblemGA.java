package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AssignmentProblemGA {
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 500;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.8;

    private int[][] costMatrix;
    private int numTasks;

    public AssignmentProblemGA(int[][] costMatrix) {
        this.costMatrix = costMatrix;
        this.numTasks = costMatrix.length;
    }

    public int[] solve() {
        List<int[]> population = initializePopulation();
        int[] bestSolution = population.get(0);
        int bestFitness = calculateFitness(bestSolution);

        System.out.println("Iniciando a busca pela melhor solução...");

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            System.out.println("Geração " + generation + " - Melhor Fitness Atual: " + bestFitness);

            population = evolve(population);

            int[] currentBest = getBestSolution(population);
            int currentBestFitness = calculateFitness(currentBest);
            if (currentBestFitness < bestFitness) {
                bestSolution = currentBest;
                bestFitness = currentBestFitness;
                System.out.println("Nova melhor solução encontrada com fitness " + bestFitness);
            }
        }

        System.out.println("Solução final após " + MAX_GENERATIONS + " gerações: ");
        System.out.println("Atribuição de tarefas: " + java.util.Arrays.toString(bestSolution));
        System.out.println("Fitness da melhor solução: " + bestFitness);
        return bestSolution;
    }

    private List<int[]> initializePopulation() {
        System.out.println("Inicializando população...");
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] solution = generateRandomSolution();
            population.add(solution);
        }
        return population;
    }

    private int[] generateRandomSolution() {
        int[] solution = new int[numTasks];
        List<Integer> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) tasks.add(i);
        Collections.shuffle(tasks);
        for (int i = 0; i < numTasks; i++) solution[i] = tasks.get(i);
        return solution;
    }

    private List<int[]> evolve(List<int[]> population) {
        List<int[]> newPopulation = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[] parent1 = select(population);
            int[] parent2 = select(population);
            int[] offspring = crossover(parent1, parent2);

            if (Math.random() < MUTATION_RATE) {
                mutate(offspring);
                System.out.println("Mutação aplicada no descendente: " + java.util.Arrays.toString(offspring));
            }

            newPopulation.add(offspring);
        }

        return newPopulation;
    }

    private int[] select(List<int[]> population) {
        return population.get(new Random().nextInt(population.size()));
    }

    private int[] crossover(int[] parent1, int[] parent2) {
        int[] offspring = new int[numTasks];
        System.arraycopy(parent1, 0, offspring, 0, numTasks / 2);
        System.arraycopy(parent2, numTasks / 2, offspring, numTasks / 2, numTasks - numTasks / 2);
        System.out.println("Crossover entre " + java.util.Arrays.toString(parent1) + " e " + java.util.Arrays.toString(parent2) +
                " resultou em " + java.util.Arrays.toString(offspring));
        return offspring;
    }

    private void mutate(int[] solution) {
        int idx1 = new Random().nextInt(numTasks);
        int idx2 = new Random().nextInt(numTasks);
        int temp = solution[idx1];
        solution[idx1] = solution[idx2];
        solution[idx2] = temp;
    }

    private int calculateFitness(int[] solution) {
        int fitness = 0;
        for (int i = 0; i < numTasks; i++) {
            fitness += costMatrix[i][solution[i]];
        }
        return fitness;
    }

    private int[] getBestSolution(List<int[]> population) {
        int[] best = population.get(0);
        for (int[] solution : population) {
            if (calculateFitness(solution) < calculateFitness(best)) {
                best = solution;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha a instância do problema para resolver:");
        System.out.println("1 - Instância pequena (N=4)");
        System.out.println("2 - Instância média (N=10)");
        System.out.println("3 - Instância grande (N=50)");
        System.out.print("Sua escolha: ");
        int choice = scanner.nextInt();

        AssignmentProblemGA ga = null;

        Random rand = new Random();
        switch (choice) {
            case 1:
                int[][] costMatrixSmall = {
                        {9, 2, 7, 8},
                        {6, 4, 3, 7},
                        {5, 8, 1, 8},
                        {7, 6, 9, 4}
                };
                ga = new AssignmentProblemGA(costMatrixSmall);
                System.out.println("Resultado para N=4:");
                break;
            case 2:
                int[][] costMatrixMedium = new int[10][10];
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        costMatrixMedium[i][j] = rand.nextInt(10) + 1;
                    }
                }
                ga = new AssignmentProblemGA(costMatrixMedium);
                System.out.println("Resultado para N=10:");
                break;
            case 3:
                int[][] costMatrixLarge = new int[50][50];
                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        costMatrixLarge[i][j] = rand.nextInt(10) + 1;
                    }
                }
                ga = new AssignmentProblemGA(costMatrixLarge);
                System.out.println("Resultado para N=50:");
                break;
            default:
                System.out.println("Escolha inválida.");
                return;
        }

        if (ga != null) {
            ga.solve();
        }

        scanner.close();
    }
}
