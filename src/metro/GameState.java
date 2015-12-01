package metro;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Trains.Train;
import metro.TrainManagement.Trains.TrainStation;
import metro.TrainManagement.Trains.TrainTemplate;

/**
 * A game state is the place where all game relevant information are stored.
 * There can only be one instance of the game state accessible by instance().
 * This class uses the singleton pattern to be sure that there's only be one instance at all.
 * 
 * @author hauke
 *
 */
public class GameState
{
	private static final GameState __INSTANCE = new GameState();

	private int _money;
	private ArrayList<TrainStation> _stationList;
	private ArrayList<TrainLine> _lineList;
	private ArrayList<Train> _trainList;
	private LinkedHashMap<String, TrainTemplate> _templateTrains;

	/**
	 * Creates a new game state. This can't be done by an external class.
	 */
	private GameState()
	{
		_money = 500000;
		_stationList = new ArrayList<>();
		_lineList = new ArrayList<>();
		_trainList = new ArrayList<>();
		_templateTrains = new LinkedHashMap<>();
		try
		{
			createTrains();
		}
		catch(IOException e)
		{
			METRO.__debug("[TrainReadError]\nCan't read train.txt due to an error: " + e.getMessage());
		}
		catch(IllegalArgumentException e)
		{
			METRO.__debug("[TrainFileSyntaxError]\n" + e.getMessage());
		}
	}

	private void createTrains() throws IOException, IllegalArgumentException
	{
		List<String> lines = Files.readAllLines((new File("data/trains.txt")).toPath(), Charset.defaultCharset());

		Pattern comment = Pattern.compile("\\s#\\w"),
			empty = Pattern.compile("\\s");

		String name = "",
			manufacturer = "",
			price = "",
			costs = "",
			costsfactor = "",
			passenger = "";

		for(String line : lines)
		{
			if(!comment.matcher(line).matches() && !empty.matcher(line).matches())
			{
				String[] lineSplit = line.split(":");
				if(lineSplit.length > 2) throw new IllegalArgumentException("Each property must contain exactly one \":\" symbol!");
				switch(lineSplit[0])
				{
					case "NAME":
						if(!name.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"NAME\" exists multiple times in the property " + name + "!");
						name = lineSplit[1];
						break;
					case "MANUFACTURER":
						if(!manufacturer.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"MANUFACTURER\" exists multiple times in the property " + name + "!");
						manufacturer = lineSplit[1];
						break;
					case "PRICE":
						if(!price.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"PRICE\" exists multiple times in the property " + name + "!");
						price = lineSplit[1];
						break;
					case "COSTS":
						if(!costs.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"COSTS\" exists multiple times in the property " + name + "!");
						costs = lineSplit[1];
						break;
					case "COSTSFACTOR":
						if(!costsfactor.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"COSTFACTOR\" exists multiple times in the property " + name + "!");
						costsfactor = lineSplit[1];
						break;
					case "PASSENGER":
						if(!passenger.equals("")) throw new IllegalArgumentException(
							"Each property has to have exactly one key of each. In this case \"PASSENGER\" exists multiple times in the property " + name + "!");
						passenger = lineSplit[1];
						break;
				}

				// when the read data is complete, create train and put it into the map and then reset all variables
				if(!name.equals("")
					&& !manufacturer.equals("")
					&& !price.equals("")
					&& !costs.equals("")
					&& !costsfactor.equals("")
					&& !passenger.equals(""))
				{
					METRO.__debug("[SuccesfullTrainRead]\n" +
						name + "\n" +
						manufacturer + "\n" +
						Integer.parseInt(price) + "\n" +
						Integer.parseInt(costs) + "\n" +
						Float.parseFloat(costsfactor) + "\n" +
						Integer.parseInt(passenger));
					_templateTrains.put(name, new TrainTemplate(name,
						manufacturer,
						Integer.parseInt(price),
						Integer.parseInt(costs),
						Float.parseFloat(costsfactor),
						Integer.parseInt(passenger)));
					name = "";
					manufacturer = "";
					price = "";
					costs = "";
					costsfactor = "";
					passenger = "";
				}
			}
		}
	}

	/**
	 * @return The instance of the game state. There can only be one instance per game.
	 */
	public static GameState getInstance()
	{
		return __INSTANCE;
	}

	/**
	 * Gets the money of the player.
	 * 
	 * @return The players money.
	 */
	public int getMoney()
	{
		return _money;
	}

	/**
	 * Add an specific amount of money to the players account. Giving a negative amount will subtract money from the account.
	 * 
	 * @param moreMoney The money to add/subtract.
	 */
	public void addMoney(int moreMoney)
	{
		_money += moreMoney;
	}

	/**
	 * @return A list of all train stations.
	 */
	public ArrayList<TrainStation> getStations()
	{
		return _stationList;
	}

	/**
	 * @return A list of all train lines.
	 */
	public ArrayList<TrainLine> getLines()
	{
		return _lineList;
	}

	/**
	 * @return A list of all trains.
	 */
	public ArrayList<Train> getTrains()
	{
		return _trainList;
	}

	/**
	 * Gets a train template by giving its model name.
	 * 
	 * @param name The model name of the train.
	 * @return A new train template object.
	 */
	public TrainTemplate getTemplateTrain(String name)
	{
		return _templateTrains.get(name);
	}

	/**
	 * @return A list with all available trains. This does NOT mean that the player already bought them, they are just available for him.
	 */
	public ArrayList<TrainTemplate> getTemplateTrains()
	{
		return new ArrayList<TrainTemplate>(_templateTrains.values());
	}

	/**
	 * Gets the train line with the given name. Thats why every line has to have an unique name ;)
	 * 
	 * @param name The name of the train line.
	 * @return The train line with the given name.
	 */
	public TrainLine getLine(String name)
	{
		TrainLine line = null;
		for(TrainLine l : _lineList)
		{
			if(l.getName().equals(name))
			{
				line = l;
				break;
			}
		}
		return line;
	}
}
