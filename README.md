# Big Nest Forum

A simple forum made with Spring Boot and React.

# Technologies stack

* Spring Boot Java framework for the backend
* React JavaScript library for the frontend

# Features

* System of accounts with different roles 
* Optional anonimous posting

# Requirements

In order to build and run this app you only need [Docker](https://www.docker.com).

# Configuration

You have to create .env file with the folowing environment variables:

* REQUIRE_CAPTCHA (true/false) - is captcha required in forms
* HCAPTCHA_SECRET
* HCAPTCHA_SITE_KEY
* SERVER_HOST - the hostname the frontend should use to connect to the backend
* SERVER_PROTOCOL (http/https) - the protocol the frontend should use to connect to the backend
* TOKEN_SIGNING_PASSWORD - the password used for signing and verification of JWT-tokens
* POSTGRES_PASSWORD - the password for the user `postgres` to connect to the database

Every line of the .env file must have the format *VARIABLE_NAME=variable-value*.

# Building and running

In order to compile and run the application you need to run the only command:

Build the backend separately. You can use my script:

```bash
./init-backend.sh
```

Prepare environment variables for the frontend using my special script that just copies `.env` file to the frontend directory and adds the `VITE_` prefix to the name of each variable in it. You have to do it every time you change `.env` file in the application root folder:

```bash
./init-frontend.sh
```

Build and run the application:

```bash
docker compose up -d
```

After that you can access the app on port `3300`.

To stop the application:

```bash
docker compose down
```
