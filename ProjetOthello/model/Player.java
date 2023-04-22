package model;

/**
 * Sp�cifie un mod�le de joueur de plateau.
 * Ce mod�le indique les information sur le joueur,
 * nom, nb de partie gagn�e, le plateau de jeu en cours.
 * 
 * @inv <pre>
 * 
 * 
 * @cons

 */
public interface Player {
	
	//REQUETES
	/**
	 * Donne le nom du player.
	 * @return
	 */
	String getName();
	
	/**
	 * Donne le nombre de victoire du joueur.
	 * @return
	 */
	int getNbWin();
	
	/**
	 * Indique le nombre de d�faite du joueur.
	 * @return
	 */
	int getNbLoose();
	
	/**
	 * Indique le ratio du joueur
	 * @post <pre>
	 * 		getRatio() = getNbWin()/getNbLoose();
	 * </pre>
	 */
	float getRatio();
	
	/**
	 * Indique le plateau de jeu actuel.
	 * @return
	 */
	BoardModel getPlateau();
	
	/**
	 * Indique si le joueur peux jouer.
	 */
	boolean canPlay();
	
	//COMMANDES
	/**
	 * change le plateau dans le model.
	 * @pre <pre>
	 * 		!plateau.isStarted
	 * 		p != null
	 * 		!p.isStarted
	 */
	void setPlateau(BoardModel p);
	
	/**
	 * Fait jouer le joueur sur le plateau � la position (x, y)
	 * @pre <pre>
	 * 		isInranch(new Point(x, y))
	 * 		canPlay()
	 * </pre>
	 * @post <pre>
	 * 		!canPlay()
	 * 		getPlateau().isPlayed(new Point(x, y)) == true
	 * </pre>
	 */
	void play(int x, int y);

	void play();
}
