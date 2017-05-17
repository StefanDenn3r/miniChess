package player;

import board.State;
import org.junit.Test;

public class HeuristicPlayerTest {
    @Test
    public void play() throws Exception {
        State state = new State();
        Player[] players = {new HeuristicPlayer(), new HeuristicPlayer()};
        Player.play(state, players);
    }
}