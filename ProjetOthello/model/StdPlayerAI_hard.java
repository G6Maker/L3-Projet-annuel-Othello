package model;

import java.awt.Point;
import java.util.List;

import javax.swing.SwingUtilities;

public class StdPlayerAI_hard extends StdPlayer{
	private final long DELAI = 700;
	// ATTRIBUTS
		private BoardModel plateau;
		private int tab[][];
	
	public StdPlayerAI_hard(String n) {
		super(n);
		initMatrice();
	}
	
	public StdPlayerAI_hard(String n, BoardModel p){
		super(n, p);
		plateau = p;
		initMatrice();
	}
	
	public Point chooseMove() {
		List <Point> valideMoves = plateau.getNextMoves(this);
		Point max = new Point();
		int lastmax = -250;
		for (Point p : valideMoves) {
			if ((tab[(int) p.getX()][(int) p.getY()]) >= lastmax){
				lastmax = tab[(int) p.getX()][(int) p.getY()];
				max = p;
			}	
		}
		return max;
	}
	
	public void setPlateau(BoardModel p) {
		if(getPlateau() != null && getPlateau().isStarted()) {
			throw new AssertionError("partie en cours");
		}
		if(p == null || p.isStarted()) {
			throw new AssertionError("p == null || p est started");
		}
		plateau = p;
	}
	
	@Override
	public void play() {
		//System.out.println("AI de but");
		final Point res = chooseMove();
		final StdPlayer p = this;
		try {
			Thread.sleep(DELAI);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (canPlay()) {
			if(!SwingUtilities.isEventDispatchThread()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						plateau.placeCoin(res , p);
					}
				});
			} else {
					plateau.placeCoin(res , p);
			}
		} else {
			//System.out.println("peux pas jouer");
		}
		
	}
	private void initMatrice() {
		tab = new int[8][8];
		for (int i = 0 ; i < 8 ; i++) {
			for (int j = 0 ; j < 8 ; j++) {
				if ((i == 0 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 0) || (i == 7 && j == 7)) {
					tab[i][j] = 500;
				}
				else if ((i == 0 && j == 1) || (i == 0 && j == 6) ||(i == 1 && j == 0) || (i == 1 && j == 7)
					|| (i == 6 && j == 0) || (i == 6 && j == 7) ||(i == 7 && j == 1) || (i == 7 && j == 6)) {
					tab[i][j] = -150;
				}
				else if ((i == 0 && j == 2) || (i == 0 && j == 5) || (i == 2 && j == 0) || (i == 2 && j == 7)
						|| (i == 5 && j == 0) || (i == 5 && j == 7) || (i == 7 && j == 2) || (i == 7 && j == 5)) {
					tab[i][j] = 30;
				}
				else if ((i == 0 && j == 3) || (i == 0 && j == 4) || (i == 7 && j == 3) || (i == 7 && j == 4)
						|| (i == 3 && j == 0) || (i == 4 && j == 0) || (i == 3 && j == 7) || (i == 4 && j == 7)) {
					tab[i][j] = 10;
				}
				else if ((i == 1 && j == 1) || (i == 6 && j == 6) || (i == 1 && j == 6) || (i == 6 && j == 1)) {
					tab[i][j] = -250;
				}
				else if ((i == 2 && j == 2) || (i == 5 && j == 5) || (i == 2 && j == 5) || (i == 5 && j == 4)) {
					tab[i][j] = 1;
				}
				else if ((i == 2 && j == 3) || (i == 2 && j == 4) || (i == 3 && j == 2) || (i == 4 && j == 2)
						|| (i == 3 && j == 5) || (i == 4 && j == 5) || (i == 5 && j == 3) || (i == 5 && j == 4)) {
					tab[i][j] = 2;
				}
				else if ((i == 3 && j == 3) || (i == 3 && j == 4) || (i == 4 && j == 3) || (i == 4 && j == 4)) {
					tab[i][j] = 16;
				}
				else {
					tab[i][j] = 0;
				}
				
			}
		}
		
	}
	
}
