package player;

import board.Move;
import board.State;

public class NegamaxPlayer extends Player {

    @Override
    public Move move(State state) {
        final Move move = state.calculateBest(10);
        return state.move(move);
    }
}
