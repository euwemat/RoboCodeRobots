package TaylorEuwema;
import robocode.*;
import robocode.AdvancedRobot;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html


/**************************************************************************
 * FigureEight - a robot by Taylor Euwema & Sreyasree Mandal
 *************************************************************************/
public class FigureEight extends AdvancedRobot
{
	// T means Top, B means Bottom, R means Right, L means Left, these are used for the initial corners 
	// the bot will try to go to.
	double pixelAway = 50, enemyX = 0, enemyY = 0, curX = 0, curY = 0, TLX = 40, TLY = 560, TRX = 760, TRY = 560, BLX = 40, BLY= 40, BRX = 760, BRY = 40;
	
	/**************************************************************************
	 * run: FigureEight's default behavior
	 *************************************************************************/
	public void run() {
		
		int count = -1;
		double closest = 2000, curX = getX(), curY = getY(), slopeToDest, enemySlopeToDest; 
		double[] temp = new double[4];
		
		
		
		//Find and go to the closest corner
		temp = findClosestCorner();
		
		//Find location of sitting duck
		turnRadarLeft(360);
		adjustCorners();		

		//Find slopes of both bots for comparison 
		slopeToDest = findSlopeToDest(curX, curY, temp[0], temp[1]);
		enemySlopeToDest = findSlopeToDest(enemyX, enemyY, temp[0], temp[1]);
		
		//Check if we have smiilar slopes
		if (slopeToDest-.2 < enemySlopeToDest && slopeToDest+.2 > enemySlopeToDest){
			
			//Check if the enemy is closer
			if (distanceFormula(enemyX, enemyY, temp[0], temp[1]) < distanceFormula(curX, curY, temp[0], temp[1])){
				temp[0] = temp[2];
				temp[1] = temp[3];
			}
		}
		
		//goToClosestCorner
		goToStraight(temp[0], temp[1]);

		//Set the robot colors
		setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		
		//Determine the next location the bot should go to in the 
		//figure eight based on where the bot is currently 
		if (closest >= distanceFormula(BRX, BRY, curX, curY)){
			closest = distanceFormula(BRX, BRY, curX, curY);
			count = 0;
		}
		if (closest >= distanceFormula(TLX, TLY, curX, curY)){
			closest = distanceFormula(TLX, TLY, curX, curY);
			count = 1;
		} 
		if (closest >= distanceFormula(BLX, BLY, curX, curY)){
			closest = distanceFormula(BLX, BLY, curX, curY);
			count = 2;
		} 
		if (closest >= distanceFormula(TRX, TRY, curX, curY)){
			count = 3;
		}
		
		// Robot main loop
		while(true) {
				
			//We are at the bottom right corner
			if (count == 0){
				turnRadarLeft(360);
				checkAndMoveIfEnemyIsInWay(TLX, TLY);
				goToStraight(TLX, TLY);
				count = 1;
				
			//We are at the top left corner
			}else if (count == 1){
				turnRadarLeft(360);
				checkAndMoveIfEnemyIsInWay(BLX, BLY);
				goToStraight(BLX, BLY);
				count = 2;
				
			//We are at the bottom left corner
			}else if (count == 2){
				turnRadarLeft(360);
				checkAndMoveIfEnemyIsInWay(TRX, TRY);
				goToStraight(TRX, TRY);
				count = 3;
				
			//We are at the top left corner
			}else{
				//turnLeft(360);
				checkAndMoveIfEnemyIsInWay(BRX, BRY);
				goToStraight(BRX, BRY);
				count = 0;
			}
		}
	}

