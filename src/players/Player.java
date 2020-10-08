package players;

import java.util.ArrayList;
import java.util.List;

import Pieces.*;

public class Player {
    public final boolean WHITE;
    public final String PLAYER_NAME;
    public List<Piece> playerPieces = new ArrayList<>();

    public Player(boolean white) {
        this.WHITE = white;
        playerPieces.add(new King(white));
        playerPieces.add(new Queen(white));
        playerPieces.add(new Rook(white));
        playerPieces.add(new Rook(white));
        playerPieces.add(new Knight(white));
        playerPieces.add(new Knight(white));
        playerPieces.add(new Bishop(white));
        playerPieces.add(new Bishop(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        playerPieces.add(new Pawn(white));
        if (white) {
            PLAYER_NAME = "Player white";
        } else {
            PLAYER_NAME = "Player black";
        }
    }
}