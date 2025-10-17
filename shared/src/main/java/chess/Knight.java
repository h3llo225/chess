package chess;

import java.util.ArrayList;

import static chess.ChessPiece.findGoodMove;



public class Knight {

    public void knightMoves(ChessPiece piece, ChessPosition myPosition, ArrayList<ChessMove> goodMoves,
                            ChessGame.TeamColor teamColor, ChessBoard board){
        int myPosRow;
        int myPosCol;
        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {

            myPosCol = (myPosition.getColumn() + 2);
            myPosRow = (myPosition.getRow() + 1);

            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

            /**
             logic works for upper upper right shape of stars below
             **
             *
             *                               */


            myPosCol = myPosition.getColumn()+2;
            myPosRow = myPosition.getRow()-1;


            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);




            /** upper left shape of stars
             **
             *
             *     */


            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() + 2;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);






            /**  shape of stars logic up and right 3
             ***
             *    */

            myPosCol = myPosition.getColumn()+1;
            myPosRow = myPosition.getRow()-2;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);



            /** shape of stars upper left
             ***
             *  */

            myPosCol = myPosition.getColumn() - 2;
            myPosRow = myPosition.getRow() - 1;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);



            /** shape of stars logic bottom left
             *
             *
             * *       */

            myPosCol = myPosition.getColumn() - 2;
            myPosRow = myPosition.getRow() + 1;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);



            /** shape of stars logic bottom right
             *
             *
             * *  */

            myPosCol = myPosition.getColumn()-1;
            myPosRow = myPosition.getRow()+2;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);


            /** bottom right shape stars
             *
             * * *      */
            myPosCol = myPosition.getColumn()-1;
            myPosRow = myPosition.getRow()-2;
            findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);



            /** bottom left shape of stars
             *
             ***       */

        }
    }
}
