/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.util.Stack;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.oologic.jpx.JpxParseException;
import com.oologic.jpx.JpxQuery;
import com.oologic.jpx.JpxSolutionSet;

public class Agent {
	Position pos;
	final static String heading = "right";
	JPanel agentPanel;
	WumpusWindow parent;
	int[][] distanceTable = new int[8][8];
	Vector<Position> visited;
	public static ImageIcon girl_up = new ImageIcon("girl_up.gif");
	public static ImageIcon girl_down = new ImageIcon("girl_down.gif");
	public static ImageIcon girl_left = new ImageIcon("girl_left.gif");
	public static ImageIcon girl_right = new ImageIcon("girl_right.gif");
	public static ImageIcon girl_up_still = new ImageIcon("girl_up_still.gif");
	public static ImageIcon girl_down_still = new ImageIcon(
			"girl_down_still.gif");
	public static ImageIcon girl_left_still = new ImageIcon(
			"girl_left_still.gif");
	public static ImageIcon girl_right_still = new ImageIcon(
			"girl_right_still.gif");
	public static ImageIcon girl_die = new ImageIcon("girl_die_up.gif");
	JLabel agentIcon;
	LogicBaseAgentSide logicbaseagentside;
	LogicBaseGameSide logicbasegameside;
	Stack<Position> path;
	Vector<Position> safePos;
	boolean firstMove = true;
	boolean killedWumpus;
	boolean foundGold;
	boolean gameOver;
	boolean afterWumpusKill;

	JPanel arrowPanel;
	JLabel arrow;

	Agent(WumpusWindow parent) {
		safePos = new Vector<Position>();
		this.parent = parent;
		agentPanel = new JPanel(); // to panel pou kineitai o agent
		agentIcon = new JLabel(girl_right_still); // to be still!
		agentIcon.setLocation(0, 0);
		agentPanel.add(agentIcon);
		logicbaseagentside = parent.logicbaseagentside;
		logicbasegameside = parent.logicbasegameside;
		pos = logicbaseagentside.getAgentInitPos();
		visited = new Vector<Position>();
		visited.add(pos);
		path = new Stack<Position>();

		agentPanel.setBounds((pos.y) * 60, -6 + (pos.x) * 60, 60, 60);
		agentPanel.setOpaque(false);
		gameOver = false;
		foundGold = false;
		killedWumpus = false;
		afterWumpusKill = false;

		arrowPanel = new JPanel(); // to panel pou kineitai to arrow
		arrow = new JLabel(new ImageIcon("arrow_right.gif"));

	}

	public JPanel getAgentPanel() {
		return agentPanel;
	}

