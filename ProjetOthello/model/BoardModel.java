package model;

import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeListener;

import java.awt.Point;
import java.beans.PropertyChangeListener;

/**
 * Sp�cifie un mod�le de jeu Othello.
 * Ce mod�le g�re le plateau de jeu, deux joueurs 
 * et les jetons qu'ils ont jou�s sur le plateau.
 * 
 * @inv <pre>
 * 
 * 
 * @cons

 */
public interface BoardModel {
	// CONSTANTES
	int SIZE = 8;
	
	String PROP_GAMEMODE = "gamemode";
	String PROP_PLAYER = "player";
	String PROP_PLACED = "placed";
    String PROP_FINISHED = "finished";
    String PROP_STARTED = "started";
	
    // REQUETES
	
    /**
     * Indique les joueurs m�moris�e par le mod�le.
     */
    List<Player> getPlayers();
    
    /**
     * Indique si le joueur fait parti des joueurs
     * @pre <pre>
     * 		p != null
     */
    boolean isPlayer(Player p);
    
    /**
     * Indique qui est le prochain joueur � jouer
     */
    Player getNextPlayer();
    
    /**
     * Le nombre de joueur dans ce mod�le.
     */
    int getNbPlayers();
    
    /**
     * Le plateau de jeu sous forme de map.
     */
    Map<Point, Player> getGame();
    
    /**
     * Indique s'il y a un jeton � l'emplacement p.
     * @pre <pre>
     * 		p != null
     * 		isInRanch(p)	</pre>
     */
    boolean isPlayed(Point p);
    
    /**
     * Indique s'il y a un jeton � l'emplacement p.
     * @pre <pre>
     * 		isInRanch(p)	</pre>
     */
    boolean isPlayed(int x, int y);
    
    Player getLastPlayer();
    
    
    List<Point> getNextMoves(Player p);
    
    boolean isNextMoves(int x, int y);
    
    /**
     * Indique le dernier emplacement (x, y) jouer dans le plateau.
     */
    Point lastPlayed();
    
    /**
     * Indique si la partie peut commencer
     */
    boolean isReady();
    
    /**
     * Indique si la partie est commencer
     */
    boolean isStarted();
    
    int getTypeGame();
    /**
     * indique si la partie est fini
     * @return game.size() == 64;
     */
    boolean isFinish();
    
    /**
     * Indique si l'emplacement (x, y) est dans le plateau.
     */
    boolean isInRanch(Point p);
    /**
     * Indique si l'emplacement peut etre jouer.
     * @pre <pre>
     * 		p != null
     * 		j != null
     * 	</pre>
     */
    boolean isPlayable(Point j, Player p);
    
    boolean isPlayable(int x, int y, Player p);
    
    Player getPlayerAt(int x, int y);

    // COMMANDES
    /**
     * Ajoute un joueur � la List m�moris�e par le mod�le <code>Player</code>. 
     * @pre <pre>
     * 	   !isStarted()
     *     p != null
     *     getNbPlayer() < 2
     *     !isPlayer(p) </pre>
     * @post <pre>
     *     getNbPlayer() == old getNbPlayer() + 1
     *     (old getPlayers()).add(p) </pre>
     */
    void setPlayer(Player p);

    /**
     * Supprime le joueur de la list des joueurs.
     * @pre <pre>
     *     !isStarted()
     *     p != null
     *     0 < getNbPlayer() < 2
     *     isPlayer(p)
     *      </pre>
     * @post <pre>
     * 		getNbPlayer() == old getNbPlayer() - 1
     *     (old getPlayers()).remove(p) </pre>
     */
    void removePlayer(Player p);
    
    /**
     * Lance la partie, et place les jetons initiaux sur le plateau de jeu.
     * @pre <pre>
     * 		!isStarted()
     * 		isReady() </pre>
     * @post <pre>
     * 		isStarted()
     * 		isPlayed(4,4)
     * 		isPlayed(4,5)
     * 		isPlayed(5,4)
     * 		isPlayed(5,5)
     */
    void Start();
    
    /**
     * efface game, met isStarted a false, last = null,
     *	lastPlayer = null, garde les anciens player.
     */
    void reset();
    
    /**
     * Place un jeton du joueur � l'emplacement du point
     * @pre <pre>
     * 		isStarted()
     * 		isPlayer(p)
     * 		!isPlayed(j) </pre>
     * @post <pre>
     * 		isPlayer(j)	</pre>
     */
    void placeCoin(Point j, Player p);
    
    void placeCoin(int x, int y, Player p);
    
    /**
     * @pre <pre>
     *     lst != null </pre>
     * @post <pre>
     *     lst a été ajouté à la liste des écouteurs
     *     de la propriété propName </pre>
     */
    void addPropertyChangeListener(String propName, PropertyChangeListener lst);
    
    /**
     * @pre <pre>
     *     lst != null </pre>
     * @post <pre>
     *     lst a été retiré de la liste des écouteurs </pre>
     */
    void removePropertyChangeListener(PropertyChangeListener lst);
    
    void addChangeListener(ChangeListener lst);
    
    void removeChangeListener(ChangeListener lst);
}
