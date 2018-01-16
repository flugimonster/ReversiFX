/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author flug
 */
public class GameLogic {

    private final Board board;
    private boolean onePlayerOutOfTurns;
    private boolean bothPlayersOutOfTurns;
    private Player currentPlayer;
    private Player nextPlayer;

    public GameLogic(Board board, Player firstPlayer, Player secondPlayer) {
        this.board = board;
        this.onePlayerOutOfTurns = false;
        this.bothPlayersOutOfTurns = false;
        this.currentPlayer = firstPlayer;
        this.nextPlayer = secondPlayer;
    }

    public boolean playOneTurn(Element move) {
        int row = move.getX();
        int col = move.getY();
        List<Element> validMoves = getValidMoves(currentPlayer, board);
        if (!validMoves.isEmpty()) {
            if (checkIfContains(row, col, validMoves)) {
                // we have a destination,
                // now find all possible origins and conquer the
                // elements on the way
                boolean firstReverse = true;
                // going over all directions
                for (int directionX = -1; directionX < 2; directionX++) {
                    for (int directionY = -1; directionY < 2; directionY++) {
                        // skip [0,0]
                        if (!(directionX == 0 && directionY == 0)) {
                            conquerInDirection(row + directionX, col + directionY,
                                    directionX, directionY, currentPlayer, firstReverse);
                        }
                    }
                }
                // conquer the chosen element
                board.setElement(row, col, currentPlayer.getToken());

                if (getValidMoves(nextPlayer, board).isEmpty()) {
                    onePlayerOutOfTurns = true;
                    if (getValidMoves(currentPlayer, board).isEmpty()) {
                        bothPlayersOutOfTurns = true;
                    }
                    refreshPlayersPieces();
                    return true;
                }
                refreshPlayersPieces();
                swapPlayers();
                return true;
            }
        } else {
            if (onePlayerOutOfTurns) {
                bothPlayersOutOfTurns = true;
                refreshPlayersPieces();
                swapPlayers();
                return true;
            } else {
                onePlayerOutOfTurns = true;
                refreshPlayersPieces();
                swapPlayers();
                return true;
            }
        }
        refreshPlayersPieces();
        return false;
    }

    public boolean gameOver() {
        return this.bothPlayersOutOfTurns;
    }

    public String getCurrentPlayer() {
        return currentPlayer.getName();
    }

    public String declareWinner() {
        currentPlayer.refreshNumberOfPieces(board);
        nextPlayer.refreshNumberOfPieces(board);
        if (currentPlayer.getNumberOfPieces() > nextPlayer.getNumberOfPieces()) {
            if (nextPlayer.getNumberOfPieces() == 0) {
                return currentPlayer.getName() + " wins! FLAWLESS VICTORY";
            }
            return currentPlayer.getName() + " wins!";
        } else if (nextPlayer.getNumberOfPieces()
                > currentPlayer.getNumberOfPieces()) {
            if (currentPlayer.getNumberOfPieces() == 0) {
                return nextPlayer.getName() + " wins! FLAWLESS VICTORY";
            }
            return nextPlayer.getName() + " wins!";
        } else {
            return "it's a tie!";
        }
    }

    public boolean checkIfContains(int row, int col, List<Element> validMoves) {
        // go over all possible moves and check if the player entered a valid
        // destination
        for (int i = 0; i < validMoves.size(); i++) {
            if (row == validMoves.get(i).getX()
                    && col == validMoves.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    public List<Element> getValidMoves(Player p, Board board) {
        List<Element> validMoves = new ArrayList<>();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getElement(row, col) == ' ') {
                    if (isValidMove(row, col, p)) {
                        Element validMove = new Element(row, col);
                        validMoves.add(validMove);
                    }
                }
            }
        }
        return validMoves;
    }

    public boolean conquerInDirection(int x, int y, int directionX,
            int directionY, Player p, boolean firstRun) {
        if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize()
                || board.getElement(x, y) == ' ') {
            // out of bounds or ' ', return false
            return false;
        } else if (board.getElement(x, y) == p.getToken()) {
            // player's token, return true
            return true;
        } else {
            // enemey token - keep going, reversing as you go
            firstRun = false;
            if (conquerInDirection(x + directionX, y + directionY, directionX,
                    directionY, p, firstRun)) {
                board.setElement(x, y, p.getToken());
                return true;
            }
            return false;
        }
    }

    public boolean isValidMove(int x, int y, Player p) {
        boolean firstRun = true;
        // check every direction
        for (int directionX = -1; directionX < 2; directionX++) {
            for (int directionY = -1; directionY < 2; directionY++) {
                if (!(directionX == 0 && directionY == 0)) {
                    if (checkDirection(x + directionX, y + directionY,
                            directionX, directionY, p, firstRun)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkDirection(int x, int y, int directionX, int directionY,
            Player p, boolean firstRun) {
        if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize()
                || board.getElement(x, y) == ' ') {
            // either out of bounds or ' ', return false
            return false;
        } else if (board.getElement(x, y) == p.getToken()) {
            // player token, if first time return false if not return true
            return !firstRun;
        } else {
            // found enemy token, keep going
            firstRun = false;
            return checkDirection(x + directionX, y + directionY, directionX,
                    directionY, p, firstRun);
        }
    }

    public void swapPlayers() {
        Player temp;
        temp = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = temp;
    }

    public void refreshPlayersPieces() {
        currentPlayer.refreshNumberOfPieces(board);
        nextPlayer.refreshNumberOfPieces(board);
    }
}
