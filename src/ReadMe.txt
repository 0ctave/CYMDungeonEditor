Commandes:
	(Information) Les argument <...> sont obligatoires, les arguments [...] sont optionels
	(Information) L'action ID -1 est l'action tuer tout les mobs
	(Information) L'action ID -2 est l'action gagner Dungeon, et -3 l'action perdre Dungeon (appeler un des deux à un moment, sinon le Dungeon ne se finira jamais)
	(Information) Lors de la sauvegarde sur le disque, les fichiers prennent de noms sous le format Object_ID (exemples: CustomMob_0.yml, Action_3.yml, ...)
	(Information) Le nom du fichier n'importe pas, donc CustomMob_0.yml peut parfaitement être renommé en Banana.yml
	
	- doaction <id>
		- Lance l'action à l'ID <id>
				
	- createaction <type> <args...>
		(Information) Les '&' seront transformés en '§' afin de permettre l'utilisation des couleurs
		
		- DISP_MESSAGE <msg>			
			- Les '@n' seront transformés en retour à la ligne
		- DISP_TITLE <title> @n[subtitle]
			- Le '@n' différentie le titre du soustitre (@n et [subtitle] optionels)
		- GIVE_EXP <(int) amount>
			- <(int) amount> un entier
		- GIVE_ITEM
			- Prend l'item dans votre main comme item à give
		- PASTE_SCHEMATIC <schematicName> [replaceAir]
			- Prend votre position comme position de paste
			-  <schematicName> nom du fichier, sans le ".schematic"
			- [replaceAir] false par défaut
		- SPAWN_ITEM
			- Prend l'item dans votre main comme item à spawn
			- Prend votre position comme position de spawn
		- SPAWN_MOD <customMobID> [count]
			- Prend votre position comme position de spawn
			- <customMobID> un entier, ID du customMob à spawn
			- [count] un entier, nombre de customMob à faire spawn, 1 par défaut
		- TELEPORT
			- Prend votre position comme position de teleportation
		- TIME_OUT <(int) tick> <(int) action1> [(int) action2] [...]
			- <tick> un entier, nombre de tick avant déclanchement
			- <action1> un entier, ID de l'action à lancer
			- [action2] et [...] entiers, ID d'actions à lancer (<action1> obligatoire, mais peut y avoir plusieur actions à lancer)
		- STOP_TIME_OUT <action1> [action2] [...]
			- <action1> [action2] [...] sont des actions de type TIME_OUT, si ces actions ont lancé des TIME_OUT qui sont encore en attente, ces TIME_OUT seront annulés
	
	- createevent <type> <args...>
		- MOB_WAVE_ENDED <fromActionID> [...] @n <todoActionID> [...]
			- Exemple: /createevent MOB_WAVE_ENDED 1 2 3 @n 4 5
				Lorsque les mobs créés par les actions d'IDs 1, 2 et 3 seront morts, les actions d'ID 4 et 5 seront lancées
		- AREA_ACTIVATION <radius> <action1> [action2] [...]
			- Prend votre position comme position de détection
			- <radius> nombre, rayon d'activation
			- <action1> [action2] [...] IDs d'actions à lancer
		- PUT_ITEM <action1> [action2] [...]
			- Prend le coffre le plus proche (rayon max: 1 block) de vous comme coffre où placer l'item
			- Prend l'item dans votre main comme item à placer
			- <action1> [action2] [...] IDs d'actions à lancer
		- PLACE_BLOCK <action1> [action2] [...]
			- Prend votre position comme position où placer le block
			- <action1> [action2] [...] IDs d'actions à lancer
		- INTERACT <action1> [action2] [...]
			- Prend le block interactable (levier, bouton ou plaque de pression) le plus proche de vous (rayon max: 1 block) comme block avec lequel intéragir
			- <action1> [action2] [...] IDs d'actions à lancer
			
	- createdungeon <name> <x1> <y1> <z1> <x2> <y2> <z2> <spawnx> <spawny> <spawnz> <events>
		- Asser explicite...
		
	- custommob <arg1> <args>	
		- info <customMobID>
			- Affiche les informations relatives au CustomMob à l'ID <customMobID>
		- list
			- Ouvre une interface avec la liste des CustomMob chargés
		- edit <customMobID>
			- Ouvre une interface d'édition du CustomMob à l'ID <customMobID>
			(Information) L'édition ne permet pas de définir la probabilité de drop l'armur du mod à sa mort (0% par défaut),
			(Information)  ni la probabilité de drop les items défini comme "à drop" (100% par défaut), pour modifier ces probabilités, une modification dans le fichier
			(Information)  est nécessaire
		- create [mobName]
			- Créé un nouveau CustomMob avec le nom [mobName] si donné
		- spawn <customMobID>
			- Spawn le CustomMob à l'ID <customMobID> sur vous
		- save <customMobID>
			- Sauvegarde le CustomMob à l'ID <customMobID> dans un fichier
			/!\ Un customMob non sauvegardé via cette commande sera supprimé de la mémoire au reload du serveur
			/!\ Lorsqu'un CustomMob est sauvegardé, le fichier est placé dans le dossier DungeonEditor/CustomMobs, cependant, les CustomMob chargés doivent être placé dans
			/!\  Dungeon/CustomMobs, si le fichier n'est pas présent dans ce dossier au chargement du plugin, le CustomMob ne sera pas chargé, ET la création d'un nouveau
			/!\  CustomMob RISQUE FORTEMENT d'overide le CustomMob précédant dû au faite que le Reference_ID est généré celon les CustomMob chargés (et comme il ne l'est pas,
			/!\  le serveur ne peut pas savoir que son Reference_ID est déjà utilisé)
		