package lk.ijse.dep.service;

public class AiPlayer extends Player{

    int col=0;
    public AiPlayer(Board board) {
        super(board);
    }

    @Override
    public void movePiece(int col) {

        col=predictColumn();                             // find the best colum to move

        board.updateMove(col,Piece.GREEN);

        board.getBoardUI().update(col,false);

        if (board.findWinner().getCol1()!=-1){
            board.getBoardUI().notifyWinner(board.findWinner());
        } else if (board.existLegalMoves()) {
            return;
        }else {
            board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
        }
    }
    public int miniMax(int depth,boolean maximizePlayer){
        Piece winningPiece = board.findWinner().getWinningPiece();     //check is there a winner in the start of the method to return the statical values
        if (winningPiece==Piece.GREEN){               // if Ai is win return 1
            return 1;
        } else if (winningPiece==Piece.BLUE) {
            return -1;                                // if human is win return -1
        }else {                                         // if there is no winner then check the depth and availability of legal move
            if (!board.existLegalMoves() || depth ==2){       //  if no available moves and depth is reached  return 0
                return 0;
            }
        }

        if (!maximizePlayer){                             // worked when maximizing false
            for (int i = 0; i < Board.NUM_OF_COL; i++) {
                if (board.isLegalMoves(i)){                 // check for find there is a legal move in the colum
                    int row = board.findNextAvailableSpot(i);   // find the available spot in the colum
                    board.updateMove(i,Piece.BLUE);             // make a move for human player in that colum
                    int heuristicVal = miniMax(depth + 1, true);    // after that move find the minimax value for that colum by caling again minimax method
                    board.updateMove(i,row,Piece.EMPTY);           // then balance the board by removing human player's move
                    if (heuristicVal==-1) return heuristicVal;
                }
            }
        }else {                                             // worked when maximizing true
            for (int i = 0; i < Board.NUM_OF_COL; i++) {
                if (board.isLegalMoves(i)){                    // check for find there is a legal move in the colum
                    int row = board.findNextAvailableSpot(i);     // find the available spot in the colum
                    board.updateMove(i,Piece.GREEN);             // make a move for ai player in that colum
                    int heuristicVal = miniMax(depth + 1, false); // after that move find the minimax value for that colum by caling again minimax method
                    board.updateMove(i,row,Piece.EMPTY);          // then balance the board by removing ai player's move
                    if (heuristicVal==1) return heuristicVal;
                }
            }
        }
        return 0;
    }

    private int predictColumn(){                                        // method to find best colum to move
        boolean isUserWinning=false;
        int tiedColumn=0;

        for (int i = 0; i < Board.NUM_OF_COL; i++) {                   // to calculate minimax values for all colums

            if (board.isLegalMoves(i)){                                // check is there a legal move in each colum. if there is a legal move then find the minimax for the relevant colum
                int row =board.findNextAvailableSpot(i);               // find the available spot in the colum
                board.updateMove(i,Piece.GREEN);                       // make a move for ai player in that colum
                int heuristicVal =miniMax(0,false);  // after that move find the minimax value for that colum
                board.updateMove(i,row,Piece.EMPTY);                   // then balance the board by removing ai player's move
                if (heuristicVal==1){                  // if heuristic value =1,it is the best move hence return colum number to move
                    return i;
                } else if (heuristicVal==-1) {  // if heuristicVAl =-1 means human user is win. hence set isHumanWining variable to true
                    isUserWinning=true;

                }else {
                    tiedColumn=i;     //if heuristicVAl =0, that means game is tied for that move, hence put colum number in tiedColumn variable
                }

            }

        }
        if (isUserWinning && board.isLegalMoves(tiedColumn)){        // this if worked when there is no colum minimax value=1. then try to not to lose the game. hence return tied colum number to move
            return tiedColumn;
        }else {                             // if there are no minimax value -1 there. here find a random colum (column have a legal move)to move
            int col=0;
            do {
                col=(int)(Math.random()*6);
            }while (!board.isLegalMoves(col));
            return col;
        }
    }
}