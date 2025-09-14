package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
    this.pieceColor = pieceColor;
    this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
        //throw new RuntimeException("Not implemented");
    }

    public boolean findGoodMove(ChessBoard board, int myPosRow,int myPosCol, ArrayList<ChessMove> goodMoves,
                                  ChessGame.TeamColor teamColor, ChessPosition myPosition){
        if (board.getPiece(new ChessPosition(myPosRow,myPosCol))!=null) {
            if (board.getPiece(new ChessPosition(myPosRow,myPosCol)).getTeamColor() == teamColor){
                myPosCol = myPosition.getColumn();
                myPosRow = myPosition.getRow();
                return false;
            } else if (board.getPiece(new ChessPosition(myPosRow,myPosCol)).getTeamColor() != teamColor) {
                goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                        myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                        ,null));
                return false;
                //break;
            }


        }else if (board.getPiece(new ChessPosition(myPosRow,myPosCol))==null){
            goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
           myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                        ,null));
            return true;
        }
        return false;

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        /** will want to calc new positions by adding from current.
         * ex bishop can mov current position  +1 row and + 1 col or -1 -1.
         * but not -1 + 1 etc or rook can go +1 row +2 row until out of board index
         * but no col (or vice versa) etc*/
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor teamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();


        ArrayList<ChessMove> goodMoves = new ArrayList<ChessMove>() ;
        // goodMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition( 6,5), null));
        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();

        if (piece.getPieceType() == PieceType.BISHOP) {

            //this logic here grabs the upper right
            int i= 0;
            for (int j = 0; i<(8-myPosition.getRow()) && j < (8-myPosition.getColumn()); i++, j++) {
                //int myPosRow = myPosition.getRow();
                //int myPosCol = myPosition.getColumn();
                myPosRow += 1;
                myPosCol += 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }

//
            } //end of logic for upper right
            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(myPosition.getRow()-1) && j < (8-myPosition.getColumn()); i++, j++) {
                myPosRow -= 1;
                myPosCol += 1;

                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
            //endof logic for upper left
            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(myPosition.getRow()-1) && j<(myPosition.getColumn()-1) ; i++, j++) {
                myPosRow -= 1;
                myPosCol -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
            //end of logic for bottom left


            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(8-myPosition.getRow()) && j<(myPosition.getColumn()-1) ; i++, j++) {
                myPosRow += 1;
                myPosCol -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
//end of logic for bottom right*/



            System.out.println(myPosition + "this is pos");


        }
        //start of rook!!!
        if (piece.getPieceType() == PieceType.ROOK) {
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            int i= 0;
            for (int j = 0; j < (8-myPosition.getColumn()); j++) {
                myPosCol += 1;
                //myPosRow = myPosition.getRow();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            } //up direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();

            i= 0;
            for (int j = 0;j < (myPosition.getColumn()-1); j++) {
                myPosCol -= 1;
                //myPosRow = myPosition.getRow();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//down direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();


            for (i= 0; i < (8-myPosition.getRow()); i++) {
                myPosRow += 1;
                //myPosCol = myPosition.getColumn();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//right direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();


            for (i = 0;i < (myPosition.getRow()-1); i++) {
                myPosRow -= 1;
                //myPosCol = myPosition.getColumn();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//left direction

        }

        if (piece.getPieceType() == PieceType.QUEEN) {
            int i= 0;
            for (int j = 0; i<(8-myPosition.getRow()) && j < (8-myPosition.getColumn()); i++, j++) {
                //int myPosRow = myPosition.getRow();
                //int myPosCol = myPosition.getColumn();
                myPosRow += 1;
                myPosCol += 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            } //end of logic for upper right
            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(myPosition.getRow()-1) && j < (8-myPosition.getColumn()); i++, j++) {
                myPosRow -= 1;
                myPosCol += 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
            //endof logic for upper left
            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(myPosition.getRow()-1) && j<(myPosition.getColumn()-1) ; i++, j++) {
                myPosRow -= 1;
                myPosCol -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
            //end of logic for bottom left


            i=0;
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            for (int j = 0; i<(8-myPosition.getRow()) && j<(myPosition.getColumn()-1) ; i++, j++) {
                myPosRow += 1;
                myPosCol -= 1;
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }

            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            i= 0;
            for (int j = 0; j < (8-myPosition.getColumn()); j++) {
                myPosCol += 1;
                //myPosRow = myPosition.getRow();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            } //up direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();

            i= 0;
            for (int j = 0;j < (myPosition.getColumn()-1); j++) {
                myPosCol -= 1;
                //myPosRow = myPosition.getRow();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//down direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();


            for (i= 0; i < (8-myPosition.getRow()); i++) {
                myPosRow += 1;
                //myPosCol = myPosition.getColumn();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//right direction
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();


            for (i = 0;i < (myPosition.getRow()-1); i++) {
                myPosRow -= 1;
                //myPosCol = myPosition.getColumn();
                boolean val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//left direction

        }

        if (piece.getPieceType() == PieceType.KNIGHT) {

            myPosCol = (myPosition.getColumn() + 2);
            myPosRow = (myPosition.getRow() + 1);

                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }




            /**
             logic works for upper upper right shape of stars below
             **
             *
             *                               */


            myPosCol = myPosition.getColumn()+2;
            myPosRow = myPosition.getRow()-1;

                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }


            /** upper left shape of stars
             **
              *
              *     */


            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() + 2;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }




            /**  shape of stars logic up and right 3
             ***
             *    */

            myPosCol = myPosition.getColumn()+1;
            myPosRow = myPosition.getRow()-2;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }

            /** shape of stars upper left
             ***
               *  */

            myPosCol = myPosition.getColumn() - 2;
            myPosRow = myPosition.getRow() - 1;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }

            /** shape of stars logic bottom left
               *
               *
             * *       */

            myPosCol = myPosition.getColumn() - 2;
            myPosRow = myPosition.getRow() + 1;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }


            /** shape of stars logic bottom right
              *
              *
              * *  */

            myPosCol = myPosition.getColumn()-1;
            myPosRow = myPosition.getRow()+2;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);

                }

            /** bottom right shape stars
             *
             * * *      */
            myPosCol = myPosition.getColumn()-1;
            myPosRow = myPosition.getRow()-2;
                if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                    findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);


                }

