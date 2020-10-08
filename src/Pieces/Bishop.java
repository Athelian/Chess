package Pieces;

import java.util.List;
import Board.Board;

public class Bishop extends Piece {
    private static final String NAME = "Bishop";
    private static final int[][] INITIALS =  {{0,2}, {0,5}};

    public Bishop(boolean white) {
        super(white, NAME, INITIALS);
    }

    @Override
    public boolean legalMove(int[] end, Board board) {
        if (super.checkDiagonal(end)){
            if (!board.obstructionPresent(super.getDiagonalObstructions(end))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<int[]> getObstructions(int[] end, Board board) {
        return super.getDiagonalObstructions(end);
    }

    @Override
    public String showSelf() {
        if (WHITE) {
            return "BW";
        } else {
            return "BB";
        }
    }
}
