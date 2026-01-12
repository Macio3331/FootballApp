# Football App

## Overview
The Football App is a Java EE project implemented with Hibernate, aimed at managing football league competitions. It encompasses entities such as clubs, players, matches, and goals.
The Football App also provides a user-friendly web interface for entities management. It also has the seeder app written in Python which connects to the external API data source and seeds the database with it.

## Features
- **Club Management**: Add, update, and remove football clubs participating in the league.
- **Player Management**: Maintain player profiles including their statistics, transfer history, etc.
- **Match Management**: Schedule matches, record match results, and manage match statistics.
- **Goal Management**: Record goals scored during matches and maintain related information.

## Technologies Used
- **Java EE**: Platform for building enterprise applications using Java.
- **Hibernate**: Object-relational mapping (ORM) tool for mapping Java objects to database tables.
- **npm**: Modern Node.js' package manager tool for frontend files and code management.
- **React**: JavaScript library for building user interfaces, especially single-page applications (SPAs).
- **Python**: high-level, general-purpose programming language that’s designed to be easy to read, write, and understand. It’s one of the most popular programming languages in the world.

## Run & Deployment
- Project needs jdk and eclipse installed in the clone folder, node.js installed together with npm package manager.
- To run a backend server run "start_server.bat" file.
- To run a frontend go into "frontend" directory, open a command line and run commands:

  ```bash
  npm i
  npm run build
  npm start
  ```

- To run a seeder app go to seeder, open a command line and run command:

  ```bash
  py main.py
  ```
