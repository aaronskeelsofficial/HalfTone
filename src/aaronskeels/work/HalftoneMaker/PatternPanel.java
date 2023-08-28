package aaronskeels.work.HalftoneMaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import aaronskeels.work.HalftoneMaker.JnaFileChooser.JnaFileChooser;

@SuppressWarnings("serial")
public class PatternPanel extends JPanel{
	public JPanel visualPanel = null;
	public File targetFile = null;
	
	public PatternPanel() {
		setBackground(Color.magenta);
		setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Components fill both horizontally and vertically
        constraints.weightx = 1;
		
		//Generate Input Stuff
		JButton button = new JButton("Load Pattern Image");
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JnaFileChooser fc = new JnaFileChooser();
            	fc.addFilter("Images", "png", "tif", "tiff", "bmp", "jpg", "jpeg", "gif");
            	if (fc.showOpenDialog((Window) Main.frame)) {
            		File f = fc.getSelectedFile();
            		updateImage(f);
            	}
            }
        });
		
		//Generate Visual
		visualPanel = new JPanel() {
			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (Main.PatternImage == null)
		        	return;
		        Graphics2D g2d = (Graphics2D) g;
		        
		        int width = getWidth();
		        int height = getHeight();
		        
		        double scale = Math.min((double) width / Main.PatternImage.getWidth(), (double) height / Main.PatternImage.getHeight());
		        int scaledCanvasWidth = (int) (Main.PatternImage.getWidth() * scale);
		        int scaledCanvasHeight = (int) (Main.PatternImage.getHeight() * scale);
		        g2d.drawImage(Main.PatternImage, (int) ((width - scaledCanvasWidth) / 2d), (int) ((height - scaledCanvasHeight) / 2d), scaledCanvasWidth, scaledCanvasHeight, null);
		        g2d.dispose();
		    }
		};
		visualPanel.setBackground(Color.magenta);
		visualPanel.addMouseListener(new MouseListener() {
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
				Main.openMaximizedImage(Main.PatternImage);
			}
		});
		
		//Add components
		constraints.gridy = 0;
		constraints.weighty = 1;
		add(button, constraints);
		constraints.gridy = 2;
		constraints.weighty = 10;
		add(visualPanel, constraints);
	}
	
	public void updateImage(File f) {
		targetFile = f;
		
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Main.PatternImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = Main.PatternImage.createGraphics();
        g2d.drawImage(bufferedImage, 0, 0, Main.PatternImage.getWidth(), Main.PatternImage.getHeight(), null);
        g2d.dispose();
        visualPanel.repaint();
	}
	
}
