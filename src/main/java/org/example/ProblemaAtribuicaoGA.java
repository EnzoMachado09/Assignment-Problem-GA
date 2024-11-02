package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProblemaAtribuicaoGA {
    private static final int TAMANHO_POPULACAO = 100;
    private static final int MAX_GERACOES = 1000; // Aumentado para mais iterações
    private static final double TAXA_MUTACAO = 0.1;
    private static final double TAXA_CROSSOVER = 0.8;

    private int[][] matrizCusto;
    private int numTarefas;

    public ProblemaAtribuicaoGA(int[][] matrizCusto) {
        this.matrizCusto = matrizCusto;
        this.numTarefas = matrizCusto.length;
    }

    public int[] resolver() {
        List<int[]> populacao = inicializarPopulacao();
        int[] melhorSolucao = populacao.get(0);
        int melhorFitness = calcularFitness(melhorSolucao);

        System.out.println("Iniciando a busca pela melhor solução...");

        for (int geracao = 0; geracao < MAX_GERACOES; geracao++) {
            System.out.println("Geração " + geracao + " - Melhor Fitness Atual: " + melhorFitness);

            populacao = evoluir(populacao);

            int[] atualMelhor = obterMelhorSolucao(populacao);
            int atualMelhorFitness = calcularFitness(atualMelhor);
            if (atualMelhorFitness < melhorFitness) {
                melhorSolucao = atualMelhor;
                melhorFitness = atualMelhorFitness;
                System.out.println("Nova melhor solução encontrada com fitness " + melhorFitness);
            }
        }

        System.out.println("Solução final após " + MAX_GERACOES + " gerações: ");
        System.out.println("Atribuição de tarefas: " + java.util.Arrays.toString(melhorSolucao));
        System.out.println("Fitness da melhor solução: " + melhorFitness);
        return melhorSolucao;
    }

    private List<int[]> inicializarPopulacao() {
        System.out.println("Inicializando população...");
        List<int[]> populacao = new ArrayList<>();
        for (int i = 0; i < TAMANHO_POPULACAO; i++) {
            int[] solucao = gerarSolucaoAleatoria();
            populacao.add(solucao);
        }
        return populacao;
    }

    private int[] gerarSolucaoAleatoria() {
        int[] solucao = new int[numTarefas];
        List<Integer> tarefas = new ArrayList<>();
        for (int i = 0; i < numTarefas; i++) tarefas.add(i);
        Collections.shuffle(tarefas);
        for (int i = 0; i < numTarefas; i++) solucao[i] = tarefas.get(i);
        return solucao;
    }

    private List<int[]> evoluir(List<int[]> populacao) {
        List<int[]> novaPopulacao = new ArrayList<>();

        for (int i = 0; i < TAMANHO_POPULACAO; i++) {
            int[] pai1 = selecionar(populacao);
            int[] pai2 = selecionar(populacao);
            int[] descendente;

            if (Math.random() < TAXA_CROSSOVER) {
                descendente = crossover(pai1, pai2);
            } else {
                descendente = pai1.clone();
            }

            if (Math.random() < TAXA_MUTACAO) {
                mutar(descendente);
                System.out.println("Mutação aplicada no descendente: " + java.util.Arrays.toString(descendente));
            }

            novaPopulacao.add(descendente);
        }

        return novaPopulacao;
    }

    private int[] selecionar(List<int[]> populacao) {
        return populacao.get(new Random().nextInt(populacao.size()));
    }

    private int[] crossover(int[] pai1, int[] pai2) {
        int[] descendente = new int[numTarefas];
        System.arraycopy(pai1, 0, descendente, 0, numTarefas / 2);
        System.arraycopy(pai2, numTarefas / 2, descendente, numTarefas / 2, numTarefas - numTarefas / 2);
        System.out.println("Crossover entre " + java.util.Arrays.toString(pai1) + " e " + java.util.Arrays.toString(pai2) +
                " resultou em " + java.util.Arrays.toString(descendente));
        return descendente;
    }

    private void mutar(int[] solucao) {
        int idx1 = new Random().nextInt(numTarefas);
        int idx2 = new Random().nextInt(numTarefas);
        int temp = solucao[idx1];
        solucao[idx1] = solucao[idx2];
        solucao[idx2] = temp;
    }

    private int calcularFitness(int[] solucao) {
        int fitness = 0;
        for (int i = 0; i < numTarefas; i++) {
            fitness += matrizCusto[i][solucao[i]];
        }
        return fitness;
    }

    private int[] obterMelhorSolucao(List<int[]> populacao) {
        int[] melhor = populacao.get(0);
        for (int[] solucao : populacao) {
            if (calcularFitness(solucao) < calcularFitness(melhor)) {
                melhor = solucao;
            }
        }
        return melhor;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escolha a instância do problema para resolver:");
        System.out.println("1 - Instância pequena (N=500)");
        System.out.println("2 - Instância média (N=2500)");
        System.out.println("3 - Instância grande (N=10000)");
        System.out.print("Sua escolha: ");
        int escolha = scanner.nextInt();

        ProblemaAtribuicaoGA ga = null;

        Random rand = new Random();
        switch (escolha) {
            case 1:
                int[][] matrizCustoPequena = new int[500][500];
                for (int i = 0; i < 500; i++) {
                    for (int j = 0; j < 500; j++) {
                        matrizCustoPequena[i][j] = rand.nextInt(100) + 1;
                    }
                }
                ga = new ProblemaAtribuicaoGA(matrizCustoPequena);
                System.out.println("Resultado para N=500:");
                break;
            case 2:
                int[][] matrizCustoMedia = new int[2500][2500];
                for (int i = 0; i < 2500; i++) {
                    for (int j = 0; j < 2500; j++) {
                        matrizCustoMedia[i][j] = rand.nextInt(100) + 1;
                    }
                }
                ga = new ProblemaAtribuicaoGA(matrizCustoMedia);
                System.out.println("Resultado para N=2500:");
                break;
            case 3:
                int[][] matrizCustoGrande = new int[10000][10000];
                for (int i = 0; i < 10000; i++) {
                    for (int j = 0; j < 10000; j++) {
                        matrizCustoGrande[i][j] = rand.nextInt(100) + 1;
                    }
                }
                ga = new ProblemaAtribuicaoGA(matrizCustoGrande);
                System.out.println("Resultado para N=10000:");
                break;
            default:
                System.out.println("Escolha inválida.");
                return;
        }

        if (ga != null) {
            ga.resolver();
        }

        scanner.close();
    }
}
