package metro.TrainManagement.Lines;

import java.util.ArrayList;

/**
 * The TrainLineOverseer knows all train lines and with this overseer you can add, remove and change a train line.
 * 
 * @author hauke
 *
 */
public class TrainLineOverseer
{
	private static ArrayList<TrainLine> _listOfTrainLines = new ArrayList<TrainLine>();

	/**
	 * Adds a train line to the list of lines. An older version of this line will be removed.
	 * 
	 * @param line The line to add.
	 */
	public static void addLine(TrainLine line)
	{
		_listOfTrainLines.remove(line); // remove old line, because maybe line.equals(old-line) == true
		_listOfTrainLines.add(line); // adds the new line to the list
	}

	/**
	 * Removes a line from the list.
	 * 
	 * @param line The line to remove.
	 */
	public static void removeLine(TrainLine line)
	{
		line.clear();
		_listOfTrainLines.remove(line);
	}

	/**
	 * Removes a line including all containing colors and settings.
	 * 
	 * @param lineName The name of the line.
	 */
	public static void removeLine(String lineName)
	{
		for(TrainLine line : _listOfTrainLines)
		{
			if(line.getName().equals(lineName))
			{
				removeLine(line);
				return;
			}
		}
	}
}
