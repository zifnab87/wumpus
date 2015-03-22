/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.util.Vector;

public class Position {
	int x;
	int y;

	Position(int y, int x) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "(" + y + "," + x + ")";
	}

	public Vector<Position> getNearPositions() {
		Vector<Position> v = new Vector<Position>();
		if (x != 0)
			v.add(new Position(y, x - 1));
		if (x != 7)
			v.add(new Position(y, x + 1));
		if (y != 0)
			v.add(new Position(y - 1, x));
		if (y != 7)
			v.add(new Position(y + 1, x));

		return v;
	}

	public boolean isEqual(Position target) {
		if ((this.x == target.x) && (this.y == target.y))
			return true;
		else
			return false;
	}

	public boolean equals(Object right) {
		if (right.getClass() != Position.class)
			return false;
		if (((Position) right).x == x && ((Position) right).y == y) {
			return true;
		}
		return false;
	}
}
