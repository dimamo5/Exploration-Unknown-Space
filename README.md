# Exploration-Unknown-Space
## Problem

An army company is fighting in the forest. The enemy has damaged existing roads, and at the moment there is only one possible way out of the forest.

The company includes several captains and soldiers, and it is decided that each of the elements will explore a part of the forest. All elements of the company have a GPS (provides position on the ground) and a radio (for short distance communication). The captains also have a cordless telephone to communicate with each other.

When two soldiers, or a soldier and a captain meet, or are close enough to communicate via radio, they can exchange information. The captain then communicates the new information to the other captains. There are also some robots that are thrown into the forest. The robots move to explore the terrain. Captains or soldiers may question robots when they encounter them. Robots have limited energy, stopping their movement when it runs out.

## Solution
To solved this problem it was implemented a Multi-Agent System using [SAJaS](https://web.fe.up.pt/~hlc/doku.php?id=sajas), [JADE](http://jade.tilab.com/) and [RepastJ3](http://repast.sourceforge.net/repast_3/).

The soldiers were disposed in teams acoording to the number of soldiers in a way that a team is composed by one captain and one or more soldiers. To find an exit each team does the following steps:
- Captain orders oldiers to explore a part of the map but the soldiers can never pass the radio range with their captain
- When the soldiers finish exploring all the map in the radio range they communicate what they saw to the captain
- Captain decides the place they should regroup to explore
- The cycle repeties until the exit is found
- When the exit is found the captain talks with the another team captain's and they start moving to the exit

## GUI
![gui](http://i.imgur.com/GbtDsdn.png "GUI")

1. Janela de seleção do agente cujo conhecimento surge na janela
2. Representação gráfica da simulação. Os espaços verdes são árvores. Os ícones azúis são os soldados, os amarelos são capitães e os ícones vermelhos são robots
3. Janela de controlo da simulação
4. Heat map que representa o conhecimento de um agente quanto à floresta
5. Controlo dos parâmetros da simulação
6. Consola do Repast onde surgem algumas informações e mensagens de debug

## Runnig the project
`Compile the javacript project including the libraries and execute model/Model.java`
