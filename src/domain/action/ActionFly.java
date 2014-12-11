package domain.action;

import java.util.List;
import java.util.stream.Collectors;

import common.Location;
import common.Player;
import common.RestrictedLocationPair;
import domain.board.Board;
import domain.square.Square;

public class ActionFly extends Action
{
	private final RestrictedLocationPair pair;
	
	private RestrictedLocationPair getPair()
	{
		return pair;
	}
	
	public ActionFly(RestrictedLocationPair pair)
	{
		this.pair = pair;
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer)
	{		
		CompositeAction action = getCompositeAction();
		if(action.isValidOn(board, currentPlayer))
		{
			Location from = pair.getFrom();
			Square fromSquare = board.getSquare(from);
			return fromSquare.getPiece().canFly(); //square now surely hasPiece
		}
		else
		{
			return false;
		}
	}

	@Override
	public void executeOn(Board board, Player currentPlayer)
	{
		CompositeAction action = getCompositeAction();
		action.executeOn(board, currentPlayer);
	}
	
	private CompositeAction getCompositeAction()
	{
		RestrictedLocationPair pair = getPair();
		
		if(pair.getDiagonalDistance() <= 1)
		{
			throw new IllegalStateException("Can only fly on diagonals and over more than one square");
		}
		
		List<RestrictedLocationPair> pairs = pair.getPairsBetweenInclusive();
		List<Action> actions = pairs.stream().map(p -> new AtomicActionStep(p)).collect(Collectors.toList());
		return new CompositeAction(actions);
	}

	@Override
	public String toString() {
		return String.format("Fly %s", getPair());
	}
}
