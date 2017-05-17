package player;

import board.Move;
import board.State;

public abstract class Player {

    abstract Move move(State state);

    public static void play(State state, Player[] players) {
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
