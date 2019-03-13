package com.mycompany.javaexifviewer;

import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author m
 */
public class Frame extends javax.swing.JFrame {

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
    
    
    
    public Frame() {
        initComponents();
        this.getContentPane().setBackground(Color.CYAN);
        descriptionMouseListener();
        descriptionMouseListener2();
        jRadioButton4.setEnabled(false);
        //jRadioButton5.setEnabled(false);
        jTextField3.setText("Sample Descriptions");
        jTextField4.setText("C:\\Users\\mario\\Desktop\\Pictures");
        FOLDER_PATH = jTextField4.getText();
    }

    public final void descriptionMouseListener() { //jtextfield1 (number)
        jTextField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTextField1.setText("");
            }
        });
    }

    public final void descriptionMouseListener2() { //jtextfield3 (date + description)
        jTextField3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTextField3.setText("");
            }
        });
    }

    public void photoInfo() {
        //Open Image and Get EXIF Metadata
        //javaxt.io.Image image = new javaxt.io.Image("/photo.jpg");
        image = new javaxt.io.Image("C:\\Users\\mario\\Desktop\\pics\\1.jpg");
        exif = image.getExifTags();

        //Print Camera Info
        System.out.println("EXIF Fields: " + exif.size());
        System.out.println("-----------------------------");
        System.out.println("Date: " + exif.get(0x0132)); //0x9003       
        System.out.println("Camera: " + exif.get(0x0110));
        System.out.println("Manufacturer: " + exif.get(0x010F));
    }

    public void changePictureNameDateWithDescription() { //"2019.01.01. 10.09.55 - Description"
        FOLDER_PATH = jTextField4.getText();
        myFolder = new File(FOLDER_PATH);
        fileArray = myFolder.listFiles();
        numberOfImages = fileArray.length;

        for (int i = 0; i < numberOfImages; i++) {
            myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
            filePath = myFile.toPath();

            image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
            exif = image.getExifTags();

            if (exif.get(0x0132) != null) {
                dateOfShooting = exif.get(0x0132).toString().replace(":", ".");
                changedImageName = dateOfShooting + " - " + descriptionOfPhotos;

                myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
            } else {
                myFile.renameTo(new File(FOLDER_PATH + "\\" + i + ". - " + descriptionOfPhotos + ".jpg"));
            }
        }
    }

    public void changePictureNameDateOfShooting() { //"2019.01.01. 10.09.55"
        FOLDER_PATH = jTextField4.getText();
        myFolder = new File(FOLDER_PATH);
        fileArray = myFolder.listFiles();
        numberOfImages = fileArray.length;

//        int min = 0;
//        int max = numberOfImages;
//        jProgressBar1.setValue(min);
//        jProgressBar1.setStringPainted(true);
        
        
        for (int i = 0; i < numberOfImages; i++) {
            jLabel9.setText("Processing the "+ i + "photos");
            myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
            filePath = myFile.toPath();

            image = new javaxt.io.Image(FOLDER_PATH + "\\" + fileArray[i].getName());
            exif = image.getExifTags();

            //jProgressBar1.setValue(i+ (max/(max-i)));
            
            if (exif.get(0x0132) != null) {
                dateOfShooting = exif.get(0x0132).toString().replace(":", ".");
                myFile.renameTo(new File(FOLDER_PATH + "\\" + dateOfShooting + ".jpg"));
            } else {
                myFile.renameTo(new File(FOLDER_PATH + "\\" + (i + 1) + ". - " + descriptionOfPhotos + ".jpg"));
            }
        }
    }

    public void changePictureNameDateOfEdit() throws IOException {  //Date of edit
        FOLDER_PATH = jTextField4.getText();
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
                System.out.println("Exception handled when trying to get file " + "attributes: " + exception.getMessage());
            }

            long milliseconds = attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS);
            if ((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE)) {
                Date lastModifiedDate = new Date(attributes.lastModifiedTime().to(TimeUnit.MILLISECONDS));
                date = (lastModifiedDate.getYear() + 1900) + "." + (lastModifiedDate.getMonth() + 1) + "." + lastModifiedDate.getDate();
                hour = lastModifiedDate.getHours() + "." + lastModifiedDate.getMinutes() + "." + lastModifiedDate.getSeconds();

                changedImageName = (i + initialSerialNumber) + ". - " + date + " " + hour + " - " + descriptionOfPhotos;
                myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
            }
        }
    }

    public void changePicsNameToNumbering() throws IOException {  //1.  2.  3.
        FOLDER_PATH = jTextField4.getText();
        File myFolder = new File(FOLDER_PATH);
        File[] fileArray = myFolder.listFiles();
        numberOfImages = fileArray.length;

        for (int i = 0; i < numberOfImages; i++) {
            File myFile = new File(FOLDER_PATH + "\\" + fileArray[i].getName());
            changedImageName = "" + (i + numberingOfPictureFromThatNumber);
            myFile.renameTo(new File(FOLDER_PATH + "\\" + changedImageName + ".jpg"));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Photo renaming APP");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Location of the folder:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Choose an option to name for your PICTURES");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Numbering of pictures from that number ");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Just Date of Shooting");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Date of Shooting with Description");

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("Numb. of Picture + Date of Shooting + Description");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("Date of edit");

        jTextField1.setText("1");

        jLabel3.setText("Etc.: 1. 2. 3.");

        jLabel4.setText("Etc.: 2019.01.01. 10.32.55");

        jLabel5.setText("Etc.: 2019.01.01. 10.32.55 - Description");

        jTextField3.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jTextField3.setText("\"Description\"");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel6.setText("Etc.: 1. - 2019.01.01. 10.32.55 - Description");

        jTextField4.setText("C:\\\\Users\\\\mario\\\\Desktop\\\\pics");

        jButton1.setText("Start the proces");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Picture description:");

        jLabel8.setText("Etc.: 2019.01.01. 10.32.55");

        jLabel9.setText("jLabel9");

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(jTextField3)))
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButton3)
                    .addComponent(jRadioButton5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9))
                        .addComponent(jRadioButton4, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jLabel8))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Start the proces Button
        //FOLDER_PATH = jTextField1.getText(); //Example: "C:\\Users\\mario\\Desktop\\pics"

        //Numbering of pictures from that number ...
        if (jRadioButton1.isSelected()) {

            try {
                try {
                    numberingOfPictureFromThatNumber = Integer.parseInt(jTextField1.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(rootPane, "It is not a number!");
                    jTextField1.setText("");
                    jTextField1.requestFocus();
                    return;
                }
                changePicsNameToNumbering();

            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //Just Date of Shooting
        if (jRadioButton2.isSelected()) {
            descriptionOfPhotos = jTextField3.getText();
            changePictureNameDateOfShooting();
        }

        //Date of Shooting with Description Of Photos
        if (jRadioButton3.isSelected()) {
            descriptionOfPhotos = jTextField3.getText();
            changePictureNameDateWithDescription();
        }

        //Numbering of picture + Date of shooting + Description
        if (jRadioButton4.isSelected()) {
            descriptionOfPhotos = jTextField3.getText();
        }

        //Date of edit
        if (jRadioButton5.isSelected()) {
            descriptionOfPhotos = jTextField3.getText();
            try {
                changePictureNameDateOfEdit();
            } catch (IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
