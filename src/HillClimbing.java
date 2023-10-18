import javafx.scene.control.Button;

public class HillClimbing extends Bot {
    // function for calculating user's score if player insert value at (x, y)
    public int objective_function(Button[][] tiles, int x, int y, String player) {
        int playerScore = 1;
        int EnemyScore = 0;
        // traverse all tiles
        if (!tiles[x][y].getText().isEmpty()) {
            return -64;
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    String tile = tiles[i][j].getText();
                    String enemy;
    
                    if (player.equals("X")) {
                        enemy = "O";
                    } else {
                        enemy = "X";
                    }
    
                    if ((i == x-1 && j == y) || (i == x+1 && j == y) || (i == x && j == y-1) || (i == x && j == y+1)) {
                        // check the adjacent
                        if (tile.equals(player)) {
                            playerScore++;
                        } else if (tile.equals(enemy)) {
                            // this means the adjacent is the enemy's tile, so naturally that tile became player's
                            playerScore++;
                        }
                    } else {
                        if (tile.equals(player)) {
                            playerScore++;
                        } else if (tile.equals(enemy)) {
                            EnemyScore++;
                        }
                    }
                }
            }
            return playerScore - EnemyScore;
        }
    }

    @Override
    public int[] move(Button[][] tiles, int roundLeft, String player) {
        int maxValue = -64; // assume this because the value cant get any lower than this
        int[] bestPosition = {-1, -1}; // assume this because this is invalid

        // this loop returns the first maximum value found
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int objectiveValue = objective_function(tiles, i, j, player);

                if (maxValue < objectiveValue) {
                    // check first if the coordinates are valid or not
                    if (tiles[i][j].getText().isEmpty()) {
                        maxValue = objectiveValue;
                        bestPosition[0] = i;
                        bestPosition[1] = j;
                    }
                }

                // System.out.print(objectiveValue);
                // System.out.print(" ");
            }
            // System.out.println("");
        }
        // System.out.println("-----------------------------------------------------------");
        // System.out.print(bestPosition[0]);
        // System.out.print(", ");
        // System.out.println(bestPosition[1]);
        
        // returns the coordinates for the next action
        return new int[] {bestPosition[0], bestPosition[1]};
    }
}
