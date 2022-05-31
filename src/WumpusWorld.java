import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*  P = player
    # = parede
    G = gold
    W = Wumpus
    - = Cheiro ruim do Wumpus 
    A = Abismo 
    ~ = Brisa
    */

public class WumpusWorld {

    private static Map<String, Integer> findRownsAndColunsToDescoveryStartAndExitPoint(String[][] maze) {
        Map<String, Integer> result = new HashMap<>();

        int originRow = 0;
        int originColumn = 0;
        int destineRow = 0;
        int destColumn = 0;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                if (maze[i][j].equals("P")) {
                    originRow = i;
                    originColumn = j;
                }
                if (maze[i][j].equals("G")) {
                    destineRow = i;
                    destColumn = j;
                }
            }
        }

        result.put("originRow", originRow);
        result.put("originColumn", originColumn);
        result.put("destineRow", destineRow);
        result.put("destineColumn", destColumn);

        return result;
    }

    private static void findWayDFS(String[][] maze, boolean[][] possibleMoves, int originRow, int originColumn,
                                   int destineRow, int destColumn) {
        Stack<String> stack = new Stack<>();
        maze[originRow][originColumn] = "P";
        String originRowString = Integer.toString(originRow);
        String originColString = Integer.toString(originColumn);
        String originRowAndColunm = originRowString.concat("," + originColString);
        stack.push(originRowAndColunm);

        while (!stack.empty()) {
            boolean finish = walking(originRow, originColumn, possibleMoves, maze, stack, destineRow, destColumn);
            if (finish) {
                System.out.println("Saída encontrada");
                break;
            } else {
                System.out.println("Fim");
            }
        }
    }

    private static boolean walking(int currentPositionOnRow, int currentPositionOnColunm, boolean[][] possibleMoves,
                                   String[][] maze, Stack<String> stack, int destineRow, int destColumn) {

        if (thereIsWumpusNear(currentPositionOnRow, currentPositionOnColunm, possibleMoves, maze, stack, destineRow, destColumn)){
            return false;
        }                              
        thereIsAbbysNear(currentPositionOnRow, currentPositionOnColunm, possibleMoves, maze, stack, destineRow, destColumn);
        
        
        
        if (possibleMoves[currentPositionOnRow][currentPositionOnColunm] == true && maze[currentPositionOnRow][currentPositionOnColunm].equals(maze[destineRow][destColumn])) {
            return true;

        } else if (possibleMoves[currentPositionOnRow - 1][currentPositionOnColunm] == true
                && !maze[currentPositionOnRow - 1][currentPositionOnColunm].equals("P")) {

            verifyIfHasBreezeOrStench(maze, currentPositionOnRow, currentPositionOnColunm);

            maze[currentPositionOnRow - 1][currentPositionOnColunm] = "P";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow - 1);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            stack.push(currentRowAndColunm);
            printMaze(maze);
            walking(currentPositionOnRow - 1, currentPositionOnColunm, possibleMoves, maze, stack, destineRow,
                    destColumn);

        } else if (possibleMoves[currentPositionOnRow][currentPositionOnColunm - 1] == true
                && !maze[currentPositionOnRow][currentPositionOnColunm - 1].equals("P")) {

            verifyIfHasBreezeOrStench(maze, currentPositionOnRow, currentPositionOnColunm);

            maze[currentPositionOnRow][currentPositionOnColunm - 1] = "P";
            printMaze(maze);
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm - 1);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            stack.push(currentRowAndColunm);
            walking(currentPositionOnRow, currentPositionOnColunm - 1, possibleMoves, maze, stack, destineRow,
                    destColumn);

        } else if (possibleMoves[currentPositionOnRow + 1][currentPositionOnColunm] == true
                && !maze[currentPositionOnRow + 1][currentPositionOnColunm].equals("P")) {

            verifyIfHasBreezeOrStench(maze, currentPositionOnRow, currentPositionOnColunm);

            maze[currentPositionOnRow + 1][currentPositionOnColunm] = "P";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow + 1);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            stack.push(currentRowAndColunm);
            printMaze(maze);
            walking(currentPositionOnRow + 1, currentPositionOnColunm, possibleMoves, maze, stack, destineRow,
                    destColumn);

        } else if (possibleMoves[currentPositionOnRow][currentPositionOnColunm + 1] == true
                && !maze[currentPositionOnRow][currentPositionOnColunm + 1].equals("P")) {

            verifyIfHasBreezeOrStench(maze, currentPositionOnRow, currentPositionOnColunm);        

            maze[currentPositionOnRow][currentPositionOnColunm + 1] = "P";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm + 1);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            stack.push(currentRowAndColunm);
            printMaze(maze);
            walking(currentPositionOnRow, currentPositionOnColunm + 1, possibleMoves, maze, stack, destineRow,
                    destColumn);

        } else {

            stack.pop();
            String lastPositionRowAndColumnPosition = stack.lastElement();
            String[] parts = lastPositionRowAndColumnPosition.split(",");
            currentPositionOnRow = Integer.parseInt(parts[0]);
            currentPositionOnColunm = Integer.parseInt(parts[1]);
            verifyIfHasBreezeOrStench(maze, currentPositionOnRow, currentPositionOnColunm);
            printMaze(maze);
            walking(currentPositionOnRow, currentPositionOnColunm, possibleMoves, maze, stack, destineRow, destColumn);

        }

        return false;
    }

    private static void verifyIfHasBreezeOrStench(String[][] maze, int currentPositionOnRow, int currentPositionOnColunm) {

        if(maze[currentPositionOnRow - 1][currentPositionOnColunm].equals("-")){
            System.out.println("\n+------------------------------+");
            System.out.println("     Que cheiro horrível!");
            System.out.println("+------------------------------+");
        } else if (maze[currentPositionOnRow - 1][currentPositionOnColunm].equals("~")) {
            System.out.println("\n+------------------------------+");    
            System.out.println("      Sentindo uma brisa!");
            System.out.println("+------------------------------+"); 
        }

    }

    private static void thereIsAbbysNear(int i, int currentPositionOnColunm2, boolean[][] possibleMoves, String[][] maze, Stack<String> stack, int destineRow, int destineColumn) {

    }

    private static boolean thereIsWumpusNear(int currentPositionOnRow, int currentPositionOnColunm, boolean[][] possibleMoves, String[][] maze, Stack<String> stack, int destineRow, int destColumn) {
        int fightOrNot = 0;
        
        if (possibleMoves[currentPositionOnRow - 1][currentPositionOnColunm] == false
                && maze[currentPositionOnRow - 1][currentPositionOnColunm].equals("W")) {

            fightOrNot = fightOrNot(); 
            currentPositionOnRow = currentPositionOnRow - 1;

        } else if (possibleMoves[currentPositionOnRow][currentPositionOnColunm - 1] == false
                && maze[currentPositionOnRow][currentPositionOnColunm - 1].equals("W")) {

            fightOrNot = fightOrNot(); 
            currentPositionOnColunm = currentPositionOnColunm - 1;

        } else if (possibleMoves[currentPositionOnRow + 1][currentPositionOnColunm] == false
                && maze[currentPositionOnRow + 1][currentPositionOnColunm].equals("W")) {

            fightOrNot = fightOrNot(); 
            currentPositionOnRow = currentPositionOnRow + 1;

        } else if (possibleMoves[currentPositionOnRow][currentPositionOnColunm + 1] == false
                && maze[currentPositionOnRow][currentPositionOnColunm + 1].equals("W")) {

            fightOrNot = fightOrNot(); 
            currentPositionOnColunm = currentPositionOnColunm + 1;
    } 
    

    if (fightOrNot == 1) {
        maze[currentPositionOnRow][currentPositionOnColunm] = "P";
        String currentPositionOnRowString = Integer.toString(currentPositionOnRow);
        String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm);
        String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
        stack.push(currentRowAndColunm);
        printMaze(maze);
        walking(currentPositionOnRow, currentPositionOnColunm + 1, possibleMoves, maze, stack, destineRow,
                destColumn);
    } else if (fightOrNot == 2) {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                maze[i][j] = "#";
            }
        }

        for (int i = 0; i < possibleMoves.length; i++) {
            for (int j = 0; j < possibleMoves[1].length; j++) {
                possibleMoves[i][j] = false;
            }
        }

        return true;

    } 

    return false;
}

    private static int fightOrNot() {

        int min = 1;
        int max = 3;    
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;      
        System.out.println("Você está cara a cara com monstro, o que fará?"); 

            switch (randomNum) {
                case 1:
                    System.out.println("\n+-----------------------------------------------------------------------------+");
                    System.out.println("Você atirou a flecha!");
                    System.out.println("Mesmo sendo o horrível de mira, sua flecha acertou o meio da testa do Wumpus!"); 
                    System.out.println("+-----------------------------------------------------------------------------+");
                    break;
                case 2:
                    System.out.println("\n+-----------------------------------------------------------------------------+");
                    System.out.println("Seu objetivo era errar ?????");
                    System.out.println("Em um golpe só, o Wumpus te matou!");
                    System.out.println("Game Over!");
                    System.out.println("+-----------------------------------------------------------------------------+");
                    break;
                case 3:
                    System.out.println("\n+-----------------------------------------------------------------------------+");
                    System.out.println("Jogou uma pedra para distrair ??");
                    System.out.println("Talvez a melhor opção é fugir mesmo hehe");
                    System.out.println("+-----------------------------------------------------------------------------+");
                    break;    
            }
            return randomNum;  
    }

    private static void printMaze(String maze[][]) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printPossibleMoves(boolean possibleMoves[][]) {
        for (int i = 0; i < possibleMoves.length; i++) {
            for (int j = 0; j < possibleMoves[1].length; j++) {
                System.out.print(possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static String[][] createMaze(){

        int min = 6;
        int max = 10;
    
        String[][] maze = new String[(int) (Math.random()*(max-min)) + min][(int) (Math.random()*(max-min)) + min];
        populateMaze(maze);
        System.out.println("Peguei minha flecha");
        
        return maze;
    }

    private static void populateMaze(String[][] maze) {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                maze[i][0] = "#";
                maze[0][j] = "#";
                maze[maze.length - 1][j] =  "#";
                maze[i][maze[1].length - 1] = "#";
            }
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                if(maze[i][j] != "#") {
                    maze[i][j] = ".";
                }
            }
        }
        
        maze[1][1] = "P";  

        int min = 0;
        int max = maze[1].length;

        generateWumpusPosition(maze);
        generateGoldPosition(maze);
        generateAbyssPosition(maze);
        printMaze(maze);
    }

    private static void generateAbyssPosition(String[][] maze) {

        int min = 0;
        int max = maze[1].length;

        int positionOnRow = (int) (Math.random()*(max-min)) + min;
        int positionOnColunm = (int) (Math.random()*(max-min)) + min;

        if(maze[positionOnRow][positionOnColunm] == "#" || maze[positionOnRow][positionOnColunm] == "P" || maze[positionOnRow][positionOnColunm] == "W") {
            generateAbyssPosition(maze);
        } else {
            maze[positionOnRow][positionOnColunm] = "A";
            if(maze[positionOnRow + 1][positionOnColunm] != "#" && maze[positionOnRow + 1][positionOnColunm] != "P" && maze[positionOnRow + 1][positionOnColunm] != "W" &&
                maze[positionOnRow + 1][positionOnColunm] != "G" && maze[positionOnRow + 1][positionOnColunm] != "-") {
                maze[positionOnRow + 1][positionOnColunm] = "~";
            }
            if(maze[positionOnRow - 1][positionOnColunm] != "#" && maze[positionOnRow - 1][positionOnColunm] != "P" &&  maze[positionOnRow - 1][positionOnColunm] != "W" &&
                maze[positionOnRow - 1][positionOnColunm] != "G" && maze[positionOnRow - 1][positionOnColunm] != "-") {
                maze[positionOnRow - 1][positionOnColunm] = "~";
            }
            if(maze[positionOnRow][positionOnColunm + 1] != "#" && maze[positionOnRow][positionOnColunm + 1] != "P" &&  maze[positionOnRow][positionOnColunm + 1] != "W" &&
                maze[positionOnRow][positionOnColunm + 1] != "G" && maze[positionOnRow][positionOnColunm + 1] != "-") {
                maze[positionOnRow][positionOnColunm + 1] = "~";
            }
            if(maze[positionOnRow][positionOnColunm - 1] != "#" && maze[positionOnRow][positionOnColunm - 1] != "P" &&  maze[positionOnRow][positionOnColunm - 1] != "W" &&
                maze[positionOnRow][positionOnColunm - 1] != "G" && maze[positionOnRow][positionOnColunm - 1] != "-") {
                maze[positionOnRow][positionOnColunm - 1] = "~";
            }
        }

    }

    private static void generateGoldPosition(String[][] maze) {

        int min = 0;
        int max = maze[1].length;

        int positionOnRow = (int) (Math.random()*(max-min)) + min;
        int positionOnColunm = (int) (Math.random()*(max-min)) + min;

        if(maze[positionOnRow][positionOnColunm] == "#" || maze[positionOnRow][positionOnColunm] == "P" || maze[positionOnRow][positionOnColunm] == "W") {
            generateGoldPosition(maze);
        } else {
            maze[positionOnRow][positionOnColunm] = "G";
        }

    }

    private static void generateWumpusPosition(String[][] maze) {
        int min = 0;
        int max = maze[1].length;

        int positionOnRow = (int) (Math.random()*(max-min)) + min;
        int positionOnColunm = (int) (Math.random()*(max-min)) + min;

        if(maze[positionOnRow][positionOnColunm] == "#" || maze[positionOnRow][positionOnColunm] == "P") {
            generateWumpusPosition(maze);
        } else {
            maze[positionOnRow][positionOnColunm] = "W";
            if(maze[positionOnRow + 1][positionOnColunm] != "#" && maze[positionOnRow + 1][positionOnColunm] != "P") {
                maze[positionOnRow + 1][positionOnColunm] = "-";
            }
            if(maze[positionOnRow - 1][positionOnColunm] != "#" && maze[positionOnRow - 1][positionOnColunm] != "P") {
                maze[positionOnRow - 1][positionOnColunm] = "-";
            }
            if(maze[positionOnRow][positionOnColunm + 1] != "#" && maze[positionOnRow][positionOnColunm + 1] != "P") {
                maze[positionOnRow][positionOnColunm + 1] = "-";
            }
            if(maze[positionOnRow][positionOnColunm - 1] != "#" && maze[positionOnRow][positionOnColunm - 1] != "P") {
                maze[positionOnRow][positionOnColunm - 1] = "-";
            }
        }
    }

    private static void findWayBFS(String[][] maze, boolean[][] possibleMoves, int originRow, int originColumn, int destineRow, int destColumn) {
        List<String> queue = new LinkedList<>();
        String originRowString = Integer.toString(originRow);
        String originColunmString = Integer.toString(originColumn);
        String currentRowAndColunm = originRowString.concat("," + originColunmString);
        queue.add(currentRowAndColunm);
        Boolean finish = false;

        while (!queue.isEmpty() && finish == false) {
            String atualPosition = ((LinkedList<String>) queue).poll();
            String[] parts = atualPosition.split(",");
            int currentPositionOnRow = Integer.parseInt(parts[0]);
            int currentPositionOnColunm = Integer.parseInt(parts[1]);

            if (currentPositionOnRow == destineRow && currentPositionOnColunm == destColumn) {
                finish = true;
                break;
            }

            discoverNeighbors(possibleMoves, maze, queue, currentPositionOnRow, currentPositionOnColunm);
            printMaze(maze);
        }

        if (finish) {
            System.out.println("Cheguemo");
        } else {
            System.out.println("Não achei essa disgraça");
        }
    }

    private static List<String> discoverNeighbors(boolean[][] possibleMoves, String[][] maze, List<String> queue,
                                                  int currentPositionOnRow, int currentPositionOnColunm) {
        maze[currentPositionOnRow][currentPositionOnColunm] = "V";

        if (possibleMoves[currentPositionOnRow][currentPositionOnColunm + 1] == true
                && maze[currentPositionOnRow][currentPositionOnColunm + 1] != "V") {
            maze[currentPositionOnRow][currentPositionOnColunm + 1] = "V";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm + 1);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            queue.add(currentRowAndColunm);
        }

        if (possibleMoves[currentPositionOnRow][currentPositionOnColunm - 1] == true
                && maze[currentPositionOnRow][currentPositionOnColunm - 1] != "V") {
            maze[currentPositionOnRow][currentPositionOnColunm - 1] = "V";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm - 1);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            queue.add(currentRowAndColunm);
        }

        if (possibleMoves[currentPositionOnRow + 1][currentPositionOnColunm] == true
                && maze[currentPositionOnRow + 1][currentPositionOnColunm] != "V") {
            maze[currentPositionOnRow + 1][currentPositionOnColunm] = "V";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow + 1);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            queue.add(currentRowAndColunm);
        }

        if (possibleMoves[currentPositionOnRow - 1][currentPositionOnColunm] == true
                && maze[currentPositionOnRow - 1][currentPositionOnColunm] != "V") {
            maze[currentPositionOnRow - 1][currentPositionOnColunm] = "V";
            String currentPositionOnRowString = Integer.toString(currentPositionOnRow - 1);
            String currentPositionOnColunmString = Integer.toString(currentPositionOnColunm);
            String currentRowAndColunm = currentPositionOnRowString.concat("," + currentPositionOnColunmString);
            queue.add(currentRowAndColunm);
        }

        return queue;
    }

    private static boolean[][] discoveryPossibleMoves(String[][] maze) {
        boolean[][] possibleMoves = new boolean[maze.length][maze[1].length];

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[1].length; j++) {
                if (maze[i][j].equals("#") || maze[i][j].equals("W") || maze[i][j].equals("A")) {
                    possibleMoves[i][j] = false;
                } else {
                    possibleMoves[i][j] = true;
                }
            }
        }
        return possibleMoves;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner read = new Scanner(System.in);
        int option = 0;

        String[][] maze = createMaze();
        boolean[][] possibleMoves;
        possibleMoves = discoveryPossibleMoves(maze);

        Map<String, Integer> rowsAndColumns = findRownsAndColunsToDescoveryStartAndExitPoint(maze);
        int originRow = rowsAndColumns.get("originRow");
        int destineRow = rowsAndColumns.get("destineRow");
        int originColumn = rowsAndColumns.get("originColumn");
        int destColumn = rowsAndColumns.get("destineColumn");
        

        /* System.out.println("Digite a opção desejada: ");
        System.out.println("1 = DFS");
        System.out.println("2 = BFS");
        option = read.nextInt(); */
        option = 1;


        switch (option) {
            case 1:
                findWayDFS(maze, possibleMoves, originRow, originColumn, destineRow, destColumn);
                break;
            case 2:
                findWayBFS(maze, possibleMoves, originRow, originColumn, destineRow, destColumn);
                break;
        }  
    } 
}

