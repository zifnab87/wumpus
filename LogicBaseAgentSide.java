/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.oologic.jpx.JpxClause;
import com.oologic.jpx.JpxException;
import com.oologic.jpx.JpxKnowledgeBase;
import com.oologic.jpx.JpxParseException;
import com.oologic.jpx.JpxQuery;
import com.oologic.jpx.JpxRoot;
import com.oologic.jpx.JpxSolutionSet;

public class LogicBaseAgentSide { // game rules => agent
	JpxKnowledgeBase logicbaseagentside;

	public LogicBaseAgentSide() {
		try {
			logicbaseagentside = JpxRoot.createKnowledgeBase(false, false);
			initiate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initiate() throws JpxParseException {
		String[] predicates = { "visited(int, int)", "pit(int,int)",
				"breezeLeft(int,int)", "breezeRight(int,int)",
				"breezeUp(int,int)", "breezeDown(int,int)",
				"smellLeft(int,int)", "smellRight(int,int)",
				"smellUp(int,int)", "smellDown(int,int)", "noWumpus(int,int)",
				"noPit(int,int)", "safe(int,int)", "wumpus(int,int)",
				"agentInitPos(int,int)", "breeze(int,int)", "smell(int,int)",
				"adjacent(int,int)", "gold(int,int)", "wumpusFound(int,int)",
				"deadWumpus(int,int)" };

		for (int i = 0; i < predicates.length; i++) {
			try {
				logicbaseagentside.addPredicate(predicates[i]);
			} catch (JpxException e) {
				e.printStackTrace();
			}
		}

		addClausesFromFile("game_rules.txt");

		Position pos = getAgentInitPos();
		logicbaseagentside.addClause("visited(" + pos.y + "," + pos.x + ").");
		logicbaseagentside.addClause("breeze(Y,X) :- fail.");
		logicbaseagentside.addClause("smell(Y,X) :- fail.");
		logicbaseagentside.addClause("wumpus(Y,X) :- fail.");
		logicbaseagentside.addClause("pit(Y,X) :- fail.");
		logicbaseagentside.addClause("deadWumpus(Y,X) :- fail.");
	}

	public void addClausesFromFile(String filename) {
		File file = new File(filename);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStreamReader strreader = null;
		try {
			strreader = new InputStreamReader(fis, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader bufreader = new BufferedReader(strreader);
		String line = null;
		try {
			while (true) {
				line = bufreader.readLine();

				if (line == null)
					break;
				if (!line.trim().equals("") && !line.startsWith("%"))
					logicbaseagentside.addClause(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addVisited(Position pos) {
		addVisited(pos.y, pos.x);
	}

	public void addVisited(int j, int i) {
		try {
			JpxQuery q = logicbaseagentside.createQuery("visited(" + j + ","
					+ i + ").");
			JpxSolutionSet ss = q.solve();
			if (!ss.querySolved()) {
				logicbaseagentside.addClause("visited(" + j + "," + i + ").");
			}
		} catch (JpxParseException e1) {
			System.out.println("Cannot create query \"visited\"");
			e1.printStackTrace();
		}
	}

	public Position getAgentInitPos() {
		Position pos = null;
		JpxQuery q = null;
		try {
			doublefor: {
				for (int j = 0; j < 8; j++) {
					for (int i = 0; i < 8; i++) {
						q = logicbaseagentside.createQuery("agentInitPos(" + j
								+ "," + i + ")");
						JpxSolutionSet ss = q.solve();
						if (ss.querySolved()) {
							pos = new Position(j, i);
							break doublefor;
						}
					}
				}
			}
			if (pos == null) {		
				return new Position(0, 0);
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get agent initialization position");
			e.printStackTrace();
		}
		return pos;

	}

	public Vector<Position> getVisitedPositions() {
		Vector<Position> v = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbaseagentside.createQuery("visited(" + j + "," + i
							+ ")");

					JpxSolutionSet ss = q.solve();
					if (ss.querySolved()) {
						v.add(new Position(j, i));
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with visited rooms");
			e.printStackTrace();
		}
		return v;
	}

	public Vector<Position> getUnvisitedPositions() {
		Vector<Position> v = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbaseagentside.createQuery("visited(" + j + "," + i
							+ ")");

					JpxSolutionSet ss = q.solve();
					if (!ss.querySolved()) {
						v.add(new Position(j, i));
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with visited rooms");
			e.printStackTrace();
		}
		return v;
	}

	public Vector<Position> getSafePositions() {
		Vector<Position> v = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbaseagentside.createQuery("safe(" + j + "," + i
							+ ")");
					JpxSolutionSet ss = q.solve();
					if (ss.querySolved()) {
						v.add(new Position(j, i));
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with safe rooms");
			e.printStackTrace();
		}
		return v;
	}

	public JpxClause[] getClauses() {
		return logicbaseagentside.getClauses();
	}

	public Position wumpusLocated() {

		Position pos = null;
		JpxQuery q = null;
		try {
			doublefor: {
				for (int j = 0; j < 8; j++) {
					for (int i = 0; i < 8; i++) {
						q = logicbaseagentside.createQuery("wumpusFound(" + j
								+ "," + i + ")");
						JpxSolutionSet ss = q.solve();
						if (ss.querySolved()) {
							pos = new Position(j, i);
							break doublefor;
						}
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with safe rooms");
			e.printStackTrace();
		}
		return pos;
	}

	public void addDeadWumpus(Position pos) {
		try {
			logicbaseagentside.addClause("deadWumpus(" + pos.y + "," + pos.x
					+ ").");
			logicbaseagentside.addClause("deadWumpus(Y,X) :- true.");
			logicbaseagentside.addClause("noPit(" + pos.y + "," + pos.x
					+ ").");

		} catch (JpxParseException e) {
			System.out.println("Cannot add deadWumpus position " + pos);
			e.printStackTrace();
		}
	}

}