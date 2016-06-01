package metro.GameScreen.MainView.TrainView;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.Graphics.Draw;
import metro.TrainManagement.TrainManagementService;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainLine;
import metro.WindowControls.ActionObserver;
import metro.WindowControls.Button;
import metro.WindowControls.List;

/**
 * This dialog part shows the lines and the trains attached to them.
 * The player can't buy a train with this dialog but can sell one.
 * 
 * @author hauke
 *
 */
public class TrainViewMain extends GameScreen
{
	private int _windowWidth;
	private List _trainList,
		_lineList;
	private Button _moveTrainButton, // to change a train
		_sellTrainButton; // to remove a train
	private Point _areaOffset; // to get the (0,0)-coordinate very easy
	private String _movedTrain;
	private TrainManagementService _trainManagementService;

	/**
	 * Creates a new main view for the train screen.
	 * 
	 * @param areaOffset The offset of the screen area.
	 * @param windowWidth The window width.
	 */
	public TrainViewMain(Point areaOffset, int windowWidth)
	{
		_windowWidth = windowWidth;
		_areaOffset = areaOffset;
		_movedTrain = "";

		_trainManagementService = TrainManagementService.getInstance();
		
		//TODO create panel for controls

		_lineList = new List(new Rectangle(_areaOffset.x + 20, _areaOffset.y + 130, _windowWidth - 300, 230), true);
		_lineList.register(new ActionObserver()
		{
			@Override
			public void selectionChanged(String entry)
			{
				if(isInMoveMode())
				{
					TrainLine selectedLine = _trainManagementService.getLine(entry);
					Train selectedTrain = _trainManagementService.getTrainByName(_movedTrain);

					selectedTrain.setLine(selectedLine);
					stopMoveMode();
				}

				_trainList.clear();
				TrainLine line = _trainManagementService.getLine(_lineList.getText());
				for(Train train : _trainManagementService.getTrains())
				{
					if(train.getLine().equals(line)) _trainList.addElement(train.getName());
				}
			}
		});

		_trainList = new List(new Rectangle(_areaOffset.x + 121, _areaOffset.y + 130, _windowWidth - 141, 230), true);

		fillLineList();

		_moveTrainButton = new Button(new Rectangle(_areaOffset.x + 12 + (_windowWidth / 3), _areaOffset.y + 380, (_windowWidth - 40) / 3 - 10, 20), "Move train");
		_sellTrainButton = new Button(new Rectangle(_areaOffset.x + 4 + (_windowWidth / 3) * 2, _areaOffset.y + 380, (_windowWidth - 40) / 3 - 10, 20), "Sell train");

		addButtonObserver();
		
		//TODO add controls to window
	}

	/**
	 * Fills the line list with the lines.
	 */
	private void fillLineList()
	{
		// Fill line list with all lines:
		ArrayList<TrainLine> lineList = _trainManagementService.getLines();
		for(TrainLine line : lineList)
		{
			_lineList.addElement(line.getName());
		}

		if(lineList.size() > 0)
		{
			_lineList.setSelectedEntry(0); // simply select the first entry
		}
	}

	/**
	 * Creates all needed observers for the buttons.
	 */
	private void addButtonObserver()
	{
		_moveTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				startMoveMode();
			}
		});
		_sellTrainButton.register(new ActionObserver()
		{
			@Override
			public void clickedOnControl(Object arg)
			{
				sellTrainButton_action();
			}
		});
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
		drawListBox();
	}

	/**
	 * Draws the list box with the label above it.
	 */
	private void drawListBox()
	{
		Draw.setColor(METRO.__metroRed);
		
		//TODO make this into labels to draw them in the correct way

		String text = "Your lines:";
		int length = Draw.getStringSize(text).width;
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 25, _areaOffset.y + 110);
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 25, _areaOffset.y + 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 25 + length, _areaOffset.y + 125);

		text = "Your trains:";
		Draw.String(text, METRO.__SCREEN_SIZE.width - _windowWidth + 125, _areaOffset.y + 110);
		length = Draw.getStringSize(text).width;
		Draw.Line(METRO.__SCREEN_SIZE.width - _windowWidth + 125, _areaOffset.y + 125,
			METRO.__SCREEN_SIZE.width - _windowWidth + 125 + length, _areaOffset.y + 125);
	}

	/**
	 * Removes the whole selected line.
	 */
	private void sellTrainButton_action()
	{
		_trainManagementService.removeTrain(_trainList.getText());
		_trainList.removeElement(_trainList.getSelected());
		_trainList.setSelectedEntry(0);
	}

	/**
	 * Fills controls with information about line so that the player can edit it.
	 * Also disabled the buttons for editing and selling trains and sets the {@code _movedTrain} variable.
	 */
	private void startMoveMode()
	{
		if(_trainList.getSelected() == -1) return;

		_moveTrainButton.setState(false);
		_sellTrainButton.setState(false);

		_movedTrain = _trainList.getText();
	}

	/**
	 * Sets the move mode to {@code false}, so that no trains can be moved to other lines.
	 */
	public void stopMoveMode()
	{
		_moveTrainButton.setState(false);
		_sellTrainButton.setState(false);

		_movedTrain = "";
	}

	/**
	 * @return The list with all trains of the selected line.
	 */
	public List getTrainList()
	{
		return _trainList;
	}

	/**
	 * @return The list with all lines.
	 */
	public List getLineList()
	{
		return _lineList;
	}

	/**
	 * @return True when the TrainViewMain is in move mode.
	 */
	public boolean isInMoveMode()
	{
		return !_movedTrain.isEmpty();
	}

	@Override
	public void reset()
	{
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	@Override
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public void mouseReleased(int mouseButton)
	{
	}

	@Override
	public void keyDown(int keyCode)
	{
	}

	@Override
	public boolean isHovered()
	{
		return METRO.__mousePosition.x > _areaOffset.x
			&& METRO.__mousePosition.y < 400;
	}

	/**
	 * @return The train that should be moved.
	 */
	public String getTrainToMove()
	{
		return _movedTrain;
	}
}
