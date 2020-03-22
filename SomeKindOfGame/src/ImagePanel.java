import javax.swing.*;
import java.awt.*;

class ImagePanel extends JPanel {
	private Image img;

	public ImagePanel(String imgStr) {
		this.img = new ImageIcon(imgStr).getImage();
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setLayout(null);
	}

	public void paintComponent(Graphics g) { // Draw the image to the JPanel
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}