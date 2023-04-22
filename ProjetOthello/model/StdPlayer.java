package model;

public class StdPlayer implements Player {

	// ATTRIBUTS
	
	private String name;
	private int nbWin;
	private int nbLose;
	public BoardModel plateau;
	
	// CONSTRUCTEUR
	
	public StdPlayer(String n, BoardModel p){
		name = n;
		plateau = p;
		nbWin = 0;
		nbLose = 0;
	}
	public StdPlayer(String n){
		name = n;
		plateau = null;
		nbWin = 0;
		nbLose = 0;
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNbWin() {
		return nbWin;
	}

	@Override
	public int getNbLoose() {
		return nbLose;
	}

	@Override
	public float getRatio() {
		if(nbLose == 0){
	    	return 1;
	    }
		return nbWin/nbLose;
	}

	@Override
	public BoardModel getPlateau() {
		return plateau;
	}
	/**
	 * change le plateau dans le model.
	 * @pre <pre>
	 * 		!plateau.isStarted
	 * 		p != null
	 * 		!p.isStarted
	 */
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
	public boolean canPlay() {
		return ((plateau.getNextPlayer() == this) && plateau.isStarted());
	}

	@Override
	public void play(int x, int y) {
		if(!plateau.isPlayable(x, y, this)) {
			return; //on rale pas avoir si besoin;
		}
		plateau.placeCoin(x, y, this);
		
		
		
	}
	@Override
	public void play() {
		throw new AssertionError("bruh"); 
		
	}

}
