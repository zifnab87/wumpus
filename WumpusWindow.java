/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WumpusWindow extends JFrame {
	JPanel board;
	bottomPanel bottom;
	Room[][] roomTable;
	Agent agent;
	LogicBaseGameSide logicbasegameside;
	LogicBaseAgentSide logicbaseagentside;
	MP3 mp3;

	public WumpusWindow() {
		gameInit();
		new infoWindow(this, "Infos");

	}

	public void sound() {
		if (mp3 != null)
			mp3.close();
		mp3 = new MP3("hal.mp3");
		mp3.play();
		MP3 door = new MP3("creakydoor.mp3");
		door.play();
	}

	public void gameInit() {
		sound();
		logicbasegameside = new LogicBaseGameSide();
		logicbaseagentside = new LogicBaseAgentSide();

		setLayout(new BorderLayout());
		board = createNewBoard();

		if (bottom == null) {
			bottom = new bottomPanel(this);
		}
		add(bottom, BorderLayout.SOUTH);

		setBounds(150, 50, 503, 572);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Wumpus - Artificial Intelligence 2");
		// setResizable(false);
		setVisible(true);
	}

	public JPanel createNewBoard() {
		if (board != null) {
			remove(board);
		}
		if (agent != null && agent.agentPanel != null) {
			remove(agent.agentPanel);
			remove(agent.arrowPanel);
		}

		roomTable = new Room[8][8];
		GridLayout grid = new GridLayout(8, 8);
		grid.setHgap(1);
		grid.setVgap(1);
		grid.setColumns(8);
		grid.setRows(8);
		board = new JPanel();
		board.setLayout(grid);

		for (int j = 0; j < 8; j++) { // gemisma me tetragwna
			for (int i = 0; i < 8; i++) {
				roomTable[j][i] = new Room(this, j, i);
				board.add(roomTable[j][i]);
			}
		}
		agent = new Agent(this);
		getRoom(agent.pos.y, agent.pos.x).changeState(); // an tin afiname
															// mesa ston agent
															// meta to visited
															// xtypage NULL
		// Put Information in Room for Wumpus to LogicBaseGameSide
		Room roomwumpus = getRoom(logicbasegameside.getWumpusPosition().y,
				logicbasegameside.getWumpusPosition().x);
		roomwumpus.setWumpus(true);
		// Put Information in Rooms for Pit according to LogicBaseGameSide
		Vector<Position> pitpositions = logicbasegameside.getPitPositions();
		for (int i = 0; i < pitpositions.size(); i++) {
			Room roompit = getRoom(pitpositions.get(i).y, pitpositions.get(i).x);
			roompit.setPit(true);
		}
		// Put Information in Room for Gold according to LogicBaseGameSide
		Room roomgold = getRoom(logicbasegameside.getGoldPosition().y,
				logicbasegameside.getGoldPosition().x);
		roomgold.setGold(true);
		board.setSize(491, 471);
		board.setLocation(0, 0);
		// Put Information in Rooms for Breeze according to LogicBaseGameSide
		Vector<Position> breezepositions = logicbasegameside
				.getBreezePositions();
		for (int i = 0; i < breezepositions.size(); i++) {
			Room breezeroom = getRoom(breezepositions.get(i).y, breezepositions
					.get(i).x);
			breezeroom.setBreeze(true);
		}
		// Put Information in Rooms for Smell according to LogicBaseGameSide
		Vector<Position> smellpositions = logicbasegameside.getSmellPositions();
		for (int i = 0; i < smellpositions.size(); i++) {
			Room smellroom = getRoom(smellpositions.get(i).y, smellpositions
					.get(i).x);
			smellroom.setSmell(true);
		}
		// Put Information in Rooms for Visited according to LogicBaseAgentSide

		add(agent.arrowPanel, BorderLayout.CENTER);
		add(agent.agentPanel, BorderLayout.CENTER);
		add(board, BorderLayout.CENTER);
		return board;
	}

	JPanel getAgentPanel() {
		return agent.agentPanel;
	}

	JPanel getBoard() {
		return board;
	}

	void reveal() {
		common();
		repaint();
	}

	void obscure() {

		common();
		repaint();
	}

	void common() {
		Component[] components = board.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass() == Room.class) {
				if( !(((Room) components[i]).isWumpus() && agent.killedWumpus) )
				((Room) components[i]).changeState();
			}
		}
	}

	public Room getRoom(int j, int i) {
		return roomTable[j][i];
	}

	public boolean onBreeze() {
		return getRoom(agent.pos.y, agent.pos.x).isBreeze();
	}

	public boolean onSmell() {
		return getRoom(agent.pos.y, agent.pos.x).isSmell();
	}

	public boolean onGold() {
		return getRoom(agent.pos.y, agent.pos.x).isGold();
	}
	

}
