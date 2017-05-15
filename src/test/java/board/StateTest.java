package board;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

public class StateTest {
    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void printCurrentBoard() throws Exception {
        State state = new State();
        state.printCurrentBoard();
    }

    @Test
    public void testMoveIsValid() throws Exception {
        State state = new State();
        state.move(new Move(new Square(4, 1), new Square(1, 2)));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Move is invalid");
        state.move(new Move(new Square(4, 1), new Square(1, 2)));
    }

    @Test
    public void testMove() throws Exception {
        State state = new State();
        state.move("e1-a4");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Move is invalid");
        state.move("e1-a4");
    }

    @Test
    public void testMoveKing() throws Exception {
        State state = new State();
        Assert.assertEquals(true, state.generateMoveList(4, 0).isEmpty());
    }

    @Test
    public void testMovePawn() throws Exception {
        State state = new State();
        Assert.assertEquals(1, state.generateMoveList(4, 1).size());
    }

    @Test
    public void testMoveKnight() throws Exception {
        State state = new State();
        Assert.assertEquals(2, state.generateMoveList(1, 0).size());
    }

    @Test
    public void testMoveRook() throws Exception {
        State state = new State();
        Assert.assertEquals(0, state.generateMoveList(0, 0).size());
    }

    @Test
    public void testPlayChess() throws Exception {
        State state = new State();
        state.move(state.generateMoveList(1,0).get(0));
        state.printCurrentBoard();
        state.move(state.generateMoveList(0, 4).get(0));
        state.printCurrentBoard();
        Assert.assertEquals(1, state.generateMoveList(0, 0).size());
        Assert.assertEquals(5, state.generateMoveList(2, 2).size());
        state.move(state.generateMoveList(2,2).get(1));
        state.printCurrentBoard();
    }
}