package Pieces;

import Board.Board;

public class Knight extends Piece {
    static final String NAME = "Knight";
    static final int[][] INITIALS = {{0,1}, {0,6}};
    private static final String WHITE_SYMBOL = "♘";
    private static final String BLACK_SYMBOL = "♞";

    public Knight(boolean white) {
        super(white, NAME, INITIALS, WHITE_SYMBOL, BLACK_SYMBOL);
    }
    
    @Override
    public boolean legalMove(int[] end, Board board) {
        int absdiffx = Math.abs(end[1] - this.currentPosition[1]);
        int absdiffy = Math.abs(end[0] - this.currentPosition[0]);
        if ( absdiffx == 1 && absdiffy == 2 || absdiffx == 2 && absdiffy == 1) {
            return true;
        }
        return false;
    }

    @Override
    public String showSelf() {
        if (WHITE) {
            return "NW";
        } else {
            return "NB";
        }
    }
}
