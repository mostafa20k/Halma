package com.company;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private Board board;
    private byte playerTurn;
    private final byte maxDepth;
    public Agent(Board board, byte maxDepth){
        this.board = board;
        this.maxDepth = maxDepth;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, (byte) (0),Integer.MIN_VALUE,Integer.MAX_VALUE);
        this.playerTurn = playerTurn;
        return temp.move;
    }

    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {
        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);
        if (depth==maxDepth){
            return new Pair(null,evaluate(currentBoard,currentColor));
        }
        // check depth here
        depth++;
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        Move best = null;
        int bestValue = Integer.MIN_VALUE;
        for (int i = 0; i < possibleMoves.size(); i++) {
            Tile[][]temp = board.doMove(possibleMoves.get(i),currentBoard);
            Pair temp_move = min(temp,(byte)1, depth,alpha,beta);
            if (temp_move.value > bestValue) {
                bestValue = temp_move.value;
                best = possibleMoves.get(i);
            }
            if(bestValue>beta) return new Pair(best, bestValue);
            if(alpha<bestValue) alpha=bestValue;
        }
        return new Pair(best, bestValue);
    }

    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {
        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);
        if (depth==maxDepth){
            return new Pair(null,evaluate(currentBoard,currentColor));
        }
        depth++;
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        Move best = null;
        int bestValue = Integer.MAX_VALUE;
        for (int i = 0; i < possibleMoves.size(); i++) {
            Tile[][]temp = board.doMove(possibleMoves.get(i),currentBoard);
            Pair temp_move = max(temp,(byte)2, depth,alpha,beta);
            if (temp_move.value < bestValue) {
                bestValue = temp_move.value;
                best = possibleMoves.get(i);
            }
            if(bestValue<alpha) return new Pair(best, bestValue);
            if(beta>bestValue) beta=bestValue;
        }
        return new Pair(best, bestValue);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
        int score = 0;

        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {

                    score += (7 - i);
                    score += (7 - j);
                    if (currentBoard[i][j].zone == 3-playerTurn){
                        score+=4;
                    }
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {

                    score -= i;
                    score -= j;
                }
                if(i!=currentBoard.length-1&&j!= currentBoard.length-1&& i!=0 && j!=0){
                    if (currentBoard[i+1][j].color==playerTurn){
                        score++;
                    }
                    if (currentBoard[i][j+1].color==playerTurn){
                        score++;
                    }
                    if (currentBoard[i+1][j+1].color==playerTurn){
                        score++;
                    }
                    if (currentBoard[i-1][j].color==playerTurn){
                        score++;
                    }
                    if (currentBoard[i][j-1].color==playerTurn){
                        score++;
                    }
                    if (currentBoard[i-1][j-1].color==playerTurn){
                        score++;
                    }
                }
            }
        }
        return score;
    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }


    public boolean checkTerminal(Tile[][] currentTiles) {

        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}