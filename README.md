# Spring Boot LDAP Rest Api

<a href="https://codecov.io/github/ItaloYeltsin/ldap-rest-api-sample/?branch=master" rel="some text">![Foo](https://codecov.io/github/ItaloYeltsin/ldap-rest-api-sample/coverage.svg?branch=master)</a>


This is an example of a LDAP server with a Http Rest Api interface. This sample illustrates the CRUD of users under **OU** Users to the domain `theinterview.com`. The management of users is made via an HTTP Rest API. To learn how to consume the API, see the docs on `http://localhost:8080/swagger-ui/` after starting up the server.

In the subsequent sections you will see: i) how to setup the environment; ii) the commands required to run the server; and ii) how to run tests.

## Setting up the environment

Have the following requirements installed on your machine:
- Docker CE ([Ubuntu](https://docs.docker.com/engine/install/ubuntu/) | [MacOS X](https://docs.docker.com/docker-for-mac/install/));
- MacOS X or Linux (Docker is unstable on Windows).

## Project Structure

See the project structure bellow.

```
.
├── README.md
├── config
│   └── create_ou_users.ldif
├── docker-compose.yaml
└── ldap-rest-api
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
```

The `docker-compose.yaml` describes the orchestration of two container services. The first one, `ldap-server`, runs instance of the OpenLDAP server, see [osixia/openldap](https://github.com/osixia/docker-openldap). Then, after `ldap-server`is started up, the docker-compose initiate the `rest-api` service that runs the Java Spring boot project inside the folder `ldap-rest-api`. 

## How to run the server

Execute the command as follows to startup the server.
```sh
docker-compose up
```

Add **OU** (organizationalUnit) *Users* under the domain `techinterview.com`, by executing the command bellow:
```sh
docker-compose exec -T ldap-server ldapmodify -h localhost -p 389 -w '123456' -D 'cn=admin,dc=techinterview,dc=com'  <  config/create_ou_users.ldif
```
The file `config/create_ou_users.ldif` describes the **OU** Users structure that will hold the users that come from the Http Rest Api. 

## How to see logs

To the logs, execute the command:
```sh
docker-compose logs -f
```

## How to run tests

Execute the maven wrapper with the following args:

```sh
./mvnw test
```
This requires **Java JDK 11** installed.