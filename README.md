# Ecommerce Backend

API REST Java/Spring Boot du projet e-commerce fullstack, consommée par le [frontend Angular](https://github.com/Soifiaouir/Projet-E-commerce-front) associé.

Projet réalisé dans le cadre d'un portfolio, avec pour objectif de démontrer une architecture backend propre, sécurisée par JWT, avec une réflexion cloud/déploiement (AWS, CI/CD).

## Stack technique

- **Java 21** (LTS)
- **Spring Boot 4.1**
- **Spring Security** + **JWT** (jjwt) pour l'authentification
- **Spring Data JPA** / **Hibernate**
- **PostgreSQL**
- **Maven**
- **Lombok** (pour les DTOs)
- **Swagger / OpenAPI** (springdoc) pour la documentation de l'API

## Architecture

Organisation en couches, inspirée du modèle BO/DAL/BLL/IHM :

src/main/java/fr/soifi/ecommerce/
- bo/ # DTOs : objets d'échange entre l'API et le client
- dal/ # Data Access Layer : entités JPA + repositories Spring Data
- bll/ # Business Logic Layer : services, logique métier
- ihm/ # Controllers REST, exposition de l'API
- config/ # Sécurité (JWT, Spring Security), configuration Swagger

  
## Prérequis

- JDK 21
- Maven
- Docker (pour PostgreSQL en local)

## Base de données

PostgreSQL est lancé via Docker :

```bash
docker run --name ecommerce-postgres -e POSTGRES_PASSWORD=<mot_de_passe> -e POSTGRES_DB=ecommerce -p 5432:5432 -d postgres
```

## Configuration

Créer un fichier `src/main/resources/application-dev.properties` (non versionné) avec :

```properties
DB_USERNAME=postgres
DB_PASSWORD=<mot_de_passe>
JWT_SECRET=<chaine_aleatoire_secrete_32_caracteres_minimum>
JWT_EXPIRATION_MS=86400000
```

## Lancer le projet en local

```bash
mvn clean compile
mvn spring-boot:run
```

L'API est alors accessible sur `http://localhost:8080`.

## Documentation de l'API

Une fois l'application lancée, la documentation Swagger est disponible sur :
http://localhost:8080/swagger-ui/index.html


## Sécurité

- Authentification par JWT (`Authorization: Bearer <token>`)
- Mots de passe hashés avec BCrypt
- Rôles `CLIENT` / `ADMIN`, avec accès restreint aux routes de gestion du catalogue et des commandes pour les administrateurs
- Catalogue produits/catégories accessible publiquement en lecture

## Fonctionnalités

- Inscription / connexion
- Gestion du catalogue (produits, catégories)
- Panier
- Commandes (création à partir du panier, historique, suivi de statut)
- Espace admin : gestion des produits, catégories et statuts de commande

## Frontend associé

Ce backend est consommé par le [projet frontend Angular](https://github.com/Soifiaouir/Projet-E-commerce-front).
