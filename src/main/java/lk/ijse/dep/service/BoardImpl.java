package lk.ijse.dep.service;

public class BoardImpl implements Board {
    private final Piece[][] pieces;

    private final BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
        pieces = new Piece[NUM_OF_COL][NUM_OF_ROWS];
        for (int i = 0; i < NUM_OF_COL; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                pieces[i][j] = Piece.EMPTY;
            }

        }
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;

    }

    @Override
    public BoardUI boardUI() {
        return null;
    }


    @Override
    public int findNextAvailableSpot(int col) {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            if (pieces[col][i] == Piece.EMPTY) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isLegalMoves(int col) {
//        if (findNextAvailableSpot(col) != -1) {
//            return true;
//        }
//        return false;
        for (int row = 0; row < NUM_OF_ROWS; row++) {
            if (pieces[col][row] == Piece.EMPTY){
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateMove(int col, int row, Piece move) {
        pieces[col][row]=move;

    }

    @Override
    public boolean existLegalMoves() {
        for (int i = 0; i < NUM_OF_COL; i++) {
            if (isLegalMoves(i)) return true;
        }
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {
        pieces[col][findNextAvailableSpot(col)] = move;

    }

    @Override
    public Winner findWinner() {

        for (int i = 0; i < NUM_OF_COL - 3; i++) {
            for (int j = 0; j < NUM_OF_ROWS; j++) {
                if (pieces[i][j] == pieces[i + 1][j] && pieces[i + 1][j] == pieces[i + 2][j] && pieces[i + 2][j] == pieces[i + 3][j] &&
                        pieces[i][j] != Piece.EMPTY) {
                    return new Winner(pieces[i][j], i, j, i + 3, j);
                }
            }
        }

        for (int i = 0; i < NUM_OF_ROWS - 3; i++) {
            for (int j = 0; j < NUM_OF_COL; j++) {
                if (pieces[j][i] == pieces[j][i + 1] && pieces[j][i + 1] == pieces[j][i + 2] && pieces[j][i + 2] == pieces[j][i + 3] &&
                        pieces[j][i] != Piece.EMPTY) {
                    return new Winner(pieces[j][i], j, i, j, i + 3);
                }
            }
        }
        return new Winner(Piece.EMPTY);

    }
}