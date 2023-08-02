# Repository Description:
EnigmaMachine is a Java implementation of an Enigma machine encryption device, accompanied by code aimed at breaking the encryption. This project is inspired by the Computerphile video on cracking the Enigma machine, which can be viewed [here](https://www.youtube.com/watch?v=ybkkiGtJmkM).

An Enigma machine is a historical mechanical encryption device that gained significant use before and during World War II.

This repository houses a complete Java implementation of the iconic Enigma machine. The implementation includes a user-friendly JavaFX graphical user interface (GUI). It provides functionalities for both encryption and decryption, and it features a challenging code-breaking mechanism designed to evaluate the security of the encryption.

In addition to its core functionalities, the project showcases the integration of an Apache Tomcat server, introducing important concepts from the computer science industry. The Tomcat server facilitates seamless communication between clients and servers, enabling practical applications and network interactions. Each of the key components, including UBoat and Allies, interacts with the server in a client-server architecture.

# Server-Client Communication:
The integration of the Apache Tomcat server enhances the project's capabilities by enabling robust client-server communication. The UBoat, Allies, and Agents interact with the server to exchange encrypted messages, decryption attempts, and competition outcomes.

**Client-Server Architecture**: The project leverages a client-server architecture to facilitate communication between different components. This architecture is widely used in networked applications and services.

**Encryption and Decryption**: The project explores encryption techniques used to secure sensitive information and decrypt encrypted messages.

**Graphical User Interface (GUI)**: The JavaFX GUI showcases the user-friendly interface of the Enigma machine, making it accessible and intuitive for users.

**Network Interactions**: The interaction between UBoat, Allies, and the Tomcat server represents real-world network interactions and data exchange.

**Project Highlights:**
The project introduces a unique and engaging concept: a battlefield competition based on the historical context of encrypted communication during wartime.

# Key Components and Entities:

**UBoat**: Represents a German submarine responsible for initializing the encryption process, transmitting encrypted messages, and determining the winning decoding team in the competition.

**Allies**: Denotes various decoding teams of allied forces. These teams compete against each other to decipher the encrypted messages transmitted by the UBoat.

**Agent**: Refers to individual agents who are members of different decoding teams. Agents are responsible for performing the actual decoding tasks. Each agent is affiliated with a specific decoding team.

**Battlefield**: Serves as the arena where the competition unfolds. The battlefield configuration includes the names of participating decoding teams, the number of teams, and the difficulty level of the decryption challenge.


# Competition Workflow:

All participants (Allies and UBoat) declare their readiness for the competition.

The UBoat selects an encryption code and uses it to encrypt a valid message from a predefined dictionary.

The encrypted message is distributed to all participating Allies teams.

Each Allies team assigns decryption tasks to its agents.

Agents from different teams initiate the decoding process and propose potential decryption candidates.

Agents submit their candidates to a central decision-maker (DM).

The DM forwards the candidates to the UBoat, who holds the true decryption key.

The UBoat determines the winning decryption candidate and declares the winning decoding team.

Incorporating a graphical interface using JavaFX, the project visualizes the automated decoding process. It showcases the decoding efforts of various teams and agents in a competitive environment.

https://github.com/shayashkenazi/EnigmaMachine/assets/66257479/308e2123-173d-4c92-a06e-fdd57e0be0d9

