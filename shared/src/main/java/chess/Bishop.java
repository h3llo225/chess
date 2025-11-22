package chess;

import java.util.ArrayList;

import static chess.ChessPiece.*;


public class Bishop {
    public void bishopMoves(ChessPiece piece, ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                            ChessGame.TeamColor teamColor, ChessBoard board){
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {

            //this logic here grabs the upper right
            int i= 0;
            helperUpperRightDirectionLoop( myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
            helperUpperLeftDirectionLoop(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
            helperBottomLeft(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            helperBottomRight(myPosition, myPosRow, myPosCol,goodMoves,
                    teamColor, board, i);
//end of logic for bottom right*/

        }

    }
}
