/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

/**
 *
 * @author flug
 */
public class Board {

    private final int size;
    private final char board[][];

    public Board(int boardSize) {
        this.size = boardSize;
        this.board = new char[size][size];
        resetBoard();
        setBoard();
    }

    public char[][] getBoard() {
        return this.board;
    }

    public final void resetBoard() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void setElement(int x, int y, char token) {
        this.board[x][y] = token;
    }

    public char getElement(int x, int y) {
        return this.board[x][y];
    }

    public final void setBoard() {
        setElement(size / 2 - 1, size / 2 - 1, 'X');
        setElement(size / 2, size / 2, 'X');
        setElement(size / 2 - 1, size / 2, 'O');
        setElement(size / 2, size / 2 - 1, 'O');
    }

    public int getSize() {
        return this.size;
    }
}
