package domain.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Location;
import common.Player;
import domain.board.Board;
import domain.board.BoardSize;
import domain.square.Square;

public class ActionCatch extends Action {
	private final static String regex = "(\\d+)\\s*x\\s*(\\d+)";
	private final int fromIndex;
	private final int toIndex;
	
	public int getFromIndex() {
		return fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}
	
	public Location getFrom(BoardSize size)
	{
		return Location.fromIndex(getFromIndex(), size);
	}
	
	public Location getTo(BoardSize size)
	{
		return Location.fromIndex(getToIndex(), size);
	}
	
	public ActionCatch(int fromIndex, int toIndex) {
		this.fromIndex = fromIndex;
		this.toIndex = toIndex;
	}
	
	public ActionCatch(String move) {
		Pattern catchPattern = Pattern.compile(regex);
		Matcher matcher = catchPattern.matcher(move);
		if(!matcher.matches())
		{
			throw new IllegalArgumentException("Invalid catch pattern: " + move);
		}
		this.fromIndex = Integer.parseInt(matcher.group(1));
		this.toIndex = Integer.parseInt(matcher.group(2));
	}
	
	public static boolean isValidPattern(String pattern)
	{
		return pattern.matches(regex);
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer) {
		BoardSize size = board.getSize();
		if(	fromIndex == toIndex ||
			!size.isValidIndex(fromIndex) ||
			!size.isValidIndex(toIndex))
		{
			return false;
		}
		
		Location from = getFrom(board.getSize());
		Location to = getTo(board.getSize());
		
		if(	!from.isOnSameDiagonal(to) || 
			from.getDiagonalDistance(to) != 2) //!to.isInFrontOf(from, currentPlayer)
		{
			return false;
		}
		
		Location center = from.getCenterBetween(to);
		Square fromSquare = board.getSquare(from);
		Square toSquare = board.getSquare(to);
		Square centerSquare = board.getSquare(center);
		
		if(	!fromSquare.hasPiece() || 
			!centerSquare.hasPiece() ||
			toSquare.hasPiece() ||
			fromSquare.getPiece().getPlayer() != currentPlayer || 
			centerSquare.getPiece().getPlayer()  == currentPlayer)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void executeOn(Board board, Player currentPlayer) {
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		Location from = getFrom(board.getSize());
		Location to = getTo(board.getSize());
		Location center = from.getCenterBetween(to);
		board.removePiece(center);
		board.movePiece(from, to);
	}

	@Override
	public String toString() {
		return String.format("Catch from square %d to %d", getFromIndex(), getToIndex());
	}
}
