package chess;

import java.util.ArrayList;

import static chess.ChessPiece.*;


public class Queen {
    public void queenMoves(ChessPiece piece, ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                           ChessGame.TeamColor teamColor, ChessBoard board){
        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            int i= 0;
            ChessPiece.helperUpperRightDirectionLoop( myPosition,  myPosRow,  myPosCol, goodMoves,
                    teamColor,  board,  i);//end of logic for upper right
            i=0;
            helperUpperLeftDirectionLoop(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
            //endof logic for upper left
            i=0;
            helperBottomLeft(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
            //end of logic for bottom left
            i=0;
            helperBottomRight(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);

            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            i= 0;
            helperUpDirectionLoop(myPosition,myPosRow, myPosCol,
                    goodMoves, teamColor,board);  //up direction
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
