package chess;

import java.util.ArrayList;

import static chess.ChessPiece.findGoodMove;

import static chess.ChessPiece.helperUpDirectionLoop;

public class Rook {
    public void rookMoves(ChessPiece piece, ChessPosition myPosition, ArrayList<ChessMove> goodMoves,
                          ChessGame.TeamColor teamColor, ChessBoard board){
        int myPosCol;
        int myPosRow;
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            int i= 0;
            helperUpDirectionLoop(myPosition,myPosRow, myPosCol,
                    goodMoves, teamColor,board);
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();

            i= 0;
            for (int j = 0;j < (myPosition.getColumn()-1); j++) {
                myPosCol -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//down direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();


            for (i= 0; i < (8-myPosition.getRow()); i++) {
                myPosRow += 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//right direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (i = 0;i < (myPosition.getRow()-1); i++) {
                myPosRow -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//left direction

        }
    }
}
