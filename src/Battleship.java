
import battleship.BattleShip;

/**
 * A long time ago in a galaxy far,
 * far away...
 *
 *          STAR WARS
 *
 *          Episode X
 *          Return of the Sith
 *
 * It is a period of civil war among the Rebels. Unknown of what is happening on the farthest ends of the galaxy, they
 * cannot choose their leader. During the chaos, Imperial spies managed to steal secret plans to the Rebel's largest
 * base.
 * The evil Galactic Empire have re-built their ultimate weapon, the DEATH STAR. They hyperspaced themselves to the
 * rebel's base on Tatooine, with none other than Emperor Palpatine aboard the Star. He wants to end the Jedi and their
 * sacred temple.
 * They have been surrounded by all the Rebel ships in the galaxy...
 *
 */
public class Battleship
{
   static final int NUMBEROFGAMES = 10000;
   public static void EnemiesAhead()
  {
    int totalShots = 0;
    System.out.println(BattleShip.version());
    for (int game = 0; game < NUMBEROFGAMES; game++) {

      BattleShip deathStar = new BattleShip();
      DeathStarBot emperorPalpatine = new DeathStarBot(deathStar);

      while (!deathStar.allSunk()) {
        emperorPalpatine.fireShot();
        
      }
      int gameShots = deathStar.totalShotsTaken();
      totalShots += gameShots;
    }
    System.out.printf("DeathStarBot - The Average # of Shots required in %d games to sink all Rebel Ships = %.2f\n", NUMBEROFGAMES, (double)totalShots / NUMBEROFGAMES);
    
  }
  public static void main(String[] args)
  {
    EnemiesAhead();
  }
}
