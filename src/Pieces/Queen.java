package Pieces;

import java.util.List;
import Board.Board;

public class Queen extends Piece {
    static final String NAME = "Queen";
    private static final String WHITE_SYMBOL = "♕";
    private static final String BLACK_SYMBOL = "♛";
    static final int[][] INITIALS = {{0,4}};

    public Queen(boolean white) {
        super(white, NAME, INITIALS, WHITE_SYMBOL, BLACK_SYMBOL);
    }

    @Override
    public boolean legalMove(int[] end, Board board) {
        if ( super.checkDiagonal(end)   &&   !board.obstructionPresent(super.getDiagonalObstructions(end, board)) ||
             super.checkLinear(end)     &&   !board.obstructionPresent(super.getLinearObstructions(end))) {
                return true;
        }
        return false;
    }

    @Override
    public List<int[]> getObstructions(int[] end, Board board) {
        if (super.checkDiagonal(end)) {
            return super.getDiagonalObstructions(end, board);
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
