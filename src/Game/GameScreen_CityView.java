package Game;

import Game.CityTravelerSpot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;


/**
 * 
 */

/**
 * @author Hauke
 *
 */
public class GameScreen_CityView implements GameScreen
{
	public static GameScreen _trainGameScreen;
	public static Point2D _offset = new Point2D.Float(0, 0);
	
	private List<CityTravelerSpot> _travelerSpots = new ArrayList<CityTravelerSpot>();
	private Point _oldMousePos; // Mouse position from last frame
	private boolean _dragMode = false;

	/**
	 * Constructor to load all the important stuff
	 */
	public GameScreen_CityView()
	{
		_travelerSpots.add(new CityTravelerSpot(new Point(500, 500), 6));
		_travelerSpots.add(new CityTravelerSpot(new Point(700, 700), 4));
	}

	/* (non-Javadoc)
	 * @see GameScreen#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(Graphics2D g)
	{
		if(_dragMode)
		{
			_offset = new Point2D.Float((float)_offset.getX() + (MouseInfo.getPointerInfo().getLocation().x - _oldMousePos.x) * 2f,
				(float)_offset.getY() + (MouseInfo.getPointerInfo().getLocation().y - _oldMousePos.y) * 2f);
		}
		
		drawCircles(g);	
		
		_oldMousePos = MouseInfo.getPointerInfo().getLocation();
	}
	/**
	 * Draws the traveler-circles.
	 * @param g The graphic handle to draw on
	 */
	private void drawCircles(Graphics2D g)
	{
		int selectedSpotNumber = -1;
		int selectedLayerNumber = -1; // the circle, the mouse is in. -1 means: Mouse out of any circle.
		
		// get the selected circle number
		for(int i = 0; i < 10; i++) // go through all layers
		{
			boolean isLayerSelected = false;
			for(int k = 0; !isLayerSelected && k < _travelerSpots.size(); k++) // go through all spots
			{
				isLayerSelected = _travelerSpots.get(k).isMouseInCircle(i); // if mouse is in ANY circle of this layer
				if(isLayerSelected)
				{
					selectedLayerNumber = i;
					selectedSpotNumber = k;
				}
			}
		}
		// draw all the circles
		for(int i = 0; i < 10; i++)
		{
			for(int k = 0; k < _travelerSpots.size() && i < _travelerSpots.get(k).getStrength(); k++)
			{
				_travelerSpots.get(k).draw(g, i, i == selectedLayerNumber, k == selectedSpotNumber, true); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
			for(int k = 0; k < _travelerSpots.size() && i < _travelerSpots.get(k).getStrength(); k++)
			{
				_travelerSpots.get(k).draw(g, i, i == selectedLayerNumber, k == selectedSpotNumber); // i==selectedLayerNumber means: if i is the selected circle level -> draw it different
			}
		}
		
		if(selectedLayerNumber != -1) // if there's a selected circle 
		{
			int gray = 55 * selectedLayerNumber;
			if(gray > 255) gray = 255;
			g.setColor(new Color(0, 0, 200));
			g.drawString(selectedLayerNumber + "", MouseInfo.getPointerInfo().getLocation().x - g.getFontMetrics(METRO.__stdFont).stringWidth(selectedLayerNumber + "") / 2 - 1, 
					MouseInfo.getPointerInfo().getLocation().y + g.getFontMetrics(METRO.__stdFont).getHeight() / 4 + 1);
		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = true;
		}
		if(METRO.__viewPortButton_Train.isPressed(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y))
		{
			METRO.__currentGameScreen = _trainGameScreen;
			METRO.__viewPortButton_City.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2 - 200, -15));
			METRO.__viewPortButton_Train.setPosition(new Point(METRO.__SCREEN_SIZE.width / 2, -5));
		}
	}

	/* (non-Javadoc)
	 * @see GameScreen#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(SwingUtilities.isMiddleMouseButton(e))
		{
			_dragMode = false;
		}
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
	}
}
