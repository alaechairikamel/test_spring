# BookShop API — Spring Boot + DevOps

API REST pour la gestion/vente de livres en ligne avec authentification JWT, panier sécurisé et endpoints admin.

## 1) Convention DevOps du groupe
- **user Linux**: `chefProjet` (à remplacer par le vrai nom choisi)
- **dossier de travail**: `/home/chefProjet/bookshop`
- **repo GitHub**: `chefProjet`

### Commandes serveur (à copier dans votre rendu)
```bash
# connexion initiale
ssh user@37.27.214.35

# création utilisateur groupe
sudo adduser chefProjet

# création dossier de travail
sudo mkdir -p /home/chefProjet/bookshop
sudo chown -R chefProjet:chefProjet /home/chefProjet/bookshop

# clone du repo
su - chefProjet
cd /home/chefProjet/bookshop
git clone https://github.com/<organisation-ou-user>/chefProjet .
```

## 2) Fonctionnalités réalisées
- Public:
  - `GET /api/public/categories`
  - `GET /api/public/books?page=&size=`
  - `GET /api/public/books/{id}`
- Auth:
  - `POST /api/auth/login` → `{ "token": "..." }`
- Cart (JWT requis):
  - `GET /api/cart`
  - `POST /api/cart/items`
  - `PUT /api/cart/items/{itemId}`
  - `DELETE /api/cart/items/{itemId}`
- Admin (ROLE_ADMIN):
  - `POST /api/admin/books`
  - `DELETE /api/admin/books/{id}`

## 3) Comptes seedés
- Admin: `admin@bookshop.com / admin123`
- User: `user@bookshop.com / user123`

## 4) Variables d'environnement
- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS` (optionnel)

## 5) Lancer localement
```bash
mvn spring-boot:run
```

## 6) Lancer avec Docker Compose
```bash
export JWT_SECRET='change-this-secret-key-min-32-chars'
export DB_PASSWORD='1111'
docker compose up -d --build
curl http://localhost:8080/actuator/health
```

## 7) CI/CD GitHub Actions
Workflow: `.github/workflows/ci-cd.yml`

Secrets attendus:
- `SSH_HOST`
- `SSH_USER`
- `SSH_PRIVATE_KEY`
- `PROJECT_USER`
- `JWT_SECRET`
- `DB_PASSWORD`

Déploiement automatique sur push `main`:
1. Build + tests Maven
2. Build image Docker
3. SSH vers serveur, `git pull`, `docker compose up -d --build`, health check `curl`
