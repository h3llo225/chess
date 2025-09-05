package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private static int row;
    private static int col;

    public ChessPosition(int row, int col) {
        ChessPosition.row = row;
        ChessPosition.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return ChessPosition.row;
        //throw new RuntimeException("Not implemented");

    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */


    public int getColumn() {
        return ChessPosition.col;
        //throw new RuntimeException("Not implemented");
    }
}
