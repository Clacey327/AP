import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Client extends JFrame implements Serializable {

    // Instantiates GUI Instance Variables
    private static JTextField PlayerBalanceText;
    private static JTextField WagerText;
    private static JLabel StartingBalance;
    private static JLabel BetLabel;
    private static JLabel BetAmountLabel;

    private static JLabel StatusLabel;

    private static JLabel Player1Value;
    private static JLabel Player2Value;
    private static JLabel DealersValue;

    private static JLabel PlayerHand1;
    private static JLabel PlayerHand2;
    private static JLabel PlayerHand3;

    private static JLabel DealerHand1;
    private static JLabel DealerHand2;
    private static JLabel DealerHand3;

    private static JButton DealButton;
    private static JButton BetButton;
    private static JButton HitButton;
    private static JButton StayButton;
    private static JButton NextRoundButton;
    private static JButton CheckWinnerButton;

    private static JPanel Card1;
    private static JPanel Card2;
    private static JPanel Card3;
    private static JPanel Card4;
    private static JPanel Card5;
    private static JPanel Card6;

    // Instance variable that references the Client Connection class
    private ClientConnection cc;

    // Creates four hand objects
    // These two hand object represent the Players hand and the dealers hand
    private static Hand playerCards = new Hand();
    private static Hand dealerCards = new Hand();

    // These two hand objects represent the deck that deals to the player and the dealer
    // They are separate to ensure that the Dealers deck remains consistent for both clients
    // Otherwise the shuffle method would result in inconsistent dealer hands
    private static Hand PlayerDeck = new Hand();
    private static Hand DealerDeck = new Hand();

    // Booleans to check weather the player or dealer has bust
    private boolean player1Bust = false;
    private boolean player2Bust = false;
    private boolean dealerBust = false;
    private boolean gameOver = false;

    // Integer used to identify the clients as different bits of code perform different actions
    // depending on which client is interacting with them
    private int PlayerID;

    // Integer to store opponents points to be passed to the server and displayed on the Swing client
    private int otherPlayersPoints;

    // Integers that store the players available balance and wager amount
    private int Money = 100;
    private int Wager;

    // Lock that is called on Threads to ensure the integrity of the manipulated values
    private ReentrantLock valueLock = new ReentrantLock();

    // Method that creates the GUI Display objects
    public Client() {

        BetButton = new JButton("Bet");
        NextRoundButton = new JButton("Next Round");
        CheckWinnerButton = new JButton("Check Winner");
        DealButton = new JButton("Deal");
        StayButton = new JButton("Stay");
        HitButton = new JButton("Hit");

        Player1Value = new JLabel(" ");
        Player2Value = new JLabel(" ");
        DealersValue = new JLabel(" ");

        PlayerBalanceText = new JTextField(String.valueOf(Money));
        WagerText = new JTextField("1");

        StartingBalance = new JLabel("Balance: ");
        BetLabel = new JLabel("Enter Wager: ");
        BetAmountLabel = new JLabel("Bet = ");

        StatusLabel = new JLabel(" ");

        PlayerHand1 = new JLabel();
        PlayerHand2 = new JLabel();
        PlayerHand3 = new JLabel();

        DealerHand1 = new JLabel();
        DealerHand2 = new JLabel();
        DealerHand3 = new JLabel();

        Card1 = new JPanel();
        Card2 = new JPanel();
        Card3 = new JPanel();
        Card4 = new JPanel();
        Card5 = new JPanel();
        Card6 = new JPanel();

    }

    // Method that builds the Clients GUI
    public void setUpGUI() {

        this.setSize(900, 700);
        this.setTitle("Player #" + PlayerID);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);

        // Calls the ImagePanel class that allows the Felt.jpg image to display proportional
        // to the size of the client window and sets its as the window pane
        ImagePanel bgImagePanel = new ImagePanel("Felt.jpg");
        bgImagePanel.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(bgImagePanel);

        // Positions the GUI buttons and sets them to disabled
        DealButton.setBounds(20, 610, 99, 50);
        DealButton.setEnabled(false);

        NextRoundButton.setBounds(720, 250, 100, 50);
        NextRoundButton.setEnabled(false);

        CheckWinnerButton.setBounds(600, 250, 100, 50);
        CheckWinnerButton.setVisible(false);

        BetButton.setBounds(121, 610, 99, 50);
        BetButton.setEnabled(false);

        StayButton.setBounds(121, 455, 99, 50);
        StayButton.setEnabled(false);

        HitButton.setBounds(20, 455, 99, 50);
        HitButton.setEnabled(false);

        // Positions the GUI text and sets them to non-editable
        PlayerBalanceText.setBounds(131, 530, 89, 28);
        PlayerBalanceText.setEditable(false);

        WagerText.setBounds(131, 560, 89, 28);
        PlayerBalanceText.setEditable(false);

        // Positions the GUI labels
        StartingBalance.setBounds(30, 536, 100, 16);
        StartingBalance.setFont(new Font("Arial", Font.BOLD, 14));
        StartingBalance.setForeground(Color.WHITE);

        BetLabel.setBounds(30, 566, 100, 16);
        BetLabel.setFont(new Font("Arial", Font.BOLD, 14));
        BetLabel.setForeground(Color.WHITE);

        BetAmountLabel.setBounds(30, 430, 100, 16);
        BetAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        BetAmountLabel.setForeground(Color.WHITE);

        StatusLabel.setBounds(600, 200, 300, 16);
        StatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        StatusLabel.setForeground(Color.WHITE);

        Player1Value.setBounds(600, 50, 300, 18);
        Player1Value.setFont(new Font("Arial", Font.BOLD, 20));
        Player1Value.setForeground(Color.WHITE);

        Player2Value.setBounds(600, 150, 300, 18);
        Player2Value.setFont(new Font("Arial", Font.BOLD, 20));
        Player2Value.setForeground(Color.WHITE);

        DealersValue.setBounds(600, 100, 300, 16);
        DealersValue.setFont(new Font("Arial", Font.BOLD, 20));
        DealersValue.setForeground(Color.WHITE);

        // Positions the GUI cards
        Card1.setBorder(BorderFactory.createRaisedBevelBorder());
        Card1.setBackground(Color.WHITE);
        Card1.setBounds(360, 380, 140, 230);

        Card2.setBounds(530, 380, 140, 230);
        Card2.setBackground(Color.WHITE);
        Card2.setBorder(BorderFactory.createRaisedBevelBorder());

        Card3.setBounds(360, 40, 140, 230);
        Card3.setBorder(BorderFactory.createRaisedBevelBorder());
        Card3.setBackground(Color.WHITE);

        Card4.setBounds(190, 40, 140, 230);
        Card4.setBackground(Color.WHITE);
        Card4.setBorder(BorderFactory.createRaisedBevelBorder());

        Card5.setBounds(700, 380, 140, 230);
        Card5.setBorder(BorderFactory.createRaisedBevelBorder());
        Card5.setBackground(Color.WHITE);
        Card5.setVisible(false);

        Card6.setBounds(20, 40, 140, 230);
        Card6.setBorder(BorderFactory.createRaisedBevelBorder());
        Card6.setBackground(Color.WHITE);
        Card6.setVisible(false);

        // Adds buttons to content pane
        getContentPane().add(DealButton);
        getContentPane().add(NextRoundButton);
        getContentPane().add(CheckWinnerButton);
        getContentPane().add(BetButton);
        getContentPane().add(StayButton);
        getContentPane().add(HitButton);

        // Adds text to content pane
        getContentPane().add(PlayerBalanceText);
        getContentPane().add(WagerText);

        // Adds labels to content pane
        getContentPane().add(StartingBalance);
        getContentPane().add(BetLabel);
        getContentPane().add(BetAmountLabel);
        getContentPane().add(StatusLabel);
        getContentPane().add(Player1Value);
        getContentPane().add(Player2Value);
        getContentPane().add(DealersValue);

        // Adds cards to content pane
        getContentPane().add(Card1);
        getContentPane().add(Card2);
        getContentPane().add(Card3);
        getContentPane().add(Card4);
        getContentPane().add(Card5);
        getContentPane().add(Card6);

        // Adds value card identifier labels to Card JPanels
        Card1.add(PlayerHand1);
        Card2.add(PlayerHand2);
        Card5.add(PlayerHand3);

        Card3.add(DealerHand1);
        Card4.add(DealerHand2);
        Card6.add(DealerHand3);

        // Sets GUI to visible
        this.setVisible(true);

    }

    // Begins a new Round

    public void newRound() {

        // Creates a new deck and shuffles it
        PlayerDeck.newDeck();
        PlayerDeck.shuffle(PlayerDeck);

        // If the player 1, enable the deal button and alert the player with the status label
        if (PlayerID == 1) {

            StatusLabel.setText("You are Player #1");
            StatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
            DealButton.setEnabled(true);

        }

        // If the player 2, disable the deal button and alert the player with the status label
        if (PlayerID == 2) {

            StatusLabel.setText("You are Player #2. Wait for Player #1");
            StatusLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Begin a new Thread for player 2 that will enable the deal button and update
            // the status value and point value once Player one has made their turn

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {

                    valueLock.lock();
                    updateTurn();
                    valueLock.unlock();

                }
            });

            t.start();
        }
    }

    public void buttons() {

        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // Retrieves which button was clicked
                JButton b = (JButton) e.getSource();

                // If the deal button is clicked:
                if (b == DealButton) {

                    // Deal cards to the player and the dealer
                    dealerCards.dealCard(DealerDeck);
                    dealerCards.dealCard(DealerDeck);
                    playerCards.dealCard(PlayerDeck);
                    playerCards.dealCard(PlayerDeck);

                    // Display the player cards suit and values on the card object
                    PlayerHand1.setText(playerCards.getCard(0).toString());
                    PlayerHand2.setText(playerCards.getCard(1).toString());

                    // Display the value of the dealers first card. Hide the second=
                    DealerHand1.setText(dealerCards.getCard(0).toString());
                    DealerHand2.setText("Hidden");

                    // Display the value of the players hand
                    Player1Value.setText("Your Value: " + playerCards.cardsValue());
                    DealersValue.setText("Dealers Value: ?");

                    // Disable and the deal button and enable the bet button
                    DealButton.setEnabled(false);
                    BetButton.setEnabled(true);

                }

                // If the bet button is clicked:
                if (b == BetButton) {

                    // Enable/disable buttons
                    HitButton.setEnabled(true);
                    StayButton.setEnabled(true);
                    BetButton.setEnabled(false);

                    // Get the integer value from the wager text box
                    Wager = Integer.parseInt(WagerText.getText());
                    BetAmountLabel.setText("Bet = " + Wager);

                }

                // If the bet button is clicked:
                if (b == HitButton) {

                    // Deal the player a card
                    playerCards.dealCard(PlayerDeck);

                    // Set the player and dealer values
                    Player1Value.setText("Your Value: " + playerCards.cardsValue());
                    DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());

                    // Set the players third card to visible and display the value
                    // on the third card object
                    Card5.setVisible(true);
                    PlayerHand3.setText(playerCards.getCard(2).toString());

                    // Check if Player 1 bust
                    if (playerCards.cardsValue() > 21 && PlayerID == 1) {
                        player1Bust = true;

                    }

                    // Check if Player 2 bust
                    if (playerCards.cardsValue() > 21 && PlayerID == 2) {
                        player2Bust = true;

                    }

                    // If the dealers card values are less than 17
                    if (dealerCards.cardsValue() < 17) {

                        // Deal the dealer a card
                        dealerCards.dealCard(DealerDeck);

                        // Set the dealers third card to visible and display the value
                        // on the second and third card object
                        Card6.setVisible(true);
                        DealerHand2.setText(dealerCards.getCard(1).toString());
                        DealerHand3.setText(dealerCards.getCard(2).toString());

                    }

                    // If the dealers card values are greater than 17
                    if (dealerCards.cardsValue() > 17) {

                        // Show the value of the dealers second card
                        DealerHand2.setText(dealerCards.getCard(1).toString());

                    }

                    // Display the values of the player and dealers hand
                    DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
                    Player1Value.setText("Your Value: " + playerCards.cardsValue());

                    // Enable/Disable buttons
                    HitButton.setEnabled(false);
                    StayButton.setEnabled(false);
                    CheckWinnerButton.setVisible(true);

                    // Begin a new thread the sends the score the server, which relays it the
                    // updateTurn() method. Lock and unlock to ensure integrity and no interference
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            cc.sendScore(playerCards.cardsValue());
                            valueLock.lock();
                            updateTurn();
                            valueLock.unlock();

                        }
                    });

                    t.start();

                }

                // If the stay button is clicked:
                if (b == StayButton) {

                    // If the dealers hand value is less than 17,
                    // Deal the dealer a card
                    if (dealerCards.cardsValue() < 17) {
                        dealerCards.dealCard(DealerDeck);

                        // Set the dealers third card to visible and display
                        // the values on the card objects
                        Card6.setVisible(true);
                        DealerHand2.setText(dealerCards.getCard(1).toString());
                        DealerHand3.setText(dealerCards.getCard(2).toString());

                    }

                    if (dealerCards.cardsValue() > 17) {

                        // Show the dealers hand and display the value
                        DealerHand2.setText(dealerCards.getCard(1).toString());
                        DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
                    }

                    if (dealerCards.cardsValue() > 21) {

                       dealerBust = true;
                       DealersValue.setText("Dealer busts!");

                    }

                    HitButton.setEnabled(false);
                    StayButton.setEnabled(false);

                    // Begin a new thread the sends the score the server, which relays it the
                    // updateTurn() method. Lock and unlock to ensure integrity and no interference
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            cc.sendScore(playerCards.cardsValue());
                            valueLock.lock();
                            updateTurn();
                            valueLock.unlock();

                        }
                    });

                    t.start();

                }

                // If the check winner button is clicked:
                if (b == CheckWinnerButton) {

                    CheckWinnerButton.setVisible(false);
                    NextRoundButton.setEnabled(true);

                    // Call check winner method
                    CheckWinner();

                }

                // If the check nex round button is clicked:
                if (b == NextRoundButton) {

                    // Hide the third card objects and disable the next round button
                    Card5.setVisible(false);
                    Card6.setVisible(false);
                    NextRoundButton.setVisible(false);

                    // Reset all text fields
                    PlayerHand1.setText(" ");
                    PlayerHand2.setText(" ");
                    DealerHand1.setText(" ");
                    DealerHand2.setText(" ");
                    Player1Value.setText(" ");
                    Player2Value.setText(" ");
                    DealersValue.setText(" ");
                    StatusLabel.setText(" ");
                    BetAmountLabel.setText("Bet = ");
                    WagerText.setText("1");
                    PlayerBalanceText.setText(String.valueOf(Money));

                    // Clear the players and dealers hand
                    playerCards.clearHand(PlayerDeck);
                    dealerCards.clearHand(DealerDeck);

                    // Re-enable wager field
                    WagerText.setEditable(true);

                    // Re-enable the deal button for player 1
                    if (PlayerID == 1) {
                        DealButton.setEnabled(true);
                    }

                    // Call check balance and new round methods
                    checkBalance();
                    newRound();

                }
            }
        };

        // Enable Action Listeners
        BetButton.addActionListener(al);
        NextRoundButton.addActionListener(al);
        DealButton.addActionListener(al);
        StayButton.addActionListener(al);
        HitButton.addActionListener(al);
        CheckWinnerButton.addActionListener(al);

    }

    public void updateTurn()  {

        // Receive player score from server and store it in otherPlayersPoints variable
        otherPlayersPoints = cc.receiveScore();

        // Let the other player now that their opponent has made their move
        StatusLabel.setText("Opponent has made their turn. Your turn");

        // Re-enable the deal button for player 2
        if (PlayerID == 2) {

            DealButton.setEnabled(true);

        }
    }

    public void CheckWinner() {

        // Display the value of the other players points
        Player2Value.setText("Opponent value: " + otherPlayersPoints);

        // If either player 1 or player 2 busts, subtract their wager from their balance
        if (player1Bust == true || player2Bust == true) {
            StatusLabel.setText("Bust");
            Money = Integer.parseInt(PlayerBalanceText.getText()) - Wager;

        }

        // If player 1 and player 2 haven't bust, check for the highest value. Adjust balance accordingly.
        if (player1Bust == false && player2Bust == false) {

            if (otherPlayersPoints > playerCards.cardsValue() && otherPlayersPoints > dealerCards.cardsValue() || playerCards.cardsValue() > 21 ) {
                StatusLabel.setText("Opponent has won");
                Money = Integer.parseInt(PlayerBalanceText.getText()) - Wager;

            }

            if (playerCards.cardsValue() > otherPlayersPoints && playerCards.cardsValue() > dealerCards.cardsValue() || otherPlayersPoints > 21 ) {
                StatusLabel.setText("You Won!");
                Money = Integer.parseInt(PlayerBalanceText.getText()) + Wager;

            }

            if (dealerCards.cardsValue() > otherPlayersPoints && dealerCards.cardsValue() > playerCards.cardsValue()) {
                StatusLabel.setText("Dealer Won!");
                Money = Integer.parseInt(PlayerBalanceText.getText()) - Wager;

            }
        }

        // Enable next Round button
        NextRoundButton.setVisible(true);

    }

    // This method was intended to check the players balance
    // If either player's balance fell to zero, the game ends
    public void checkBalance() {

        if (Money <= 0 && PlayerID == 1) {
            StatusLabel.setText("You are out of money. Game Over");
            DealButton.setEnabled(false);
            gameOver = true;

        }

        if (Money <= 0 && PlayerID == 2) {
            StatusLabel.setText("You are out of money. Game Over");
            DealButton.setEnabled(false);
            gameOver = true;

        }
    }

    // Creates the client connection object
    public void ConnectToServer() {

        cc = new ClientConnection();

    }

    // Nested client connection class
    private class ClientConnection implements Serializable {

        // Instantiates Client Instance Variables
        private Socket socket;
        private ObjectInputStream objectIn;
        private ObjectOutputStream objectOut;

        // Client Connection constructor
        // Establishes the connection host and port
        // Creates the object in and object out streams
        // Reads the Player ID from the server
        // Reads the deck form the server and casts it as a hand object
        public ClientConnection() {

            try {

                socket = new Socket("localhost", 8765);

                objectIn = new ObjectInputStream(socket.getInputStream());
                objectOut = new ObjectOutputStream(socket.getOutputStream());

                PlayerID = objectIn.readInt();
                DealerDeck = (Hand) objectIn.readObject();

                System.out.println("Connected to server as Player #" + PlayerID + ".");

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Exception from ClientConnection() " + ex);

            }
        }

        // Method that sends the player score to the server, flushes the data, and resets the output
        public void sendScore (int n) {

            try {

                objectOut.writeInt(n);
                objectOut.flush();
                objectOut.reset();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        // Method that receives the player score from the server and returns it
        public int receiveScore() {

            int n = 0;
            try {

                n = objectIn.readInt();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return n;
        }

    }

    public static void main(String[] args) {
        Client p = new  Client();
        p.ConnectToServer();
        p.setUpGUI();
        p.newRound();
        p.buttons();

    }
}