# Spring Boot LDAP Rest Api

<a href="https://codecov.io/github/ItaloYeltsin/ldap-rest-api-sample/?branch=master" rel="some text">![Foo](https://codecov.io/github/ItaloYeltsin/ldap-rest-api-sample/coverage.svg?branch=master)</a>


This is an example of a LDAP server with a HTTP REST API interface. This sample illustrates the CRUD of users under **OU** Users to the domain `theinterview.com`. The management of users is made via an HTTP REST API. To learn how to consume the API, see the docs on `http://localhost:8080/swagger-ui/` after starting up the server.

In the subsequent sections you will see: i) how to setup the environment; ii) the commands required to run the server; and iii) how to run tests.

## Setting up the environment

Have the following requirements installed on your machine:
- Docker CE ([Ubuntu](https://docs.docker.com/engine/install/ubuntu/) | [MacOS X](https://docs.docker.com/docker-for-mac/install/));
- MacOS X or Linux (Docker is unstable on Windows).

## Project Structure

See the project structure bellow.

```
.
├── .env
├── README.md
├── config
│   └── create_ou_users.ldif
├── docker-compose.yaml
└── ldap-rest-api (Java Project)
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
```

The `docker-compose.yaml` describes the orchestration of two container services. The first one, `ldap-server`, runs an instance of the OpenLDAP server, see [osixia/openldap](https://github.com/osixia/docker-openldap). The second service, `rest-api`, runs the Java Spring Boot project inside folder `ldap-rest-api` which is the HTTP REST API interface. 

The file `config/create_ou_users.ldif` describes the **OU** Users structure that will hold the users that come from the HTTP REST API. 

## How to run the server

Execute the command as follows to startup the server.
```sh
docker-compose up -d
```
The default **LDAP_ADMIN_PASSWORD** is *123456* and comes from the `.env` file at the root of the project. If you want to change the LDAP server password you can override the value by exporting the environment variable **LDAP_ADMIN_PASSWORD** or change the value at the `.env` file.

To shutdown the server, execute `docker-compose down`.

## How to see logs

To see the logs, execute the command:
```sh
docker-compose logs -f
```

## How to run tests

Execute the maven wrapper with the following args:

```sh
./mvnw test
```
This requires **Java JDK 11** installed.