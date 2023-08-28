package aaronskeels.work.HalftoneMaker;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/*
 * MainV1 works around the 32GB of precomputed influence data by splitting it into many files (one file per string)
 * This is slow. Not good.
 */
public class Main {
	//Config Numbers
	public static final int NAIL_RADIUS = 5;
	//Graphics objects
	public static final JFrame frame = new JFrame("Halftone Maker");
	public static BufferedImage OffscreenImage;
	public static BufferedImage TargetImage;
	public static final InputPanel inputPanel = new InputPanel();
	public static final MainPanel mainPanel = new MainPanel();
	public static final PreviewPanel previewPanel = new PreviewPanel();
	public static BufferedImage PatternImage;
	//Other global objects
	public static double lowerPatternSizePercent = 0;
	public static double upperPatternSizePercent = .02;
	public static double tolerancePatternSizePx = 0;
	public static InterpType interpType = InterpType.LINEAR;
	public static short[] grayscaleData;
	public static double xDeltaPx = 10;
	public static double yDeltaPx = 10;
	public static double toleranceLocationSizePx = 0;
	public static boolean doRandomizeRotation = false;
	
	public static void main(String[] args) {
		//Setup frame config
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH; // Components fill both horizontally and vertically
        constraints.weighty = 1;
        
        //Add components to frame
        constraints.gridx = 0;
        constraints.weightx = .5;
        frame.add(inputPanel, constraints);
        constraints.gridx = 1;
        constraints.weightx = 2.0;
        frame.add(mainPanel, constraints);
        constraints.gridx = 2;
        constraints.weightx = 1.0;
        frame.add(previewPanel, constraints);
        
        //Finally show frame
        frame.setVisible(true);
	}
	
	public static void updateMainPanel() {
		mainPanel.repaint();
	}
	
	public static void generateImage() {
		//Update vars from textboxes
		InputPanel p = inputPanel;
		try {
			lowerPatternSizePercent = Double.valueOf(p.lowerPatternPanel_text.getText());
		} catch (Exception e) {}
		try {
			upperPatternSizePercent = Double.valueOf(p.upperPatternPanel_text.getText());
		} catch (Exception e) {}
		try {
			tolerancePatternSizePx = Double.valueOf(p.tolerancePatternPanel_text.getText());
		} catch (Exception e) {}
		try {
			xDeltaPx = Double.valueOf(p.xDeltaPanel_text.getText());
		} catch (Exception e) {}
		try {
			yDeltaPx = Double.valueOf(p.yDeltaPanel_text.getText());
		} catch (Exception e) {}
		try {
			toleranceLocationSizePx = Double.valueOf(p.toleranceLocationPanel_text.getText());
		} catch (Exception e) {}
		doRandomizeRotation = p.doRandomizeRotationPanel_box.isSelected();
		
		clearImage(OffscreenImage, Color.white);
		boolean ifShouldResizeAccordingToWidth = getIfShouldResizeAccordingToWidth();
		Set<Integer> alreadyCalculatedCoordinates = new HashSet<>();
		Graphics2D g2d = OffscreenImage.createGraphics();
		int lengthYPixels = (int)(Math.log10(TargetImage.getHeight())+1);
		Random rand = new Random();
		for (int y = 0;y < TargetImage.getHeight();y += yDeltaPx) {
			for (int x = 0;x < TargetImage.getWidth();x += xDeltaPx) {
				int coordinateKey = (int) (x * Math.pow(10, lengthYPixels) + y);
				if (alreadyCalculatedCoordinates.contains(coordinateKey)) //Low resolution pic with low % delta yields hitting same coords numerous times. DON'T re-draw for these
					continue;
				alreadyCalculatedCoordinates.add(coordinateKey);
				
				int _1DConvertedIndex = (int) (y*TargetImage.getWidth() + x);
				int grayscaleValue = 255-grayscaleData[_1DConvertedIndex];
				double patternTargetPerc = getInterpolatedValue(0, 255, lowerPatternSizePercent, upperPatternSizePercent, grayscaleValue, interpType);
				double patternResizeRatio;
				if (ifShouldResizeAccordingToWidth)
					patternResizeRatio = (TargetImage.getWidth() * patternTargetPerc) / PatternImage.getWidth();
				else
					patternResizeRatio = (TargetImage.getHeight() * patternTargetPerc) / PatternImage.getHeight();
				double patternWidthPx = /*(int) Math.round*/(PatternImage.getWidth() * patternResizeRatio);
				double patternHeightPx = /*(int) Math.round*/(PatternImage.getHeight() * patternResizeRatio);
				
				//Handle size tolerance
				if (tolerancePatternSizePx != 0) {
					patternWidthPx = getTolerance(patternWidthPx, tolerancePatternSizePx, rand);
					patternHeightPx = getTolerance(patternHeightPx, tolerancePatternSizePx, rand);
				}
				
				//Handle location tolerance
				int pseudoX = x, pseudoY = y;
				if (toleranceLocationSizePx != 0) {
//					double shift = getTolerance(x, toleranceLocationSizePx, rand);
//					if (y == 0)
//						System.out.println(shift + " : " + x + " : " + toleranceLocationSizePx);
					pseudoX = (int) getTolerance(x, toleranceLocationSizePx, rand);
					pseudoY = (int) getTolerance(y, toleranceLocationSizePx, rand);
				}
				
				//Handle Random Rotation
				BufferedImage imageToDraw = PatternImage;
				if (doRandomizeRotation) {
					imageToDraw = rotateImage(PatternImage, rand.nextDouble()*2*Math.PI);
				}
				
				g2d.drawImage(imageToDraw, (int) ((pseudoX*6)-(patternWidthPx*6)/2), (int) ((pseudoY*6)-(patternHeightPx*6)/2), (int) (patternWidthPx*6), (int) (patternHeightPx*6), frame);
//				g2d.drawImage(imageToDraw, (int) ((x)-(patternWidthPx)/2), (int) ((y)-(patternHeightPx)/2), (int) (patternWidthPx), (int) (patternHeightPx), frame);
			}
		}
		g2d.dispose();
		
		mainPanel.repaint();
	}
	
