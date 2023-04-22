package model;

import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


public class StdOthelloModel implements BoardModel{
	// CONSTANTES
	int MIN_NB_PLAYER = 2;
	int MAX_NB_PLAYER = 2;
	
	// ATTRIBUTS 
	private List<Player> players;
	private Map<Point, Player> game;
	private boolean started;
	private Point last;
	private Player lastPlayer;
	private int typeGame;
	private Map<Point, List<Point>> playable0;
	private Map<Point, List<Point>> playable1;
	
	private EventListenerList listeners;
	private ChangeEvent event;
	
	private PropertyChangeSupport pcs;
	
	// CONSTRUCTEUR
	public StdOthelloModel() {
		listeners = new EventListenerList();
		started = false;
		game = new HashMap<Point, Player>();
		players = new ArrayList<Player>();
		last = null;
		lastPlayer = null;
		typeGame = 0;
		playable0 = new HashMap<Point, List<Point>>();
		playable1 = new HashMap<Point, List<Point>>();
	}
	
	// RETOURNE LE JOUEUR GAGNANT
	
	public Player winner() {
		int p1 = 0;
		int p2 = 0;
		for(Map.Entry<Point, Player> m : game.entrySet()) {
			if(m.getValue() == players.get(0)) {
				p1 += 1;
			} else {
				p2 += 1;
			}
			
		}
		if (p1 > p2) {
			return players.get(0);
		} else {
			return players.get(1);
		}
	}
	
	public Player looser() {
		int p1 = 0;
		int p2 = 0;
		for(Map.Entry<Point, Player> m : game.entrySet()) {
			if(m.getValue() == players.get(0)) {
				p1 += 1;
			} else {
				p2 += 1;
			}
			
		}
		if (p1 < p2) {
			return players.get(0);
		} else {
			return players.get(1);
		}
	}
	
	// RETOURNE LE NOMBRE DE PION PLACE SUR LE JEU DU JOUEUR p
	public int getNumberPions(Player p) {
		int i = 0;
		for(Map.Entry<Point, Player> m : game.entrySet()) {
			if(m.getValue() == p) {
				i += 1;
			}
		}
		return i;
	}
	
	// REQUETES
	
    /**
     * Indique les joueurs m�moris�e par le mod�le.
     */
    public List<Player> getPlayers(){
		return players;
    }
    
    /**
     * Indique si le joueur fait parti des joueurs
     * @pre <pre>
     * 		p != null
     */
    public boolean isPlayer(Player p) {
    	if(p == null) {
    		throw new AssertionError("Player == null");
    	}
    	return getPlayers().contains(p);
    }
    
    /**
     * Indique le dernier joueur aillant jouer
     */
    public Player getLastPlayer() {
    	return lastPlayer;
    }
    /**
     * Indique qui est le prochain joueur � jouer
     */
    public Player getNextPlayer() {
    	if(!isStarted()) {
    		return null;
    	}
    	Player p = (players.get(0) == getLastPlayer())?players.get(1):players.get(0);
    	// renvoie la list des pojnts d'un joueur si vide alors next player = lastPlayer
    	return (getNextMoves(p).size() == 0)?getLastPlayer():p;
    }
    
    public Map<Point, List<Point>> getNextMovesMap(Player p){
    	int n = (players.get(0) == p)?0:1;
    	return n==1?playable1:playable0;
    }
    