	/**************************************************************************
	 * Method used to adjust the location of the corners if the enemey is to close
	 *************************************************************************/
	public void adjustCorners(){
		double temp = 0, toClose = 60;
		temp = distanceFormula(BRX, BRY, enemyX, enemyY);
		
		//Check if a corner is to close to the enemy
		if (temp < toClose){
			
			//Check if we should move closer to center
			if (toClose < distanceFormula(BRX-50, BRY+50, enemyX, enemyY)){
				BRX = BRX - 50;
				BRY = BRY + 50;
				
			//Check if we should move to the left
			} else if (toClose < distanceFormula(BRX-50, BRY, enemyX, enemyY)){
				BRX = BRX - 50;
				
			//We should move up
			} else {
				BRY = BRY + 50;
			}
		}
		temp = distanceFormula(TRX, TRY, enemyX, enemyY);
		
		//Check if a corner is to close to the enemy
		if (temp < toClose){
		
			//Check if we should move closer to center
			if (toClose < distanceFormula(TRX-50, TRY-50, enemyX, enemyY)){
				TRX = TRX - 50;
				TRY = TRY - 50;
				
			//Check if we should move to the left
			} else if (toClose < distanceFormula(BRX-50, BRY, enemyX, enemyY)){
				TRX = TRX - 50;
				
			//We should move down
			} else {
				TRY = TRY - 50;
			}
		}
		temp = distanceFormula(BLX, BLY, enemyX, enemyY);
		
		//Check if a corner is to close to the enemy
		if (temp < toClose){
			
			//Check if we should move closer to center
			if (toClose < distanceFormula(BLX+50, BLY+50, enemyX, enemyY)){
				BLX = BLX + 50;
				BLY = BLY + 50;
				
			//Check if we should move to the right
			} else if (toClose < distanceFormula(BLX+50, BLY, enemyX, enemyY)){
				BLX = BLX + 50;
				
			//We should move up
			} else {
				BLY = BLY + 50;
			}
		}
		temp = distanceFormula(TLX, TLY, enemyX, enemyY);
		
		//Check if a corner is to close to the enemy
		if (temp < toClose){
			
			//Check if we should move closer to center
			if (toClose < distanceFormula(TLX+50, TLY-50, enemyX, enemyY)){
				TLX = TLX + 50;
				TLY = TLY - 50;
				
			//Check if we should move to the right
			} else if (toClose < distanceFormula(TLX+50, TLY, enemyX, enemyY)){
				TLX = TLX + 50;
				
			//We should move down
			} else {
				TLY = TLY - 50;
			}
		}
	
	}	
	/**************************************************************************
	 * Method used to go straight from the current location to a specific spot
	 *************************************************************************/
	public void goToStraight(double X, double Y){
	 	double curX = getX(), curY = getY();
		double distance = distanceFormula(curX, curY, X, Y);
		
		//We need to move south west
		if (curX > X && curY > Y){
			turnTowardDestination(X, Y);
			ahead(distance);
		//We need to move north west
		} else if (curX > X && curY < Y){
			turnTowardDestination(X, Y);
			ahead(distance);			

		//We need to move north east
		} else if (curX < X && curY < Y){
			turnTowardDestination(X, Y);
			ahead(distance);
	
		//We need to move south east
		} else {
			turnTowardDestination(X, Y);
			ahead(distance);
		} 
	}
	
	/**************************************************************************
	 * Method used to turn toward a specific location
	 *************************************************************************/
	public void turnTowardDestination(double X, double Y){
		double curX = getX(), curY = getY(), heading = getHeading();
		
		//We need to turn south west
		if (curX > X && curY > Y){
			turnSouthWest(heading, X, Y);
			
		//We need to turn north west
		} else if (curX > X && curY < Y){
			turnNorthWest(heading, X, Y);
			
		//We need to turn north east
		} else if (curX < X && curY < Y){
			turnNorthEast(heading, X, Y);
	
		//We need to turn south east
		} else {
			turnSouthEast(heading, X, Y);
		}
	}
	
	/**************************************************************************
	 * Method used to turn South West
	 *************************************************************************/	
	public void turnSouthWest(double heading, double X, double Y){
		double curX = getX(), curY = getY();
		double distance = distanceFormula(curX, curY, X, Y);
		if (heading <= (180+inverseSign((curX-X)/distance))){
			turnRight(180 - heading + inverseSign((curX-X)/distance));
		} else {
			turnLeft(heading - (180+inverseSign((curX-X)/distance)));
		}
	}
	
	/**************************************************************************
	 * Method used to turn North West
	 *************************************************************************/	
	public void turnNorthWest(double heading, double X, double Y){
		double curX = getX(), curY = getY();
		double distance = distanceFormula(curX, curY, X, Y);
		if (heading <= (360-inverseSign((curX-X)/distance))){
			turnRight(360 - (heading + inverseSign((curX-X)/distance)));				
		} else {
			turnLeft(heading - (360 - inverseSign((curX-X)/distance)));
		}
	}

