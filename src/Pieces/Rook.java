package Pieces;

import java.util.List;
import Board.Board;

public class Rook extends Piece {
    static String NAME = "Rook";
    static int[][] INITIALS = {{0,0},{0,7}};

    public Rook(boolean white) {
        super(white, NAME, INITIALS);
    }

    public boolean legalMove(int[] start, int[] end, Board board) {
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

    @Override
    public String showSelf() {
        if (WHITE) {
            return "RW";
        } else {
            return "RB";
        }
    }
}
