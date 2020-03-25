import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.*;

public class Player extends JFrame {

    private int width;
    private int height;
    private Container contentPane;
    private JTextArea message;
    private JButton b1;
    private int playerID;
    private int otherPlayer;
    private int[] values;
    private int turnsMade;
    private int player1Points;
    private int player2Points;
    private boolean buttonsEnabled;
    private ClientSideConnection csc;

    public Player(int w, int h) {
        width = w;
        height = h;
        contentPane = this.getContentPane();
        message = new JTextArea();
        b1 = new JButton("Hit");
        values = new int[2];
        turnsMade = 0;
        player1Points = 0;
        player2Points = 0;

    }

    public void GUI() {

        this.setSize(width, height);
        this.setTitle("Player # " + playerID);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(5,1));
        contentPane.add(message);
        message.setText("Sup");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);
        contentPane.add(b1);
        b1.setBounds(20, 610, 99, 50);


        if (playerID == 1) {
            message.setText("Go, you're player 1");
            otherPlayer = 2;
            buttonsEnabled = true;

        } else {
            message.setText("Wait for your turn");
            otherPlayer = 1;
            buttonsEnabled = false;

            Thread t = new Thread(new Runnable() {
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        }

        ToggleButtons();
        this.setVisible(true);

    }

    public void connectToServer() {
        csc = new ClientSideConnection();
    }

    public void Buttons() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                if (ae.getSource() == b1) {

                    message.setText("You clicked hit. Wait for the next Player");
                    turnsMade++;
                    System.out.println("Turns made: " + turnsMade);

                    buttonsEnabled = false;
                    ToggleButtons();

                    player1Points += values[0];
                    System.out.println("My Points: " + player1Points);
                    csc.sendButtonNum(1);

                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            updateTurn();
                        }
                    });

                    t.start();
                }
            }
        };

        b1.addActionListener(al);
    }

    public void ToggleButtons() {
        b1.setEnabled(buttonsEnabled);
    }

    public void updateTurn() {
        int n = csc.receiveButtonNum();
        message.setText("Opponent clicked hit. Your turn.");
        player2Points += values[n-1];
        System.out.println();
        buttonsEnabled = true;
        ToggleButtons();

    }

    public class ClientSideConnection {

        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public ClientSideConnection() {

            System.out.println("----Client----");
            try {
                socket = new Socket("127.0.0.1", 8765);
                dataIn = new DataInputStream(socket.getInputStream());
                dataOut = new DataOutputStream(socket.getOutputStream());
                playerID = dataIn.readInt();
                System.out.println("Connected to Server as Player #" + playerID);
                values[0] = dataIn.readInt();
                System.out.println("Card Value  is " + values[0]);

            } catch (IOException e) {
                System.out.println("nah");
                e.printStackTrace();
            }
        }

        public void sendButtonNum(int n) {
            try {
                dataOut.writeInt(n);
                dataOut.flush();

            } catch (IOException e) {
                System.out.println("ah fuck  can't do it");
            }
        }

        public int receiveButtonNum() {
            int n = -1;
            try {
                n = dataIn.readInt();
                System.out.println("Player #" + otherPlayer + "clicked button # " + n);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return n;
        }

    }

    public static void main(String[] args) {
        Player p = new Player(500, 200);
        p.connectToServer();
        p.GUI();
        p.Buttons();

    }
}


