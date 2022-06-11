# AutoWhitelister

### Description
Cette application permet de whitelist rapidement tous les pseudos d'une game fournis par mon bot Discord. L'application va simplement simuler l'appui sur la touche d'ouverture du chat, puis Ctrl V pour coller la commande de whitelist, puis l'appui sur la touche "Entrée".

Note : Les pseudos sont sauvegardés jusqu'à ce que la commande /endgame soit utilisée : vous pouvez faire plusieurs pick, l'application ne whitelistera que les nouveaux pseudos (sauf si vous la fermez et la ré-ouvrez).

Ce repo est présent pour que tout le monde puisse avoir accès au code afin de vérifier que je n'ai rien ajouté de mauvais dans le code.

### Téléchargement
[![CI](https://github.com/Azn9/AutoWhitlister/actions/workflows/build.yml/badge.svg)](https://github.com/Azn9/AutoWhitlister/actions/workflows/build.yml)

#### A. [Lien direct](https://nightly.link/Azn9/AutoWhitelister/workflows/build/master/AutoWhitelister.zip)
#### B. Instructions : (si vous préférez être sûrs)
1. Cliquez sur l'onglet "Actions"
2. Cliquez sur le dernier workflow (le plus haut de la liste).
3. Descendez jusqu'à la sectiop "Artifacts", et cliquez sur le fichier "AutoWhitelister.zip"

### Utilisation
0. Utilisez mon bot avec les modules "Pick" et "AutoUsername". L'API doit être activée. Pour ce faire, envoyez-moi un simple DM.
1. Utilisez la commande /pick ou pick via message : le bot vous enverra un message du type
```Markdown
Informations AutoWhitelister (A NE PAS DIVULGER)
Votre clé d'API : xxxxxxxxxxxxxxxx
Lien : https://bots-api.azn9.dev/whitelist/xxxxxxx/xxxxxxxxxxx
```
2. (Lancez l'application)
3. Remplissez le champ "Clé API"
4. Remplissez le champ "Lien"
5. Choisissez le délai entre deux commandes (aller en-dessous d'une seconde est déconseillé, certains serveurs pourraient vous kick pour spam).
6. Modifiez la commande de whitelist si besoin
7. Cliquez sur "Démarrer"
8. Allez sur votre client Minecraft (en jeu, chat fermé, pas sur le menu echap ou autre) et appuyez sur la touche F7 (les instructions sont affichées en haut à gauche de votre écran)
9. Appuyez sur la touche pour ouvrir votre chat.
10. Le chat devrait se fermer instantanément, puis l'application passera en fonctionnement et commencera à copier/coller les commandes.

**Appuyez sur F7 pour quitter le mode whitelist**

### Pour les développeurs
Si vous souhaitez intégrer la whitelist automatique dans votre code de plugin ou autre, vous pouvez simplement faire une requête GET sur l'url donnée par le bot, avec le header "Authorization: Bearer xxxxxxxxx" avec la clé d'API donnée. Vous récupèrerez une array JSON contenant tous les pseudos à whitelist. 