package chess;

import java.util.ArrayList;

import static chess.ChessPiece.*;

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


            helperDownLoop(myPosition,myPosRow, myPosCol,
                    goodMoves, teamColor,board);
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
