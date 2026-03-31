.PHONY: run build docker-up docker-down docker-build clean

# Rodar aplicação localmente
run:
	mvn spring-boot:run

# Build do projeto
build:
	mvn clean package

# Subir projeto completo com docker
docker-up:
	make docker-db
	make build
	docker compose up --build -d

# Subir apenas banco
docker-db:
	docker compose up -d postgres

# Parar containers
docker-down:
	docker compose down

# Parar e limpar volumes
docker-reset:
	docker compose down -v

# Rebuild completo (rebuild Maven + Docker)
rebuild:
	mvn clean package
	docker compose up --build

# Limpar build
clean:
	mvn clean