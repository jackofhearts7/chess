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

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> aheadLeft = straightPath(board, position, 1,-1);
        Collection<ChessMove> aheadRight = straightPath(board, position, 1,1);
        Collection<ChessMove> backLeft = straightPath(board, position, -1,-1);
        Collection<ChessMove> backRight = straightPath(board, position, -1,1);

        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(aheadLeft);
        validMoves.addAll(aheadRight);
        validMoves.addAll(backLeft);
        validMoves.addAll(backRight);

        return validMoves;
    }
    private Collection<ChessMove> straightPath(ChessBoard board, ChessPosition position, int rowChange, int colChange) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPosition destination = position;
        while(true) {
            destination = new ChessPosition(destination.getRow() + rowChange, destination.getColumn() + colChange);
            if (!destination.isOnBoard()) {
                break;
            }
            ChessPiece piece = board.getPiece(destination);
            if (piece == null) {
                validMoves.add(new ChessMove(position, destination, null));
            } else {
                if (piece.getTeamColor() != getTeamColor()) {
                    validMoves.add(new ChessMove(position, destination, null));
                }
                break;
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition position) {

        ArrayList<ChessPosition> possibleDestinations = new ArrayList<>();

        possibleDestinations.add(new ChessPosition(position.getRow() + 2, position.getColumn() + 1));
        possibleDestinations.add(new ChessPosition(position.getRow() + 2, position.getColumn() - 1));
        possibleDestinations.add(new ChessPosition(position.getRow() - 2, position.getColumn() + 1));
        possibleDestinations.add(new ChessPosition(position.getRow() - 2, position.getColumn() - 1));
        possibleDestinations.add(new ChessPosition(position.getRow() + 1, position.getColumn() + 2));
        possibleDestinations.add(new ChessPosition(position.getRow() - 1, position.getColumn() + 2));
        possibleDestinations.add(new ChessPosition(position.getRow() + 1, position.getColumn() - 2));
        possibleDestinations.add(new ChessPosition(position.getRow() - 1, position.getColumn() - 2));

        return extractValidDestinations(board, position, possibleDestinations);
    }

    private Collection<ChessMove> extractValidDestinations(ChessBoard board, ChessPosition position, ArrayList<ChessPosition> possibleDestinations) {
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

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> left = straightPath(board, position, 0, -1);
        Collection<ChessMove> right = straightPath(board, position, 0,1);
        Collection<ChessMove> ahead = straightPath(board, position, 1,0);
        Collection<ChessMove> back = straightPath(board, position, -1,0);

        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(left);
        validMoves.addAll(right);
        validMoves.addAll(ahead);
        validMoves.addAll(back);

        return validMoves;
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
            if (aheadOne.getRow() == 8 || aheadOne.getRow() == 1) {
                validMoves.addAll(getAllPromosForPos(position, aheadOne));
            } else {
                validMoves.add(new ChessMove(position, aheadOne, null));
            }

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
                if (frontLeft.getRow() == 8 || frontLeft.getRow() == 1) {
                    validMoves.addAll(getAllPromosForPos(position, frontLeft));
                } else {
                    validMoves.add(new ChessMove(position, frontLeft, null));
                }
            }
        }

        ChessPosition frontRight = new ChessPosition(position.getRow() + ahead, position.getColumn() + 1);
        if (frontRight.isOnBoard()) {
            ChessPiece destinationPiece = board.getPiece(frontRight);
            if (destinationPiece != null && destinationPiece.getTeamColor() == opponentTeamColor) {
                if (frontRight.getRow() == 8 || frontRight.getRow() == 1) {
                    validMoves.addAll(getAllPromosForPos(position, frontRight));
                } else {
                    validMoves.add(new ChessMove(position, frontRight, null));
                }
            }
        }

        return validMoves;
    }
    private Collection<ChessMove> getAllPromosForPos(ChessPosition startPosition, ChessPosition endPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
        moves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
        moves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));

        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> left = straightPath(board, position, 0, -1);
        Collection<ChessMove> right = straightPath(board, position, 0,1);
        Collection<ChessMove> ahead = straightPath(board, position, 1,0);
        Collection<ChessMove> back = straightPath(board, position, -1,0);

        Collection<ChessMove> aheadLeft = straightPath(board, position, 1,-1);
        Collection<ChessMove> aheadRight = straightPath(board, position, 1,1);
        Collection<ChessMove> backLeft = straightPath(board, position, -1,-1);
        Collection<ChessMove> backRight = straightPath(board, position, -1,1);

        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(aheadLeft);
        validMoves.addAll(aheadRight);
        validMoves.addAll(backLeft);
        validMoves.addAll(backRight);
        validMoves.addAll(left);
        validMoves.addAll(right);
        validMoves.addAll(ahead);
        validMoves.addAll(back);

        return validMoves;
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

        return extractValidDestinations(board, position, possibleDestinations);
    }
}
