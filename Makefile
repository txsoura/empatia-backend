up:
	docker-compose -f docker-compose.yml up -d

down:
	docker-compose -f docker-compose.yml down

git:
	bash git-cli.sh

pull:
	git pull origin develop
	git pull
