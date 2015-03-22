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

import com.oologic.jpx.JpxException;
import com.oologic.jpx.JpxKnowledgeBase;
import com.oologic.jpx.JpxParseException;
import com.oologic.jpx.JpxQuery;
import com.oologic.jpx.JpxRoot;
import com.oologic.jpx.JpxSolutionSet;

public class LogicBaseGameSide { // game rules + world configuration => game
	// init
	JpxKnowledgeBase logicbasegameside;

	public LogicBaseGameSide() {
		try {
			logicbasegameside = JpxRoot.createKnowledgeBase(false, false);
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
				logicbasegameside.addPredicate(predicates[i]);
			} catch (JpxException e) {
				e.printStackTrace();
			}
		}
		addClausesFromFile("world_configuration.txt");
		addClausesFromFile("game_rules.txt");

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
					logicbasegameside.addClause(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Position getAgentInitPos() {
		Position pos = null;
		JpxQuery q = null;
		try {
			doublefor: {
				for (int j = 0; j < 8; j++) {
					for (int i = 0; i < 8; i++) {
						q = logicbasegameside.createQuery("agentInitPos(" + j
								+ "," + i + ")");
						JpxSolutionSet ss = q.solve();
						if (ss.querySolved()) {
							pos = new Position(j, i);
							break doublefor;
						}
					}
					if (pos == null) {
						return new Position(0, 0);
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with safe rooms");
			e.printStackTrace();
		} 
		return pos;

	}

	public Position getWumpusPosition() {
		Position pos = null;
		JpxQuery q = null;
		try {
			doublefor: {
				for (int j = 0; j < 8; j++) {
					for (int i = 0; i < 8; i++) {
						q = logicbasegameside.createQuery("wumpus(" + j + ","
								+ i + ")");
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

	public Vector<Position> getPitPositions() {
		Vector<Position> vec = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbasegameside.createQuery("pit(" + j + "," + i
							+ ")");
					JpxSolutionSet ss = q.solve();
					if (ss.querySolved()) {
						vec.add(new Position(j, i));
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with safe rooms");
			e.printStackTrace();
		}
		return vec;
	}

	public Vector<Position> getSmellPositions() {
		Vector<Position> v = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbasegameside.createQuery("smell(" + j + "," + i
							+ ")");
					JpxSolutionSet ss = q.solve();
					if (ss.querySolved())
						v.add(new Position(j, i));
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get vector with safe rooms");
			e.printStackTrace();
		}
		return v;
	}

	public Vector<Position> getBreezePositions() {
		Vector<Position> v = new Vector<Position>();
		JpxQuery q = null;
		try {
			for (int j = 0; j < 8; j++) {
				for (int i = 0; i < 8; i++) {
					q = logicbasegameside.createQuery("breeze(" + j + "," + i
							+ ")");
					JpxSolutionSet ss = q.solve();
					if (ss.querySolved())
						v.add(new Position(j, i));
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get breezy room");
			e.printStackTrace();
		}
		return v;
	}

	public Position getGoldPosition() {
		Position pos = null;
		JpxQuery q = null;
		try {
			doublefor: {
				for (int j = 0; j < 8; j++) {
					for (int i = 0; i < 8; i++) {
						q = logicbasegameside.createQuery("gold(" + j + "," + i
								+ ")");
						JpxSolutionSet ss = q.solve();
						if (ss.querySolved()) {
							pos = new Position(j, i);
							break doublefor;
						}
					}
				}
			}
		} catch (JpxParseException e) {
			System.out.println("Cannot get golden room");
			e.printStackTrace();
		}
		return pos;
	}

}