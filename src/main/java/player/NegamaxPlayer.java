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
        Move move = null;
        try {
            if (depth >= 0)
                move = state.calculateBest(depth);
            else
                move = state.iterativeDeepening();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return state.move(move);
    }
}
