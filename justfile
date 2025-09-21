set dotenv-load

default:
    just --list

build:
    mvn clean compile

test:
    mvn test

run:
    mvn spring-boot:run

package:
    mvn clean package

clean:
    mvn clean

up:
    docker-compose up

down:
    docker-compose down

logs:
    docker-compose logs -f

install:
    mvn clean install