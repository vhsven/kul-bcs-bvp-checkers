package domain.observers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import common.Player;
import domain.Game;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.action.request.ActionRequest;
import domain.action.request.AtomicCatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.updates.GameUpdateSource;
import domain.updates.contracts.IBasicGameObserver;
import domain.updates.contracts.IGameObserver;

/**
 * This {@link IGameObserver} monitors the {@link Game} 
 * for possible out-of-moves occurrences every time the {@link Player}s switch turns.
 * If it found such an occurrence, it signals this to its own observers 
 * through the {@link IGameObserver#outOfMoves(Player) } event.
 */
public class OutOfMovesObserver extends GameUpdateSource implements IBasicGameObserver
{
	private static boolean isCurrentPlayerOutOfMoves(IReadOnlyBoard board, Player player)
	{
		Set<Location> locations = board.getPlayerPieces(player).keySet();
		for(Location location : locations)
		{
			if(canAct(board, player, location))
			{
				return false;
			}
		}
		return true;
	}
	
	private static <T extends ActionRequest> List<T> filterValidActionRequests(IReadOnlyBoard board, Player player, List<T> requests)
	{
		List<T> filteredRequests = new ArrayList<T>();
		for(T step : requests)
		{
			Action action = ActionFactory.create(step, board, player);
			if(action.isValidOn(board, player)) //no need to execute in case of atomic actions
			{
				filteredRequests.add(step);
			}
		}
		
		return filteredRequests;
	}
	
	private static boolean canMove(IReadOnlyBoard board, Player player, Location location)
	{
		List<MoveActionRequest> possibleAtomicSteps = getAtomicStepsFromLocation(board, player, location);		
		List<MoveActionRequest> validAtomicSteps = filterValidActionRequests(board, player, possibleAtomicSteps);
		
		return validAtomicSteps.size() > 0;
	}
	
	private static boolean canCatch(IReadOnlyBoard board, Player player, Location location)
	{
		List<AtomicCatchActionRequest> possibleAtomicCatches = getAtomicCatchesFromLocation(board, player, location);		
		List<AtomicCatchActionRequest> validAtomicCatches = filterValidActionRequests(board, player, possibleAtomicCatches);
		
		return validAtomicCatches.size() > 0;
	}

	private static boolean canAct(IReadOnlyBoard board, Player player, Location location)
	{
		return canMove(board, player, location) || canCatch(board, player, location);
	}
	
	public static List<MoveActionRequest> getAtomicStepsFromLocation(IReadOnlyBoard board, Player player, Location location)
	{
		List<MoveActionRequest> requests = new ArrayList<MoveActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		for(Location target : targets)
		{
			if(	board.isLocationOccupiedBy(player, location) &&
				board.isLocationFree(target))
			{
				requests.add(new MoveActionRequest(location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
	
	public static List<AtomicCatchActionRequest> getAtomicCatchesFromLocation(IReadOnlyBoard board, Player player, Location location)
	{
		List<AtomicCatchActionRequest> requests = new ArrayList<AtomicCatchActionRequest>();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		
		for(Location target : targets)
		{
			DiagonalLocationPair pair = new DiagonalLocationPair(location, target);
			if(	board.isLocationOccupiedBy(player, location) &&
					board.isLocationFree(target) && 
					board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				requests.add(new AtomicCatchActionRequest(location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
	
	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
	}
	
	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		if(isCurrentPlayerOutOfMoves(board, switchedIn))
		{
			updateObserversOutOfMoves(switchedIn);
		}
	}

	@Override
	public void executeAction(Action action)
	{
	}
}