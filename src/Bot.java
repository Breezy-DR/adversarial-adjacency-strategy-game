import javafx.scene.control.Button;
public abstract class Bot {
    public abstract int[] move(Button[][] tiles, int roundLeft, String player);
    
    public Button[][] copyBoardGame(Button[][] board) {
        Button[][] tiles2 = new Button[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                tiles2[x][y] = new Button();
                tiles2[x][y].setText(board[x][y].getText());
            }
        }
        return tiles2;
    }
}
