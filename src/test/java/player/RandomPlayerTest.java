package player;

import board.Color;
import board.State;
import org.junit.Test;

import static board.Color.BLACK;
import static board.Color.WHITE;
import static board.State.gameOver;
import static org.junit.Assert.*;

/**
 * Created by std on 16.05.2017.
 */
public class RandomPlayerTest {
    @Test
    public void play() throws Exception {
        State state = new State();
        Player[] players = {new RandomPlayer(), new RandomPlayer()};
        while (!gameOver) {
            state.printCurrentBoard();
            players[0].move(state);
            if (!gameOver) {
                state.printCurrentBoard();
                players[1].move(state);
            }
        }
    }
}