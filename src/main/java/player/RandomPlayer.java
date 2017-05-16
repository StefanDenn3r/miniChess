package player;

import board.Color;
import board.Move;
import board.State;

import java.util.List;

/**
 * Created by std on 16.05.2017.
 */
public class RandomPlayer extends Player {

    @Override
    public Move move(State state) {
        final List<Move> moves = state.generateMoveList();
        if (moves.isEmpty()) {
            state.finishedGame();
            System.out.println(State.getSideOnMove() + " is unable to move.");
            return null;
        }
        return state.move(moves.get((int) ((moves.size() - 1) * Math.random())));
    }
}
