package com.mycompany.javaexifviewer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;



/**
 * Simple Photo Renamer Desktop Application
 * @author mario szocs
 * 
 * Used library: javaxt.io.Image
 * The javaxt.io.Image class is designed to simplify reading, writing, and manipulating image files.
 * https://www.javaxt.com/javaxt-core/io/Image/
 *
 */

public class Swing {

	// Variables declaration
	private JFrame frame;
	private static JTextField textFieldLocation;
	private static JTextField textFieldPictureDescription;
	private static JTextField textFieldStartNumber;
	
	private static JRadioButton radioButton01;
	private static JRadioButton radioButton02;
	private static JRadioButton radioButton03;
	private static JRadioButton radioButton04;
	private static JRadioButton radioButton05;
	
	private static JLabel lblNewLabel;
	private static JLabel lblNewLabel_1;
	private static JLabel lblNewLabel_2;
	private static JLabel lblNewLabel_3;
	private static JLabel lblNewLabel_4;
	private static JLabel labelForProgressBar;
	
	JFileChooser fileChooser;
	JProgressBar progressBar;

	private static String FOLDER_PATH;
	private static int numberOfImages = 0;
	private static int numberingOfPictureFromThatNumber;
	private static String date;
	private static String dateOfShooting;
	private static String hour;
	private static int initialSerialNumber = 1;
	private static String descriptionOfPhotos;
	private static String changedImageName;
	private static File myFolder;
	private static File[] fileArray;
	private static File myFile;
	private static Path filePath;
	private static javaxt.io.Image image;
	private static java.util.HashMap<Integer, Object> exif;
	private static final int PROGRESSBAR_MINIMUM = 0;
	int progressBarIter = 0;
	int iteration = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Swing window = new Swing();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	
	/**
	 * Create the application.
	 */
	public Swing() {
		initialize();
		FOLDER_PATH = textFieldLocation.getText();
	}

	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Simple Photo Renamer");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 13));
		frame.setBounds(100, 100, 731, 452);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.DARK_GRAY);

		//ProgressBar
		progressBar = new JProgressBar();
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setFont(new Font("Arial", Font.BOLD, 16));
		progressBar.setBounds(281, 340, 369, 37);
		progressBar.setVisible(true);
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		frame.getContentPane().add(progressBar);

		JButton StartTheProcessButton = new JButton("Start the Process");
		StartTheProcessButton.setForeground(Color.BLACK);
		StartTheProcessButton.setBackground(new Color(240, 230, 140));
		StartTheProcessButton.setFont(new Font("Arial", Font.BOLD, 17));
		StartTheProcessButton.addActionListener(new ActionListener() {

			
			
			public void actionPerformed(ActionEvent e) {
				
				FOLDER_PATH = textFieldLocation.getText();
				myFolder = new File(FOLDER_PATH);
				fileArray = myFolder.listFiles();
				numberOfImages = fileArray.length;
				
				//Progress Bar Settings
				progressBar.setMinimum(PROGRESSBAR_MINIMUM);
				progressBarIter = 10000/numberOfImages;
				progressBar.setMaximum(numberOfImages*progressBarIter);
				
				
				
				
				
				
				
				
				
				
				
				//Set the start number
				try {
					numberingOfPictureFromThatNumber = Integer.parseInt(textFieldStartNumber.getText());
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
					textFieldStartNumber.setText("");
					textFieldStartNumber.requestFocus();
					return;
				}
				
				
				
				// Numbering of Pictures from given number ...
				if (radioButton01.isSelected()) {
					try {
						changePicsNameToNumbering();
					} catch (IOException ex) {
						Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

				// Date of Shooting
				if (radioButton02.isSelected()) {
					descriptionOfPhotos = textFieldPictureDescription.getText();
					changePictureNameDateOfShooting();
				}

				// Date of Shooting + Description Of Photos
				if (radioButton03.isSelected()) {
					descriptionOfPhotos = textFieldPictureDescription.getText();
					changePictureNameDateWithDescription();
				}

				// Numbering of picture + Date of shooting + Description
				if (radioButton04.isSelected()) {
					descriptionOfPhotos = textFieldPictureDescription.getText();
					changePictureNameToNumberingDateAndDescription();
				}

				// Date of edit
				if (radioButton05.isSelected()) {
					descriptionOfPhotos = textFieldPictureDescription.getText();
					try {
						changePictureNameDateOfEdit();
					} catch (IOException ex) {
						Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		});

		StartTheProcessButton.setBounds(27, 320, 218, 57);
		frame.getContentPane().add(StartTheProcessButton);

		JLabel labelLocation = new JLabel("Location of the folder:");
		labelLocation.setForeground(Color.WHITE);
		labelLocation.setFont(new Font("Arial", Font.BOLD, 13));
		labelLocation.setBounds(27, 19, 155, 16);
		frame.getContentPane().add(labelLocation);

		textFieldLocation = new JTextField();
		textFieldLocation.setFont(new Font("Arial", Font.PLAIN, 13));
		textFieldLocation.setForeground(Color.WHITE);
		textFieldLocation.setBackground(Color.DARK_GRAY);
//		textFieldLocation.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				fileChooser = new JFileChooser();
//				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				int option = fileChooser.showOpenDialog(frame);
//				File selectedDirectory = fileChooser.getSelectedFile();
//				textFieldLocation.setText("" + selectedDirectory);
//			}
//		});
		textFieldLocation.setText("C:\\Users\\mario\\Desktop\\Pictures");
		textFieldLocation.setBounds(204, 16, 268, 22);
		frame.getContentPane().add(textFieldLocation);
		textFieldLocation.setColumns(10);

		JLabel labelPictureDescription = new JLabel("Photo description:");
		labelPictureDescription.setForeground(Color.WHITE);
		labelPictureDescription.setFont(new Font("Arial", Font.BOLD, 13));
		labelPictureDescription.setBounds(27, 54, 140, 16);
		frame.getContentPane().add(labelPictureDescription);

		textFieldPictureDescription = new JTextField();
		textFieldPictureDescription.setBackground(Color.DARK_GRAY);
		textFieldPictureDescription.setForeground(Color.WHITE);
		textFieldPictureDescription.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textFieldPictureDescription.setText("");
			}
		});
		textFieldPictureDescription.setFont(new Font("Arial", Font.PLAIN, 13));
		textFieldPictureDescription.setText("Sample description");
		textFieldPictureDescription.setBounds(204, 51, 268, 22);
		frame.getContentPane().add(textFieldPictureDescription);
		textFieldPictureDescription.setColumns(10);

		JLabel labelChoosingAnOption = new JLabel("Choose an option to rename your PHOTOS:");
		labelChoosingAnOption.setForeground(Color.WHITE);
		labelChoosingAnOption.setFont(new Font("Arial", Font.BOLD, 15));
		labelChoosingAnOption.setBounds(27, 103, 343, 16);
		frame.getContentPane().add(labelChoosingAnOption);

		radioButton01 = new JRadioButton("Numbering of Photos from that number");
		radioButton01.setFont(new Font("Arial", Font.PLAIN, 14));
		radioButton01.setForeground(Color.WHITE);
		radioButton01.setSelected(true);
		radioButton01.setBackground(Color.DARK_GRAY);
		radioButton01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButton01.isSelected()) {
					radioButton02.setSelected(false);
					radioButton03.setSelected(false);
					radioButton04.setSelected(false);
					radioButton05.setSelected(false);
				}
			}
		});
		radioButton01.setBounds(27, 136, 294, 25);
		frame.getContentPane().add(radioButton01);

		textFieldStartNumber = new JTextField();
		textFieldStartNumber.setFont(new Font("Arial", Font.BOLD, 13));
		textFieldStartNumber.setForeground(Color.WHITE);
		textFieldStartNumber.setBackground(Color.DARK_GRAY);
		textFieldStartNumber.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textFieldStartNumber.setText("");
			}
		});
		textFieldStartNumber.setText("1");
		textFieldStartNumber.setBounds(344, 137, 53, 22);
		frame.getContentPane().add(textFieldStartNumber);
		textFieldStartNumber.setColumns(10);

		radioButton02 = new JRadioButton("Just Date of Shooting");
		radioButton02.setForeground(Color.WHITE);
		radioButton02.setFont(new Font("Arial", Font.PLAIN, 14));
		radioButton02.setBackground(Color.DARK_GRAY);
		radioButton02.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButton02.isSelected()) {
					radioButton01.setSelected(false);
					radioButton03.setSelected(false);
					radioButton04.setSelected(false);
					radioButton05.setSelected(false);
				}
			}
		});
		radioButton02.setBounds(27, 166, 275, 25);
		frame.getContentPane().add(radioButton02);

		radioButton03 = new JRadioButton("Date of Shooting with Description");
		radioButton03.setFont(new Font("Arial", Font.PLAIN, 14));
		radioButton03.setForeground(Color.WHITE);
		radioButton03.setBackground(Color.DARK_GRAY);
		radioButton03.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButton03.isSelected()) {
					radioButton02.setSelected(false);
					radioButton01.setSelected(false);
					radioButton04.setSelected(false);
					radioButton05.setSelected(false);
				}
			}
		});
		radioButton03.setBounds(27, 196, 259, 25);
		frame.getContentPane().add(radioButton03);

		radioButton04 = new JRadioButton("Numb. of Photos + Date of Shooting + Description");
		radioButton04.setFont(new Font("Arial", Font.PLAIN, 14));
		radioButton04.setForeground(Color.WHITE);
		radioButton04.setBackground(Color.DARK_GRAY);
		radioButton04.setEnabled(true);
		radioButton04.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButton04.isSelected()) {
					radioButton02.setSelected(false);
					radioButton03.setSelected(false);
					radioButton01.setSelected(false);
					radioButton05.setSelected(false);
				}
			}
		});
		radioButton04.setBounds(27, 226, 355, 25);
		frame.getContentPane().add(radioButton04);

		radioButton05 = new JRadioButton("Date of edit");
		radioButton05.setFont(new Font("Arial", Font.PLAIN, 14));
		radioButton05.setForeground(Color.WHITE);
		radioButton05.setBackground(Color.DARK_GRAY);
		radioButton05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButton05.isSelected()) {
					radioButton02.setSelected(false);
					radioButton03.setSelected(false);
					radioButton04.setSelected(false);
					radioButton01.setSelected(false);
				}
			}
		});
		radioButton05.setBounds(27, 256, 127, 25);
		frame.getContentPane().add(radioButton05);

		lblNewLabel = new JLabel("001, 012, 123, ...");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(416, 140, 234, 16);
		frame.getContentPane().add(lblNewLabel);

		lblNewLabel_1 = new JLabel("2020-08-17_10-28-24 ");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(416, 170, 234, 16);
		frame.getContentPane().add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel("2020-08-17_10-28-24 - Description");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setBounds(416, 200, 234, 16);
		frame.getContentPane().add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("012 - 2020-01-01_10-32-15 - Description");
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_3.setBounds(416, 230, 285, 16);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("012 - 2020-8-17_10-32-15 - Description");
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_4.setBounds(416, 260, 268, 16);
		frame.getContentPane().add(lblNewLabel_4);

		labelForProgressBar = new JLabel("");
		labelForProgressBar.setForeground(Color.WHITE);
		labelForProgressBar.setFont(new Font("Arial", Font.BOLD, 13));
		labelForProgressBar.setBounds(376, 321, 170, 16);
		frame.getContentPane().add(labelForProgressBar);
		
		JButton btnFolderChooser = new JButton("Select a folder");
		btnFolderChooser.setBackground(new Color(240, 230, 140));
		btnFolderChooser.setFont(new Font("Arial", Font.BOLD, 14));
		btnFolderChooser.setForeground(Color.BLACK);
		btnFolderChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser(); 
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int option = fileChooser.showOpenDialog(frame);
		        File selectedDirectory = fileChooser.getSelectedFile();
		        textFieldLocation.setText(""+selectedDirectory);
				
			}
		});
		btnFolderChooser.setBounds(496, 18, 154, 19);
		frame.getContentPane().add(btnFolderChooser);
	}


	

	
	/**
	 * Just Numbering of Photos
	 * Etc.: 001, 020, 312
	 * @throws IOException
	 */
	public void changePicsNameToNumbering() throws IOException {
		for (int i = 0; i < numberOfImages; i++) {
			File myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			
			//Repaint the labelForProgressBar & progressBar
			labelForProgressBar.setText("Renaming the " + (i+1) + ". Photo!");
			labelForProgressBar.paintImmediately(labelForProgressBar.getVisibleRect());
			progressBar.setValue(progressBarIter*(i+1));
			progressBar.paintImmediately(progressBar.getVisibleRect());
			
			int numbToImageName = i + numberingOfPictureFromThatNumber;
			
			if(numbToImageName <= 9) {
				changedImageName = "00" + numbToImageName;
			}
			
			else if(numbToImageName > 9 && numbToImageName <= 99) {
				changedImageName = "0" + numbToImageName;
			} 
			
			else {
				changedImageName = "" + numbToImageName;
			}
						
			myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
		}
		JOptionPane.showMessageDialog(frame, "" +numberOfImages+ " Photos have been renamed!");
	}
	
	
	
	
	/**
	 * Just Date of Shooting
	 * "2020-08-17_10-28-24"
	 */
	public void changePictureNameDateOfShooting() { 

		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

			//Repaint the labelForProgressBar & progressBar
			labelForProgressBar.setText("Renaming the " + (i+1) + ". Photo!");
			labelForProgressBar.paintImmediately(labelForProgressBar.getVisibleRect());
			progressBar.setValue(progressBarIter*(i+1));
			progressBar.paintImmediately(progressBar.getVisibleRect());
			
			image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
			exif = image.getExifTags();

			if (exif.get(0x0132) != null) {
				dateOfShooting = exif.get(0x0132).toString().replace(":", "-");
				String date = dateOfShooting.substring(0, 10);
				String time = dateOfShooting.substring(11, 19);
				changedImageName = date + "_" + time;

				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			} else {
				myFile.renameTo(new File(FOLDER_PATH + "\\" + (i + 1) + " - " + descriptionOfPhotos + ".jpg"));
			}
		}
		JOptionPane.showMessageDialog(frame, "" +numberOfImages+ " Photos have been renamed!");
	}
	
	
	
	

	
	/**
	 * Date of Shooting + Description
	 * "2020-08-17_10-28-24  - Sample description..."
	 */
	public void changePictureNameDateWithDescription() { 

		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

			//Repaint the labelForProgressBar & progressBar
			labelForProgressBar.setText("Renaming the " + (i+1) + ". Photo!");
			labelForProgressBar.paintImmediately(labelForProgressBar.getVisibleRect());
			progressBar.setValue(progressBarIter*(i+1));
			progressBar.paintImmediately(progressBar.getVisibleRect());
			
			image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
			exif = image.getExifTags();

			//Exif.Image.DateTime 0x0132 - The date and time of image creation.
			if (exif.get(0x0132) != null) {
				dateOfShooting = exif.get(0x0132).toString().replace(":", "-");
				String date = dateOfShooting.substring(0, 10);
				String time = dateOfShooting.substring(11, 19);
				changedImageName = date + "_"+ time +" - "+ descriptionOfPhotos;

				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			} else {
				myFile.renameTo(new File(FOLDER_PATH + "\\" + i + " - " + descriptionOfPhotos + ".jpg"));
			}
		}
		JOptionPane.showMessageDialog(frame, "" +numberOfImages+ " Photos have been renamed!");
	}
	
	
	
	
	
	
	
	
	/**
	 * Numbering of Photos + Date of Shooting + Description
	 * "012 - 2020-08-17_10-28-24  - Sample description..." 
	 */
	public void changePictureNameToNumberingDateAndDescription() { 

		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();
			
			//Repaint the labelForProgressBar & progressBar
			labelForProgressBar.setText("Renaming the " + (i+1) + ". Photo!");
			labelForProgressBar.paintImmediately(labelForProgressBar.getVisibleRect());
			progressBar.setValue(progressBarIter*(i+1));
			progressBar.paintImmediately(progressBar.getVisibleRect());

			image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
			exif = image.getExifTags();

			int numbToImageName = i + numberingOfPictureFromThatNumber;
			
			//Exif.Image.DateTime 0x0132 - The date and time of image creation.
			if (exif.get(0x0132) != null) {
				dateOfShooting = exif.get(0x0132).toString().replace(":", "-");
				String date = dateOfShooting.substring(0, 10);
				String time = dateOfShooting.substring(11, 19);
	
				if (numbToImageName <= 9) {
					changedImageName = "00" + numbToImageName + " - " + date + "_" + time + " - " + descriptionOfPhotos;
				}
				else if (numbToImageName > 9 && numbToImageName <= 99) {
					changedImageName = "0" + numbToImageName + " - " + date + "_" + time + " - " + descriptionOfPhotos;
				} else {
					changedImageName = numbToImageName + " - " + date + "_" + time + " - " + descriptionOfPhotos;
				}		
				
				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			} else {
				
				if (numbToImageName <= 9) {
					changedImageName = "00" + numbToImageName;
				}
				else if (numbToImageName > 9 && numbToImageName <= 99) {
					changedImageName = "0" + numbToImageName;
				} else {
					changedImageName = ""+numbToImageName;
				}	
				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + " - " + descriptionOfPhotos + ".jpg"));
			}
		}
		JOptionPane.showMessageDialog(frame, "" +numberOfImages+ " Photos have been renamed!");
	}
	
	

	
	
	
	/**
	 * Numbering of Photos + Date of Edit + Description
	 * "012 - 2020-08-17_10-28-24  - Sample description..."
	 * @throws IOException
	 */
	public void changePictureNameDateOfEdit() throws IOException { // Date of edit
		
		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

			//Repaint the labelForProgressBar & progressBar
			labelForProgressBar.setText("Renaming the " + (i+1) + ". Photo!");
			labelForProgressBar.paintImmediately(labelForProgressBar.getVisibleRect());
			progressBar.setValue(progressBarIter*(i+1));
			progressBar.paintImmediately(progressBar.getVisibleRect());
			
			BasicFileAttributes attributes = null;

			try {
				attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
			} catch (IOException exception) {
				System.out.println("Exception handled when trying to get file " + "attributes: " + exception.getMessage());
			}

			long milliseconds = attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS);
			if ((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE)) {
				Date lastModifiedDate = new Date(attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS));
				date = (lastModifiedDate.getYear() + 1900) + "-" + (lastModifiedDate.getMonth() + 1) + "-" + lastModifiedDate.getDate();
				hour = lastModifiedDate.getHours() + "-" + lastModifiedDate.getMinutes() + "-" + lastModifiedDate.getSeconds();

				//Pictures numbering format: 001, 010, ...
				if (i <= 8) {
					changedImageName = "00" + (i + initialSerialNumber) + " - " + date + "_" + hour + " - " + descriptionOfPhotos;
				}
				else if (i > 8 && i <= 98) {
					changedImageName = "0" + (i + initialSerialNumber) + " - " + date + "_" + hour + " - " + descriptionOfPhotos;
				} else {
					changedImageName = (i + initialSerialNumber) + " - " + date + "_" + hour + " - " + descriptionOfPhotos;
				}
				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			}
		}
		JOptionPane.showMessageDialog(frame, "" +numberOfImages+ " Photos have been renamed!");
	}

	
	
	
	/**
	 * TODO new functionality
	 * Print photo data
	 */
	public void photoInfo() {
		// Open Image and Get EXIF Metadata
		// javaxt.io.Image image = new javaxt.io.Image("/photo.jpg");
		image = new javaxt.io.Image("C:\\Users\\mario\\Desktop\\pics\\1.jpg");
		exif = image.getExifTags();

		// Print Camera Info
		System.out.println("EXIF Fields: " + exif.size());
		System.out.println("-----------------------------");
		System.out.println("Date: " + exif.get(0x0132)); // 0x9003
		System.out.println("Camera: " + exif.get(0x0110));
		System.out.println("Manufacturer: " + exif.get(0x010F));
	}
	
	
	
}
