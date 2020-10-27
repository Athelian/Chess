package Board;

import java.util.ArrayList;
import java.util.List;
import Pieces.*;
import Player.Player;

public class Board {
    public Piece[][] board;
    private final Player PLAYER_W;
    private final Player PLAYER_B;
    public Player currentPlayer = PLAYER_W = new Player(true);
    private Player enemyPlayer = PLAYER_B = new Player(false);
    private ArrayList<Piece> deadPieces = new ArrayList<Piece>();

    public Board() {
        this.board = new Piece[8][8];
        fillBoard(PLAYER_W);
        mirror("diagonal");
        fillBoard(PLAYER_B);
        mirror("diagonal");
    }

    public void switchTurns() {
        Player finishingPlayer = currentPlayer;
        currentPlayer = enemyPlayer;
        enemyPlayer = finishingPlayer;
    }

    private void fillBoard(Player player) {
        for (Piece piece : player.playerPieces) {
            for (int[] position : piece.INITIALS) {
                if (board[position[0]][position[1]] == null) {
                    board[position[0]][position[1]] = piece;
                    piece.currentPosition = position;
                    break;
                }
            }
        }
    }

    public void mirror(String direction) {
        for (int i = 0; i < board.length - 0.5 * board.length; i++) {
            if (direction.equals("horizontal")) {
                Piece[] holder = board[i];
                board[i] = board[board.length - 1 - i];
                board[board.length - 1 - i] = holder;
            }
            if (direction.equals("diagonal")) {
                for (int j = 0; j < board[0].length; j++) {
                    Piece holder = board[i][j];
                    board[i][j] = board[board.length - 1 - i][board[0].length - 1 - j];
                    board[board.length - 1 - i][board[0].length - 1 - j] = holder;
                }
            }
        }
    }

    public boolean onBoard(int coord) {
        if (coord < 0 || coord > 7) {
            return false;
        }
        return true;
    }

    public void movePiece(int[] start, int[] end, boolean realMove) {
        Piece playerPiece = board[start[0]][start[1]];
        Piece enemyPiece = null;

        if (board[end[0]][end[1]] != null) {
            enemyPiece = board[end[0]][end[1]];
            enemyPlayer.playerPieces.remove(board[end[0]][end[1]]);
            if (realMove) {
                // System.out.println("Your " + playerPiece.NAME + " destroyed the enemy's " +
                //                     enemyPiece.NAME);
                deadPieces.add(enemyPiece);
            }
        }
        playerPiece.currentPosition = end;
        board[end[0]][end[1]] = board[start[0]][start[1]];
        board[start[0]][start[1]] = null;
    }

    public void reverseMove(int[] start, int[] end, Piece originalPiece, Piece finalSquare) {
        originalPiece.currentPosition = start;
        board[start[0]][start[1]] = originalPiece;
        board[end[0]][end[1]] = finalSquare;
        if (finalSquare != null) {
            enemyPlayer.playerPieces.add(finalSquare);
        }
    }
    
    public boolean obstructionPresent(List<int[]> intermediateSquareCoordinates) {
        for (int[] coordinates : intermediateSquareCoordinates) {
            if (board[coordinates[0]][coordinates[1]] != null) {
                return true;
            }
        }
        return false;
    }

    public void turnEnd() {
        if (currentPlayer.WHITE) {
            switchTurns();
            mirror("diagonal");

        } else {             
            switchTurns();
            mirror("diagonal");
        }
    }

    public boolean nowChecked(boolean warningsOn) {
        Piece playerKing = currentPlayer.playerPieces.get(0);
        boolean checked = false;
        mirror("diagonal");
        playerKing.mirrorCurrentPosition(false);
        for (Piece enemyPiece : enemyPlayer.playerPieces) {
            if (enemyPiece.legalMove(playerKing.currentPosition, this)) {
                checked = true;
                // if (warningsOn) {
                //     System.out.println("The enemy's " + enemyPiece.NAME + " can attack your king if you move here!!");
                // }
                break;
            }
        }
        playerKing.mirrorCurrentPosition(false);
        mirror("diagonal");

        return checked;
    }

    public boolean willBeChecked(int[] start, int[] end, boolean warningsOn) {
        boolean checked = false;
        Piece startPiece = board[start[0]][start[1]];
        Piece selectedSquare = board[end[0]][end[1]];

        movePiece(start, end, false);
        if (nowChecked(warningsOn)) {
            checked = true;
        }
        reverseMove(start, end, startPiece, selectedSquare);
        return checked;
    }

    public void checkCheckMated() {
        King playerKing = (King) currentPlayer.playerPieces.get(0); //Manual downcast
        List<int[]> kingPossMoves = playerKing.possMoves(this);
        Piece checkingPiece = null;

        if (!nowChecked(false)) {
            return;
        }

        for (int[] move : kingPossMoves) {
            if (!willBeChecked(playerKing.currentPosition, move, false)) {
                return;
            }
        }

        mirror("diagonal");
        playerKing.mirrorCurrentPosition(false);
        for (Piece enemyPiece : enemyPlayer.playerPieces) {
            if (enemyPiece.legalMove(playerKing.currentPosition, this)) {
                checkingPiece = enemyPiece;
                break;
            }
        }
        playerKing.mirrorCurrentPosition(false);
        mirror("diagonal");

        checkingPiece.mirrorCurrentPosition(false);
        for (Piece playerPiece : currentPlayer.playerPieces) {
            if (playerPiece.legalMove(checkingPiece.currentPosition, this) && 
                                      playerPiece.NAME != "King"    ) {
                checkingPiece.mirrorCurrentPosition(false);
                return;
            }
        }

        if (checkingPiece.NAME == "Knight" || checkingPiece.NAME == "Pawn" ) {
            gameOver(checkingPiece);
        }

        List<int[]> blockSquares = checkingPiece.getObstructions(playerKing.currentPosition, this);

        if (blockSquares.size() == 0) {
            gameOver(checkingPiece);
        }

        for (int[] square : blockSquares) {
            for (Piece playerPiece : currentPlayer.playerPieces) {
                if (playerPiece.legalMove(square, this) && playerPiece.NAME != "King") {
                    checkingPiece.mirrorCurrentPosition(false);
                    return;
                }
            }
        }
        gameOver(checkingPiece);
    }

    private void gameOver(Piece checkingPiece) {
        // System.out.println( "Player " + currentPlayer.PLAYER_NAME + " loses!!\n" +
        //                     "The winning piece is: " + checkingPiece + "!");
        System.exit(0);
    }
}
