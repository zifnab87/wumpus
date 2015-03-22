/**
 * Mavroforakis Xaralampos - p3050086
 * Michaelidis Michail - p3050112
 * Xaralampidou Kassandra - p3050196
 **/

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class bottomPanel extends JPanel implements ActionListener {
	JButton newGame;
	JButton step;
	JButton play;
	JButton stop;
	JButton revealobscure;
	JButton exit;
	JButton kb;
	WumpusWindow parent;
	private boolean plays;
	JDialog dl;
	KnowledgeBaseWindow txt;

	public bottomPanel(WumpusWindow par) {
		this.parent = par;
		setLayout(new FlowLayout());
		initButtons();
		add(step);
		add(play);
		add(stop);
		add(revealobscure);
		add(newGame);
		add(exit);
		add(kb);
	}

	private void init() {
		for (Component bt : getComponents()) {
			bt.setEnabled(true);
		}
		stop.setEnabled(false);
		plays = true;
		if (dl != null) {
			dl.dispose();
		}
		dl = new JDialog();
		txt = new KnowledgeBaseWindow(parent);
	}

	private void initButtons() {
		newGame = new JButton("New Game");
		step = new JButton("Step");
		play = new JButton("Play");
		stop = new JButton("Stop");
		exit = new JButton("Exit");
		kb = new JButton("Kb");
		revealobscure = new JButton("Reveal");
		newGame.addActionListener(this);
		step.addActionListener(this);
		play.addActionListener(this);
		stop.addActionListener(this);
		exit.addActionListener(this);
		kb.addActionListener(this);
		revealobscure.addActionListener(this);
		txt = new KnowledgeBaseWindow(parent);
		stop.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == step) {
			Thread worker = new Thread() {
				public void run() {
					for (Component bt : getComponents()) {
						bt.setEnabled(false);
					}
					parent.agent.play();
					txt.populate();
					if ((parent.agent.foundGold && parent.agent.killedWumpus)
							|| parent.agent.gameOver) {
						for (Component bt : getComponents()) {
							bt.setEnabled(false);
						}
						newGame.setEnabled(true);
						kb.setEnabled(true);
						revealobscure.setEnabled(true);
					} else {
						for (Component bt : getComponents()) {
							bt.setEnabled(true);
							stop.setEnabled(false);
						}
					}
				}
			};
			worker.start();

		} else if (e.getSource() == play) {
			plays = true;
			Thread playThread = new Thread() {
				public void run() {
					for (Component bt : getComponents()) {
						bt.setEnabled(false);
					}
					stop.setEnabled(true);
					revealobscure.setEnabled(true);
					kb.setEnabled(true);
					while (!((parent.agent.foundGold && parent.agent.killedWumpus) || parent.agent.gameOver)
							&& plays) {
						parent.agent.play();
						txt.populate();
						if ((parent.agent.foundGold && parent.agent.killedWumpus)
								|| parent.agent.gameOver) {
							step.setEnabled(false);
							play.setEnabled(false);
							stop.setEnabled(false);
							newGame.setEnabled(true);
							exit.setEnabled(true);
						}

					}
				}
			};
			playThread.start();
		} else if (e.getSource() == stop) {
			plays = false;
			for (Component bt : getComponents()) {
				bt.setEnabled(true);
			}
			stop.setEnabled(false);
		}

		else if (e.getSource() == newGame) {
			if (JOptionPane.showConfirmDialog(null,
					"Are you sure you want a new game?", "",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				parent.gameInit();
				init();
			}
		} else if (e.getSource() == exit) {
			if (JOptionPane.showConfirmDialog(null,
					"Are you sure you want to exit?", "",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				parent.dispose();
				System.exit(0);
			}
		} else if (e.getSource() == revealobscure) {
			if (((JButton) e.getSource()).getText().equals("Reveal")) {
				parent.reveal();
				((JButton) e.getSource()).setText("Obscure");
			} else if (((JButton) e.getSource()).getText().equals("Obscure")) {
				parent.obscure();
				((JButton) e.getSource()).setText("Reveal");
			}

		} else if (e.getSource() == kb) {

			if (dl == null) {
				dl = new JDialog();
			}
			if (dl.isVisible()) {
				dl.setVisible(false);
			} else {
				dl.setVisible(true);
			}

			JScrollPane scrollPane = new JScrollPane(txt);
			dl.add(scrollPane);
			dl.setBounds(parent.getLocation().x + parent.getWidth() / 2, 300,
					630, 650);
		}

	}

	public boolean isPlaying() {
		return plays;
	}

	public void setStopEnabled() {
		stop.setEnabled(true);
	}

}
