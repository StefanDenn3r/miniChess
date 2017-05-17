package player;

import board.Move;
import board.State;

public class NegamaxPlayer extends Player {

    int depth;

    public NegamaxPlayer(int depth) {
        super();
        this.depth = depth;
    }

    public Move move(State state) {
        final Move move = state.calculateBest(depth);
        return state.move(move);
    }
}
