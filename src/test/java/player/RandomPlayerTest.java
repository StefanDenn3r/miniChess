package player;

import board.State;
import org.junit.Test;

public class RandomPlayerTest {
    @Test
    public void play() throws Exception {
        State state = new State();
        Player[] players = {new RandomPlayer(), new RandomPlayer()};
        Player.play(state, players);
    }
}