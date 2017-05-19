package player;

import board.Move;
import board.State;

public class NegamaxPlayer extends Player {

    private int depth;

    public NegamaxPlayer(int depth) {
        super();
        this.depth = depth;
    }

    public Move move(State state) {
        Move move = state.calculateBestWithNegamax(depth);
        return state.move(move);
    }
}
