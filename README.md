# Exploration-Unknown-Space
## Problem

An army company is fighting in a forest. The enemy has damaged the existing roads, and at the moment there is only one possible way out of the forest.

The company include several captains and soldiers and each element of the team will explore a part of the forest. All elements have a GPS coordinate (which provides position on the ground) and a radio (for short distance communication). The captains also have a mobile phone to communicate with each other.

When two soldiers, or a soldier and a captain meet, or are close enough to communicate via radio, they can exchange information. The captain then communicates the new information to the other captains. There are also some robots that are thrown into the forest. The robots move randomly to explore the terrain. Captains or soldiers may question robots when they encounter them. Robots have limited energy, stopping their movement when it runs out.

## Solution
To solved this problem a Multi-Agent System was implemented using [SAJaS](https://web.fe.up.pt/~hlc/doku.php?id=sajas), [JADE](http://jade.tilab.com/) and [RepastJ3](http://repast.sourceforge.net/repast_3/).

The soldiers were disposed in teams according to the number of soldiers in a way that a team is composed by one captain and one or more soldiers. To find an exit each team does the following steps:
- Captain orders soldiers to explore a part of the map but the soldiers can never pass the radio range of their captain
- When the soldiers finish exploring all the map in the radio range they communicate the information collected to the captain
- Captain decides the place they should regroup to explore
- The cycle continues until the exit is found
- When the exit is found the captain comunicates it to the others teams captain's and they start moving to the exit

## GUI
![gui](http://i.imgur.com/GbtDsdn.png "GUI")

1. Agent selection window whose knowledge appears in the window
2. Graphical representation of the simulation. The green spaces are trees. The blue icons are the soldiers, the yellow ones are captains and the red icons are robots
3. Simulation control window
4. Heat map representing an agent's knowledge of the forest
5. Control of simulation parameters
6. Repast console where some information and debug messages

## Runnig the project
`Compile the javacript project including the libraries and execute model/Model.java`
