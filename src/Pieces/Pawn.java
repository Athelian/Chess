package Pieces;

import Board.Board;

public class Pawn extends Piece {
    private static final String NAME = "Pawn";
    private static final String WHITE_SYMBOL = "♙";
    private static final String BLACK_SYMBOL = "♟";
    private static final int[][] INITIALS = {{1,0}, {1,1},
                                             {1,2}, {1,3},
                                             {1,4}, {1,5},
                                             {1,6}, {1,7}};

    public Pawn(boolean white) {
        super(white, NAME, INITIALS, WHITE_SYMBOL, BLACK_SYMBOL);
    }

    @Override
    public boolean legalMove(int[] end, Board board) {
        int diffx = end[1] - this.currentPosition[1];
        int diffy = end[0] - this.currentPosition[0];
        Piece selectedSquare = board.board[end[0]][end[1]];
        if ( (Math.abs(diffx) == 1 && diffy == 1 && selectedSquare != null && selectedSquare.WHITE != WHITE) ||
             (diffx == 0 && diffy == 1 && selectedSquare == null)) {
            return true;
        }
        return false;
    }

    @Override
    public String showSelf() {
        if (WHITE) {
            return "PW";
        } else {
            return "PB";
        }
    }
}
