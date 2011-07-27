package com.minederp.kingdoms.games.tetris;

public class TetrisPiece {
	public int[][] PieceInfo;

	public TetrisPiece(int[][] pi) {
		PieceInfo = pi;
	}

	public void rotateRight() {
		int[][] tmp = new int[PieceInfo.length][PieceInfo.length];

		for (int a = 0; a < PieceInfo.length; a++) {
			for (int i = 0; i < PieceInfo.length; i++) {
				tmp[i][1 - (a - (PieceInfo.length - 2))] = PieceInfo[a][i];
			}
		}
		PieceInfo = tmp;

	}

	public void rotateLeft() {

		int[][] tmp = new int[PieceInfo.length][PieceInfo.length];

		for (int a = 0; a < PieceInfo.length; a++) {
			for (int i = 0; i < PieceInfo.length; i++) {
				tmp[1 - (i - (PieceInfo.length - 2))][a] = PieceInfo[a][i];
			}
		}
		PieceInfo = tmp;

	}

	public TetrisPiece cloneMe() {

		int[][] tmp = new int[PieceInfo.length][PieceInfo.length];

		for (int a = 0; a < PieceInfo.length; a++) {
			for (int i = 0; i < PieceInfo.length; i++) {
				tmp[a][i] = PieceInfo[a][i];
			}
		}

		return new TetrisPiece(tmp);
	}
}
