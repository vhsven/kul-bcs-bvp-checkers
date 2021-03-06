package domain.update.contracts;

/**
 * Provides a basic interface for every class that wants to accept {@link IObserver}s and send them updates.
 */
public interface IUpdateSource extends IBasicUpdateSource
{
	/**
	 * Subscribe an observer to receive future updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer that will receive updates.
	 */
	public void subscribe(IObserver observer);

	/**
	 * Unsubscribe an observer to stop receiving updates about the game.
	 * 
	 * @param 	observer
	 * 			The observer to unsubscribe.
	 */
	public void unsubscribe(IObserver observer);
	
	/**
	 * Returns true if the given observer is already subscribed, false otherwise.
	 * 
	 * @param 	observer
	 * 			The observer to check
	 */
	public boolean isSubscribed(IBasicObserver observer);
}