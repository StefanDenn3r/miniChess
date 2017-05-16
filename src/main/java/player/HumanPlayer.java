package player;

import board.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static board.State.gameOver;

public class HumanPlayer extends Player {
    void move(State state) {
        System.out.println("Next move: ");
        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(inputStreamReader);
        boolean legalMove = false;

        while (!legalMove) {
            try {
                state.move(br.readLine());
                legalMove = true;
            } catch (IllegalArgumentException e) {
               System.out.println("Move was invalid. Next move: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
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
    }
}
