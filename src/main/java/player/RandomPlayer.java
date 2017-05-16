package player;

import board.Color;
import board.Move;
import board.State;

import java.util.List;

/**
 * Created by std on 16.05.2017.
 */
public class RandomPlayer extends Player {

    public RandomPlayer(Color color) {
        super(color);
    }

    @Override
    public void move(State state) {
        final List<Move> moves = state.generateMoveList();
        state.move(moves.get((int) ((moves.size() - 1) * Math.random())));
    }
}
