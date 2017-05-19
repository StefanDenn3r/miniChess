package player;

import board.Move;
import board.State;

abstract class Player {

    abstract Move move(State state) throws InterruptedException;

    static void play(State state, Player[] players) throws InterruptedException {
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
