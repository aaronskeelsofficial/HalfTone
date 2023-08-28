package aaronskeels.work.HalftoneMaker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import aaronskeels.work.HalftoneMaker.JnaFileChooser.JnaFileChooser;

@SuppressWarnings("serial")
public class InputPanel extends JPanel{
	public JTextField lowerPatternPanel_text, upperPatternPanel_text, tolerancePatternPanel_text,
		xDeltaPanel_text, yDeltaPanel_text, toleranceLocationPanel_text;
	public JCheckBox doRandomizeRotationPanel_box;
	
	public InputPanel() {
		setBackground(Color.red);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		setMaximumSize(new Dimension(MainV3.frame.getWidth()/4, MainV3.frame.getHeight()));
		
		//Generate Nails Stuff
		JPanel rowOnePanel = new JPanel();
		rowOnePanel.setBackground(new Color(0, 0, 0, 0));
		rowOnePanel.setOpaque(false);
		JTextField textRowOne = new JTextField(3);
		textRowOne.setText("250");
		rowOnePanel.add(textRowOne);
		
		//Generate pattern sizes
		JPanel lowerPatternPanel = new JPanel();
		lowerPatternPanel.setBackground(new Color(0, 0, 0, 0));
		lowerPatternPanel.setOpaque(false);
		JLabel lowerPatternPanel_label = new JLabel("Lower Pattern Size %");
		lowerPatternPanel_text = new JTextField(3);
		lowerPatternPanel_text.setText(".0");
		lowerPatternPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(lowerPatternPanel_text.getText());
                Main.lowerPatternSizePercent = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		lowerPatternPanel.add(lowerPatternPanel_label);
		lowerPatternPanel.add(lowerPatternPanel_text);
		//
		JPanel upperPatternPanel = new JPanel();
		upperPatternPanel.setBackground(new Color(0, 0, 0, 0));
		upperPatternPanel.setOpaque(false);
		JLabel upperPatternPanel_label = new JLabel("Upper Pattern Size %");
		upperPatternPanel_text = new JTextField(3);
		upperPatternPanel_text.setText(".02");
		upperPatternPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(upperPatternPanel_text.getText());
                Main.upperPatternSizePercent = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		upperPatternPanel.add(upperPatternPanel_label);
		upperPatternPanel.add(upperPatternPanel_text);
		//
		JPanel tolerancePatternPanel = new JPanel();
		tolerancePatternPanel.setBackground(new Color(0, 0, 0, 0));
		tolerancePatternPanel.setOpaque(false);
		JLabel tolerancePatternPanel_label = new JLabel("Pattern Size Randomized Tolerance");
		tolerancePatternPanel_text = new JTextField(3);
		tolerancePatternPanel_text.setText("0");
		tolerancePatternPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(tolerancePatternPanel_text.getText());
                Main.tolerancePatternSizePx = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		tolerancePatternPanel.add(tolerancePatternPanel_label);
		tolerancePatternPanel.add(tolerancePatternPanel_text);
		
		//Generate delta sizes
		JPanel xDeltaPanel = new JPanel();
		xDeltaPanel.setBackground(new Color(0, 0, 0, 0));
		xDeltaPanel.setOpaque(false);
		JLabel xDeltaPanel_label = new JLabel("X Delta Px");
		xDeltaPanel_text = new JTextField(3);
		xDeltaPanel_text.setText("10");
		xDeltaPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(xDeltaPanel_text.getText());
                Main.xDeltaPx = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		xDeltaPanel.add(xDeltaPanel_label);
		xDeltaPanel.add(xDeltaPanel_text);
		//
		JPanel yDeltaPanel = new JPanel();
		yDeltaPanel.setBackground(new Color(0, 0, 0, 0));
		yDeltaPanel.setOpaque(false);
		JLabel yDeltaPanel_label = new JLabel("Y Delta Px");
		yDeltaPanel_text = new JTextField(3);
		yDeltaPanel_text.setText("10");
		yDeltaPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(yDeltaPanel_text.getText());
                Main.yDeltaPx = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		yDeltaPanel.add(yDeltaPanel_label);
		yDeltaPanel.add(yDeltaPanel_text);
		//
		JPanel toleranceLocationPanel = new JPanel();
		toleranceLocationPanel.setBackground(new Color(0, 0, 0, 0));
		toleranceLocationPanel.setOpaque(false);
		JLabel toleranceLocationPanel_label = new JLabel("Pattern Location Randomized Tolerance");
		toleranceLocationPanel_text = new JTextField(3);
		toleranceLocationPanel_text.setText("0");
		toleranceLocationPanel_text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when the Enter key is pressed
            	try {
                double value = Double.valueOf(toleranceLocationPanel_text.getText());
                Main.toleranceLocationSizePx = value;
                Main.generateImage();
            	} catch (Exception ex) {
            		//do nothing
            	}
            }
        });
		toleranceLocationPanel.add(toleranceLocationPanel_label);
		toleranceLocationPanel.add(toleranceLocationPanel_text);
		
		//Generate rotation randomizer
		JPanel doRandomizeRotationPanel = new JPanel();
		doRandomizeRotationPanel.setBackground(new Color(0, 0, 0, 0));
		doRandomizeRotationPanel.setOpaque(false);
		JLabel doRandomizeRotationPanel_label = new JLabel("Randomize Rotation?");
		doRandomizeRotationPanel_box = new JCheckBox();
		doRandomizeRotationPanel_box.setSelected(false);
		doRandomizeRotationPanel_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will be executed when clicked
                boolean value = doRandomizeRotationPanel_box.isSelected();
                Main.doRandomizeRotation = value;
                Main.generateImage();
            }
        });
		doRandomizeRotationPanel.add(doRandomizeRotationPanel_label);
		doRandomizeRotationPanel.add(doRandomizeRotationPanel_box);
		
		//Generate The Generate Image Button
		JPanel generatePanel = new JPanel();
		generatePanel.setBackground(new Color(0, 0, 0, 0));
		generatePanel.setOpaque(false);
		JButton generatePanel_generate = new JButton("Generate Image");
		generatePanel_generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (Main.TargetImage != null && Main.PatternImage != null)
            		Main.generateImage();
            }
        });
		JButton generatePanel_noise = new JButton("Debug Image");
		generatePanel_noise.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (Main.TargetImage != null && Main.PatternImage != null)
            		Main.debugTargetOnOffscreen();
            }
        });
		generatePanel.add(generatePanel_generate);
		generatePanel.add(generatePanel_noise);
		
		//Generate download png
		JPanel downloadPNGPanel = new JPanel();
		JButton downloadPNGPanel_button = new JButton("Download PNG");
		downloadPNGPanel_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JnaFileChooser fileChooser = new JnaFileChooser();
            	fileChooser.addFilter("Images", "png", "tif", "tiff", "bmp", "jpg", "jpeg", "gif");
            	fileChooser.setDefaultFileName("halftone.png");
                boolean result = fileChooser.showSaveDialog(Main.frame);
                if (result == true) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().endsWith(".png"))
                    	selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
                    try {
						ImageIO.write(Main.OffscreenImage, "png", selectedFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                }
                System.out.println("Saved PNG File!");
            }
        });
		downloadPNGPanel.add(downloadPNGPanel_button);
		
		add(rowOnePanel);
		add(lowerPatternPanel);
		add(upperPatternPanel);
		add(tolerancePatternPanel);
		add(xDeltaPanel);
		add(yDeltaPanel);
		add(toleranceLocationPanel);
		add(doRandomizeRotationPanel);
		add(generatePanel);
		add(downloadPNGPanel);
	}
	
}
