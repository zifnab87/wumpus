/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class infoWindow extends JDialog {
	JPanel content = new JPanel();
	Box titlePane = Box.createVerticalBox();
	Box rulesPane = Box.createVerticalBox();
	Box gamePane = Box.createVerticalBox();
	JButton closeButton = new JButton("Close");
	ImageIcon infos = new ImageIcon("info.jpg");
	JTextArea rules = new JTextArea(6, 30);
	JTextArea goal = new JTextArea();
	JTextArea game = new JTextArea(3, 30);

	public infoWindow(Frame frame, String title) {
		super(frame, title, true);

		setLocation(300, 300);
		setBounds(170, 70, 400, 580);
		content.setLayout(new FlowLayout());
		setResizable(false);

		JLabel imageLabel = new JLabel(infos);

		JLabel title1Label = new JLabel("AI Assignment 2");
		JLabel title2Label = new JLabel("Wumpus");

		titlePane.add(title1Label);
		titlePane.add(title2Label);
		titlePane.add(Box.createVerticalGlue());

		JLabel rulesLabel = new JLabel("Enviroment");
		rules
				.setText("Squares adjacent to wumpus are smelly \nSquares adjacent to pit are breezy \nGlitter if and only if gold is in the same square \nMoves allowed for agent: Left turn, Right turn, Forward \nAgent dies if it enters a pit or square with wumpus  \nShooting kills the wumpus if you are facing it \nShooting uses up the only arrow\n");
		rules.setLineWrap(true);
		rules.setEditable(false);

		JLabel goalLabel = new JLabel("Goal");
		goal.setText("Find the gold and kill wumpus, without getting killed\n");
		goal.setLineWrap(true);
		goal.setEditable(false);

		JLabel gameLabel = new JLabel("Game");
		game
				.setText("Step: Agent makes a move \nPlay/Stop: Agent makes a row of moves \nReveal: Board is revealed\n");
		game.setLineWrap(true);
		game.setEditable(false);

		rulesPane.add(rulesLabel);
		rulesPane.add(rules);
		rulesPane.add(goalLabel);
		rulesPane.add(goal);
		rulesPane.add(gameLabel);
		rulesPane.add(game);

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		try {
			content.add(titlePane);
			content.add(imageLabel);
			content.add(rulesPane);
			content.add(closeButton);
			getContentPane().add(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		show();
	}
}
