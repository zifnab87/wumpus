/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Room extends JPanel {
	Position pos;
	private boolean wumpus, gold, pit, smell, breeze, visited;
	WumpusWindow parent;
	private boolean revealed;

	Room(WumpusWindow parent, int y, int x) {
		this.parent = parent;
		pos = new Position(y, x);
		setLayout(null);
		setBackgroundLabel(pickIcon());
		revealed = false;
	}

	private String pickIcon() {
		if (isWumpus()) {
			return "wumpus.gif";
		} else if (isGold()) {
			return "gold.jpg";
		} else if (isPit()) {
			return "pit.jpg";
		} else
			return "floor.jpg";
	}

	public boolean isVisited() {
		return visited;
	}

	public boolean isWumpus() {
		return wumpus;
	}

	public boolean isGold() {
		return gold;
	}

	public boolean isPit() {
		return pit;
	}

	public boolean isSmell() {
		return smell;
	}

	public boolean isBreeze() {
		return breeze;
	}

	public void setWumpus(boolean wumpus) {
		this.wumpus = wumpus;
	}

	public void setGold(boolean gold) {
		this.gold = gold;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}

	public void setSmell(boolean smell) {
		this.smell = smell;
	}

	public void setBreeze(boolean breeze) {
		this.breeze = breeze;
	}

	public String toString() {
		return "Room(" + pos.y + "," + pos.x + ")";
	}

	public void setBackgroundLabel(String image) {
		if (getComponentCount() > 0)
			remove(0);
		ImageIcon img = new ImageIcon(image);
		JLabel bgLbl = new JLabel(img);
		bgLbl.setBounds(0, 0, 60, 60);
		add(bgLbl);

	}

	public boolean equals(Object right) {
		if (right.getClass() != Room.class)
			return false;
		return (((Room) right).pos.x == this.pos.x && ((Room) right).pos.y == this.pos.y);

	}

	public void revealedState() {
		String tempiconname = "floor";
		if (isWumpus() && !parent.agent.killedWumpus)
			tempiconname += "_wumpus";
		else if (isWumpus() && parent.agent.killedWumpus) {
			tempiconname += "_wumpusdead";
		} else if (isPit())
			tempiconname += "_pit";
		if (!isWumpus() && !isPit()) {
			if (isGold())
				tempiconname += "_gold";
			if (isBreeze())
				tempiconname += "_breeze";
			if (isSmell())
				tempiconname += "_smell";
			if (!isWumpus() && !isPit() && !isBreeze() && !isSmell()
					&& !isGold())
				tempiconname = "floor_revealed";
		}
		if (tempiconname.equals("floor_wumpus"))
			tempiconname += ".gif";
		else
			tempiconname += ".jpg";

		setBackgroundLabel(tempiconname);
		revealed = true;
		repaint();

	}

	public void obscuredState() {
		setBackgroundLabel("floor.jpg");
		revealed = false;
	}

	public void changeState() {
		if (!parent.agent.visited.contains(new Position(pos.y, pos.x))
				&& revealed) { // not -visited
			obscuredState();

		} else
			revealedState();
	}

}
