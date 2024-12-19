build-jar:
	@ cd ../backend && ./gradlew bootJar

build-image-backend:
	@ docker build -f Dockerfile.backend -t inventory-backend:1 /home/quintoa/workspace/Vallejo_Llumiquinga/backend/devops

build-image-frontend:
	@ docker build -f Dockerfile.frontend -t inventory-frontend:1 /home/quintoa/workspace/Vallejo_Llumiquinga/frontend/devops

deploy:
	@ docker stack deploy --with-registry-auth -c stack.yml inventory_stack

rm:
	@ docker stack rm inventory_stack

crear-red:
	@ docker network create --driver overlay --scope swarm inventory_net || true

borrar-red:
	@ docker network rm inventory_net

crear-volumen:
	@ docker volume create pg_inventory_data

borrar-volumen:
	@ docker volume rm pg_inventory_data
