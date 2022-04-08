- [Introduction](#introduction)
- [Explication](#explication)
- [Technologies](#technologies)

# todo

## Introduction
Todo est une application Web créée en Java EE qui permet à un utilisateur de créer un profil pour ajouter 
est enregistrer des tâches dans une base de données. Ainsi que d'accéder et de modifier leurs détails.

## Explication
Dans le package Rest, les classes TodoRest.java et UserRest.java gèrent la communication entre l'utilisateur et les requêtes.
Les service QueryService.java est TodoService.java communique avec les entités pour le Base de donnes MySQL.
La sécurité utilise un jeton BEARER qui est extrait du "contexte" de l'application.

## Technologies
+ Payara
+ Glassfish
+ MySQL
+ Javax
+ Maven
+ junit
+ jsonwebtoken
