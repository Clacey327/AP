import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

	public static Hand playerCards = new Hand();
	public static Hand dealerCards = new Hand();
	private static Main Main = new Main();

	private static JFrame frame = new MainFrame();

	private static JTextField BalanceText;
	private static JTextField WagerText;

	private static JLabel StartingBalance;
	private static JLabel BetLabel;
	private static JLabel YourValue;
	private static JLabel DealersValue;
	private static JLabel WinnerLabel;

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

	private static JPanel Card1;
	private static JPanel Card2;

	private static JPanel Card3;
	private static JPanel Card4;

	private static JPanel Card5;
	private static JPanel Card6;

	public static void gui() {

		Hand deck = new Hand();
		deck.newHand();
		deck.shuffle();

		BetButton = new JButton("Bet");
		BetButton.setBounds(20, 610, 99, 50);
		BetButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				playerCards.dealCard(deck);
				playerCards.dealCard(deck);

				dealerCards.dealCard(deck);
				dealerCards.dealCard(deck);

				DealButton.setEnabled(true);
				BetButton.setEnabled(false);

			}
		});

		frame.getContentPane().add(BetButton);

		NextRoundButton = new JButton("Next Round");
		NextRoundButton.setBounds(600, 200, 100, 50);
		NextRoundButton.setVisible(false);
		NextRoundButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				playerCards.clearHand(deck);
				dealerCards.clearHand(deck);

				BetButton.setEnabled(true);
				HitButton.setEnabled(false);
				StayButton.setEnabled(false);

				Card5.setVisible(false);
				Card6.setVisible(false);
				NextRoundButton.setVisible(false);

				DealerHand1.setText(" ");
				DealerHand2.setText(" ");
				PlayerHand1.setText(" ");
				PlayerHand2.setText(" ");

				YourValue.setText(" ");
				DealersValue.setText(" ");

			}
		});

		frame.getContentPane().add(NextRoundButton);

		DealButton = new JButton("Deal");
		DealButton.setBounds(121, 610, 99, 50);
		DealButton.setEnabled(false);
		DealButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DealButton.setEnabled(false);
				DealerHand1.setText(dealerCards.getCard(0).toString());
				PlayerHand1.setText(playerCards.getCard(0).toString());
				PlayerHand2.setText(playerCards.getCard(1).toString());
				YourValue.setText("Your Value: " + playerCards.cardsValue());
				DealersValue.setText("Dealers Value: ?");
				HitButton.setEnabled(true);
				StayButton.setEnabled(true);

			}
		});

		frame.getContentPane().add(DealButton);

		StayButton = new JButton("Stay");
		StayButton.setBounds(121, 455, 99, 50);
		StayButton.setEnabled(false);
		StayButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());

				if (dealerCards.cardsValue() < 17) {

					dealerCards.dealCard(deck);

					if (dealerCards.cardsValue() > 21) {

						DealerHand2.setText(dealerCards.getCard(1).toString());
						DealerHand3.setText(dealerCards.getCard(2).toString());
						DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
						Card6.setVisible(true);
						WinnerLabel.setText("Dealer Busts. You win!");
						HitButton.setEnabled(false);
						StayButton.setEnabled(false);

					}

					if (playerCards.cardsValue() > dealerCards.cardsValue()) {

						DealerHand2.setText(dealerCards.getCard(1).toString());
						DealerHand3.setText(dealerCards.getCard(2).toString());
						DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
						WinnerLabel.setText("You win the hand.");
						HitButton.setEnabled(false);
						StayButton.setEnabled(false);

					}


				} else {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("Dealer wins the hand.");
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if (dealerCards.cardsValue() > 17) {


					if (playerCards.cardsValue() > dealerCards.cardsValue()) {

						DealerHand2.setText(dealerCards.getCard(1).toString());
						DealerHand3.setText(dealerCards.getCard(2).toString());
						DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
						WinnerLabel.setText("You win the hand.");
						HitButton.setEnabled(false);
						StayButton.setEnabled(false);

					}

				} else {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("Dealer wins the hand.");
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);
				}

			}
		});

		frame.getContentPane().add(StayButton);

		HitButton = new JButton("Hit");
		HitButton.setBounds(10, 455, 99, 50);
		HitButton.setEnabled(false);
		HitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int Money = 100;
				int wager = Integer.parseInt(WagerText.getText());

				playerCards.dealCard(deck);
				DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
				Card5.setVisible(true);
				NextRoundButton.setVisible(true);
				PlayerHand3.setText(playerCards.getCard(2).toString());

				if (dealerCards.cardsValue() < 17) {

					dealerCards.dealCard(deck);
					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					Card6.setVisible(true);
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if (dealerCards.cardsValue() > 17) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					Card6.setVisible(true);
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if ((dealerCards.cardsValue() > 21) ) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					Card6.setVisible(true);
					WinnerLabel.setText("Dealer Busts. You win!");
					Money += wager;
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if ((playerCards.cardsValue() > 21) ) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("You Busts. Dealer wins!");
					Money += wager;
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if ((dealerCards.cardsValue() == playerCards.cardsValue())) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					Card6.setVisible(true);
					WinnerLabel.setText("Push");
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if ((playerCards.cardsValue() > dealerCards.cardsValue()) && playerCards.cardsValue() < 21 ) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("You win the hand.");
					Money += wager;
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if (( dealerCards.cardsValue()) > playerCards.cardsValue() && dealerCards.cardsValue() < 21) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("Dealer wins the hand.");
					Money += wager;
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}

				if (( dealerCards.cardsValue()) > 21 &&  playerCards.cardsValue() > 21) {

					DealerHand2.setText(dealerCards.getCard(1).toString());
					DealerHand3.setText(dealerCards.getCard(2).toString());
					DealersValue.setText("Dealers Value: " + dealerCards.cardsValue());
					WinnerLabel.setText("Push");
					HitButton.setEnabled(false);
					StayButton.setEnabled(false);

				}
			}
		});

		frame.getContentPane().add(HitButton);

		BalanceText = new JTextField();
		BalanceText.setText("100");
		BalanceText.setEditable(false);
		BalanceText.setBounds(131, 530, 89, 28);
		frame.getContentPane().add(BalanceText);

		WagerText = new JTextField();
		WagerText.setText("1");
		WagerText.setBounds(131, 560, 89, 28);
		frame.getContentPane().add(WagerText);

		StartingBalance = new JLabel("Balance: ");
		StartingBalance.setFont(new Font("Arial", Font.BOLD, 14));
		StartingBalance.setForeground(Color.WHITE);
		StartingBalance.setBounds(30, 536, 100, 16);
		frame.getContentPane().add(StartingBalance);

		BetLabel = new JLabel("Enter Wager: ");
		BetLabel.setFont(new Font("Arial", Font.BOLD, 14));
		BetLabel.setForeground(Color.WHITE);
		BetLabel.setBounds(30, 566, 100, 16);
		frame.getContentPane().add(BetLabel);

		WinnerLabel = new JLabel(" ");
		WinnerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		WinnerLabel.setForeground(Color.WHITE);
		WinnerLabel.setBounds(600, 180, 300, 16);
		frame.getContentPane().add(WinnerLabel);

		YourValue = new JLabel(" ");
		YourValue.setFont(new Font("Arial", Font.BOLD, 20));
		YourValue.setForeground(Color.WHITE);
		YourValue.setBounds(600, 50, 300, 18);
		frame.getContentPane().add(YourValue);

		DealersValue = new JLabel(" ");
		DealersValue.setFont(new Font("Arial", Font.BOLD, 20));
		DealersValue.setForeground(Color.WHITE);
		DealersValue.setBounds(600, 100, 300, 16);
		frame.getContentPane().add(DealersValue);

		Card1 = new JPanel();
		Card1.setBorder(BorderFactory.createRaisedBevelBorder());
		Card1.setBackground(Color.WHITE);
		Card1.setBounds(360, 380, 140, 230);
		frame.getContentPane().add(Card1);

		Card2 = new JPanel();
		Card2.setBackground(Color.WHITE);
		Card2.setBorder(BorderFactory.createRaisedBevelBorder());
		Card2.setBounds(530, 380, 140, 230);
		frame.getContentPane().add(Card2);

		Card3 = new JPanel();
		Card3.setBorder(BorderFactory.createRaisedBevelBorder());
		Card3.setBackground(Color.WHITE);
		Card3.setBounds(360, 40, 140, 230);
		frame.getContentPane().add(Card3);

		Card4 = new JPanel();
		Card4.setBackground(Color.WHITE);
		Card4.setBorder(BorderFactory.createRaisedBevelBorder());
		Card4.setBounds(190, 40, 140, 230);
		frame.getContentPane().add(Card4);

		Card5 = new JPanel();
		Card5.setBorder(BorderFactory.createRaisedBevelBorder());
		Card5.setBackground(Color.WHITE);
		Card5.setBounds(700, 380, 140, 230);
		Card5.setVisible(false);
		frame.getContentPane().add(Card5);

		Card6 = new JPanel();
		Card6.setBorder(BorderFactory.createRaisedBevelBorder());
		Card6.setBackground(Color.WHITE);
		Card6.setBounds(20, 40, 140, 230);
		Card6.setVisible(false);
		frame.getContentPane().add(Card6);

		PlayerHand1 = new JLabel("Hidden");
		Card1.add(PlayerHand1);

		PlayerHand2 = new JLabel("Hidden");
		Card2.add(PlayerHand2);

		PlayerHand3 = new JLabel("Hidden");
		Card5.add(PlayerHand3);

		DealerHand1 = new JLabel("Hidden");
		Card3.add(DealerHand1);

		DealerHand2 = new JLabel("Hidden");
		Card4.add(DealerHand2);

		DealerHand3 = new JLabel("Hidden");
		Card6.add(DealerHand3);

		frame.setVisible(true);

	}

	public static void main(String[] args) {

		gui();

	}
}