package Pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Board.Board;

public abstract class Piece {
    public final String NAME;
    public final boolean WHITE;
    public final int[][] INITIALS;
    public final String SYMBOL;
    public int[] currentPosition = new int[2];

    protected Piece(boolean white, String name, int[][] initials, String white_symbol, String black_symbol) {
        this.WHITE = white;
        this.NAME = name;
        this.INITIALS = initials;
        if (white) {
            this.SYMBOL = white_symbol;
        } else {
            this.SYMBOL = black_symbol;
        }
    }

    public String showSelf() {
        return "";
    }

    public boolean legalMove(int[] end, Board board){
        return false;
    }

    public List<int[]> getObstructions(int[] end, Board board) {
        return new ArrayList<int[]>();
    }

    public void mirrorCurrentPosition(boolean yOnly) {
        if (yOnly) {
            this.currentPosition[0] = 7 - currentPosition[0];
        } else {
        this.currentPosition = new int[] { 7 - this.currentPosition[0], 7 - this.currentPosition[1] };
        }
    }

    public int[] mirrorYCoordinate(int[] end) {
        end[0] = 7 - end[0];
        return end;
    }

    protected boolean checkLinear(int[] end) {
        int diffx = end[1] - this.currentPosition[1];
        int diffy = end[0] - this.currentPosition[0];
        if (!(diffx == 0 || diffy == 0) || Arrays.equals(end, this.currentPosition)) {
            return false; 
        }
        return true;
    }

    protected List<int[]> getLinearObstructions(int[] end) {
        int diffy = end[0] - this.currentPosition[0];
        int lowestIndexOfMove;
        int highestIndexOfMove;
        List<int[]> intermediateSquareCoordinates = new ArrayList<>();

        if (diffy == 0) {
            lowestIndexOfMove = Math.min(this.currentPosition[1],end[1]);
            highestIndexOfMove = Math.max(this.currentPosition[1],end[1]);
        } else {
            lowestIndexOfMove = Math.min(this.currentPosition[0],end[0]);
            highestIndexOfMove = Math.max(this.currentPosition[0],end[0]);
        }

        for (int i = lowestIndexOfMove + 1; i < highestIndexOfMove; i++) {
            if (diffy == 0) {
                intermediateSquareCoordinates.add(new int[] {this.currentPosition[0], i});
            } else {
                intermediateSquareCoordinates.add(new int[] {i, this.currentPosition[1]});
            }
        }
        return intermediateSquareCoordinates;
    }

    protected boolean checkDiagonal(int[] end) {
        int diffx = end[1] - this.currentPosition[1];
        int diffy = end[0] - this.currentPosition[0];
        if (Math.abs(diffx) != Math.abs(diffy) || Arrays.equals(end, this.currentPosition)) {
            return false;
        }
        return true;
    }
    
    protected List<int[]> getDiagonalObstructions(int[] end, Board board) {
        int diffx = end[1] - this.currentPosition[1];
        int diffy = end[0] - this.currentPosition[0];
        int squaresToCheck = Math.abs(diffx) - 1;

        boolean secondaryAxis = false;
        List<int[]> intermediateSquareCoordinates = new ArrayList<>();

        if ( diffx < 0 && diffy > 0 || diffx > 0 && diffy < 0 ) {
            secondaryAxis = true;
            mirrorCurrentPosition(true);
            mirrorYCoordinate(end);
            board.mirror("horizontal");
            board.showBoard();
        }

        int lowestIndexOfMoveX = Math.min(this.currentPosition[1], end[1]);
        int lowestIndexOfMoveY = Math.min(this.currentPosition[0], end[0]);

        for ( int xCoordToCheck = lowestIndexOfMoveX + 1;
              xCoordToCheck <= lowestIndexOfMoveX + squaresToCheck; 
              xCoordToCheck++ ) {
                int yCoordToCheck = xCoordToCheck - lowestIndexOfMoveX + lowestIndexOfMoveY;
                intermediateSquareCoordinates.add(new int[] {yCoordToCheck, xCoordToCheck});
            }

        if ( secondaryAxis ) {
            mirrorCurrentPosition(true);
            mirrorYCoordinate(end);
            board.mirror("horizontal");
            board.showBoard();
            for (int[] square : intermediateSquareCoordinates) {
                mirrorYCoordinate(square);
            }
        }
        return intermediateSquareCoordinates;
    }
}
