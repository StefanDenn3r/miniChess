package player;

import board.Move;
import board.State;

public abstract class Player {

    abstract Move move(State state);
}
