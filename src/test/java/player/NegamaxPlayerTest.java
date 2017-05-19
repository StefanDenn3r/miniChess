package player;

import board.State;
import org.junit.Test;

import static board.Color.BLACK;
import static board.Color.WHITE;

public class NegamaxPlayerTest {
    @Test
    public void play() throws Exception {
        int whiteWins = 0;
        int blackWins = 0;
        for (int i = 0; i < 1; i++) {
            State state = new State();
            Player[] players = {new NegamaxPlayer(5), new ABPruningPlayer()};
            Player.play(state, players);
            System.out.println("------------------------- winner is: " + state.winner);
            if (state.winner == WHITE) {
                whiteWins++;
            }
            if (state.winner == BLACK)
                blackWins++;
        }
        System.out.println("w: " + whiteWins + ", b: " + blackWins);
    }
}