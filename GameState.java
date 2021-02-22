//kc18182 - 1803189

package eight_puzzle;

import java.util.ArrayList;
import java.util.HashMap;

/*
      Instances of the class GameState represent states that can arise in the sliding block puzzle.
      The char array board represents the board configuration; that is, the location of each of the
      six tiles. Black tiles are represented by the char 'B', white tiles by the char 'W' and the
      empty location by the space character.
      The int spacePos holds the position of the empty location. (Although this is redundant it saves a
      lot of computation as it is frequently necessary to refer to this location).
      INITIAL_BOARD and GOAL_BOARD are constant arrays holding the initial and goal board configurations.
 */
//GameState creates the game
public class GameState {
    final int[] board;
    private int spacePos;
    //Initial board state
    static final int[] INITIAL_STATE = { 8, 7, 6,
                                         5, 4, 3,
                                         2, 1, 0};
    //Goal board state
    static final int[] GOAL_STATE = { 1, 2, 3,
                                      4, 5, 6,
                                      7, 8, 0};
    //length of the board (9)
    private static final int length = INITIAL_STATE.length;

    /*
        GameState is a constructor that takes a char array holding a board configuration as argument.
     */
    GameState(int[] board) {
        this.board = board;
        for (int i = 0; i < length; i++){
            if (board[i] == 0) {
                this.spacePos = i;
                break;
            }
        }
    }

    /*
        clone returns a new GameState with the same board configuration as the current GameState.
     */
    public GameState clone() {
        int[] clone = new int[length];
        System.arraycopy(this.board, 0, clone, 0, length);
        return new GameState(clone);
    }

    /*
        toString returns the board configuration of the current GameState as a printable string.
    */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            s.append(this.board[i - 1]);
            if (i % 3 == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    /*
        isGoal returns true if and only if the board configuration of the current GameState is the goal
        configuration.
    */
    public boolean isGoal() {
        for (int i = 0; i < length; i++) {
            if (this.board[i] != GOAL_STATE[i]) {
                return false;
            }
        }
        return true;
    }

    /*
         sameBoard returns true if and only if the GameState supplied as argument has the same board
         configuration as the current GameState.
     */
    public boolean sameBoard(GameState gs) {
        for (int i = 0; i < length; i++) {
            if (this.board[i] != gs.board[i]) {
                return false;
            }
        }
        return true;
    }

    /*
        possibleMoves returns a list of all GameStates that can be reached in a single move from the current GameState.
     */
    public ArrayList<GameState> possibleMoves(){
        //Hashmap to store the move and how it should move.
        HashMap<String, Integer> legalMoves = new HashMap<>();
        //ArrayList of GameStates to keep possibleMoves.
        ArrayList<GameState> moves = new ArrayList<>();
        //put each move in the HashMap.
        legalMoves.put("UP", this.spacePos - 3);
        legalMoves.put("DOWN", this.spacePos + 3);
        legalMoves.put("LEFT", this.spacePos - 1);
        legalMoves.put("RIGHT", this.spacePos + 1);
        for (String moveWord : legalMoves.keySet()) {
            int moveIndex = legalMoves.get(moveWord);
            if (isLegalMove(moveWord, moveIndex)) {
                GameState cloneBoard = this.clone();
                cloneBoard.board[this.spacePos] = this.board[moveIndex];
                cloneBoard.board[moveIndex] = 0;
                cloneBoard.spacePos = moveIndex;
                moves.add(cloneBoard);
            }
        }
        return moves;
    }

    //method for checking if that move is legal.
    private boolean isLegalMove(String key, int move) {
        //Checks if move is between 0 and 8, if moved left is it valid and if moved right is it valid.
        return move >= 0 && move <= 8 && (!key.equals("LEFT") || (move != 2 && move != 5)) && ((!key.equals("RIGHT") || (move != 3 && move != 6)));
    }
}

