/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.util.Scanner;

/**
 *
 * @author flug
 */
public class Player {

    private int numberOfPieces;
    private final char token;
    private final String color;
    private final String name;

    public Player(char token, String color, String name) {
        this.token = token;
        this.numberOfPieces = 2;
        this.color = color;
        this.name = name;
    }

    public char getToken() {
        return this.token;
    }

    public String getColor() {
        return this.color;
    }

    public int getNumberOfPieces() {
        return this.numberOfPieces;
    }

    public void refreshNumberOfPieces(Board board) {
        int counter = 0;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getElement(i, j) == this.token) {
                    counter++;
                }
            }
        }
        this.numberOfPieces = counter;
    }

    public Element chooseMove(Board board) {
        System.out.println("give me a move");
        int row, col;
        Scanner reader = new Scanner(System.in);
        row = reader.nextInt();
        col = reader.nextInt();

        Element move = new Element(--row, --col);
        return move;
    }

    public String getName() {
        return this.name;
    }
}
