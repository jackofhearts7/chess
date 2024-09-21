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
        if (pieceColor == ChessGame.TeamColor.BLACK) {
            this.opponentTeamColor = ChessGame.TeamColor.WHITE;
        } else {
            this.opponentTeamColor = ChessGame.TeamColor.BLACK;
        }
        this.type = type;
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessGame.TeamColor opponentTeamColor;
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
    public ChessGame.TeamColor getOpponentTeamColor() {
        return opponentTeamColor;
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
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //case1: move forward a square
        //case2: if in og pos, move forward 2 sqs
        //case3: if opp is frontLeft or frontRight, move up and over 1sq (left & right)
        //og pos is different for team colors
        int ahead = 1;
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            ahead = -1;
        }

        ChessPosition aheadOne = new ChessPosition(position.getRow() + ahead, position.getColumn());
        if (aheadOne.isOnBoard() && board.getPiece(aheadOne) == null) {
            ChessPiece.PieceType promotionType = null;
            if (aheadOne.getRow() == 8 || aheadOne.getRow() == 1) {
                promotionType = ChessPiece.PieceType.QUEEN;
            }
            validMoves.add(new ChessMove(position, aheadOne, promotionType));

            if ((getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7)) {
                ChessPosition aheadTwo = new ChessPosition(position.getRow() + ahead*2, position.getColumn());
                if (aheadTwo.isOnBoard() && board.getPiece(aheadTwo) == null) {
                    validMoves.add(new ChessMove(position, aheadTwo, null));
                }
            }
        }
        ChessPosition frontLeft = new ChessPosition(position.getRow() + ahead, position.getColumn() - 1);
        if (frontLeft.isOnBoard()) {
            ChessPiece destinationPiece = board.getPiece(frontLeft);
            if (destinationPiece != null && destinationPiece.getTeamColor() == opponentTeamColor) {
                ChessPiece.PieceType promotionType = null;
                if (frontLeft.getRow() == 8 || frontLeft.getRow() == 1) {
                    promotionType = ChessPiece.PieceType.QUEEN;
                }
                validMoves.add(new ChessMove(position, frontLeft, null));
            }
        }

        ChessPosition frontRight = new ChessPosition(position.getRow() + ahead, position.getColumn() + 1);
        if (frontRight.isOnBoard()) {
            ChessPiece destinationPiece = board.getPiece(frontRight);
            if (destinationPiece != null && destinationPiece.getTeamColor() == opponentTeamColor) {
                ChessPiece.PieceType promotionType = null;
                if (frontRight.getRow() == 8 || frontRight.getRow() == 1) {
                    promotionType = ChessPiece.PieceType.QUEEN;
                }
                validMoves.add(new ChessMove(position, frontRight, promotionType));
            }
        }

        return validMoves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {

        return new ArrayList<>();
    }
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition position) {
        //king can only move to 8 possible positions
        ArrayList<ChessPosition> possibleDestinations = new ArrayList<>();

        possibleDestinations.add(new ChessPosition(position.getRow() + 1, position.getColumn() - 1));
        possibleDestinations.add(new ChessPosition(position.getRow() + 1, position.getColumn()));
        possibleDestinations.add(new ChessPosition(position.getRow() + 1, position.getColumn() + 1));
        possibleDestinations.add(new ChessPosition(position.getRow(), position.getColumn() + 1));
        possibleDestinations.add(new ChessPosition(position.getRow() - 1, position.getColumn() + 1));
        possibleDestinations.add(new ChessPosition(position.getRow() - 1, position.getColumn()));
        possibleDestinations.add(new ChessPosition(position.getRow() - 1, position.getColumn() - 1));
        possibleDestinations.add(new ChessPosition(position.getRow(), position.getColumn() - 1));

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        for (ChessPosition destination : possibleDestinations) {
            if (destination.isOnBoard()) {
                ChessPiece pieceAtDest = board.getPiece(destination);
                if (pieceAtDest == null || pieceAtDest.getTeamColor() != getTeamColor()) {
                    validMoves.add(new ChessMove(position, destination, null));
                }
            }
        }
        return validMoves;
    }
}
