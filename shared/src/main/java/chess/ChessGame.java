package chess;

import javax.swing.*;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
public ChessBoard board = new ChessBoard();
public TeamColor color;
    public ChessGame() {
        //this.board = board;
    board.resetBoard();
    color = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return color;
    }

    public TeamColor swapTurns(TeamColor teamColor){
        if (teamColor == TeamColor.BLACK){
            teamColor = TeamColor.WHITE;
        } else if (teamColor == TeamColor.WHITE){
            teamColor = TeamColor.BLACK;
        }
        return teamColor;
    }
    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        color = team;
        //throw new RuntimeException("Not implemented");
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
    public Collection<ChessMove> validMoves (ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));
        Collection<ChessMove> goodMoves=  new ArrayList<>();
        if (piece == null){
            return null;
        } else{
            Collection<ChessMove> all= piece.pieceMoves(board,startPosition);
            ChessBoard original = (ChessBoard) board.clone();
            for (ChessMove move : all){
                ChessBoard prevBoard =(ChessBoard) original.clone();
                setBoard(prevBoard);
                if (move.getPromotionPiece() != null){
                    ChessPiece.PieceType promo = move.getPromotionPiece();
                    ChessPiece promotionOfPiece = new ChessPiece(board.getPiece(startPosition).getTeamColor(), promo);
                    if (!movePiece2(move.getStartPosition(),move.getEndPosition(),piece,piece.getTeamColor(), promotionOfPiece)){
                        goodMoves.add(move);
                    };
                } else if (move.getPromotionPiece() == null){
                    if (!movePiece2(move.getStartPosition(),move.getEndPosition(),piece,piece.getTeamColor(), null)){
                        goodMoves.add(move);
                }


                };
            }
            setBoard(original);
        }


        //throw new RuntimeException("Not implemented");
        return goodMoves;
    }

    public boolean movePiece2(ChessPosition start, ChessPosition end, ChessPiece piece, TeamColor teamColor, ChessPiece promotion){
        board.addPiece(end, piece);
        board.addPiece(start, null);
        if (promotion != null){
            board.addPiece(end,promotion);
        }
        return isInCheck(teamColor);
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        //ChessBoard board = getBoard();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        //ChessPiece piece2 = board.getPiece(new ChessPosition(4,5));

        if (validMoves(start) == null ){
            throw new InvalidMoveException();
        } else if (validMoves(start).isEmpty()){
            throw new InvalidMoveException();
        }
        else if (piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }
        else if (!validMoves(start).contains(new ChessMove(start, end, move.getPromotionPiece()))){
            throw new InvalidMoveException();
        }
        else {
            //Collection<ChessPosition> all = new ArrayList<>();
            boolean foundFlag = false;
            for (ChessMove moves : validMoves(start)) {
                //TeamColor bob = getTeamTurn();
                //TeamColor bill = board.getPiece(start).getTeamColor();
                if (board.getPiece(start) != null){
                    if (moves.getEndPosition().equals(end) && getTeamTurn().equals(board.getPiece(start).getTeamColor())) {
                        if (moves.getPromotionPiece() != null && moves.getPromotionPiece().equals(move.getPromotionPiece())) {
                            ChessPiece.PieceType promo = moves.getPromotionPiece();
                            ChessPiece promotionOfPiece = new ChessPiece(board.getPiece(start).getTeamColor(), promo);
                            movePiece2(start, end, piece, board.getPiece(start).getTeamColor(), promotionOfPiece );
                        } else if (moves.getPromotionPiece() == null) {
                            movePiece2(start, end, piece, board.getPiece(start).getTeamColor(), null);
                        }

                    }

            }


            }
            setTeamTurn(swapTurns(getTeamTurn()));
            foundFlag = true;
            if (foundFlag == false){
                throw new InvalidMoveException();
            }


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
        //ChessBoard board = getBoard();
        ChessPosition kingPos = null;
        Collection<ChessMove> goodMoves = new ArrayList<ChessMove>();
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                if (board.getPiece(new ChessPosition(i,j)) != null){
                    ChessPosition myPos = new ChessPosition(i,j);
                if (board.getPiece(myPos).getTeamColor() == teamColor && board.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING) {
                    kingPos = new ChessPosition(i, j);
                }
                }
            }
        }
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (board.getPiece(new ChessPosition(i,j)) != null){
                    ChessPosition myPos = new ChessPosition(i,j);
                    if (piece.getTeamColor() != teamColor){
                        goodMoves = piece.pieceMoves(board, myPos);
                    }// if logic for grabbing all moves if the team color is not same as kings
                    for (ChessMove move : goodMoves){
                        ChessPosition endPos = move.getEndPosition();
                        if (kingPos.equals(endPos)){
                            return true;
                        }

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
//     *///public boolean loopThroughAllPieces( TeamColor teamColor,ChessMove moveCheckingKingHere){
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                ChessPosition myPos = new ChessPosition(i,j);
//                if (board.getPiece(myPos) != null && board.getPiece(myPos).getTeamColor() == teamColor && board.getPiece(myPos).getPieceType() != ChessPiece.PieceType.KING){
//                    ChessPiece piece = board.getPiece(myPos);
//                    goodMoves= piece.pieceMoves(board,myPos);
//                }//get all the pieces
//            }
//        }
//    }


    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPos = null;
        //ChessBoard board = getBoard();
        //throw new RuntimeException("Not implemented");
        Collection<ChessMove> goodMovesKing = new ArrayList<ChessMove>();
        Collection<ChessMove> goodMoves = new ArrayList<ChessMove>();
        if (isInCheck(teamColor) == true){
             for (int i = 1; i < 9; i++) {
                 for (int j = 1; j < 9; j++) {
                     if (board.getPiece(new ChessPosition(i, j)) != null) {
                         ChessPosition myPos = new ChessPosition(i, j);
                         if (board.getPiece(myPos).getTeamColor() == teamColor) {
                             ChessPiece piece = board.getPiece(myPos);
                             goodMoves = validMoves(myPos);
                             for (ChessMove move : goodMoves) {
                                 if (!goodMoves.isEmpty()) {
                                     return false;
                                 }

                             }

                         }
                     }

                 }
             }
            return true;
        }
    return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");

        Collection<ChessMove> goodMoves = new ArrayList<ChessMove>();
        Collection<ChessMove> validsMoves = new ArrayList<>();
        if (!isInCheckmate(teamColor)){
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        ChessPosition myPos = new ChessPosition(i, j);
                        if (board.getPiece(myPos).getTeamColor() == teamColor) {
                            ChessPiece piece = board.getPiece(myPos);

                            goodMoves = validMoves(myPos);
                            validsMoves.addAll(validMoves(myPos));
                        }


                    }
                }

            }
            if (validsMoves.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        boolean theSame = Objects.equals(board, chessGame.board) && color.equals(chessGame.color);
        return theSame;
    }


    @Override
    public int hashCode() {
        return Objects.hash(board, color);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                '}';
    }

}




