# Efficient OOP Chess Engine

The purpose of this app was to generate a novel chess engine with a focus on shifting away from hard-coded values, as in every piece either possesses its own logic, or inherits shared logic for determining its moveset.

### Comments on code design (a balance of efficiency and eloquence)
### Points of eloquence (reducing the length of the script)
- The internal board viewed by the engine is flipped depending on the current player's turn, while the board is shown to the users in a static orientation.

- Every piece owns coordinates denoting its current position, however, considering that the board's orientation is reversed each turn, two pieces of different colors may in fact share the same coordinates. 
For example, consider a white piece sitting at it's true starting position, A0, the corresponding black piece is sitting at H8, however both pieces have internal coordinates of A0. This is because when the board is flipped for black's turn, position H8 becomes A0 and the coordinates of black are now true.

- This remains intuitive while considering scenarios where only the player in question's pieces can be moved, as in the simple cases of "Can player white's piece move to this square during player white's turn?".
This strategy however has lead to some snippets of unintuitive code when we must consider scenarios of how the enemy can move during the current player's turn. A typical example would be "If player white moves their king to this location during player white's turn, would there be any black piece which could attack at this location? If so, ask player white to choose a different location".

- It follows that in this scenario we must temporarily move white's piece to player white's chosen location, flip the board so as to test any potential moves player black could make, and then reflip the board once again so that player white may continue with their turn.

- While this is technically sufficient to assess whether or not player black would be checking player white in this scenario, while checking player black's logic, the coordinates of player white's pieces are made incorrect once more. While we could search the entire board for the real-time location of player white's king, it is more concise to simply flip player white's king's coordinates in tandem with the board flip. This gives easy to access real-time coordinates of player white's king without having to search the entire board for it.
Note that it is not necessary to flip the coordinates of all the white pieces in this scenario (in case there were any white pieces blocking an attack path to the king), because the board maintains the real-time location of all pieces at all times, and each piece determines its attack path via a reference to the board object. When a piece is checking for obstructions along a desired attack path, it references the real-time board squares between its start location and its desired end location, returning them as an array. If the array contains no references to piece objects, there are no obstructions.

- Another caveat of this method is in writing concise code for returning obstructions in a diagonal line. When determining whether or not a move is strictly diagonal, it is a simple matter of determining whether or not the change in the absolute value of the x-position change of the piece is equal to the absolute value of the y-position change. The matter is complicated when we must determine if there are any obstructions between the start location and the end location.

- Of the four diagonal directions on a square:
  - " ++ "
  - " +- "
  - " -+ "
  - " -- "
  
- The most simple direction is '++', where we can write a single for-loop with a positive 'i++' increment. Each square between the start and end locations are simply one to the right of, and one above the previous square.

- This loop can be reused for the direct analogue of '++', the '--' direction. The order of the obstructions is in unimportant, the answer of 'is there an obstruction?' becomes 'yes' the moment a piece is found in any of the squares. Therefore, the relevant algorithms across the program find the index position with the lowest index positions first, and then use the same single positive increment for-loop to check the squares.

- This technique fails however when we must check two opposing directions at the same time, as in, when we are going positive in one direction and negative in the other. One way to fix this would be to use the 'board-flip' method for the movement logic outlined above. However, this board flip was originally intended to reorder both the rows and the columns of the array in a diagonal fashion so that the board could be viewed from either direction as if it were sitting in front of two people at a table, not a horizontal fashion where only the rows are flipped.

- This led to the original implementation of a logic-gate in the board-flip method which will flip columns in addition to rows depending on a boolean argument passed in at the time of the method-call:

# Simple case
![Picture3](https://user-images.githubusercontent.com/67857298/95444897-51166300-0999-11eb-947f-9ad8d908daf1.png)

# Consolidating a secondary axis diagonal move into a primary axis diagonal move
![Picture7](https://user-images.githubusercontent.com/67857298/95449634-03512900-09a0-11eb-9b78-6bbc635ec1ab.png)

### Efficiency (writing more code, but reducing the processing time)
- The checkmate test after checking the enemy player is simple and has numerous exit points as the board conditions get more complicated:
  - Find every possible move for the enemy King
  - Calculate if any of the king's moves would result in it coming out of check
  - If not, find the current attacking piece.
  - For every live ally of the checked King, find if it can kill the current attacking piece.
  - If not, and the checking piece is either a pawn or a knight, the game is over (these pieces do not have obstructions to worry about) (the king is unnecessary to test for)
  - If not, find the obstructing squares between the attacking piece and the king (can use obstruction methods written earlier for finding legal moves)
  - For every square in the obstructing squares, check if any any ally of the king can move into that square in the next turn.
  - If not, the game is over.
  
- Perhaps the shortest way to write a chess game is to give every single piece in the game a possible move list, whereafter we can crossreference the lists to determine if pieces have points of intersection, or perhaps if they can check the king given the king's potential moves. However this means that for each turn every single piece on the board must find all of its possible moves, which can become extremely messy when we consider that every piece must also check for obstructions.
- Therefore, in this engine there is a distinction between a 'legal move' and a 'possible move', it turns out that determining if a move is a 'legal move' requires a fraction of the processing time to generate all possible moves. Moreover, the only piece that needs to know its 'possible moves' is the king, as this is the only piece for which the engine is required to check theoretical placements (when looking for a checkmate).
