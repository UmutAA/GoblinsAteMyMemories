# OOP Course Project - Terminal-Based Java Game

This repository contains an interactive, terminal-based game developed in Java as a term project for the Object-Oriented Programming (OOP) course.

## Project Purpose and Overview
The primary objective of this project is to apply core Object-Oriented Programming concepts in a practical scenario. The entire application runs directly within the terminal, processing real-time user inputs through the command line and displaying text-based visual game states.

---

## Applied OOP Concepts and Technical Implementation

The system architecture leverages fundamental software engineering principles and Java features:

* **Encapsulation:** Critical game data (such as player attributes, statistics, and game states) are protected using appropriate access modifiers (`private`, `protected`) and exposed safely via getter and setter methods.
* **Inheritance:** Redundant code is minimized by deriving specific entities (e.g., distinct character types, items, or enemies) from generic base classes to establish clear hierarchical structures.
* **Polymorphism:** Method overriding and interfaces are utilized to allow identical method triggers to behave uniquely depending on the runtime context of the objects.
* **Abstraction:** Essential game loops, managers, and entities decouple definition from implementation using abstract classes or interfaces to ensure a modular design.
* **File I/O:** Story lines have been storaged as txt files for cleaner code.
---

## How to Play

1. Run the application to display the main interactive terminal menu.
2. Follow the command-line prompts and input numbers or characters to make actions.
3. Objective: To defeat every enemy and reach the end.

---

## Getting Started and Execution

### Prerequisites
* Java Development Kit (JDK) 8 or higher
* Any standard Java IDE (e.g., IntelliJ IDEA, Eclipse) or a command-line terminal

### Execution via Terminal
1. Clone the repository to your local machine
2. Navigate into the root project directory
3. Compile the java source files
4. Run the compiled application:
   ```bash
   java GameEmgine
   ```

---
## License

MIT
