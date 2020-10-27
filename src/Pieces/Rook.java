package Pieces;

import java.util.List;
import Board.Board;

public class Rook extends Piece {
    static String NAME = "Rook";
    private static final String WHITE_SYMBOL = "♖";
    private static final String BLACK_SYMBOL = "♜";
    static int[][] INITIALS = {{0,0},{0,7}};

    public Rook(boolean white) {
        super(white, NAME, INITIALS, WHITE_SYMBOL, BLACK_SYMBOL);
    }

    public boolean legalMove(int[] end, Board board) {
        if (super.checkLinear(end)){
            if (board.obstructionPresent(super.getLinearObstructions(end))) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public List<int[]> getObstructions(int[] end, Board board) {
        return super.getLinearObstructions(end);
    }
}