//
            /** bottom left shape of stars
               *
             ***       */

        }

        if (piece.getPieceType() == PieceType.PAWN && board.getPiece(new ChessPosition(myPosition.getRow(),myPosition.getColumn())).getTeamColor() == ChessGame.TeamColor.WHITE) {
            myPosCol = myPosition.getColumn();
            myPosRow = (myPosition.getRow()+1);
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) == null) {
                    if (myPosRow == 7){
                        goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                                , PieceType.QUEEN));
                    }
                    else if(myPosRow!=8) {
                        goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                                , null));
                    }
                    if (myPosition.getRow()==2 && board.getPiece(new ChessPosition((myPosRow+1), myPosCol)) == null){
                        goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                myPosition.getColumn()), new ChessPosition((myPosRow+1), myPosCol)
                                , null));
                    } //endof logic for initial move going 2 spaces.

                }else if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) != null) {
                    myPosCol = myPosition.getColumn();
                    myPosRow = myPosition.getRow();
                }



            if (board.getPiece(new ChessPosition(myPosRow, (myPosCol-1))) != null){
                if (board.getPiece(new ChessPosition(myPosRow, myPosCol-1)).getTeamColor() == teamColor) {
                    myPosCol = myPosition.getColumn();
                    myPosRow = myPosition.getRow();
                } else if (board.getPiece(new ChessPosition(myPosRow,myPosCol-1)).getTeamColor() != teamColor) {
                    goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                            myPosition.getColumn()), new ChessPosition(myPosRow, (myPosCol-1))
                            ,null));
                    if (myPosRow == 8){
                        goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                myPosition.getColumn()), new ChessPosition(myPosRow, (myPosCol-1))
                                , PieceType.QUEEN));
                    }
                }
            }//end of take left piece logic

                if (myPosCol != 8 && board.getPiece(new ChessPosition(myPosRow, (myPosCol+1))) != null){
                    if (board.getPiece(new ChessPosition(myPosRow, (myPosCol+1))).getTeamColor() == teamColor) {
                        myPosCol = myPosition.getColumn();
                        myPosRow = myPosition.getRow();
                    } else if (board.getPiece(new ChessPosition(myPosRow,(myPosCol+1))).getTeamColor() != teamColor) {
                        goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                myPosition.getColumn()), new ChessPosition(myPosRow, (myPosCol+1))
                                ,null));
                        if (myPosRow == 8){
                            goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                                    myPosition.getColumn()), new ChessPosition(myPosRow, (myPosCol+1))
                                    , PieceType.QUEEN));
                        }
                    }
                }

            }

        }

        if (piece.getPieceType() == PieceType.KING) {
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow();
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move right
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow();
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move left
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow() + 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move up
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow() - 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move down
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow() - 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move bottom left
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() + 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move upper right diag
            myPosCol = myPosition.getColumn() - 1;
            myPosRow = myPosition.getRow() + 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move left upper diag
            myPosCol = myPosition.getColumn() + 1;
            myPosRow = myPosition.getRow() - 1;
            if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
                findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
            }//move right bottom diag
        }
        return goodMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }
}
