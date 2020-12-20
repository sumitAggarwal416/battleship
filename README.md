# battleshipBoardGameOptimized

It is a regular Battleship board game but using simple probability, you could destroy all the enemy ships under 53 shots.
The map is a regular 10x10 grid. The ship sizes have been hardcoded to 2,3,3,4 and 5. The program provides the average number of shots needed to sink all ships in 10,000 games. The cells which have already been shot at cannot be shot again since those coordinates have been "blocked". 
The probability of a ship being at a certain coordinate changes after each shot is fired. I have used very simple probability and can be improved using different methods. Also, I have kept the calling of my firing function basic and if improved, can lead to better results. Furthermore, the program does not optimize the what to do after a shot fired fails to hit a ship.
