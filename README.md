# Java Tetris Server

In this homework, we will refactor and convert our Java Tetris game into a training server for reinforcment learining. The source code is adapted from [ZetCode](https://zetcode.com/javagames/tetris/). The Python test code can be found in the ([Colab notebook](https://colab.research.google.com/drive/14mas2qTCY4FrLNn7-CB4rK-yoPJaHY3I?usp=sharing)). The Tetris server is multi-threaded and each thread has its own Tetris game. The client connects to the server, controls its own game with ASCII commands, and receives game status and screenshots from the server. The server **TetrisMTServer.java** is ready and fully functional. Our goal is to complete the Java client **TetrisTestTCPClient.java** and pass 4 unit tests.

## Communication Protocol

We use a request-response pattern. The client sends command to the server first. The server runs the command and responds with new game status and screenshots.

### Client -> Server

The client command format is a keyword + ' ' + parameters.The main commands are listed below:

| Command            | Description                               |
|--------------------|-------------------------------------------|
| start              | Start the game                            |
| move [x]           | Move current piece X steps horizontally   |
| rotate [0 \| 1]    | Rotate 0: counter-clockwise, 1: clockwise |
| drop               | Drop current piece                        |



### Client <- Server

The server response data are binary (1 + 4 + img_size + PNG screenshot). Note that the screenshot (100 * 200) is compressed in PNG format and vary in size. Therefore, we need to send the image size first. The binary packet format is:

| Bytes |  1    |    4         |  4         | Variable length |
|-------|-------|--------------|------------|------------|
| Field |IsOver | Removed Lines| Image Size | PNG file   |

The description of each field is listed below:

| Bytes    | Format  | Description             |
|----------|---------|-------------------------|
| 1        | Boolean | Is game over            |
| 4        | Integer | Number of lines removed |
| 4        | Integer | Screenshot image size   |
| Variable | PNG     | Compressed screenshot (100 * 200)   |
        

## Assignments
There are 4 unit tests. Execute `gradle test` to run all the tests. Modify the source code files under `src/main/` to make all tests green. The 4 tests are:

1. **Cilent Connection Test**: Connect to the local server. Check if the port is correct.
2. **Game Launch Test**: Send the command "start" to the server to launch the game.
3. **Image Reception Test**: Receive the PNG image from the server and save to disk.
4. **Line Removing Test**: Create multiple Square tetrominoe, move and drop to remove at leat 2 lines  
