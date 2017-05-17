package player;

import board.State;
import org.junit.Test;

public class NegamaxPlayerTest {
    @Test
    public void play() throws Exception {
        State state = new State();
        Player[] players = {new NegamaxPlayer(), new RandomPlayer()};
        Player.play(state, players);
    }
}