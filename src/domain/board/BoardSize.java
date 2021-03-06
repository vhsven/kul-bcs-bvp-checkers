package domain.board;

import domain.board.contracts.IBoardSize;

public class BoardSize implements IBoardSize {
	private final int rows;
	private final int cols;
	
	@Override
	public int getRows()
	{
		return rows;
	}
	
	@Override
	public int getCols()
	{
		return cols;
	}
	
	/**
	 * Constructs a new BoardSize using the given dimensions.
	 * 
	 * @param 	rows
	 * 			The number of rows of the Board.
	 * @param 	cols
	 * 			The number of columns of the Board.
	 */
	public BoardSize(int rows, int cols) {
		if(!isValidSize(rows, cols))
		{
			throw new IllegalArgumentException(String.format("Invalid board size (%d, %d).", rows, cols));
		}
		this.rows = rows;
		this.cols = cols;
	}
	
	@Override
	public boolean isValidIndex(int index)
	{
		return index >= 1 && index <= getRows()*getCols()/2;
	}
	
	/**
	 * Checks whether the given dimensions form a valid BoardSize.
	 * Both the number of rows and columns should be even and greater than zero.
	 * 
	 * @param 	rows
	 * 			The number of rows of the Board.
	 * @param 	cols
	 * 			The number of columns of the Board.
	 * 
	 * @return	True if the given dimensions form a valid BoardSize.
	 */
	public static boolean isValidSize(int rows, int cols)
	{
		return 	rows > 0 &&
				cols > 0 &&
				rows % 2 == 0 &&
				cols % 2 == 0;
	}
	
	@Override
	public boolean isValidLocation(int row, int col)
	{
		return 	row >= 0 && row < getRows() &&
				col >= 0 && col < getCols();
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", getRows(), getCols());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		if(obj instanceof BoardSize)
		{
			IBoardSize casted = (IBoardSize)obj;
			return 	this.getRows() == casted.getRows() && 
					this.getCols() == casted.getCols();
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return 37 * Integer.hashCode(getRows()) + Integer.hashCode(getCols());
	}
}
