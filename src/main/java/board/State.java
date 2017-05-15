package board;

import static board.SideOnMove.BLACK;
import static board.SideOnMove.WHITE;

/**
 * Created by std on 15.05.2017.
 */
public class State {
    public static Board board;
    public static int move;
    public static SideOnMove sideOnMove;

    public State() {
        getInitialState();
    }

    private void getInitialState() {
        move = 1;
        sideOnMove = WHITE;
        board = new Board();
    }

    public void printCurrentBoard() {
        if (board == null) {
            getInitialState();
        }
        System.out.println(move + " " + sideOnMove);
        System.out.println(board.toString());
    }

    public State move(Move move) {
        return null;
    }

    private void changeSideOnMove() {
        if (sideOnMove.equals(WHITE))
            sideOnMove = BLACK;
        else sideOnMove = WHITE;
    }
}
