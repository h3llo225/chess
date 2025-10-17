package chess;

import java.util.ArrayList;

import static chess.ChessPiece.findGoodMove;


import static chess.ChessPiece.pawnPromotionFindPiece;



public class Pawn {
    public void pawnMoves(ChessPiece piece, ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                          ChessGame.TeamColor teamColor, ChessBoard board){
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(new ChessPosition(myPosition.getRow(),
                myPosition.getColumn())).getTeamColor() == ChessGame.TeamColor.BLACK) {
            myPosCol = myPosition.getColumn();
            myPosRow = (myPosition.getRow()-1);
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                if (myPosition.getRow()==7 && board.getPiece(new ChessPosition((myPosRow-1), myPosCol)) == null
                        && board.getPiece(new ChessPosition((myPosRow), myPosCol))==null)
                    findGoodMove(board, myPosRow-1, myPosCol, goodMoves, teamColor, myPosition);
                //endof logic for initial move going 2 spaces.
                if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) == null) {
                    helpPawnMoveBlack(myPosRow, myPosCol, myPosition, board,  goodMoves, teamColor);

                }





                if (myPosCol != 1 &&  board.getPiece(new ChessPosition(myPosRow, (myPosCol-1))) != null){
                    if (board.getPiece(new ChessPosition(myPosRow,myPosCol-1)).getTeamColor() == teamColor) {
                        return;
                    }

                    helpPawnMoveBlack(myPosRow, myPosCol-1, myPosition, board,  goodMoves, teamColor);


                }//end of take left piece logic

                if (myPosCol != 8 && board.getPiece(new ChessPosition(myPosRow, (myPosCol+1))) != null){
                    if (board.getPiece(new ChessPosition(myPosRow,(myPosCol+1))).getTeamColor() == teamColor) {
                        return;
                    }
                    helpPawnMoveBlack(myPosRow, myPosCol+1, myPosition, board,  goodMoves, teamColor);
                }

            }

        }


        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(new ChessPosition(
                myPosition.getRow(),myPosition.getColumn())).getTeamColor() == ChessGame.TeamColor.WHITE) {
            myPosCol = myPosition.getColumn();
            myPosRow = (myPosition.getRow()+1);
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                if (myPosition.getRow()==2 && board.getPiece(new ChessPosition((myPosRow+1), myPosCol)) == null
                        && board.getPiece(new ChessPosition((myPosRow), myPosCol)) == null){
                    findGoodMove(board, myPosRow+1, myPosCol, goodMoves, teamColor, myPosition);
                }
                //endof logic for initial move going 2 spaces.
                if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) == null) {
                    helpPawnMove(myPosRow, myPosCol, myPosition, board,  goodMoves, teamColor);
                }

                if (myPosCol != 1 && board.getPiece(new ChessPosition(myPosRow, (myPosCol-1))) != null){
                    if (board.getPiece(new ChessPosition(myPosRow,myPosCol-1)).getTeamColor() != teamColor) {
                        helpPawnMove(myPosRow, myPosCol-1, myPosition, board,  goodMoves, teamColor);
                    }
                }//end of take left piece logic

                if (myPosCol != 8 && board.getPiece(new ChessPosition(myPosRow, (myPosCol+1))) != null){
                    if (board.getPiece(new ChessPosition(myPosRow,(myPosCol+1))).getTeamColor() != teamColor) {
                        helpPawnMove(myPosRow, myPosCol+1, myPosition, board,  goodMoves, teamColor);
                    }
                }

            }

        }
    }

    public void helpPawnMove(int myPosRow, int myPosCol, ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> goodMoves, ChessGame.TeamColor teamColor){
        if (myPosRow == 8) {
            pawnPromotionFindPiece(board, myPosRow, myPosCol, goodMoves, myPosition);
        }
        else {
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
        }
    }

    public void helpPawnMoveBlack(int myPosRow, int myPosCol, ChessPosition myPosition, ChessBoard board, ArrayList<ChessMove> goodMoves, ChessGame.TeamColor teamColor){
        if (myPosRow == 1) {
            pawnPromotionFindPiece(board, myPosRow, myPosCol, goodMoves, myPosition);
        }
        else {
            findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
        }
    }
}
