package player;

import board.Color;
import board.State;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by std on 16.05.2017.
 */
public class RandomPlayerTest {
    @Test
    public void play() throws Exception {
        State state = new State();
        Player[] players = {new RandomPlayer(), new RandomPlayer()};
        while (!state.gameOver) {
            state.printCurrentBoard();
            players[0].move(state);
            if (!state.gameOver) {
                state.printCurrentBoard();
                players[1].move(state);
            }
        }
    }
}