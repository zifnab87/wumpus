/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.awt.Color;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.oologic.jpx.JpxClause;

public class KnowledgeBaseWindow extends JTextPane {
	WumpusWindow parent;
	Vector<String> lines;
	StyleContext context;
	StyledDocument document;
	Style common, newClause;

	public KnowledgeBaseWindow(WumpusWindow par) {
		lines = new Vector<String>();
		this.parent = par;
		setEditable(false);
	}

	public void populate() {
		boolean firstTime = false;
		if (lines.isEmpty()) {
			firstTime = true;
		}
		setEditable(true);

		selectAll();
		replaceSelection("");
		JpxClause[] clauses = parent.logicbaseagentside.getClauses();
		for (JpxClause cl : clauses) {
			if (firstTime || lines.contains(cl.toString())) {
				append(Color.BLACK, cl.toString());
				lines.add(cl.toString());
			} else if (!lines.contains(cl.toString())) {
				append(Color.RED, cl.toString());
				lines.add(cl.toString());
			}

		}

		for (Position safe : parent.logicbaseagentside.getSafePositions()) {
			String txt = new String("safe(" + safe.y + "," + safe.x + ").");
			if (firstTime || lines.contains(txt)) {
				append(Color.BLACK, txt);
				lines.add(txt);
			} else if (!lines.contains(txt)) {
				append(Color.RED, txt);
				lines.add(txt);
			}
		}
		setEditable(false);
	}

	public void append(Color c, String s) { 
		s = s + System.getProperty("line.separator");
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);

		int len = getDocument().getLength(); // same value as getText().length();
		setCaretPosition(len); // place caret at the end (with no selection)
		setCharacterAttributes(aset, false);
		replaceSelection(s); // there is no selection, so inserts at caret
	}
}
