HTTP-based mini game back-end in Java
=======================
----------

Design considerations
===========

Game Service could be a singleton (the thread safe version). I chose not to implement it as such because it holds state.

Tests (unit tests and integration tests) are available under src/test.

Execution
===========

To run the server:

>       java -jar minigame-backend.jar

The server will start in the port 8081.