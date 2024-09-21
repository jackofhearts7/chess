package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = null;
        switch (type) {
            case PieceType.KING:
                moves = kingMoves(board, myPosition);
                break;
            case PieceType.QUEEN:
                moves = queenMoves(board, myPosition);
                break;
            case PieceType.BISHOP:
                moves = bishopMoves(board, myPosition);
                break;
            case PieceType.KNIGHT:
                moves = knightMoves(board, myPosition);
                break;
            case PieceType.ROOK:
                moves = rookMoves(board, myPosition);
                break;
            case PieceType.PAWN:
                moves = pawnMoves(board, myPosition);
                break;
            default:
                moves = new ArrayList<>();
                break;
        }
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {

        return new ArrayList<>();
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {
        return new ArrayList<>();
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition position) {

        return new ArrayList<>();
    }

}
