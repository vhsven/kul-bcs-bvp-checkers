package common;

import java.awt.Color;
import domain.board.BoardSize;

public class Configs {
	public final static BoardSize Size = new BoardSize(10,10);
	public final static boolean MandatoryCatching = true;
	public final static boolean MandatoryMaximalCatching = true;
	public final static boolean BackwardCatchingAllowed = true;
	public final static Player FirstPlayer = Player.White;
	public final static boolean FlyingDame = true;
	
	//GUI
	public final static Color DarkColor = Color.GRAY;
	public final static Color LightColor = Color.WHITE;
	public final static int SquareSizePx = 50;
}
