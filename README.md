# SD Assignment 3

## Description

The assignment goal was to develop a Java RMI implementation of the "Game of the Rope", as described in the [description of the assignment](description.pdf) provided by the course's professor. The solution was implemented in Java 21 and is built on top of the first assignment (that uses reentrant locks to ensure mutual exclusion and condition variables to ensure synchronization between threads). Each envolved entity is now encapsulated in a process running on seperate machines, that communicate by remote object invocation using Java RMI.

The game is played by two teams, each with 5 players and 1 coach. A referee is responsible for the flow of the game. There is a [general interaction diagram](general-interaction-diagram.pdf) to help understand the game's flow, and a [client architecture](client-architecture.pdf), [server architecture](server-architecture.pdf)  and [register remote object architecture](register-remote-object-architecture) to help understand how the client, server and remote object registry logic are implemented.

The following entities were defined:

- `Referee`: organizes the game and ensures that the rules are followed.
  - The referee thread is encapsulated in the `Referee` client.
  - Client of the `Referee Site` and `Playground` servers.
- `Coach`: responsible for preparing the team for the game.
  - The 2 coach threads are encapsulated in the `Coach` client.
  - Client of the `Contestants Bench`, `Referee Site` and `Playground` servers.
- `Contestant`: responsible for playing the game.
  - The 10 contestant threads are encapsulated in the `Contestant` client.
  - Client of the `Contestants Bench` and `Playground` servers.

To allow synchronization between the threads, that are running in each process, the following information sharing regions were defined:

- `Referee Site`: where the referee shares the game state with the coaches.
  - The referee site monitor is encapsulated in the `Referee Site` server.
  - Server of the `Coach` and `Referee` clients.
  - Client of the `General Repository` server.
- `Playground`: where the contestants play the game.
  - The playground monitor is encapsulated in the `Playground` server.
  - Server of the `Contestant`, `Coach` and `Referee` clients
  - Client of the `General Repository` server.
- `Contestants Bench`: where the contestants wait for their turn to play.
  - The contestants bench monitor is encapsulated in the `Contestants Bench` server.
  - Server of the `Contestant` and `Coach` clients.
  - Client of the `General Repository` server.
- `General Repository`: where the state of the game is logged by the referee site, playground and contestants bench.
  - The general repository monitor is encapsulated in the `General Repository` server.
  - Server of the `Contestants Bench`, `Referee Site` and `Playground` servers.

To allow for the information sharing regions to register in the `RMI Registry`, an additional server was created, that runs in the same machine as the `RMI Registry`. This is due to a servers not being able to register themselves outside of the machine running the `RMI Registry`. Lookups are done directly to the `RMI Registry` by each client that needs a certain remote object provided by a server.

- `Remote Object Registry`: where servers outside of the `RMI Registry` machine register their remote object.
  
  - Used by `Contestants Bench`, `Referee Site`, `Playground` and `General Repository` servers to register their remote objects.

**Course:** Distributed Systems (2023/2024).

## Running the program

- Run `./build.sh` in root to compile, distribute and compress the execution environment of each entity.
- Open 9 terminals and deploy and run each entity to a separate machine in this order (remember to be connected to the university's VPN).
  - Run `./rmi_registry.sh` for the RMI Registry.
  - Wait for it to start. 
  - Run `./obj_registry.sh` for the Remote Object Registry server.
  - Wait for it to start.
  - Run `./general_repository.sh` for the General Repository server.
  - Wait for it to start.
  - Run `./contestants_bench.sh` for the Contestants Bench server.
  - Run `./coach.sh` for the Coach client.
  - Run `./contestant.sh` for the Contestant client.
  - Wait for them to start.
  - Run `./referee.sh` for the Referee client.
  - Run `./playground.sh` for the Playground server.
  - Run `./referee_site.sh` for the Referee Site server.
- Alternatively, in a single terminal, run `run_all_first_time.sh` to automate the process. You should press ENTER to wait for each component to stabilize.
- If you already executed the previous script and left the `RMI Registry` and `Remote Object Registry` terminal windows open, you can just run `run_all_other_times.sh` to start the missing components.

**Note:** Change values in `.env` file to the pretended configuration of the deployment. 

## Logging

Logs of the runs are saved in the `logs` directory where the General Repository server is running. The logs are named according to `<timestamp>.log`.

## Documentation

JavaDoc documentation can be found in the `docs` directory. Open `index.html` in a browser to access it.

## Authors

- Diogo Paiva, 103183
- João Fonseca, 103154
