package com.mycompany.javaexifviewer;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * Simple Photo Renaming Desktop Application
 * @author mario szocs
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
	private static JLabel jProgressBar1;

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
		frame = new JFrame();
		frame.setBounds(100, 100, 731, 452);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.ORANGE);

		JButton StartTheProcessButton = new JButton("Start the Process");
		StartTheProcessButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Numbering of Pictures from given number ...
				if (radioButton01.isSelected()) {
					try {
						try {
							numberingOfPictureFromThatNumber = Integer.parseInt(textFieldStartNumber.getText());
						} catch (Exception ee) {
							JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
							textFieldStartNumber.setText("");
							textFieldStartNumber.requestFocus();
							return;
						}
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

		StartTheProcessButton.setBounds(27, 320, 183, 57);
		frame.getContentPane().add(StartTheProcessButton);

		JLabel labelLocation = new JLabel("Location of the folder:");
		labelLocation.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelLocation.setBounds(27, 19, 155, 16);
		frame.getContentPane().add(labelLocation);

		textFieldLocation = new JTextField();
		textFieldLocation.setText("C:\\Users\\mario\\Desktop\\Pictures");
		textFieldLocation.setBounds(204, 16, 268, 22);
		frame.getContentPane().add(textFieldLocation);
		textFieldLocation.setColumns(10);

		JLabel labelPictureDescription = new JLabel("Picture description:");
		labelPictureDescription.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelPictureDescription.setBounds(27, 54, 140, 16);
		frame.getContentPane().add(labelPictureDescription);

		textFieldPictureDescription = new JTextField();
		textFieldPictureDescription.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textFieldPictureDescription.setText("");
			}
		});
		textFieldPictureDescription.setFont(new Font("Tahoma", Font.ITALIC, 13));
		textFieldPictureDescription.setText("Sample Description");
		textFieldPictureDescription.setBounds(204, 51, 268, 22);
		frame.getContentPane().add(textFieldPictureDescription);
		textFieldPictureDescription.setColumns(10);

		JLabel labelChoosingAnOption = new JLabel("Choose an option to name for your PICTURES");
		labelChoosingAnOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		labelChoosingAnOption.setBounds(27, 97, 343, 16);
		frame.getContentPane().add(labelChoosingAnOption);

		radioButton01 = new JRadioButton("Numbering of pictures from that number");
		radioButton01.setSelected(true);
		radioButton01.setBackground(Color.ORANGE);
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
		radioButton01.setBounds(27, 136, 275, 25);
		frame.getContentPane().add(radioButton01);

		textFieldStartNumber = new JTextField();
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
		radioButton02.setBackground(Color.ORANGE);
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
		radioButton03.setBackground(Color.ORANGE);
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

		radioButton04 = new JRadioButton("Numb. of Picture + Date of Shooting + Description");
		radioButton04.setBackground(Color.ORANGE);
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
		radioButton04.setBounds(27, 226, 339, 25);
		frame.getContentPane().add(radioButton04);

		radioButton05 = new JRadioButton("Date of edit");
		radioButton05.setBackground(Color.ORANGE);
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

		lblNewLabel = new JLabel("Etc.: 001, 012, 123, ...");
		lblNewLabel.setBounds(416, 140, 234, 16);
		frame.getContentPane().add(lblNewLabel);

		lblNewLabel_1 = new JLabel("Etc.: 2020-08-17_10-28-24 ");
		lblNewLabel_1.setBounds(416, 170, 234, 16);
		frame.getContentPane().add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel("Etc.: 2020-08-17_10-28-24 - Description");
		lblNewLabel_2.setBounds(416, 200, 234, 16);
		frame.getContentPane().add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("Etc.: 012. - 2020-01-01_10-32-15 - Description");
		lblNewLabel_3.setBounds(416, 230, 285, 16);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("Etc.: 012. - 2020-8-17_10-32-15 - Description");
		lblNewLabel_4.setBounds(416, 260, 268, 16);
		frame.getContentPane().add(lblNewLabel_4);

		jProgressBar1 = new JLabel("ProgressBar");
		jProgressBar1.setBounds(292, 340, 56, 16);
		frame.getContentPane().add(jProgressBar1);
	}

	
	
	
	/**
	 * Exiv2
	 * is a Cross-platform C++ library and a command line utility to manage image metadata.
	 * https://www.exiv2.org/tags.html
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
	
	
	//TODO
	public void changePictureNameToNumberingDateAndDescription() { // "012. - 2020-08-17_10-28-24  - Description"
		
	}
	

	public void changePictureNameDateWithDescription() { // "2020-08-17_10-28-24  - Description"
		FOLDER_PATH = textFieldLocation.getText();
		myFolder = new File(FOLDER_PATH);
		fileArray = myFolder.listFiles();
		numberOfImages = fileArray.length;

		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

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
				myFile.renameTo(new File(FOLDER_PATH + "\\" + i + ". - " + descriptionOfPhotos + ".jpg"));
			}
		}
	}
	
	
	
	

	public void changePictureNameDateOfShooting() { // "2020-08-17_10-28-24"
		FOLDER_PATH = textFieldLocation.getText();
		myFolder = new File(FOLDER_PATH);
		fileArray = myFolder.listFiles();
		numberOfImages = fileArray.length;

		for (int i = 0; i < numberOfImages; i++) {
			// jProgressBar1.setText("Processing the " + i + "photos");
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

			image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
			exif = image.getExifTags();

			if (exif.get(0x0132) != null) {
				dateOfShooting = exif.get(0x0132).toString().replace(":", "-");
				String date = dateOfShooting.substring(0, 10);
				String time = dateOfShooting.substring(11, 19);
				changedImageName = date + "_" + time;

				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			} else {
				myFile.renameTo(new File(FOLDER_PATH + "\\" + (i + 1) + ". - " + descriptionOfPhotos + ".jpg"));
			}
		}
	}

	
	
	
	
	public void changePictureNameDateOfEdit() throws IOException { // Date of edit
		FOLDER_PATH = textFieldLocation.getText();
		myFolder = new File(FOLDER_PATH);
		fileArray = myFolder.listFiles();
		numberOfImages = fileArray.length;

		for (int i = 0; i < numberOfImages; i++) {
			myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			filePath = myFile.toPath();

			BasicFileAttributes attributes = null;

			try {
				attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
			} catch (IOException exception) {
				System.out.println(
						"Exception handled when trying to get file " + "attributes: " + exception.getMessage());
			}

			long milliseconds = attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS);
			if ((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE)) {
				Date lastModifiedDate = new Date(attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS));
				date = (lastModifiedDate.getYear() + 1900) + "-" + (lastModifiedDate.getMonth() + 1) + "-"
						+ lastModifiedDate.getDate();
				hour = lastModifiedDate.getHours() + "-" + lastModifiedDate.getMinutes() + "-"
						+ lastModifiedDate.getSeconds();

				
				if (i <= 9) {
					changedImageName = "00" + (i + initialSerialNumber) + ". - " + date + "_" + hour + " - "
							+ descriptionOfPhotos;
				}
				else if (i > 99 && i <= 99) {
					changedImageName = "0" + (i + initialSerialNumber) + ". - " + date + "_" + hour + " - "
							+ descriptionOfPhotos;
				} else {
					changedImageName = (i + initialSerialNumber) + ". - " + date + "_" + hour + " - "
							+ descriptionOfPhotos;
				}

				myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
			}
		}
	}

	
	
	
	
	
	public void changePicsNameToNumbering() throws IOException { // Etc.: 001, 020, 312
		FOLDER_PATH = textFieldLocation.getText();
		File myFolder = new File(FOLDER_PATH);
		File[] fileArray = myFolder.listFiles();
		numberOfImages = fileArray.length;

		for (int i = 0; i < numberOfImages; i++) {
			File myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
			
			int numbToImageName = i + numberingOfPictureFromThatNumber;
			System.out.println("numbToImageName"+ numbToImageName);
			
			if(numbToImageName <= 9) {
				changedImageName = "00" + (i + numberingOfPictureFromThatNumber);
			}
			
			else if(numbToImageName > 9 && numbToImageName <= 99) {
				changedImageName = "0" + (i + numberingOfPictureFromThatNumber);
			} 
			
			else {
				changedImageName = "" + (i + numberingOfPictureFromThatNumber);
			}
						
			myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
		}
	}
}
