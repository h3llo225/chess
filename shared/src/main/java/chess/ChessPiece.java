package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        ArrayList<ChessMove> goodMoves = new ArrayList<ChessMove>() ;
        goodMoves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition( 6,5), null));
        if (piece.getPieceType() == PieceType.BISHOP) {

            for (int i = myPosition.getRow(); i<=8; i++){


            }
            System.out.println(myPosition + "this is pos");
            //if (chessMove.getEndPosition();){ }
            //myPosition.getEndPosition();
            return List.of(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                    new ChessPosition(myPosition.getRow() /** + x */, myPosition.getColumn() /** + x */ ), PieceType.BISHOP));
            //chess.ChessPiece index = board.getPiece(myPosition);
            //System.out.println(index + "this is pos");

        }
        return List.of();
    }
}
