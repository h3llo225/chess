package chess;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
public ChessBoard board = new ChessBoard();
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

//    public Collection<ChessMove> getGoodMoves(Collection<ChessMove> moves){
//        ChessPiece piece = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        piece.pieceMoves();
//    }
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        } else{
            return piece.pieceMoves(board,startPosition);
        }

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        ChessBoard board = getBoard();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);


        if (validMoves(start) == null ){
            throw new InvalidMoveException("Invalid move");
        }else {
            board.addPiece(end, piece);
            board.addPiece(start, null);

        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        ChessPosition kingPos = null;
        Collection<ChessMove> goodMoves = List.of();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (piece != null){
                    ChessPosition myPos = new ChessPosition(i,j);
                    if (piece.getTeamColor() != teamColor){
                     goodMoves = piece.pieceMoves(board, myPos);
                }// if logic for grabbing all moves if the team color is not same as kings
                    if (board.getPiece(myPos).getTeamColor() == teamColor && board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING){
                         kingPos = new ChessPosition(i,j);
                    }// if logic for grabbing king
                    for (ChessMove move : goodMoves){
                        ChessPosition endPos = move.getEndPosition();
                        if (kingPos == endPos){
                            return true;
                        }else{return false;}

                    }
                }


            }
            //return false;
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        // if (isInCheck == true; && va)
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        //ChessBoard board = new ChessBoard();
        // throw new RuntimeException("Not implemented");
        return board;
    }
}
