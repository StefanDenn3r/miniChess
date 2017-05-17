package player;

import board.Move;
import board.State;

public class EvaluationPlayer extends Player {

    @Override
    public Move move(State state) {
        final Move move = state.calculateBestMove();
        return state.move(move);
    }
}
