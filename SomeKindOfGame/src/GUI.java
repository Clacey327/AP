import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class GUI {

    private static JFrame frame = new MainFrame();

    private static JTextField BalanceText;
    private static JTextField WagerText;

    private static JLabel StartingBalance;
    private static JLabel BetLabel;

    private static JButton DealButton;
    private static JButton BetButton;

    public static void gui() {

        BetButton = new JButton("Bet");
        BetButton.setBounds(20, 610, 99, 50);
        BetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DealButton.setEnabled(true);
                BetButton.setEnabled(false);

            }
        });

        frame.getContentPane().add(BetButton);

        DealButton = new JButton("Deal");
        DealButton.setBounds(121, 610, 99, 50);
        DealButton.setEnabled(false);
        DealButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BetButton.setEnabled(true);
                DealButton.setEnabled(false);

            }
        });

        frame.getContentPane().add(DealButton);

        BalanceText = new JTextField();
        BalanceText.setText("100");
        BalanceText.setBounds(131, 530, 89, 28);
        frame.getContentPane().add(BalanceText);
        BalanceText.setColumns(10);

        StartingBalance = new JLabel("Balance:");
        StartingBalance.setFont(new Font("Arial", Font.BOLD, 14));
        StartingBalance.setForeground(Color.WHITE);
        StartingBalance.setBounds(30, 536, 100, 16);
        frame.getContentPane().add(StartingBalance);

        WagerText = new JTextField();
        WagerText.setText("");
        WagerText.setBounds(131, 560, 89, 28);
        frame.getContentPane().add(WagerText);

        BetLabel = new JLabel("Enter Wager");
        BetLabel.setFont(new Font("Arial", Font.BOLD, 14));
        BetLabel.setForeground(Color.WHITE);
        BetLabel.setBounds(30, 566, 100, 16);
        frame.getContentPane().add(BetLabel);

    }

    public static void main(String[] args) {
        gui();
        frame.setVisible(true);

    }
}