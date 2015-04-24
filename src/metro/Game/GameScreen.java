package metro.Game;

import java.awt.Point;
import java.awt.Rectangle;

import metro.Graphics.Draw;
import metro.WindowControls.Window;
import metro.WindowControls.Button;
import metro.WindowControls.Label;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Every Menu or Game Sreen has to implement this interface for start() and update(). This will make the creation process more easy.
 * @author Hauke
 * 
 */

public abstract class GameScreen
{
	public SettingsWindow _settingsWindow; // makes it possible to create a settings-window from every(!) game screen.
	public InGameMenuWindow _inGameMenuWindow; // makes it possible to create a ingame-menu-window from every(!) game screen.
	
	/**
	 * Will be executed as fast as possible ;)
	 * This method will actually not directly update the game screen, but fires the {@code updateGameScreen(g)} method to do this.
	 * @param g SpriteBatch to use.
	 */
	public void update(SpriteBatch g)
	{
		if(_inGameMenuWindow != null) _inGameMenuWindow.update();
		if(_settingsWindow != null) _settingsWindow.update();
		
		updateGameScreen(g);
	}
	
	/**
	 * Updates the actual game screen.
	 * @param g SpriteBatch to draw on.
	 */
	public abstract void updateGameScreen(SpriteBatch g);

	/**
	 * When mouse has clicked
	 * @param screenX The x-position on the screen
	 * @param screenY The y-position on the screen
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseClicked(int screenX, int screenY, int mouseButton);
	
	/**
	 * When mouse has been released.
	 * @param mouseButton The number of the button like Buttons.LEFT
	 */
	public abstract void mouseReleased(int mouseButton);
	
	/**
	 * When a key was pressed.
	 * @param keyCode Key number from Gdx.Input
	 */
	public void keyPressed(int keyCode)
	{
		if(keyCode == Keys.ESCAPE && _inGameMenuWindow == null) // Escape pressed and NO ingame window yet
		{
			createMenuWindow();
		}
		keyDown(keyCode);
	}
	
	/**
	 * When a key was pressed AND it has been checked weather the ESC key for the ingame menu window has been pressed.
	 * @param keyCode Key number from Gdx.Input
	 */
	public abstract void keyDown(int keyCode);
	
	/**
	 * Fires when user scrolls.
	 * @param amount Positive or negative amount of steps since last frame. 
	 */
	public abstract void mouseScrolled(int amount);
	
	/**
	 * Creates the in-game menu window with the yes/no option for exiting the game but provides a settings button as well.
	 */
	public void createMenuWindow()
	{
		if(_inGameMenuWindow == null) _inGameMenuWindow = new InGameMenuWindow();
	}
	
	/**
	 * Create a settings menu with some options to configure METRO.
	 */
	public void createSettingsWindow()
	{
		if(_settingsWindow == null) _settingsWindow = new SettingsWindow();
	}

	/**
	 * This is the settings window, that provides some options to configure.
	 * @author hauke
	 *
	 */
	public class SettingsWindow
	{
		private Window _window;
		private Button _okButton;
		
		/**
		 * Creates a settings window.
		 */
		public SettingsWindow()
		{
			_window = new Window("METRO settings",new Point(METRO.__SCREEN_SIZE.width / 2 - 250, 
				METRO.__SCREEN_SIZE.height / 2 - 225), new Point(500, 450), METRO.__metroBlue);
			
			new Label("To make things easier, you don't need to click on \"save\" everythin will be saved in realtime by just changing settings.", new Point(20, 20), 460, _window);
			
			_okButton = new Button(new Rectangle(200, 420, 100, 20), "OK", _window);
		}
		
		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			if(_window.isClosed()) close();
			
			if(_okButton.isPressed())
			{
				close();
			}
		}
		
		/**
		 * Just closes the window and sets the _settingsWindow to null.
		 */
		private void close()
		{
			_window.close();
			_settingsWindow = null;
		}
	}
	
	/**
	 * This is the menu that'll appear when ESC is pressed during the game. It provides exit options and a settings button.
	 * @author hauke
	 *
	 */
	public class InGameMenuWindow
	{
		private Window _window;
		private Button _yesButton,
			_noButton,
			_settingsButton;
		
		/**
		 * Creates a new exit game window with a settings button.
		 */
		public InGameMenuWindow()
		{
			_window = new Window("Really quit?",new Point(METRO.__SCREEN_SIZE.width / 2 - 200, 
					METRO.__SCREEN_SIZE.height / 2 - 50), new Point(400, 100), METRO.__metroRed);
			_yesButton = new metro.WindowControls.Button(new Rectangle(10, 70, 120, 20), "Yes", _window);
			_settingsButton = new metro.WindowControls.Button(new Rectangle(140, 70, 120, 20), "Settings", _window);
			_noButton = new metro.WindowControls.Button(new Rectangle(270, 70, 120, 20), "No", _window);
			new metro.WindowControls.Label("Really quit METRO? Or go into settings?", new Point(200 - (Draw.getStringSize("Really quit METRO? Or go into settings?").width)/2, 25), _window);
		}
		
		/**
		 * Updates everything and handles clicks.
		 */
		public void update()
		{
			if(_window.isClosed()) close();
			
			if(_yesButton.isPressed())
			{
				METRO.__application.exit();
			}
			else if(_settingsButton.isPressed())
			{
				if(_settingsWindow == null)
				{
					_settingsWindow = new SettingsWindow();
					close();
				}
				else
				{
					METRO.__windowList.remove(_settingsWindow._window);
					METRO.__windowList.add(_settingsWindow._window);
				}
			}
			else if(_noButton.isPressed())
			{
				close();
			}
		}

		/**
		 * Just closes the window and sets the _inGameMenuWindow to null.
		 */
		private void close()
		{
			_window.close();
			_inGameMenuWindow = null;
		}
	}
}
