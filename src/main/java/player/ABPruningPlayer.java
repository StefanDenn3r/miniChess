package player;

import board.Move;
import board.State;


public class ABPruningPlayer extends Player {

    public Move move(State state) {
        Move move = state.calculateBestWithABPruning();
        return state.move(move);
    }
}
