### About

Task Manager REST API web service. The program encapsulates all
business logic and data storage logic. For communication with
external system used web protocol HTTP.

### Requirements

 - installed Docker version 20.10.16
 - installed Docker Compose plugin version 2.5.0

### Components

- Apache Tomcat 8.5.60
- Oracle database 11.2.0.2

### Usage

Start all services:

```bash
docker compose --project-directory docker up -d
```

Stop all services:

```bash
docker compose --project-directory docker stop
```

Repackage and redeploy war archive:

```bash
docker compose --project-directory docker up war-deploy
```

Service must have admin user with full access to system. At each
start, the service checks for the existence of admin user with
name 'admin' and full access to the system. If this user is not found,
it will be recreated. If the user is disabled or does not have full
access to the system, the user will be updated to the initial state
without changing the password.

For first launch you must sign in to system with admin user
credentials and change default admin user password. When admin user
recreated it has next default credentials:
 - username: admin
 - password: password
