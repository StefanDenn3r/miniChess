package player;

import board.Color;
import board.State;

public abstract class Player {

    Color COLOR;

    public Player(Color color) {
        this.COLOR = color;
    }

    abstract void move(State state);
}