	/**************************************************************************
	 * Method used to turn North East
	 *************************************************************************/	
	public void turnNorthEast(double heading, double X, double Y){
		double curX = getX(), curY = getY();
		double distance = distanceFormula(curX, curY, X, Y);
		if (heading <= inverseSign((X-curX)/distance)){
			turnRight(inverseSign((X-curX)/distance) - heading);
		} else {
			turnLeft(heading - inverseSign((X-curX)/distance));
		}
	}
	
	/**************************************************************************
	 * Method used to turn South East
	 *************************************************************************/
	public void turnSouthEast(double heading, double X, double Y){
		double curX = getX(), curY = getY();
		double distance = distanceFormula(curX, curY, X, Y);	
		if (heading <= (180 - inverseSign((X-curX)/distance))){
			turnRight(180 - (heading + inverseSign((X-curX)/distance)));				
		} else {
			turnLeft(heading - (180 - inverseSign((X-curX)/distance)));
		}
	}
	
	/**************************************************************************
	 * Method used check if enemy is in the enemy is in the way and will then
	 * move around then enemy 
	 *************************************************************************/	
	public void checkAndMoveIfEnemyIsInWay(double X, double Y){
		double curX = getX(), curY = getY(), heading, extraDistance = 20, slopeToDest, enemySlopeToDest; 
		slopeToDest = findSlopeToDest(curX, curY, X, Y);
		enemySlopeToDest = findSlopeToDest(enemyX, enemyY, X, Y);
		turnTowardDestination(X, Y);
		heading = getHeading();
		
		//Check if we're facing southish. This is important beause 
		//if the duck is in the way it's slope will be near infinity
		//so we need to we need to check the inverse slope.
		if (heading > 160 && heading < 200){
			slopeToDest = findSlopeToDest(curY, curX, Y, X);
			enemySlopeToDest = findSlopeToDest(enemyY, enemyX, Y, X);	
		}
		//Check if enemy has similar slopes to us
		if (slopeToDest-.25 < enemySlopeToDest && slopeToDest+.25 > enemySlopeToDest){
			
			//Check if enemy is closer than us
			if (distanceFormula(enemyX, enemyY, X, Y) < distanceFormula(curX, curY, X, Y)){
			
				//We need to move south west
				if (curX > X && curY > Y){
					
					//Check if we're facing southish
					if (heading > 160 && heading < 200){
						
						//Check if location is to close to the edge
						if((enemyX-pixelAway-extraDistance) < pixelAway){
							goToStraight((enemyX+pixelAway+extraDistance), enemyY);
						} else if ((enemyX+pixelAway+extraDistance) > getWidth()-pixelAway){
							goToStraight((enemyX-pixelAway-extraDistance), enemyY);
						} else {
							goToStraight((enemyX-pixelAway-extraDistance), enemyY);	
						}
					} 
							
				//We need to move north west
				} else if (curX > X && curY < Y){
					
					if (enemyY+pixelAway > TRY){
						goToStraight(enemyX-pixelAway, enemyY-pixelAway);
					} else if (enemyY-pixelAway < BRY){
						goToStraight(enemyX+pixelAway, enemyY+pixelAway);
					
					}else{
						if (distanceFormula(X, Y, enemyX-pixelAway, enemyY-pixelAway) < distanceFormula(X, Y, enemyX+pixelAway, enemyY+pixelAway)){
							goToStraight(enemyX-pixelAway, enemyY-pixelAway);
						} else {
							goToStraight(enemyX+pixelAway, enemyY+pixelAway);
						}
					}
				
				//We need to move north east	
				} else if (curX < X && curY < Y){
					if (enemyY+pixelAway > TRY){
						goToStraight(enemyX+pixelAway, enemyY-pixelAway);
					} else if (enemyY-pixelAway < BRY){
						goToStraight(enemyX-pixelAway, enemyY+pixelAway);
					
					}else{
						if (distanceFormula(X, Y, enemyX+pixelAway, enemyY-pixelAway) < distanceFormula(X, Y, enemyX-pixelAway, enemyY+pixelAway)){
							goToStraight(enemyX+pixelAway, enemyY-pixelAway);
						} else {
							goToStraight(enemyX-pixelAway, enemyY+pixelAway);
						}
					}
					
				//We need to move south east
				} else {
					
					//Check if we're facing southish
					if (heading > 160 && heading < 200){
						
						//Check if location is to close to the edge
						if((enemyX-pixelAway-extraDistance) < pixelAway){
							goToStraight((enemyX+pixelAway+extraDistance), enemyY);
						} else if ((enemyX+pixelAway+extraDistance) > getWidth()-pixelAway){
							goToStraight((enemyX-pixelAway-extraDistance), enemyY);
						} else {
							goToStraight((enemyX-pixelAway-extraDistance), enemyY);	
						}
					} 
				}
			} else {
				goToStraight(X, Y);
			}
		}
	}
	
