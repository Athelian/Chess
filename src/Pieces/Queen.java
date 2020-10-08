package Pieces;

import java.util.List;
import Board.Board;

public class Queen extends Piece {
    static final String NAME = "Queen";
    static final int[][] INITIALS = {{5,4}};

    public Queen(boolean white) {
        super(white, NAME, INITIALS);
    }

    @Override
    public boolean legalMove(int[] end, Board board) {
        if ( super.checkDiagonal(end)   &&   !board.obstructionPresent(super.getDiagonalObstructions(end)) ||
             super.checkLinear(end)     &&   !board.obstructionPresent(super.getLinearObstructions(end))) {
                return true;
        }
        return false;
    }

    @Override
    public List<int[]> getObstructions(int[] end, Board board) {
        if (super.checkDiagonal(end)) {
            return super.getDiagonalObstructions(end);
        }
        return super.getLinearObstructions(end);
    }
    
    @Override
    public String showSelf() {
        if (WHITE) {
            return "QW";
        } else {
            return "QB";
        }
    }
}
