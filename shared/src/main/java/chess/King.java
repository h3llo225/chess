package chess;

import java.util.ArrayList;

import static chess.ChessPiece.findGoodMove;



public class King {
    public void kingMoves(ChessPiece piece, ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                          ChessGame.TeamColor teamColor, ChessBoard board){
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow();
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move right
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow();
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move left
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow() + 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move up
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow() - 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move down
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow() - 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move bottom left
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() + 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move upper right diag
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow() + 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move left upper diag
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() - 1;
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            //move right bottom diag
        }
    }
}