	/**************************************************************************
	 * Find the slope of the line between two points. 
	 *************************************************************************/	
	public double findSlopeToDest(double X1, double Y1, double X2, double Y2){
		return ((Y2-Y1)/(X2-X1));
	}
		
	/**************************************************************************
	 * Used to find the angle of a right triangle. sin(x) = opposite/hypotenuse
	 *************************************************************************/
	public double inverseSign(double val){
		return Math.asin(val) * (180/Math.PI);
	}
	
	/**************************************************************************
	 * Distance formula used to find the the distance from one point to another
	 *************************************************************************/
	public double distanceFormula(double X1, double Y1, double X2, double Y2){
		return Math.sqrt(((X2-X1)*(X2-X1)) + ((Y2-Y1)*(Y2-Y1)));
	}
	
	/**************************************************************************
	 * Method used to find the closest corner from the bots current location
	 * This will return the two closest corners in one array. The closest will 
	 * be in locatoin 0 & 1 and the second closest will be in 2 & 3
	 *************************************************************************/
	public double[] findClosestCorner(){
		double[] coordinates = new double [4];
		double total, tempTotal, curX = getX(), curY = getY();
		
		//Find distance to Top Right Corner
		
		total = distanceFormula(curX, curY, TRX, TRY);
		coordinates[0] = TRX;
		coordinates[1] = TRY;
		
		//Check if Bottom Right Corner is closer
		if (total > (tempTotal = distanceFormula(curX, curY, BRX, BRY))){
			coordinates[2] = coordinates[0];
			coordinates[3] = coordinates[1];
			coordinates[0] = BRX;
			coordinates[1] = BRY;
			total = tempTotal;
		} 
		
		//Check if Bottom Left Corner is closer
		if (total > (tempTotal = distanceFormula(curX, curY, BLX, BLY))){
			coordinates[2] = coordinates[0];
			coordinates[3] = coordinates[1];
			coordinates[0] = BLX;
			coordinates[1] = BLY;
			total = tempTotal;
		} 
		
		//Check if Top Left Corner is closer
		if (total >  distanceFormula(curX, curY, TLX, TLY)){
			coordinates[2] = coordinates[0];
			coordinates[3] = coordinates[1];
			coordinates[0] = TLX;
			coordinates[1] = TLY;
		}
		return coordinates;
	}
	
	/******************************************************************************************
	 * These next three methods (RobotStatus, onStatus, & onScannedRobot) were found at:
	 * http://stackoverflow.com/questions/22107351/robocode-how-to-get-the-enemies-co-ordinates
	 * They're used to find the enemy coordinates when the enemy is scanned
	 *****************************************************************************************/
	public RobotStatus robotStatus;														/*****/	
																						/*****/
	public void onStatus(StatusEvent e){												/*****/
		this.robotStatus = e.getStatus();												/*****/
	}																					/*****/
																						/*****/
	public void onScannedRobot(ScannedRobotEvent e) { 									/*****/
		double angleToEnemy = e.getBearing();											/*****/
        // Calculate the angle to the scanned robot										/*****/
        double angle = Math.toRadians((robotStatus.getHeading() + angleToEnemy % 360));	/*****/
																						/*****/
        // Calculate the coordinates of the robot										/*****/		
        enemyX = (robotStatus.getX() + Math.sin(angle) * e.getDistance());				/*****/
        enemyY = (robotStatus.getY() + Math.cos(angle) * e.getDistance());				/*****/
    }																					/*****/
	/*****************************************************************************************/
	
}