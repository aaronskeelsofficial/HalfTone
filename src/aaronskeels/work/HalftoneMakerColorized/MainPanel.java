package aaronskeels.work.HalftoneMakerColorized;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel{
	public BufferedImage debugOverlayImage = new BufferedImage(1920, 1920, BufferedImage.TYPE_INT_RGB);
	public boolean isShowingDebug = false;
	
	public MainPanel() {
		setBackground(Color.green);
		addClickListener();
	}
	
	private void addClickListener() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				BufferedImage targetImageToCopy = null;
		        if (isShowingDebug)
		        	targetImageToCopy = debugOverlayImage;
		        else
		        	targetImageToCopy = Main.OffscreenImage;
				Main.openMaximizedImage(targetImageToCopy);
			}
		});
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Main.OffscreenImage == null)
        	return;
        Graphics2D g2d = (Graphics2D) g;
        
        int width = getWidth();
        int height = getHeight();
        
        double scale = Math.min((double) width / Main.OffscreenImage.getWidth(), (double) height / Main.OffscreenImage.getHeight());
        int scaledCanvasWidth = (int) (Main.OffscreenImage.getWidth() * scale);
        int scaledCanvasHeight = (int) (Main.OffscreenImage.getHeight() * scale);
        g2d.drawImage(Main.OffscreenImage, (int) ((width - scaledCanvasWidth) / 2d), (int) ((height - scaledCanvasHeight) / 2d), scaledCanvasWidth, scaledCanvasHeight, null);
    }
	
}
