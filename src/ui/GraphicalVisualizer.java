package ui;

import java.awt.event.WindowEvent;

import ui.contracts.IVisualizer;
import bvp.Bord;
import bvp.Figuren;
import bvp.Figuur;
import common.Configs;
import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

public class GraphicalVisualizer implements IVisualizer
{
	private final Bord frame = new Bord("Checkers");
	
	private Bord getFrame()
	{
		return frame;
	}
	
	public GraphicalVisualizer() { }

	private Figuur getFigure(Figuren pieces, IPiece piece) //50 x 50
	{
		if(!piece.canPromote())
		{
			if(piece.getPlayer() == Player.White)
			{
				return pieces.getFiguur("wittedamschijf");
			}
			else
			{
				return pieces.getFiguur("zwartedamschijf");
			}
		}
		else
		{
			if(piece.getPlayer() == Player.White)
			{
				return pieces.getFiguur("witteschijf");
			}
			else
			{
				return pieces.getFiguur("zwarteschijf");
			}
		}
	}
	
	@Override
	public void paint(IReadOnlyBoard board)
	{
		Bord frame = getFrame();
		IBoardSize size = board.getSize();
		Figuren numbers = new Figuren(Configs.CijfersPath);
		Figuren pieces = new Figuren(Configs.SchijvenPath);
		Figuur background = new Figuur(size.getCols()*50, size.getRows()*50);
		Figuur whiteSquare = new Figuur(Configs.SquareSizePx, Configs.SquareSizePx);
		Figuur blackSquare = new Figuur(Configs.SquareSizePx, Configs.SquareSizePx);
		whiteSquare.vulRechthoek(0,0,Configs.SquareSizePx,Configs.SquareSizePx,Configs.LightColor);
		blackSquare.vulRechthoek(0,0,Configs.SquareSizePx,Configs.SquareSizePx,Configs.DarkColor);
		for (int row = 0; row < size.getRows(); row++)
		{
			for (int col = 0; col < size.getCols(); col++)
			{
				try
				{
					Location location = new Location(row, col, size);
					IReadOnlySquare square = board.getSquare(location);
					if (location.isWhite())
					{
						background.plaatsFiguur(whiteSquare, col*Configs.SquareSizePx, row*Configs.SquareSizePx);
					}
					else
					{
						background.plaatsFiguur(blackSquare, col*Configs.SquareSizePx, row*Configs.SquareSizePx);
						int hPixels = col*Configs.SquareSizePx;
						int vPixels = row*Configs.SquareSizePx;
						if(square.hasPiece())
						{
							IPiece piece = square.getPiece();
							Figuur figure = getFigure(pieces, piece);
							background.plaatsFiguur(figure, hPixels, vPixels);
						}
						int index = location.getIndex();
						String digits = Integer.toString(index);
						for(int i=0; i<digits.length(); i++)
						{
							String digit = digits.substring(i, i+1);
							Figuur figure = numbers.getFiguur(digit).scaleer(10,10);
							background.plaatsFiguur(figure, hPixels + Configs.SquareSizePx/3 + 10*i, vPixels + Configs.SquareSizePx/3);
						}
					}
				}
				catch (LocationOutOfRangeException e)
				{
					assert false;
				}
			}
		}
		frame.toon(background);
		try {Thread.sleep(Configs.PaintDelayMs);} catch (InterruptedException e) { }
	}
	
	private void close()
	{
		Bord frame = getFrame();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		paint(board);
	}

	@Override
	public void fireGameOver(Player winner)
	{
		close();		
	}

	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
		paint(board);
	}

	@Override
	public void fireOutOfMoves(Player player)
	{
	}

	
	@Override
	public void fireProposeRemise(Player proposer)
	{
	}

	@Override
	public void fireAcceptRemise()
	{
	}

	@Override
	public void fireDeclineRemise()
	{
	}

	@Override
	public void fireResign(Player resignee)
	{
	}

	@Override
	public void fireStart(IReadOnlyBoard board, Player starter)
	{
		paint(board);
	}

	@Override
	public void fireWarning(String message)
	{
	}

	@Override
	public void fireError(String message, Exception ex)
	{
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
	}

	@Override
	public void fireForcedRemise()
	{
	}
}
