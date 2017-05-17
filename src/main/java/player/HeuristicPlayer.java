package player;

import board.Move;
import board.State;

public class HeuristicPlayer extends Player {

    @Override
    public Move move(State state) {
        final Move move = state.calculateBestMove();
        return state.move(move);
    }
}
