package player;

import board.Move;
import board.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player {
    Move move(State state) {
        System.out.println("Next move: ");
        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(inputStreamReader);
        boolean legalMove = false;

        while (!legalMove) {
            try {
                Move move = state.move(br.readLine());
                legalMove = true;
                return move;
            } catch (IllegalArgumentException e) {
                System.out.println("Move was invalid. Next move: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


   /* public static void main(String[] args) {
        State state = new State();
        Player[] players = {new RandomPlayer(), new HumanPlayer()};
        while (!gameOver) {
            state.printCurrentBoard();
            players[0].move(state);
            if (!gameOver) {
                state.printCurrentBoard();
                players[1].move(state);
            }
        }
    }*/
}
