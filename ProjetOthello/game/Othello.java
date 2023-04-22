package game;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import model.Player;
import model.StdOthelloModel;
import model.StdPlayer;
import model.StdPlayerAI_base;
import model.StdPlayerAI_normal;
import model.StdPlayerAI_hard;

public class Othello {
	
	String PROP_GAMEMODE = "gamemode";
	String PROP_PLAYER = "player";
	String PROP_PLACED = "placed";
    String PROP_FINISHED = "finished";
    String PROP_STARTED = "started";
	
	// ATTRIBUTS
	/* Main Frame*/
	private JFrame mainFrame;
	
	/* Barre de Menu */
	private JMenuBar menuBar;
	private JMenu jmNvPartie;
	private JMenu jmiOnePlayer;
	private JMenuItem jmiTwoPlayer;
	private JMenu jmGameOption;
	private JMenuItem jmiKnowNextMove;
	private JMenu jmInfo;
	private JMenuItem levelOne;
	private JMenuItem levelTwo;
	private JMenuItem levelThree;

	/* Boutton controle de partie */
	protected JButton lanceGame;
	private JButton resetGame;
	
	/* Information de jeu */
	private JLabel wichPlay;
	private String title;
	
	/* model */
	protected StdOthelloModel model; 

	/* Plateau de jeu */
	protected static JButton[][] cases;
	
	/* images pions */
	private Icon PionB = new ImageIcon(this.getClass().getResource("../img/PionB.png"));
	private Icon PionN = new ImageIcon(this.getClass().getResource("../img/PionN.png"));
	
	private boolean knowNextMove;
	
	
	// CONSTRUCTEUR
	
	public Othello() {
		cases = new JButton[8][8];
		createModel();
		createView();
		placeComponent();
		createController();
	}
	
	// REQUETES
	
	// COMMANDES
	
	public void display() {
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        refreshGoButton();
        mainFrame.setVisible(true);
    }
	
	// OUTILS
	
	private void createModel() {
		model = new StdOthelloModel();
		initTabCases(cases);
	}
	
	private void createView() {
		mainFrame = new JFrame("Othello");
		
		/* Menu */
		menuBar = new JMenuBar();
		jmNvPartie = new JMenu("Nouvelle Partie");
		jmiOnePlayer = new JMenu("Un Joueur");
		levelOne = new JMenuItem("Facile");
		levelTwo = new JMenuItem("Normal");
		levelThree = new JMenuItem("Difficile");
		jmiOnePlayer.add(levelOne);
		jmiOnePlayer.add(levelTwo);
		jmiOnePlayer.add(levelThree);
		jmiTwoPlayer = new JMenuItem("Deux Joueur");
		jmNvPartie.add(jmiOnePlayer);
		jmNvPartie.add(jmiTwoPlayer);
		jmInfo = new JMenu("Infos");
		menuBar.add(jmNvPartie);
		jmGameOption = new JMenu("Option de Jeu");
		jmiKnowNextMove = new JMenuItem("Savoir les prochains coups a jouer");
		jmGameOption.add(jmiKnowNextMove);
		menuBar.add(jmGameOption);
		menuBar.add(jmInfo);

		/* Bouttons */
		lanceGame = new JButton("Lancer la partie !");
		resetGame = new JButton("Recommencer la Partie");
		resetGame.setEnabled(false);
		
		wichPlay = new JLabel("");

		title = "Score - Noir : 0 - Blanc : 0";
		knowNextMove = false;
	}
	
	private void placeComponent() {
		JPanel p = new JPanel(); {
			p.add(menuBar);
		}
		mainFrame.add(p,BorderLayout.NORTH);
		p = new JPanel(); {
			p.add(new JLabel("  "));
		}
		mainFrame.add(p,BorderLayout.WEST);
		p = new JPanel(); {
			p.add(wichPlay);
			p.add(lanceGame);
			p.add(resetGame);
		}
		mainFrame.add(p, BorderLayout.SOUTH);
		
		componentGameFrame();
		p = new JPanel(); {
			p.add(new JLabel("  "));
		}
		mainFrame.add(p,BorderLayout.EAST);
		
	}
	
	private void createController() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// PROPERTY CHANGE --------------------------------	
		
