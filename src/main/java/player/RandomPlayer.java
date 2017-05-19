package player;

import board.Move;
import board.State;

import java.util.List;

public class RandomPlayer extends Player {

    @Override
    public Move move(State state) {
        final List<Move> moves = state.generateMoveList();
        return state.move(moves.get((int) ((moves.size() - 1) * Math.random())));
    }
}
