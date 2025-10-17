package chess;

import java.lang.reflect.Array;
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
public class ChessPiece implements Cloneable {
    public ChessGame.TeamColor pieceColor;
    public PieceType pieceType;
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

    public static boolean findGoodMove(ChessBoard board, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                                       ChessGame.TeamColor teamColor, ChessPosition myPosition) {
        if (myPosCol > 0 && myPosCol < 9 && myPosRow > 0 && myPosRow < 9) {
            if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) != null) {
                if (board.getPiece(new ChessPosition(myPosRow, myPosCol)).getTeamColor() == teamColor) {
                    myPosCol = myPosition.getColumn();
                    myPosRow = myPosition.getRow();
                    return false;
                } else if (board.getPiece(new ChessPosition(myPosRow, myPosCol)).getTeamColor() != teamColor) {
                    goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                            myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                            , null));
                    return false;
                    //break;
                }


            } else if (board.getPiece(new ChessPosition(myPosRow, myPosCol)) == null) {
                goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                        myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                        , null));
                return true;
            }
        }
            return false;

        }

        public static boolean helperUpDirectionLoop(ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                                             ChessGame.TeamColor teamColor, ChessBoard board ){
            boolean val = false;
        for (int j = 0; j < (8-myPosition.getColumn()); j++) {
                myPosCol += 1;
                 val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }
            return val;
        }

        public static boolean helperUpperRightDirectionLoop(ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                                                            ChessGame.TeamColor teamColor, ChessBoard board, int i) {
            boolean val = false;
        for (int j = 0; i < (8 - myPosition.getRow()) && j < (8 - myPosition.getColumn()); i++, j++) {
                //int myPosRow = myPosition.getRow();
                //int myPosCol = myPosition.getColumn();
                myPosRow += 1;
                myPosCol += 1;
                 val = findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
                if (!val) {
                    break;
                }
//
            }
            return val;
        }

        public static boolean helperUpperLeftDirectionLoop(ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                                                           ChessGame.TeamColor teamColor, ChessBoard board, int i) {
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            boolean val = false;
            for (int j = 0; i < (myPosition.getRow() - 1) && j < (8 - myPosition.getColumn()); i++, j++) {
                myPosRow -= 1;
                myPosCol += 1;

                val = findGoodMove(board, myPosRow, myPosCol, goodMoves, teamColor, myPosition);
                if (!val) {
                    break;
                }
            }
            return val;
        }
        public static boolean helperDownLoop(ChessPosition myPosition, int myPosRow, int myPosCol, ArrayList<ChessMove> goodMoves,
                                             ChessGame.TeamColor teamColor, ChessBoard board){
            myPosCol = myPosition.getColumn();
            myPosRow = myPosition.getRow();
            int i= 0;
            boolean val = false;
            for (int j = 0;j < (myPosition.getColumn()-1); j++) {
                myPosCol -= 1;
                 val =  findGoodMove(board,myPosRow,myPosCol, goodMoves, teamColor, myPosition);
                if (!val){
                    break;
                }
            }//down direction
            return val;
        }

        public static boolean pawnPromotionFindPiece(ChessBoard board, int myPosRow,int myPosCol, ArrayList<ChessMove> goodMoves,
                                               ChessPosition myPosition) {
            for (PieceType typeOfPiece : PieceType.values()) {
                if (typeOfPiece != PieceType.KING && typeOfPiece != PieceType.PAWN) {
                    goodMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),
                            myPosition.getColumn()), new ChessPosition(myPosRow, myPosCol)
                            , typeOfPiece));
                }
            }
            return true;
        }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor teamColor = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor();
        ArrayList<ChessMove> goodMoves = new ArrayList<ChessMove>() ;
        int myPosRow = myPosition.getRow();
        int myPosCol = myPosition.getColumn();
        new Bishop().bishopMoves(piece, myPosition, myPosRow, myPosCol, goodMoves,
                teamColor, board);
        new Rook().rookMoves(piece, myPosition, goodMoves,
                teamColor, board);
        new Queen().queenMoves(piece, myPosition, myPosRow, myPosCol, goodMoves,
                 teamColor, board);
        new Knight().knightMoves(piece, myPosition,  goodMoves,
                 teamColor, board);
        new Pawn().pawnMoves(piece, myPosition, myPosRow, myPosCol, goodMoves,
                teamColor, board);
        new King().kingMoves(piece, myPosition, myPosRow, myPosCol, goodMoves,
                teamColor, board);
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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