		model.addPropertyChangeListener(PROP_FINISHED, 
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						wichPlay.setText("Partie finie");
						Player winner = model.winner();
						Player looser = model.looser();
						int nbW = model.getNumberPions(winner);
						int nbL = model.getNumberPions(looser);
						if(nbL + nbW == 64) {
							JOptionPane.showMessageDialog
							(mainFrame, "Le gagnant est " + winner.getName() 
							+ " sur un score de " + nbW
							+ " à " + nbL, "Victoire !", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog
							(mainFrame, "Le gagnant est " + winner.getName() 
							+ " sur un score de " + nbW + (64 - nbL + nbW)
							+ " à " + nbL, "Victoire !", JOptionPane.INFORMATION_MESSAGE);
						}
					}
		});
		
		model.addPropertyChangeListener(PROP_PLACED,
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						refreshCases();
					}
		});
		
		// --------------------------------------------------
		
		resetGame.addActionListener(new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!model.isFinish()) {
					int rep = JOptionPane.showConfirmDialog
							(mainFrame,"La partie est encore en cours voulez vous quitter ?",
							"Quitter la partie",JOptionPane.YES_NO_OPTION);
					if(rep == JOptionPane.YES_OPTION) {
						ResetButton();
					} else {
						return;
					}
				} else {
					ResetButton();
				}
			}
		});	
		
		levelOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testTypeGameAlreadyChoose();
				model.setPlayer(new StdPlayer("Joueur 1", model));
				model.setPlayer(new StdPlayerAI_base("IA", model));
				model.setTypeGame(1);
				refreshGoButton();
				wichPlay.setText("Veuillez Lancer la partie");
				refreshGoButton();
				mainFrame.setTitle("IA niveau facile");
			}
		});
		
		levelTwo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				testTypeGameAlreadyChoose();
				model.setPlayer(new StdPlayer("Joueur 1", model));
				model.setPlayer(new StdPlayerAI_normal("IA", model));			
				model.setTypeGame(1);
				refreshGoButton();
				wichPlay.setText("Veuillez Lancer la partie");
				refreshGoButton();
				mainFrame.setTitle("IA niveau moyen");
			}
		});
		
		levelThree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testTypeGameAlreadyChoose();
				model.setPlayer(new StdPlayer("Joueur 1", model));
				model.setPlayer(new StdPlayerAI_hard("IA", model));	
				model.setTypeGame(1);
				refreshGoButton();
				wichPlay.setText("Veuillez Lancer la partie");
				refreshGoButton();
				mainFrame.setTitle("IA niveau difficile");
			}
		});
		
		jmiTwoPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testTypeGameAlreadyChoose();
				model.setPlayer(new StdPlayer("Joueur 1", model));
				model.setPlayer(new StdPlayer("Joueur 2", model));
				model.setTypeGame(2);
				refreshGoButton();
				wichPlay.setText("Veuillez Lancer la partie");
				mainFrame.setTitle("Joueur contre joueur");
			}
		});
			
		jmiKnowNextMove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rep = JOptionPane.showConfirmDialog(mainFrame,"Voulez vous voir les prochains coups ? ", "Procahins coups",JOptionPane.YES_NO_OPTION);
				if(rep == JOptionPane.YES_OPTION) {
					knowNextMove = true;
				} else {
					knowNextMove = false;
				}	
			}
		});
		
		lanceGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lanceGame.setEnabled(false);
				model.Start();
				wichPlay.setText(" Au tour de " + model.getNextPlayer().getName() + " ");
				refreshCases();
				resetGame.setEnabled(true);
				int sp1 = model.getNumberPions(model.getPlayers().get(0));
				int sp2 = model.getNumberPions(model.getPlayers().get(1));
				title = "Score - Noir : " + sp1 + " - Blanc : " + sp2;
				mainFrame.setTitle(title);
			}
		});
		
		jmInfo.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					java.awt.Desktop.getDesktop().open(new File("./docs/Notice_Utilisation.pdf"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
	}
	
	private void ResetButton() {
		model.reset();
		removeAllPlayers();
		refreshCasesFinished();
		wichPlay.setText("");
		resetGame.setEnabled(false);
		lanceGame.setEnabled(false);
		mainFrame.setTitle("Othello");
	}
	
	private void refreshGoButton() {
		if((model.getPlayers().size() == 1 && model.getTypeGame() == 1) 
			|| (model.getPlayers().size() == 2 && model.getTypeGame() == 2) 
			|| ((model.getPlayers().size() == 2 && model.getTypeGame() == 1))){
				lanceGame.setEnabled(true);
		} else {
			lanceGame.setEnabled(false);
		}
	}
	
	private void componentGameFrame() {
		JPanel k = new JPanel(new GridLayout(8,8)); {
			for (int i = 0 ; i < 8 ; i++) {
				for (int j = 0 ; j < 8 ; j++) {
					k.add(cases[i][j]);
				}
			}
		}
		mainFrame.add(k, BorderLayout.CENTER);
	}
	
	public void initTabCases(JButton[][] tab) {
		for (int i = 0 ; i < 8 ; i++) {
			for (int j = 0 ; j < 8 ; j++) {
				tab[i][j] = new JButton();
				int r = 127;
				int g = 185;
				int b = 84;
				Color backColor = new Color(r, g, b);
				tab[i][j].setPreferredSize(new Dimension(100,100));
				tab[i][j].setBackground(backColor);
				tab[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				tab[i][j].setIcon(null);
				tab[i][j].setEnabled(false);
			}
		}
	}
		
	public void refreshCasesFinished() {
		for (int i = 0 ; i < 8 ; i++) {
			for (int j = 0 ; j < 8 ; j++) {
				int r = 127;
				int g = 185;
				int b = 84;
				Color backColor = new Color(r, g, b);
				cases[i][j].setPreferredSize(new Dimension(100,100));
				cases[i][j].setBackground(backColor);
				cases[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cases[i][j].setIcon(null);
				cases[i][j].setEnabled(false);
			}
		}
	}
	
	public void refreshCases() {
		if(!model.isFinish()) {
			wichPlay.setText("Au tour de " + model.getNextPlayer().getName());	
		}
		for (int i = 0 ; i < 8 ; i++) {
			for (int j = 0 ; j < 8 ; j++) {
				
				if(model.isPlayed(i, j)) {
					Player p = model.getPlayerAt(i, j);
					if(p == model.getPlayers().get(0)) {
						cases[i][j].setIcon(PionN);
						cases[i][j].setEnabled(false);
						cases[i][j].setDisabledIcon(PionN);
					}
					if(p == model.getPlayers().get(1)) {	
						cases[i][j].setIcon(PionB);
						cases[i][j].setEnabled(false);
						cases[i][j].setDisabledIcon(PionB);
					}
				}
				if((model.isNextMoves(i, j) 
					&& !(model.getNextPlayer() instanceof StdPlayerAI_base) 
					&& !(model.getNextPlayer() instanceof StdPlayerAI_normal)
					&& !(model.getNextPlayer() instanceof StdPlayerAI_hard))){
					final int x = i;
					final int y = j;
					if(knowNextMove) {
						cases[i][j].setBackground(Color.GREEN);
					} else {
						int r = 127;
						int g = 185;
						int b = 84;
						Color backColor = new Color(r, g, b);
						cases[i][j].setBackground(backColor);
					}
					cases[i][j].setEnabled(true);
					cases[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {													
							model.getNextPlayer().play(x, y);
						}
					});
				} else {
					int r = 127;
					int g = 185;
					int b = 84;
					Color backColor = new Color(r, g, b);
					cases[i][j].setBackground(backColor);
					cases[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					cases[i][j].setEnabled(false);
				}
			}
		}
		int sp1 = model.getNumberPions(model.getPlayers().get(0));
		int sp2 = model.getNumberPions(model.getPlayers().get(1));
		mainFrame.setTitle("Score - Noir : " + sp1 + " - Blanc : " + sp2);
	}
	
	public void removeAllPlayers() {
		model.removePlayer(model.getPlayers().get(0));
		model.removePlayer(model.getPlayers().get(0));
	}
	
	public void testTypeGameAlreadyChoose() {
		if (model.getPlayers().size() != 0) {
			removeAllPlayers();
			model.setTypeGame(0);
		}
	}
	

	// POINT D'ENTREE

	public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                new Othello().display();
	            }
	        });
	    }
	
}
