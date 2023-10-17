import javafx.scene.control.Button;

public class Minimax extends Bot{

    @Override
    public int[] move(Button[][] tiles, int roundLeft) {
        int[] move = new int[2];
        int maxScore = -9999;
        int alpha = -9999;
        int beta = 9999;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (tiles[i][j].getText().isEmpty()) {
                    Button[][] tiles2 = copyBoardGame(tiles);

                    tiles2 = updatetiles(i, j, tiles2, "O");
                    int score = minimax(tiles2, alpha, beta, false, roundLeft, 0);
                    if (score >= maxScore) {
                        move[0] = i;
                        move[1] = j;
                        maxScore = score;
                    }
                }
            }
        }
        return move;
    }

    public int minimax(Button[][] tiles, int alpha, int beta, boolean isMaximizing, int roundLeft, int depth) {
        // max depth: 5
        if (roundLeft == 0 || depth == 3) {
            // There are no rounds left
            int XScore = 0;
            int OScore = 0;
            // Count the total score
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tiles[i][j].getText().equals("O")) {
                        OScore++;
                    } else if (tiles[i][j].getText().equals("X")) {
                        XScore++;
                    }
                }
            }
            return OScore - XScore;
        }
        boolean isPruning = false;
        // Game is not over yet
        if (isMaximizing) {
            // Maximizing
            int maxScore = -9999;
            for (int i=0; i < 8; i++) {
                if (!isPruning) {
                    for (int j=0; j < 8; j++) {
                        if (tiles[i][j].getText().isEmpty()) {
                            Button[][] tiles2 = copyBoardGame(tiles);
                            tiles2 = updatetiles(i, j, tiles2, "O");
                            int score = minimax(tiles2, alpha, beta, false, roundLeft - 1, depth + 1);
                            if (score > maxScore) {
                                maxScore = score;
                            }
                            if (score > alpha) {
                                alpha = score;
                            }
                            if (alpha >= beta) {
                                isPruning = true;
                                break;
                            }
                        }
                    }
                }
                else {
                    break;
                }
            }
            return maxScore;
        } else {
            // Minimizing
            int minScore = 9999;
            for (int i=0; i < 8; i++) {
                if (!isPruning) {
                    for (int j=0; j < 8; j++) {
                        if (tiles[i][j].getText().isEmpty()) {
                            Button[][] tiles2 = copyBoardGame(tiles);
                            tiles2 = updatetiles(i, j, tiles2, "X");
                            int score = minimax(tiles2, alpha, beta, true, roundLeft - 1, depth + 1);
                            if (score < minScore) {
                                minScore = score;
                            }
                            if (score < beta) {
                                beta = score;
                            }
                            if (alpha >= beta) {
                                isPruning = true;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }

            }
            return minScore;
        }
    }

    public Button[][] updatetiles(int i, int j, Button[][] tiles, String player) {
        tiles[i][j].setText(player);
        if (i + 1 < 8 && !tiles[i + 1][j].getText().equals(player)) {
            tiles[i + 1][j].setText(player);
        }
        if (i - 1 >= 0 && !tiles[i - 1][j].getText().equals(player)) {
            tiles[i - 1][j].setText(player);
        }
        if (j + 1 < 8 && !tiles[i][j + 1].getText().equals(player)) {
            tiles[i][j + 1].setText(player);
        }
        if (j - 1 >= 0 && !tiles[i][j - 1].getText().equals(player)) {
            tiles[i][j - 1].setText(player);
        }
        return tiles;
    }
}
