import Board.Board;
import Pieces.Piece;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        String startInput;
        String endInput;
        int[] startCoords = new int[2];
        int[] endCoords = new int[2];
        Scanner sc = new Scanner(System.in);
        Board board = new Board();

        board.showBoard();

        PieceSelection:
        while (true) {
            board.checkCheckMated();
            if (board.currentPlayer.WHITE) {
                System.out.println("It is white's turn!");
            } else {
                System.out.println("It is black's turn!");
            }

            if (board.nowChecked(false)) {
                System.out.println("You are checked!\n"
                                    + "note: You must make a move this turn\n"
                                    + "      You may change your mind after selecting a piece (type 'try again' after selection)");
            }
            
            System.out.println("Please enter piece to move," + 
                                    " type 'skip' to skip, or type 'exit' at any time to exit the game:");
            startInput = sc.nextLine();
            checkExit(startInput, sc);

            if (startInput.equals("skip")) {
                // if (!nowChecked(board, false)) {
                //     System.out.println("You are skipping your turn!");
                //     turnEnd(board);
                // } else {
                //     System.out.println("You cannot skip while you are checked!\nPlease make a move");
                // }
                System.out.println("You are skipping your turn!");
                board.turnEnd();
                continue;
            } 

            if (!validInput(startInput)) {
                System.out.println("This is not a valid input!\nPlease try again");
                continue;
            }

            startCoords = inputToCoords(startInput, board.currentPlayer.WHITE);

            if (!board.onBoard(startCoords[0]) || !board.onBoard(startCoords[1])) {
                System.out.println("You must select a square on the board!\nPlease try again");
                continue;
            }

            Piece selectedPiece = board.board[startCoords[0]][startCoords[1]];

            if (selectedPiece == null) {
                System.out.println("There is no piece here!\nPlease try again");
                continue;
            }

            if (selectedPiece.WHITE != board.currentPlayer.WHITE) {
                System.out.println("This is not your piece!\nPlease try again");
                continue;
            }

            System.out.println("You have selected " + selectedPiece.NAME + " at " +
                                startInput.charAt(0) + startInput.charAt(1) + "!");

            while (true) {
                System.out.println( "Enter location to move or type 'skip' to skip your turn" + 
                                    " (you must move this piece or skip your turn)");
                endInput = sc.nextLine();
                checkExit(endInput, sc);

                if (endInput.equals("skip")) {
                    if (!board.nowChecked(false)) {
                        System.out.println("You are skipping your turn!");
                        board.turnEnd();
                        continue PieceSelection;
                    } else {
                        System.out.println("You cannot skip while you are checked, please make a valid selection!");
                        continue;
                    }
                }

                if (endInput.equals("try again") && board.nowChecked(false)) {
                    continue PieceSelection;
                }

                if (!validInput(endInput)) {
                    System.out.println("This is not a valid input!\nPlease try again");
                    continue;
                }

                endCoords = inputToCoords(endInput, board.currentPlayer.WHITE);

                if (!board.onBoard(endCoords[0]) || !board.onBoard(endCoords[1])) {
                    System.out.println("You must select a square on the board!\nPlease try again");
                    continue;
                }

                if (!selectedPiece.legalMove(endCoords, board)) {
                    System.out.println("You cannot move this piece like that!\nPlease try again");
                    continue;
                }

                Piece selectedSquare = board.board[endCoords[0]][endCoords[1]];

                if (selectedSquare != null && selectedSquare.WHITE == board.currentPlayer.WHITE) {
                    System.out.println("You cannot move onto one of your own pieces!\nPlease try again");
                    continue;
                }

                if (board.willBeChecked(startCoords, endCoords, true)) {
                    System.out.println("You cannot check yourself!\nPlease make an appropriate move");
                }
                board.movePiece(startCoords, endCoords, true);
                board.turnEnd();
                break;
            }
        }
    }

    static void checkExit(String playerInput, Scanner sc) {
        if (playerInput.equals("exit")) {
            System.out.println("Program Terminated");
            sc.close();
            System.exit(0);
        }
    }

    static boolean validInput(String playerInput) {
        if ( playerInput.length() != 2 || 
             !Character.isDigit(playerInput.charAt(1)) || 
             !Character.isLetter(playerInput.charAt(0)) ) {
            return false;
        }
        return true;
    }

    static int[] inputToCoords (String input, boolean whiteTurn) {
        int[] coords = new int[2];
        if (whiteTurn) {
            coords[0] = (input.charAt(1) - '0' - 1);
            coords[1] = (input.charAt(0) - 'a');
        } else {
            coords[0] = (7 - (input.charAt(1) - '0' - 1));
            coords[1] = (7 - (input.charAt(0) - 'a'));
        }
        return coords;
    }
}
