package metro.GameScreen.LineView;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import metro.METRO;
import metro.GameScreen.GameScreen;
import metro.GameScreen.MainView.MainView;
import metro.TrainManagement.Lines.TrainLine;
import metro.TrainManagement.Nodes.RailwayNode;
import metro.TrainManagement.Nodes.RailwayNodeOverseer;

/**
 * The LineSelectTool allows the user to create train lines by clicking on all the nodes this line should contain.
 * It can also generate a new train line from all selected nodes.
 * 
 * @author hauke
 *
 */
public class LineSelectTool extends GameScreen
{
	private ArrayList<RailwayNode> _listOfNodes;
	private boolean _isActive;
	private Color _color;
	private String _lineName;

	/**
	 * Creates a new tool to select the train line.
	 */
	public LineSelectTool()
	{
		_listOfNodes = new ArrayList<RailwayNode>();
		_isActive = true;
		_color = METRO.__metroBlue;
		_lineName = "";
	}

	/**
	 * Creates a new tool to edit the given line.
	 * 
	 * @param line A line to edit.
	 */
	public LineSelectTool(TrainLine line)
	{
		_listOfNodes = line.getNodes();
		_isActive = true;
		_color = line.getColor();
		_lineName = line.getName();
	}

	/**
	 * Creates the TrainLine object for further using.
	 * This method WON'T return null under any circumstances.
	 * 
	 * @return The train line with sorted nodes as TrainLine object. It won't be null!
	 */
	public TrainLine getTrainLine()
	{
		@SuppressWarnings("unchecked") // _listOfNodes.clone can safely be converted into an array list (because listOfNodes is an array list)
		ArrayList<RailwayNode> newList = sortNodes((ArrayList<RailwayNode>)_listOfNodes.clone(), getAnyEndNode());
		return new TrainLine(newList, _lineName, _color);
	}

	/**
	 * Sorts the given list of nodes by choosing one start node an then the neighbors.
	 * 
	 * @param list The list of nodes that should be sorted.
	 * @param startNode An end node of the line.
	 * @return A sorted list with all the nodes.
	 */
	private ArrayList<RailwayNode> sortNodes(ArrayList<RailwayNode> list, RailwayNode startNode)
	{
		METRO.__debug("[TrainLineSorting]\nLine length (amount of nodes): " + list.size());
		if(list.size() <= 1) return list;
		ArrayList<RailwayNode> newList = new ArrayList<>();
		// be sure that an end node is the first element in this list
		newList.add(startNode);
		list.remove(startNode);

		// create a dummy node to enter toe first for-loop
		RailwayNode neighbor = new RailwayNode(null, null);
		int listLength = list.size();

		for(int i = 0; i < listLength && neighbor != null; ++i)
		{
			neighbor = null;
			int k; // the index of the neighbor for the node at index i
			for(k = 0; k < list.size() && neighbor == null; ++k)
			{
				if(newList.get(i).isNeighbor(list.get(k))) neighbor = list.get(k);
			}
			if(neighbor != null)
			{
				METRO.__debug(newList.get(i).getPosition() + "  ==>  " + neighbor.getPosition());
				newList.add(neighbor);
				list.remove(neighbor);
			}
		}

		return newList;
	}

	/**
	 * This method determines an end node of the given line.
	 * It's not specified which node of the two end nodes will be chosen, because the algorithm will take the first in the list of nodes.
	 * 
	 * @param line The line whose end node you want to know.
	 * @return One of the two end nodes of the given line.
	 */
	private RailwayNode getAnyEndNode()
	{
		for(RailwayNode node : _listOfNodes)
		{
			if(TrainLine.isEndNode(node, _listOfNodes)) return node;
		}

		return null;
	}

	/**
	 * Sets the color for this in-progress-line.
	 * 
	 * @param newColor The new color.
	 * @return A message if something went wrong.
	 */
	public String setColor(Color newColor)
	{
		// for(RailwayNode node : _listOfNodes)
		// {
		// try
		// {
		// node.changeColor(_color, newColor);
		// }
		// catch(IllegalArgumentException ex)
		// {
		// return ex.getMessage();
		// }
		// }

		_color = newColor;
		return "";
	}

	/**
	 * Sets a new name for this train line.
	 * 
	 * @param newName The new name.
	 */
	public void setName(String newName)
	{
		_lineName = newName;
	}

	/**
	 * Sets the current line to a new one.
	 * 
	 * @param line The new line.
	 */
	public void setLine(TrainLine line)
	{
		_listOfNodes = new ArrayList<RailwayNode>(line.getNodes());
		_color = line.getColor();
		_lineName = line.getName();
	}

	/**
	 * Enables or disables this tool. When enabled it works otherwise it won't do anything.
	 * 
	 * @param enabled True to enable, false to disable.
	 */
	public void setState(boolean enabled)
	{
		_isActive = enabled;
	}

	@Override
	public void mouseClicked(int screenX, int screenY, int mouseButton)
	{
	}

	/**
	 * Adds or removes a node at the clicked position (screenX, screenY).
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void leftClick(int screenX, int screenY, Point2D offset)
	{
		RailwayNode clickedNode = RailwayNodeOverseer.getNodeByPosition(MainView._selectedCross);
		if(clickedNode == null) return;
		// ArrayList<RailwayNode> neighbors = clickedNode.getNeighbors();

		// for(RailwayNode neighborNode : neighbors)
		// {
		// if(_listOfNodes.contains(neighborNode))
		// {
		// if(_listOfNodes.contains(clickedNode)) // if list contains node, remove it
		// {
		// neighborNode.removeColorTo(clickedNode, _color);
		// clickedNode.removeColorTo(neighborNode, _color);
		// }
		// else
		// {
		// neighborNode.addColorTo(clickedNode, _color);
		// clickedNode.addColorTo(neighborNode, _color);
		// }
		// }
		// }
		if(_listOfNodes.contains(clickedNode)) _listOfNodes.remove(clickedNode);
		else _listOfNodes.add(clickedNode);
	}

	/**
	 * Deactivates the tool to point out that it can be closed.
	 * 
	 * @param screenX The y-coordinate of the click.
	 * @param screenY The y-coordinate of the click.
	 * @param offset The current map offset.
	 */
	public void rightClick(int screenX, int screenY, Point2D offset)
	{
		_isActive = false;
	}

	@Override
	public void updateGameScreen(SpriteBatch g)
	{
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
	public void mouseScrolled(int amount)
	{
	}

	@Override
	public boolean isActive()
	{
		return _isActive;
	}

	@Override
	public void reset()
	{
	}
}
