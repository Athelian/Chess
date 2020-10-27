import Board.Board;
import GUI.GUI;

public class App {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        GUI gui = new GUI(board);
    }
}
