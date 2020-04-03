import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Serializable {

    // Instantiates Client Instance Variables
    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnections Player1;
    private ServerSideConnections Player2;

    private int playerScore;
    private int player2Score;

    private String player1Button;
    private String player2Button;

    // Creates a new hand object
    Hand Deck = new Hand();

    public Server() {

        System.out.println("Server Running");
        numPlayers = 0;

        // Creates a new Deck and shuffles it. This will be send to the clients and act
        // as the dealers deck
        Deck.newDeck();
        Deck.shuffle(Deck);

        // Connects to port
        try {

            ss = new ServerSocket(8765);

        } catch (IOException ex) {
            System.out.println("Exception from server Constructor");
        }
    }

    public void acceptConnections() {

        // Accepts connections from clients and runs each one on a new thread.
        // Unfortunately, I had difficulty coding for a number greater than three

        try {

            System.out.println("Establishing Connections");

            while(numPlayers < 3) {

                Socket s = ss.accept();
                numPlayers++;

                System.out.println("Player #" + numPlayers + " has Connected");

                ServerSideConnections cc = new ServerSideConnections(s, numPlayers);

                if (numPlayers == 1) {
                    Player1 = cc;

                }

                if (numPlayers == 2) {
                    Player2 = cc;

                }

                Thread t = new Thread(cc);
                t.start();
            }

            System.out.println("Max Players Connected");

        } catch (IOException ex) {
            System.out.println("Exception from acceptConnections()");
        }
    }

    // Nested ServerSideConnections class
    private class ServerSideConnections implements Runnable, Serializable  {

        // Instantiates Client Instance Variables
        private Socket socket;
        private ObjectOutputStream objectOut;
        private ObjectInputStream objectIn;
        private int playerID;


        // Server Connection constructor
        // Creates object in and object out streams

        public ServerSideConnections(Socket s, int id) {

            socket = s;
            playerID = id;

            try {

                objectOut = new ObjectOutputStream(socket.getOutputStream());
                objectIn = new ObjectInputStream(socket.getInputStream());


            } catch (IOException ex) {
                System.out.println("Exception from SSC constructor");
            }
        }

        // Writes out the Player ID to the client
        // Writes the deck to the client
        // Sends the other players score to the respective client
        public void run() throws NullPointerException {

            try {

                objectOut.writeInt(playerID);
                objectOut.flush();

                objectOut.writeObject(Deck);
                objectOut.flush();

                while(true) {

                    if (playerID == 1) {

                        playerScore = objectIn.readInt();
                        Player2.sendScore(playerScore);


                    }

                    if (playerID == 2) {

                        player2Score = objectIn.readInt();
                        Player1.sendScore(player2Score);


                    }
                }

            } catch (IOException ex) {
                System.out.println("You must have two players to play");
            }

        }


        // Method that sends the player score to the client, flushes the data, and resets the output
        public void sendScore (int n) {

            try {

                objectOut.writeInt(n);
                objectOut.flush();
                objectOut.reset();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();

    }
}