	//
	// MISC UTILITIES
	//
	
	public static void clearImage(BufferedImage targetImage) {
		Graphics2D g2d = targetImage.createGraphics();
		g2d.setColor(new Color(0, 0, 0, 0)); // Transparent black color
        g2d.fillRect(0, 0, targetImage.getWidth(), targetImage.getHeight());
        g2d.dispose();
	}
	public static void clearImage(BufferedImage targetImage, Color overrideColor) {
		Graphics2D g2d = targetImage.createGraphics();
		g2d.setColor(overrideColor); // Transparent black color
        g2d.fillRect(0, 0, targetImage.getWidth(), targetImage.getHeight());
        g2d.dispose();
	}
	
	public static void debugTargetOnOffscreen() {
		//checkerboard offscreenimage
		boolean doUseColor1 = true;
		Color color1 = Color.green;
		Color color2 = color1.darker();
		for (int y = 0;y < OffscreenImage.getHeight();y++) {
			doUseColor1 = !doUseColor1;
			for (int x = 0;x < OffscreenImage.getWidth();x++) {
				OffscreenImage.setRGB(x, y, (doUseColor1 ? color1 : color2).getRGB());
				doUseColor1 = !doUseColor1;
			}
		}
		
		//checkerboard offscreenimage
		doUseColor1 = true;
		color1 = Color.blue;
		color2 = color1.darker();
		BufferedImage noiseImage = new BufferedImage(TargetImage.getWidth(), TargetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int y = 0;y < noiseImage.getHeight();y++) {
			doUseColor1 = !doUseColor1;
			for (int x = 0;x < noiseImage.getWidth();x++) {
				noiseImage.setRGB(x, y, (doUseColor1 ? color1 : color2).getRGB());
				doUseColor1 = !doUseColor1;
			}
		}
		
		Graphics2D g2d = OffscreenImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% opacity
		g2d.drawImage(noiseImage, 0, 0, OffscreenImage.getWidth(), OffscreenImage.getHeight(), Color.white, frame);
		g2d.dispose();
		mainPanel.repaint();
	}
	
