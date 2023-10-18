import javafx.scene.control.Button;

import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class Genetic extends Bot {
    private final int maxIteration = 100;
    private final int populationCount = 100;
    private ArrayList<Integer> pool = new ArrayList<>();

    public Genetic(){
        for (int i = 0; i < 78; i++) {
            if((i > 7 && i < 10)){
                continue;
            }
            else if ((i > 17 && i < 20)) {
                continue;
            }
            else if ((i > 27 && i < 30)) {
                continue;
            }
            else if ((i > 37 && i < 40)) {
                continue;
            }
            else if ((i > 47 && i < 50)) {
                continue;
            }
            else if ((i > 57 && i < 60)) {
                continue;
            }
            else if ((i > 67 && i < 70)) {
                continue;
            }
            pool.add(i);
        }
        System.out.println(pool);
    }
    @Override
    public int[] move(Button[][] tiles, int roundLeft, String player) {
        ArrayList<Integer> bannedCell = banCell(tiles);
//        System.out.println("genetic start");
        ArrayList<Integer> chromosome = genetic(roundLeft,bannedCell,maxIteration,tiles);
        return generateMove(chromosome);
    }

    public ArrayList<Integer> copyList(ArrayList<Integer> originalList){
        ArrayList<Integer> copyList = new ArrayList<>();

        for (Integer element : originalList) {
            // Create new objects for the copy
            Integer copyElement = element.intValue();
            copyList.add(copyElement);
        }

        return copyList;
    }

    public ArrayList<ArrayList<Integer>> copyMultiList(ArrayList<ArrayList<Integer>> originalList){
        ArrayList<ArrayList<Integer>> copiedList = new ArrayList<>();

        for(ArrayList<Integer> element : originalList){
            ArrayList<Integer> copyElement = copyList(element);
            copiedList.add(copyElement);
        }

        return copiedList;
    }

    public ArrayList<Integer> banCell(Button[][] tiles){
        ArrayList<Integer> bannedCell = new ArrayList<>();

        for (int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(Objects.equals(tiles[x][y].getText(), "X") || Objects.equals(tiles[x][y].getText(), "O")){
                    String cellStr = Integer.toString(x) + Integer.toString(y);
                    bannedCell.add(Integer.valueOf(cellStr));
                }
            }
        }

        return copyList(bannedCell);
    }

    public int[] coordinateFromCell(int cell){
        int[] coordinate = new int[2];

        if (cell < 8){
            coordinate[0] = 0;
            coordinate[1] = cell;
        }
        else{
            String cellStr = Integer.toString(cell);

            coordinate[0] = Integer.valueOf(cellStr.substring(0,1));
            coordinate[1] = Integer.valueOf(cellStr.substring(1));
        }
        return coordinate;
    }

    public int generateCell(ArrayList<Integer> cellBanned){
        ArrayList<Integer> numberPool = copyList(pool);
        Random random = new Random();

        for(Integer element : cellBanned){
            numberPool.remove(Integer.valueOf(element));
        }

        int cellIndex = random.nextInt(numberPool.size());
        return numberPool.get(cellIndex);

    }

    public ArrayList<ArrayList<Integer>> generatePopulation(int populationCount, int genesCount, ArrayList<Integer> bannedCellDef){
        ArrayList<ArrayList<Integer>>  population = new ArrayList<>();
        ArrayList<Integer> bannedCell = copyList(bannedCellDef);


        for(int i = 0; i < populationCount; i++){
            ArrayList<Integer> chromosomeTemp = new ArrayList<>();
//            System.out.println("loop in");
            for(int j = 0; j < genesCount; j++){
//                System.out.println("ban " + bannedCell.size());
                int cell = generateCell(bannedCell);
                bannedCell.add(cell);
                chromosomeTemp.add(cell);
//                System.out.println("i="+ i+ "j=" + j + " " + chromosomeTemp);
            }
//            System.out.println("loop out");
            bannedCell.clear();
            bannedCell = copyList(bannedCellDef);
            population.add(copyList(chromosomeTemp));
            chromosomeTemp.clear();
        }
//        System.out.println("inside" + population);
        return population;
    }

    public char[][] updateGameBoard(Button[][] tiles){
        char[][] gameBoard = new char[8][8];

        for (int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(tiles[x][y].getText() == "X"){
                    gameBoard[x][y] = 'X';
                }
                else if (tiles[x][y].getText() == "O") {
                    gameBoard[x][y] = 'O';
                }
                else{
                    gameBoard[x][y] = ' ';
                }
            }
        }
        return gameBoard;
    }

    public int fitnessFunction(ArrayList<Integer> chromosome, char[][] gameBoard){

        //deep copy
        char[][] gameBoardTemp = new char[gameBoard.length][gameBoard[0].length];
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                gameBoardTemp[i][j] = gameBoard[i][j];
            }
        }

        //game simulation
        for(int i = 0; i < chromosome.size(); i++) {
            int[] coordinate = coordinateFromCell(chromosome.get(i));
            int x = coordinate[0];
            int y = coordinate[1];
            char player = 'O';

            if(i % 2 == 0){
                player = 'X';
            }

            gameBoardTemp[x][y] = player;

            // convert cells
            if(y-1 >= 0 && gameBoardTemp[x][y-1] != ' '){ gameBoardTemp[x][y-1] = player; }
            if(y+1 <= 7 && gameBoardTemp[x][y+1] != ' '){ gameBoardTemp[x][y+1] = player; }
            if(x-1 >= 0 && gameBoardTemp[x-1][y] != ' '){ gameBoardTemp[x-1][y] = player; }
            if(x+1 <= 7 && gameBoardTemp[x+1][y] != ' '){ gameBoardTemp[x+1][y] = player; }
        }

        // count score
        int xCount = 0;
        int oCount = 0;

        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(gameBoardTemp[i][j] == 'X'){ xCount++; }
                else if(gameBoardTemp[i][j] == 'O'){ oCount++; }
            }
        }
        return oCount-xCount;
    }

    public ArrayList<Integer> selection(ArrayList<ArrayList<Integer>> population, Button[][] tiles){
        ArrayList<Integer> chosen = new ArrayList<>();
        char[][] gameBoard = updateGameBoard(tiles);
        int maxScore = -999;
        int chosenIndex = 0;

        for(int i = 0; i < population.size();i++) {
            int score = fitnessFunction(population.get(i), gameBoard);
            if (maxScore < score) {
                chosen = population.get(i);
                maxScore = score;
                chosenIndex = i;
            }
        }
//        System.out.println(maxScore);
//        System.out.println(chosen);
        population.remove(chosen);
        return chosen;
    }

    public ArrayList<Integer>[] crossOver(int crossPoint, ArrayList<Integer> chosen_1, ArrayList<Integer> chosen_2){
        ArrayList<Integer>[] childs = new ArrayList[2];

        ArrayList<Integer> partition_1 = copyList(chosen_1);
        ArrayList<Integer> partition_2 = copyList(chosen_2);
        int temp1 = 0;
        int temp2 = 0;

        for(int i = crossPoint; i < chosen_2.size();i++){
            // swap
            temp1 = partition_1.get(i);
            temp2 = partition_2.get(i);

            partition_1.remove(i);
            partition_2.remove(i);

            partition_1.add(i, temp2);
            partition_2.add(i, temp1);
        }

        childs[0] = partition_1;
        childs[1] = partition_2;

        return childs;
    }

    public void mutation(ArrayList<Integer>[] childs, ArrayList<Integer> bannedCellDef){
        int foundIndex = -1;
        boolean found = false;
        // ganti gene yang sama dengan sebelumnya

        for (int childIndex=0 ; childIndex < 2 ; childIndex++){
            ArrayList<Integer> bannedCell = copyList(bannedCellDef);
            bannedCell.addAll(childs[childIndex]);

            for (int i = 0; i < childs[childIndex].size(); i++) {
                int currentElement = childs[childIndex].get(i);

                for (int j = i + 1; j < childs[childIndex].size(); j++) {
                    if (currentElement == childs[childIndex].get(j)) {
                        found = true;
                        foundIndex = j;
                        break;
                    }

                }
                if(found){
                    childs[childIndex].remove(foundIndex);
//                    System.out.println("in found " + bannedCell);
                    childs[childIndex].add(foundIndex, generateCell(bannedCell));
                    found = false;
                }
            }
            bannedCell = bannedCellDef;
        }
    }


    public ArrayList<Integer> genetic(int roundLeft, ArrayList<Integer> bannedCellDef,int maxIter, Button[][] tiles){
        System.out.println("in");
        ArrayList<Integer> bestChromosome = new ArrayList<>();
        int bestScore = -999;
        ArrayList<Integer> chosen_1 = new ArrayList<>();
        ArrayList<Integer> chosen_2 = new ArrayList<>();
        ArrayList[] childs = new ArrayList[2];
        ArrayList<ArrayList<Integer>> population = generatePopulation(populationCount, roundLeft,bannedCellDef);

        System.out.println("Initialize " + roundLeft + bannedCellDef);
        System.out.println("ban count " + bannedCellDef.size());
        System.out.println(bannedCellDef);

        for(int iteration=0; iteration < maxIter; iteration++){
            ArrayList<ArrayList<Integer>> newPopulation = new ArrayList<>();
            System.out.println("Number Iteration: " + iteration );
            // find best individual
            for(int i = 0; i < population.size();i++){
                int score = fitnessFunction(population.get(i), updateGameBoard(tiles));
                if(score >= bestScore){
                    bestScore = score;
                    bestChromosome = copyList(population.get(i));
                }
            }
            System.out.println("best " + bestChromosome + bestScore);
            System.out.println(population);
            if(bestScore == 63){
                break;
            }
            int a = 0;
            while(!population.isEmpty()){
                System.out.println("Number Selection: " + a);
//                System.out.println("inside" + population);
//                System.out.println("selection in" + i);
                chosen_1 = selection(population, tiles);
                chosen_2 = selection(population, tiles);
                System.out.println("selection out" + chosen_1 + chosen_2);

                // cross over masih belum memperhitungkan langkah yg sama
//                System.out.println("cross over in" + chosen_1 + chosen_2);
                childs = crossOver(chosen_1.size()/2, chosen_1, chosen_2);
                System.out.println("cross over out" + childs[0] + childs[1]);
                mutation(childs, bannedCellDef);
                System.out.println("mutation out");

                newPopulation.add(childs[0]);
                newPopulation.add(childs[1]);
                a++;
            }
            System.out.println("Loop 2");
//            System.out.println("before"  + population);
            population = copyMultiList(newPopulation);
//            System.out.println("after"  + population);
        }
        System.out.println("return " + bestChromosome);
        return bestChromosome;
    }

    public int[] generateMove(ArrayList<Integer> chromosome){
//        System.out.println(chromosome);
        return coordinateFromCell(chromosome.get(0));
    }
}