    public List<Point> getNextMoves(Player p){ //a modifier aussi !!!!!!!!!!!!!!!!!!
    	int n = (players.get(0) == p)?0:1;
    	Map<Point, List<Point>> playable = n==1?playable1:playable0;
    	List<Point> result = new ArrayList<Point>();
    	for (Map.Entry<Point, List<Point>> entry : playable.entrySet()) {
    		result.add(entry.getKey());
    	}
    	return result;
    }
    
    
    public boolean isNextMoves(int x, int y) {
    	return getNextMoves(getNextPlayer()).contains(new Point(x, y));
    }
    /**
     * Le nombre de joueur dans ce mod�le.
     */
    public int getNbPlayers() {
    	return getPlayers().size();
    }
    /**
     * Le type de jeux du plateau
     * typeGame == 1 => 1 joueur
     * typeGame == 2 => 2 joueurs
     */
    public int getTypeGame() {
    	return typeGame;
    }
    /**
     * Le plateau de jeu sous forme de map.
     */
    public Map<Point, Player> getGame(){
    	return game; //cr�er un clone ou encapsuler !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
    
    public boolean isFinish() {
    	if (getNextMoves(players.get(0)).size() == 0 && getNextMoves(players.get(1)).size() == 0) {
            return true;
        }
    	return game.size() == SIZE * SIZE;
    	
    }
    
    /**
     * Indique s'il y a un jeton � l'emplacement p.
     * @pre <pre>
     * 		p != null
     * 		isInRanch(p)	</pre>
     */
    public boolean isPlayed(Point p) {
    	if(p == null || !isInRanch(p)) {
    		throw new AssertionError("position incorrect");
    	}
    	return getGame().containsKey(p);
    }
    
    public boolean isPlayed(int x, int y) {
    	Point p = new Point(x, y);
    	if(p == null || !isInRanch(p)) {
    		throw new AssertionError("position incorrect");
    	}
    	return getGame().containsKey(p);
    }
    
    public Player getPlayerAt(int x, int y) {
    	Point p = new Point(x, y);
    	return getGame().get(p);
    }
    
    /**
     * Indique le dernier emplacement (x, y) jouer dans le plateau.
     */
    public Point lastPlayed() {
    	return last;
    }
    
    /**
     * Indique si la partie peut commencer
     */
    public boolean isReady() {
    	return (getNbPlayers() >= MIN_NB_PLAYER) 
    			&& (getNbPlayers() <= MAX_NB_PLAYER) && !isStarted();
    }
    
    /**
     * Indique si la partie est commencer
     */
    public boolean isStarted() {
    	return started;
    }
    
    /**
     * Indique si l'emplacement (x, y) est dans le plateau.
     */
    public boolean isInRanch(Point p) {
    	return ((p.x >= 0) 
    			&& (p.x < SIZE) 
    			&& (p.y >= 0) 
    			&& (p.y < SIZE));
    }
    /**
     * Indique si l'emplacement peut etre jouer par le joueur.
     * @pre <pre>
     * 		p != null
     * 		j != null
     * 	</pre>
     */
    public boolean isPlayable(Point j, Player p) {
    	if(p == null || j == null) {
    		throw new AssertionError("p == null");
    	}
    	return (getNextPlayer() == p) && getNextMoves(p).contains(j);
    }
    public boolean isPlayable(int x, int y, Player p) {
    	return isPlayable(new Point(x, y), p);
    }

    // COMMANDES
    /**
     * Enregistre au pr�s du model toutes les cases jouable par un joueur
     */
    public void setArePlayable(Player p) {
    	if (p == null || !isPlayer(p)) {
    		throw new AssertionError("player not good");
    	}
    	Map<Point, List<Point>> playable = getNextMovesMap(p);
    	playable.clear();
    	for (Map.Entry<Point, Player> entry : game.entrySet()) {
            if(entry.getValue() == p) {
            	isEnc(entry.getKey());
            }
        }
    	//fireStateChanged(); //useless sera refresh dans l'appel placeCoin
    }
    
    /**
     * Enregistre le type de la partie du plateau (1 ou 2 joueurs)
     * @pre <pre>
     * 		i == 1 || i == 2 || i == 0
     * 		!isStarted()
     * @post 
     * 		getTypeGame() == 1 || getTypeGame() == 2 || getTypeGame() == 0
     */
    public void setTypeGame(int i) {
    	if (i != 1 && i != 2 && i != 0){
    		throw new AssertionError("i != 1 / 2");
    	}
    	if (isStarted()) {
    		throw new AssertionError("started");
    	}
    	int tmp = typeGame;
    	typeGame = i;
    	fireStateChanged();
    	firePropertyChange(PROP_GAMEMODE, tmp, typeGame);
    }
    
    /**
     * Ajoute un joueur � la List m�moris�e par le mod�le <code>Player</code>. 
     * @pre <pre>
     * 	   !isStarted()
     *     p != null
     *     getNbPlayers() < MAX_NB_PLAYER
     *     !isPlayer(p) </pre>
     * @post <pre>
     *     getNbPlayer() == old getNbPlayer() + 1
     *     (old getPlayers()).add(p) </pre>
     */
    public void setPlayer(Player p) {
    	if(isStarted()) {
    		throw new AssertionError("ISStarted");
    	}
    	if(p == null) {
    		throw new AssertionError("p == null");
    	}
    	if(getNbPlayers() >= MAX_NB_PLAYER) {
    		throw new AssertionError("getNbPlayers() >= MAX_NB_PLAYER");
    	}
    	if(isPlayer(p)) {
    		throw new AssertionError("isPlayer");
    	}
    	try {
    		p.setPlateau(this);
    	} catch (AssertionError e) {
    		return;// new AssertionError("p isPlaying");
    	}
    	players.add(p);
    	firePropertyChange(PROP_PLAYER, null, p);
    	fireStateChanged();
    }

    /**
     * Supprime le joueur de la list des joueurs.
     * @pre <pre>
     *     !isStarted()
     *     p != null
     *     isPlayer(p)
     *      </pre>
     * @post <pre>
     * 		getNbPlayer() == old getNbPlayer() - 1
     *     (old getPlayers()).remove(p) </pre>
     */
    public void removePlayer(Player p) {
    	if(isStarted() || p == null || !isPlayer(p)) {
    		throw new AssertionError("@pre"); //sans raler c'st mieux 
    	}
    	players.remove(p);
    	firePropertyChange(PROP_PLAYER, null, p);
    	fireStateChanged();
    }
    
    /**
     * Lance la partie, et place les jetons initiaux sur le plateau de jeu.
     * @pre <pre>
     * 		isReady() </pre>
     * @post <pre>
     * 		isStarted()
     * 		isPlayed(4,4)
     * 		isPlayed(4,3)
     * 		isPlayed(3,4)
     * 		isPlayed(3,3)
     */
    public void Start() {
    	if(!isReady()) {
    		throw new AssertionError("game not isReady");
    	}
    	started = true;
    	game.put(new Point(4,4), getPlayers().get(1));
    	game.put(new Point(3,3), getPlayers().get(1));
    	game.put(new Point(4,3), getPlayers().get(0));
    	game.put(new Point(3,4), getPlayers().get(0));
    	setArePlayable(getPlayers().get(0));
    	
    	fireStateChanged();
    	firePropertyChange(PROP_STARTED, false, true);
    }
    
    /**
     * Place un jeton du joueur � l'emplacement du point
     * @pre <pre>
     * 		isStarted()
     * 		isPlayer(p)
     * 		!isPlayed(j) </pre>
     * @post <pre>
     * 		isPlayer(j)	</pre>
     */
    public void placeCoin(Point j, Player p) {
    	if (p == null || j == null) {
    		throw new AssertionError("p or j null");
    	}
    	if (!isStarted() || !isPlayer(p) || !isInRanch(j) || isPlayed(j)) {
    		throw new AssertionError("Player or Point not valide");
    	}
    	if (!isPlayable(j, p)) {
    		throw new AssertionError("Not Playable");
    	}
    	last = j;
    	lastPlayer = p;
    	game.put(j, p);
    	List<Point> lp = getNextMovesMap(p).get(j);
    	for(Point n : lp) { // Retourner tous ce qui est entre deux point (encadrer)
    		game.replace(n, p);
    	}
    	Player pl = (players.get(0) == p)?players.get(1):players.get(0);
    	setArePlayable(pl);
    	
    	if (pl != getNextPlayer()){
        	setArePlayable(getNextPlayer());
        	
        } 
    	
    	fireStateChanged();
    	firePropertyChange(PROP_PLACED, null, true);
    	if(isFinish()) {
    		//qui win et update le score du joueur
    		firePropertyChange(PROP_FINISHED, null, true);
    	} else {
    		if (getNextPlayer() instanceof StdPlayerAI_base 
    			|| getNextPlayer() instanceof StdPlayerAI_normal 
    			|| getNextPlayer() instanceof StdPlayerAI_hard){
        		new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						getNextPlayer().play();
					}
        		}).start();
        		fireStateChanged();
            	firePropertyChange(PROP_PLACED, null, true);
        	}
    	}
    }
    
    public void placeCoin(int x, int y, Player p) {
    	placeCoin(new Point(x, y), p);
    }
    
    public void reset() {
    	game.clear();
    	started = false;
    	last = null;
    	lastPlayer = null;
    }
    
    //outils
    private void isEnc(Point j) {
    	if(j == null || !game.containsKey(j)) {
    		throw new AssertionError("j not good");
    	}
    	int x = j.x;
    	int y = j.y;
    	Player p = game.get(j);
    	Map<Point, List<Point>> mp = (players.get(0) == p)?playable0:playable1;
    	List <Point> lp = new ArrayList<Point>();
    	//check de la ligne vers gauche
    	Point c = new Point(x-1, y);
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x - 1), c.y);
    	}
    	if(isInRanch(c) && c.x != (x-1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la ligne vers droite
    	c = new Point(x+1, y);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x + 1), c.y);
    	}
    	if(isInRanch(c) && c.x != (x+1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la colone vers haut
    	c = new Point(x, y-1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation(c.x, (c.y-1));
    	}
    	if(isInRanch(c) && c.y != (y-1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la colone vers bas
    	c = new Point(x, y+1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation(c.x, (c.y+1));
    	}
    	if(isInRanch(c) && c.y != (y+1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la diag vers haut-gauche
    	c = new Point(x-1, y-1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x - 1), (c.y - 1));
    	}
    	if(isInRanch(c) && c.x != (x-1) && c.y != (y-1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la diag vers haut-droite
    	c = new Point(x+1, y-1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x + 1), (c.y - 1));
    	}
    	if(isInRanch(c) && c.x != (x+1) && c.y != (y-1) && !isPlayed(c)) { 
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la diag vers bas-gauche
    	c = new Point(x-1, y+1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x-1), (c.y+1));
    	}
    	if(isInRanch(c) && c.x != (x-1) && c.y != (y+1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    	//check de la diag vers bas-droite
    	c = new Point(x+1, y+1);
    	lp = new ArrayList<Point>();
    	while(isInRanch(c) && game.get(c) != null && game.get(c) != p) {
    		lp.add(new Point(c.x, c.y));
    		c.setLocation((c.x+1), (c.y+1));
    	}
    	if(isInRanch(c) && c.x != (x+1) && c.y != (y+1) && !isPlayed(c)) {
    		if (!mp.containsKey(c)) {
    			mp.put(new Point(c.x, c.y), lp);
    		} else {
    			List<Point> lp2 = mp.get(c);
    			lp2.addAll(lp);
    		}
    	}
    }
    
    public void addPropertyChangeListener(String propName, PropertyChangeListener lst) {
		if (lst == null) {
			throw new AssertionError("lst");
		}
		if (pcs == null) {
			pcs = new PropertyChangeSupport(this);
		}
		
		pcs.addPropertyChangeListener(propName, lst);
	}
    
    public void removePropertyChangeListener(PropertyChangeListener lst) {
    	if (lst == null) {
			throw new AssertionError("lst");
		}
    	if (pcs == null) {
			pcs = new PropertyChangeSupport(this);
		}
    	
		pcs.removePropertyChangeListener(lst);
	}
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue){
    	if (pcs == null) {
			pcs = new PropertyChangeSupport(this);
		}
    	pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
    
    public void addChangeListener(ChangeListener lst) {
    	listeners.add(ChangeListener.class, lst);
    }
    
    public void removeChangeListener(ChangeListener lst) {
    	listeners.remove(ChangeListener.class, lst);
    }
    
    protected void fireStateChanged() {
    	Object[] lst = listeners.getListenerList();
    	for (int i = lst.length - 2; i >= 0; i -= 2) {
    		if(lst[i] == ChangeListener.class) {
    			if (event == null) {
    				event = new ChangeEvent(this);
    			}
    			((ChangeListener) lst[i + 1]).stateChanged(event);;
    		}
    	}
    }
}
