import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private ServerSocket ss;
    private int numPlayers;
    private ServerSideConnection player1;
    private ServerSideConnection player2;
    private int turnMade;
    private int[] values;
    private int player1ButtonNum;
    private int player2ButtonNum;

    public GameServer() {
        System.out.println("---Game Server-----");
        numPlayers = 0;
        turnMade = 0;
        values = new int[1];

        for (int i = 0; i < values.length; i++) {
            values[i] = (int) (Math.random() * ((11)));
            System.out.println("Card value is " + values[i]);
        }

        try {
            ss = new ServerSocket(8765);
        } catch (IOException e) {
            System.out.println("Nah bitch cant run");
            e.printStackTrace();
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Seeking Connection");
            while (numPlayers < 2) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player # "  + numPlayers + " has Connected");
                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);

                if (numPlayers == 1) {
                    player1 = ssc;

                } else {
                    player2 = ssc;
                }

                Thread t = new Thread(ssc);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Nah Bitch can't run");
            e.printStackTrace();
        }
    }

    private class ServerSideConnection implements Runnable {

        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private int PlayerID;

        public ServerSideConnection(Socket s, int id) {

            socket = s;
            PlayerID = id;
            try {
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                dataOut.writeInt(PlayerID);
                dataOut.writeInt(values[0]);
                dataOut.flush();

                while (true) {
                    if (PlayerID == 1) {
                        player1ButtonNum = dataIn.readInt();
                        System.out.println("Player 1 clicked hit" + player1ButtonNum);
                        player2.sendButton(player1ButtonNum);
                    } else {
                        player2ButtonNum = dataIn.readInt();
                        System.out.println("Player 2 clicked hit" + player2ButtonNum);
                        player1.sendButton(player2ButtonNum);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendButton(int n) {
            try {
                dataOut.writeInt(n);
                dataOut.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
