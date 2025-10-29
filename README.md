## Description de l'application

L’application va permettre aux utilisateurs de découvrir une vaste bibliothèque d'images de chiens, d'explorer les différentes races et d'obtenir des informations détaillées sur chaque race. Les utilisateurs pourront rechercher, filtrer, 
et consulter les détails de chaque race pour mieux comprendre leurs caractéristiques et besoins.  L'application intègre l'API **Dog API** : [Dog API Documentation](https://docs.thedogapi.com/docs/intro).

---

### Écran 1 : Exploration des races de chiens

**Objectif :** Permet aux utilisateurs de parcourir une galerie d'images de chiens, de rechercher une race spécifique, et de filtrer les résultats.

- **Barre de recherche** : Un champ en haut de l'écran pour rechercher une race spécifique (exemple : "Bulldog", "Golden Retriever").
- **Filtres** : Des options pour affiner la recherche par critères comme la taille, la durée de vie ou le tempérament.
- **Galerie d'images** : Une grille d'images montrant différents chiens. Chaque image est cliquable et renvoie vers les détails de la race correspondante.

### Écran 2 : Détails d'une race de chien

**Objectif :** Lorsque l'utilisateur clique sur une image d'un chien, il accède à une page détaillée de la race avec des informations complètes.

- **Image de la race** : Une image représentant la race sélectionnée.
- **Nom de la race** : Le nom de la race (exemple : **Golden Retriever**).
- **Informations principales** :
  - Origine de la race
  - Taille et poids moyen
  - Espérance de vie
  - Tempérament (ex : Amical, intelligent, joueur)
- **Description** : Un petit paragraphe expliquant l'histoire et les caractéristiques spécifiques de la race.

### Écran 3 : Favoris et Listes

**Objectif :** Permet à l’utilisateur de sauvegarder ses races préférées et de gérer ses listes personnalisées.

Cette section affiche toutes les images que l'utilisateur a ajoutées à ses favoris. L’utilisateur peut naviguer dans ses favoris, voir les chiens qu'il a sauvegardés, et gérer cette liste (ajouter ou supprimer des images).

- **Ajouter aux favoris** : L'utilisateur peut ajouter une image à ses favoris en appuyant sur un bouton "Ajouter aux favoris" lorsqu'il est sur l'écran de détails de la race.
- **Supprimer un favori** : En cliquant sur une image déjà dans ses favoris, l'utilisateur peut choisir de la retirer de sa liste en appuyant sur "Supprimer des favoris".
