package model;


import java.awt.Point;
import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;


public class StdPlayerAI_base extends StdPlayer {
	//CONSTANTE
	private final long DELAI = 700;
	// ATTRIBUTS
	Point random;
	BoardModel plateau;
	

	//private BoardModel plateau;

	public StdPlayerAI_base(String n) {
		super(n);
	}
	
	public StdPlayerAI_base(String n, BoardModel p){
		super(n, p);
		plateau = p;
	}

	public Point chooseMove() {
		List <Point> valideMoves = plateau.getNextMoves(this)	;	
		Random randomizer = new Random();
		if(valideMoves.size() == 0) {
			throw new AssertionError("Pas de coups possible");
		}
		random = valideMoves.get(randomizer.nextInt(valideMoves.size()));
		return random;
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
		final Point res = chooseMove();
		final StdPlayer p = this;
		try {
			Thread.sleep(DELAI);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (canPlay()) {
			if (plateau.getLastPlayer()!=this) {
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
			}
		}
		
		
	}
	

	
	
	
	//OUTILS

}
