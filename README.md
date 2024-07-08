# Mityorrent - A bare-bones torrenting application
TechStack:
- Java (with java.nio)

## Description
The project consits of a server and client applications. It's main feature is
to sync available torrents and users and establish peer-to-peer channels 
through which the data is streamed.

### Server
The server keeps record of the peers and their torrents.
It has endpoints to accomplish the following tasks:
- Share the available torrents and their peers.
- Add/Remove additions to the torrents list from each host.
- Delete all torrents from hosts that have lost connection.

##### Design
It's using a 3-layer architecture as it is much more intuitive for web servers. 
It makes the necessary separation of conecrns into 3 modules.

### Client
The client application has three major features:
- Sync data with the central server
- Establish a mini server that delegates sending and receiving data from other peers.
- CLI-based UI where the user can pass commands

##### Design
For the UI application I've decided on a Command Pattern behavior as it abstracts UI components 
logic from the rest of the modules and is perfect if we want to implement a feature like `undo`. 
