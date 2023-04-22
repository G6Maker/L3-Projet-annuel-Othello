package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;


public class StdPlayerAI_normal extends StdPlayer {
	//CONSTANTE
	private final long DELAI = 700;
	// ATTRIBUTS
	private BoardModel plateau;
	private Random hypoT = new Random();

	public StdPlayerAI_normal(String n) {
		super(n);
	}
	
	public StdPlayerAI_normal(String n, BoardModel p){
		super(n, p);
		plateau = p;
	}
// Pour ce niveau normal j'ai regardé sur le site du jeu et ils propose quelques conseils pour les pseduo-avancés pour améliorer leurs jeu avec les conseils :
// -essayer d'avoir le moin pions possibles 
//-essayer de retourner les 4 premieres pieces rapidemnt
//-eviter de trop retourner des pieces en frontiere 
//regrouper ses pions en une seule masse 
//eviter de se construire des murs en metant des pieces au bord.
	
	
	public Point chooseMove() {
		List <Point> valideMoves = plateau.getNextMoves(this);	
	    List <Double> xs = new ArrayList<Double>();
	    List <Double> ys = new ArrayList<Double>();
	    
		for (int j = 0 ; j<(valideMoves.size()) ;j++) {
			Point enCours = valideMoves.get(j);
			 xs.add(enCours.getX());
			 ys.add(enCours.getY());
		}
		double xPlusProche = xs.get(0);
	    double yPlusProche = ys.get(0);
		for (int i = 0 ; i<(valideMoves.size()) ; i++) {
			double val1=xs.get(i)-4. ;
			double val2=ys.get(i)-4. ;
			double val3=xPlusProche-4.;
			double val4=yPlusProche-4.;
			//pour previligier les placement les plus proches du centre
			if (Math.abs(val1)<=Math.abs(val3) && Math.abs(val2)<=Math.abs(val4)){
				xPlusProche = xs.get(i);
				yPlusProche = ys.get(i);
			}
			Point plusProche= valideMoves.get(hypoT.nextInt(valideMoves.size()));
			//previligier les placement loin de la frontiere 
			if (xPlusProche <= 0 && xs.get(i) > 0 && xs.get(i) < 8 || yPlusProche <= 0 && ys.get(i) >0 && ys.get(i) <8
				|| xPlusProche <=0 && xs.get(i)< 8 && xs.get(i) > 0|| yPlusProche<=0 && ys.get(i)<8 && ys.get(i) >0
				|| xPlusProche >=8 && xs.get(i)>0 && xs.get(i)< 8||yPlusProche>=8 && ys.get(i)>0 && ys.get(i) <8 ){
				xPlusProche = xs.get(i);
				yPlusProche = ys.get(i);
			}	
			return plusProche;
		   
			
			//je dois avoir acces a la liste des pions posés par chaque joueur pour pouvoir continuer avec les autres conditions 
		}
		return new Point((int)xPlusProche,(int)yPlusProche);
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

}

