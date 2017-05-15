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
        state.move(new Move(new Square(4, 5), new Square(1, 2)));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Move is invalid");
        state.move(new Move(new Square(4, 5), new Square(1, 2)));
    }

    @Test
    public void testMove() throws Exception {
        State state = new State();
        state.move("e6-a2");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Move is invalid");
        state.move("e6-a2");
    }

    @Test
    public void testMoveKing() throws Exception {
        State state = new State();
        Assert.assertEquals(state.generateMoveList(0, 0).isEmpty(), true);

    }
}