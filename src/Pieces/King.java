package Pieces;

import java.util.ArrayList;
import java.util.List;
import Board.Board;

public class King extends Piece {
    private static final String NAME = "King";
    private static final String WHITE_SYMBOL = "♔";
    private static final String BLACK_SYMBOL = "♚";
    private static final int[][] INITIALS = {{0,3}};

    public King(boolean white) {
        super(white, NAME, INITIALS, WHITE_SYMBOL, BLACK_SYMBOL);
    }

    public List<int[]> possMoves(Board board) {
        List<int[]> allPossMoves = new ArrayList<int[]>();

        for (int i = -1; i <= 1; i++) {
            if ( board.onBoard(currentPosition[0] + i) ) {
                for (int j = -1; j <= 1; j++) {
                    if ( board.onBoard(currentPosition[1] + j) ) {
                        Piece selectedSquare = board.board[currentPosition[0] + i][currentPosition[1] + j];
                        if ( selectedSquare == this ||
                             selectedSquare == null ||
                             selectedSquare.WHITE != WHITE) {
                                allPossMoves.add(new int[]{ currentPosition[0] + i, currentPosition[1] + j});
                        }
                    }
                }
            }
        }
        return allPossMoves;
    }

    @Override
    public boolean legalMove(int[] end, Board board){
        int absdiffx = Math.abs(end[1] - this.currentPosition[1]);
        int absdiffy = Math.abs(end[0] - this.currentPosition[0]);

        if ( absdiffx == 1 && absdiffy == 0 || 
             absdiffx == 0 && absdiffy == 1 || 
             absdiffx == 1 && absdiffy == 1    ) {
            return true;
        }
        return false;
    }

    @Override
    public String showSelf() {
        if (WHITE) {
            return "KW";
        } else {
            return "KB";
        }
    }
}
