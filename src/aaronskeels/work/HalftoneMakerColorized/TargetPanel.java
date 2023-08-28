package aaronskeels.work.HalftoneMakerColorized;

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
public class TargetPanel extends JPanel{
	public JPanel visualPanel = null;
	public File targetFile = null;
	
	public TargetPanel() {
		setBackground(Color.magenta);
		setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Components fill both horizontally and vertically
        constraints.weightx = 1;
		
		//Generate Input Stuff
		JButton button = new JButton("Load Source Image");
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
		JButton button2 = new JButton("Invert");
		button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	invertImage();
            }
        });
		
		//Generate Visual
		visualPanel = new JPanel() {
			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (Main.TargetImage == null)
		        	return;
		        Graphics2D g2d = (Graphics2D) g;
		        
		        int width = getWidth();
		        int height = getHeight();
		        
		        double scale = Math.min((double) width / Main.TargetImage.getWidth(), (double) height / Main.TargetImage.getHeight());
		        int scaledCanvasWidth = (int) (Main.TargetImage.getWidth() * scale);
		        int scaledCanvasHeight = (int) (Main.TargetImage.getHeight() * scale);
		        g2d.drawImage(Main.TargetImage, (int) ((width - scaledCanvasWidth) / 2d), (int) ((height - scaledCanvasHeight) / 2d), scaledCanvasWidth, scaledCanvasHeight, null);
		        g2d.dispose();
		    }
		};
		visualPanel.setBackground(Color.orange);
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
				Main.openMaximizedImage(Main.TargetImage);
			}
		});
		
		//Add components
		constraints.gridy = 0;
		constraints.weighty = 1;
		add(button, constraints);
		constraints.gridy = 1;
		constraints.weighty = 1;
		add(button2, constraints);
		constraints.gridy = 2;
		constraints.weighty = 10;
		add(visualPanel, constraints);
	}
	
	public void updateImage(File f) {
		targetFile = f;
		
		//file -> image
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//precompute RGB->gray into Main grayscaleData
  		Main.grayscaleData = new short[bufferedImage.getWidth()*bufferedImage.getHeight()];
  		System.out.println("Image Size: " + bufferedImage.getWidth() + "x" + bufferedImage.getHeight() + " Grayscale: " + Main.grayscaleData.length);
  		int pixelIndex = -1;
  		Color preAllocColor;
  		for (int row = 0;row < bufferedImage.getHeight();row++) {
  			for (int col = 0;col < bufferedImage.getWidth();col++) {
  				pixelIndex++;
  				preAllocColor = new Color(bufferedImage.getRGB(col, row), true);
  				Main.grayscaleData[pixelIndex] = (short) (preAllocColor.getAlpha() == 0 ? 255 : (preAllocColor.getRed()+preAllocColor.getBlue()+preAllocColor.getGreen())/3);
  			}
  		}
        
		//Setup Main image spaces
		Main.TargetImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Main.OffscreenImage = new BufferedImage(bufferedImage.getWidth()*6, bufferedImage.getHeight()*6, BufferedImage.TYPE_INT_ARGB);
//		Main.OffscreenImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Main.clearImage(Main.OffscreenImage, Color.white);
		Main.mainPanel.repaint();
		
		//Draw image -> target viewport
        Graphics2D g2d = Main.TargetImage.createGraphics();
        g2d.drawImage(bufferedImage, 0, 0, Main.TargetImage.getWidth(), Main.TargetImage.getHeight(), null);
        g2d.dispose();
        visualPanel.repaint();
	}
	
	public void invertImage() {
		BufferedImage img = Main.TargetImage;
		int[] rgbArray = Main.TargetImage.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		for (int i = 0;i < rgbArray.length;i++) {
			Color tempColor = new Color(rgbArray[i]);
			int grayscale = 255 - tempColor.getRed();
			rgbArray[i] = new Color(grayscale, grayscale, grayscale).getRGB();
		}
		img.setRGB(0, 0, img.getWidth(), img.getHeight(), rgbArray, 0, img.getWidth());
        visualPanel.repaint();
	}
	
}