	void left() {

		agentIcon.setIcon(girl_left);

		for (int i = 0; i < 61; i++) {
			getAgentPanel().setLocation(getAgentPanel().getLocation().x - 1,
					getAgentPanel().getLocation().y);

			agentPanel.revalidate();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		pos.x--;
		logicbaseagentside.addVisited(pos);
		visited.add(pos);
		parent.getRoom(pos.y, pos.x).changeState();
		fixDistanceTable();
		agentIcon.setIcon(girl_left_still);
		checkMove(pos);

	}

	void right() {
		agentIcon.setIcon(girl_right);
		for (int i = 0; i < 61; i++) {
			getAgentPanel().setLocation(getAgentPanel().getLocation().x + 1,
					getAgentPanel().getLocation().y);

			agentPanel.revalidate();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		pos.x++;
		logicbaseagentside.addVisited(pos);
		visited.add(pos);
		parent.getRoom(pos.y, pos.x).changeState();
		fixDistanceTable();
		agentIcon.setIcon(girl_right_still);
		checkMove(pos);

	}

	void up() {
		agentIcon.setIcon(girl_up);

		for (int i = 0; i < 61; i++) {
			getAgentPanel().setLocation(getAgentPanel().getLocation().x,
					getAgentPanel().getLocation().y - 1);

			agentPanel.revalidate();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		pos.y--;
		logicbaseagentside.addVisited(pos);
		visited.add(pos);
		parent.getRoom(pos.y, pos.x).changeState();
		fixDistanceTable();
		agentIcon.setIcon(girl_up_still);
		checkMove(pos);
	}

	void down() {

		agentIcon.setIcon(girl_down);
			for (int i = 0; i < 61; i++) {
			getAgentPanel().setLocation(getAgentPanel().getLocation().x,
					getAgentPanel().getLocation().y + 1);

			agentPanel.revalidate();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		pos.y++;
		logicbaseagentside.addVisited(pos);
		visited.add(pos);
		parent.getRoom(pos.y, pos.x).changeState();
		fixDistanceTable();
		agentIcon.setIcon(girl_down_still);
		checkMove(pos);

	}


	void checkMove(Position pos) {

		if (parent.roomTable[pos.y][pos.x].isPit()) {
			JOptionPane.showMessageDialog(null, "Agent fell in a pit.",
					"Game Over", JOptionPane.ERROR_MESSAGE);
			gameOver = true;
			agentIcon.setIcon(girl_die);
			MP3 scream = new MP3("girlscream.mp3");
			scream.play();
		}

		if (parent.roomTable[pos.y][pos.x].isWumpus() && !killedWumpus) {
			JOptionPane.showMessageDialog(null, "Wumpus killed the agent.",
					"Game Over", JOptionPane.ERROR_MESSAGE);
			gameOver = true;
			agentIcon.setIcon(girl_die);
			MP3 scream = new MP3("girlscream.mp3");
			scream.play();
		}

		if (parent.roomTable[pos.y][pos.x].isGold() && !foundGold) {
			MP3 mp3 = new MP3("golddrop.mp3");
			mp3.play();
			JOptionPane.showMessageDialog(null, "Agent found gold!",
					"Congratulations", JOptionPane.INFORMATION_MESSAGE);
			foundGold = true;
		}
		if ((foundGold) && (killedWumpus)) {
			JOptionPane.showMessageDialog(null,
					"Agent has found the gold and killed Wumpus!", "Victory",
					JOptionPane.INFORMATION_MESSAGE);
			gameOver = true;
		}
	}

	private Stack<Position> getShortestPath(Position target) {
		int a;
		Position nextPosition = new Position(-1, -1);
		Stack<Position> path = new Stack<Position>();
		int minDist = 100;

		if (this.pos.equals(target)) {
			return path;
		}
		if (distanceTable[target.y][target.x] == -1) {
			return path;
		}
		Vector<Position> nearPositions = target.getNearPositions();
		for (int i = 0; i < nearPositions.size(); i++) {
			a = distanceTable[nearPositions.get(i).y][nearPositions.get(i).x];
			if (a > -1) {// An to room einai visited
				if (a < minDist) {
					minDist = a;
					nextPosition = nearPositions.get(i);// Pairnoume to room me
					// tin mikroteri
					// apostasi
				}

			}
		}

		if (minDist == 100)
			System.out.println("No safe path!");
		else {
			path.push(target);
			if (!nextPosition.equals(this.pos)) {
				path.push(nextPosition);
			}
			while (minDist > 1) {// Loop mexri na ftasei sto room pou einai o
				// agent
				nearPositions = nextPosition.getNearPositions();
				for (int i = 0; i < nearPositions.size(); i++) {
					a = distanceTable[nearPositions.get(i).y][nearPositions
							.get(i).x];
					if (a == minDist - 1) {// Psaxnei to room me tin apostasi
						// tou proigoumenou -1
						minDist = a;
						nextPosition = nearPositions.get(i);
						path.push(nextPosition);
					}
				}

			}// while
		}// else
		return path;
	}

	public void printPath(Stack<Position> path) {
		System.out.print("Path " + path.size() + ": ");
		System.out.println(path);
	}

	public void fixDistanceTable() {
		visited = logicbaseagentside.getVisitedPositions();
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				distanceTable[j][i] = -1;
			}
		}
		distanceTable[pos.y][pos.x] = 0;

		updateDistances(pos);
	}

	public void printDistanceTable() {
		for (int j = 0; j < 8; j++) {
			for (int i = 0; i < 8; i++) {
				if (distanceTable[j][i] >= 0 && distanceTable[j][i] <= 9)
					System.out.print(distanceTable[j][i] + " |");
				else
					System.out.print(distanceTable[j][i] + "|");
			}
			System.out.println();
		}
	}

	private void updateDistances(Position center) {
		for (Position neighbor : center.getNearPositions()) {

			if ((distanceTable[neighbor.y][neighbor.x] == -1 || distanceTable[neighbor.y][neighbor.x] > distanceTable[center.y][center.x] + 1)
					&& visited.contains(neighbor)) {
				distanceTable[neighbor.y][neighbor.x] = distanceTable[center.y][center.x] + 1;

				updateDistances(neighbor);
			}
		}

	}

	public String toString() {
		return "Agent(" + pos.y + "," + pos.x + ")";
	}

	public void play() {
		if (!killedWumpus) {
			killedWumpus = killWumpus();
			if (killedWumpus) {
				afterWumpusKill = true;
				checkMove(pos);
				return;
			}
		}

		if (path.isEmpty()) {
			if (firstMove || afterWumpusKill) {
				safePos = logicbaseagentside.getSafePositions();
				if (firstMove)
					firstMove = false;
				else
					afterWumpusKill = false;
			}

			if (safePos.isEmpty()) // An den uparxoun safe positions
			{
				JOptionPane
						.showMessageDialog(
								null,
								"No safe positions to go! Agent will go to nearest Room.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				Vector<Position> unvisitedPos = new Vector<Position>();
				unvisitedPos = logicbaseagentside.getUnvisitedPositions();
				getNearestPath(unvisitedPos);
			}

			else { // An uparxoun safe positions
				getNearestPath(safePos);
			}
		}

		moveOnPath();
		try {
			if (parent.onBreeze()) {
				logicbaseagentside.logicbaseagentside.addClause("breeze("
						+ pos.y + "," + pos.x + ").");
			}
			if (parent.onSmell()) {
				logicbaseagentside.logicbaseagentside.addClause("smell("
						+ pos.y + "," + pos.x + ").");
			}
			if (parent.onGold()) {
				logicbaseagentside.logicbaseagentside.addClause("gold(" + pos.y
						+ "," + pos.x + ").");
			}
		} catch (JpxParseException e) {
			System.out.println("Error while agent plays");
			e.printStackTrace();
		}
		safePos = logicbaseagentside.getSafePositions();
	}

	private void getNearestPath(Vector<Position> myVector) {
		Position nearest = null;
		int minDistance = 100;
		Position nearestPos = null;
		for (Position myPos : myVector)  { // Gia ka8e Unvisited position
											// psaxnw
			// to kontinotero (adjacent)
			// visited se emas
			fixDistanceTable();
			for (Position adj : myPos.getNearPositions()) {
				if (distanceTable[adj.y][adj.x] < minDistance
						&& distanceTable[adj.y][adj.x] > -1) {
					minDistance = distanceTable[adj.y][adj.x];
					nearest = adj;
					nearestPos = myPos;
				}
			}
		}
		if (nearest.equals(pos)) {
			path.push(nearestPos);
		} else {
			path = getShortestPath(nearest);
		}
	}

	private void moveOnPath() {
		Position next = path.pop();
		if (next.x == pos.x + 1)
			right();
		else if (next.x == pos.x - 1)
			left();
		else if (next.y == pos.y + 1)
			down();
		else
			up();
	}

	private boolean killWumpus() {
		Position wumpusPos = logicbaseagentside.wumpusLocated();
		if (wumpusPos == null)
			return false;
		else {
			killedWumpus = true;
			if (wumpusPos.x == pos.x + 1) {
				agentIcon.setIcon(girl_right_still);
				shootArrow(1, 0);
			} else if (wumpusPos.x == pos.x - 1) {
				agentIcon.setIcon(girl_left_still);
				shootArrow(-1, 0);
			} else if (wumpusPos.y == pos.y - 1) {
				agentIcon.setIcon(girl_up_still);
				shootArrow(0, -1);
			} else if (wumpusPos.y == pos.y + 1) {
				agentIcon.setIcon(girl_down_still);
				shootArrow(0, 1);
			}
			MP3 mp3 = new MP3("scream.mp3");
			mp3.play();
			logicbaseagentside.addDeadWumpus(wumpusPos);
			parent.getRoom(wumpusPos.y, wumpusPos.x).revealedState();
			parent.repaint();

			JOptionPane.showMessageDialog(null, "Agent killed Wumpus!",
					"Congratulations", JOptionPane.INFORMATION_MESSAGE);

			return true;
		}
	}

	private void shootArrow(int right, int up) {

		if (right == 1) {
			arrow = new JLabel(new ImageIcon("arrow_right.gif"));
		} else if (right == -1) {
			arrow = new JLabel(new ImageIcon("arrow_left.gif"));
		} else if (up == 1) {
			arrow = new JLabel(new ImageIcon("arrow_down.gif"));
		} else if (up == -1) {
			arrow = new JLabel(new ImageIcon("arrow_up.gif"));
		}
		MP3 mp3 = new MP3("fireball.mp3");
		mp3.play();
		arrowPanel.add(arrow);
		arrowPanel.setBounds(agentPanel.getLocation().x, agentPanel
				.getLocation().y, 60, 60);
		arrowPanel.setOpaque(false);

		for (int i = 0; i < 61; i++) {
			arrowPanel.setLocation(arrowPanel.getLocation().x + right,
					arrowPanel.getLocation().y + up);

			agentPanel.revalidate();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		arrowPanel.remove(arrowPanel.getComponent(0));
		mp3.close();
	}

}