	public static double generateRandomHash(int value1, double value2) {
		double hash = ((value1 << 16) + value2) % 360;
		return hash;
    }
	
	public static int getByteUnsigned(byte b) {
		return ((int)(b)) & 0xFF;
	}
	
	public static boolean getIfShouldResizeAccordingToWidth() {
		double widthPercentage = PatternImage.getWidth() / OffscreenImage.getWidth();
		double heightPercentage = PatternImage.getHeight() / OffscreenImage.getHeight();
		return Double.compare(widthPercentage, heightPercentage) >= 0d;
	}
	
	public static double getInterpolatedValue(double x1, double x2, double y1, double y2, double x, InterpType interpType) {
		if (interpType.equals(InterpType.LINEAR)) {
			return y1 + (x-x1)*(y2-y1)/(x2-x1);
		}
		if (interpType.equals(InterpType.EASEINOUTQUART)) {
			//TODO: This scales from -1 -> 1.
			double t = (x - x1) / (x2 - x1);
	        t = Math.max(0, Math.min(1, t)); // Ensure t stays between 0 and 1
	        if (t < 0.5) {
	        	double ease = y1 + 8 * t * t * t * t * (y2 - y1);
	        	return ease;
//	            return mapToRange(ease, 0, 1, y1, y2);
	        } else {
	            t -= 1;
	            double ease = y1 - 8 * t * t * t * t * (y2 - y1);
	            return ease;
//	            return mapToRange(ease, -1, 0, y1, y2);
	        }
		}
		return -1;
	}
	
	public static double getTolerance(double origin, double tolerance, Random rand) {
		double sign = rand.nextBoolean() ? 1 : -1;
		return origin + rand.nextDouble()*tolerance*sign;
	}
	
	public static double mapToRange(double value, double oldMin, double oldMax, double newMin, double newMax) {
        return ((value - oldMin) / (oldMax - oldMin)) * (newMax - newMin) + newMin;
    }
	
	public static void openMaximizedImage(BufferedImage targetImage) {
		JFrame frame = new JFrame("Image Preview");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Create JScroll
        JScrollPane pane = new JScrollPane();
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //Create JPanel
        ZoomableJPanel panel = new ZoomableJPanel(targetImage);
        pane.setViewportView(panel);
        
        //Add MouseWheelListener for zooming
        pane.addMouseWheelListener(e -> {
            int notches = -e.getWheelRotation();
            boolean isControlDown = e.isControlDown();

            if (isControlDown) {
                double scaleFactor = Math.pow(1.05, notches);
                double newScale = panel.getScale() * scaleFactor;

                // Limit the scale to a reasonable range
                if (newScale > 0.1 && newScale < 10.0) {
                    panel.setScale(newScale);
                    Dimension scaledSize = new Dimension((int) (panel.getPreferredSize().width * panel.getScale()), (int) (panel.getPreferredSize().height * panel.getScale()));
                    panel.setPreferredSize(scaledSize);
                    pane.revalidate();
                }
            }
        });
        //Adjust scroll speed
        JScrollBar verticalScrollBar = pane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);
        JScrollBar horizontalScrollBar = pane.getHorizontalScrollBar();
        horizontalScrollBar.setUnitIncrement(20);
        
        frame.add(pane);
        
        frame.setVisible(true);
	}
	
	public static  BufferedImage rotateImage(BufferedImage image, double radians) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radians, image.getWidth() / 2, image.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }
	
}


/*We will have the following images
 * - Target (1000x1000): This is a resolution-adjusted offscreen static image we try to recreate
 * - Computed (1000x1000): This is the offscreen image where computational additions happen
 * - Offscreen (1920x1920): This is the offscreen full res drawing with nails and strings
 */
/*We will have the following panels
 * - Input (WhateverxWhatever): This is the panel for input and config stuff
 * - Main (VisiblexVisible): This is the central panel w/ downscaled "Offscreen"
 * - Preview (WhateverxWhatever): This is the right side panel
 * 		- Target (halfheightxhalfheight): This will be a downscaled copy of "Target" image
 * 		- Computed (halfheightxhalfheight): This will be a downscaled copy of "Computed" image
 */