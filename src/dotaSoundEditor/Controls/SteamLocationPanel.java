package dotaSoundEditor.Controls;

import Helpers.Utility;
import dotaSoundEditor.UserPrefs;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class SteamLocationPanel extends javax.swing.JPanel
{

    UserPrefs userPrefs;
    JDialog hostingDialog;

    /**
     * Creates
     * new
     * form
     * SteamLocationPanel
     */
    public SteamLocationPanel(UserPrefs _userPrefs, boolean fromMainForm, JDialog hostingDialog)
    {
        this.userPrefs = _userPrefs;
        this.hostingDialog = hostingDialog;        
        Utility.setFrameIcon(hostingDialog);

        initComponents();
        if (!fromMainForm)
        {
            cancelButton.setVisible(false);
        }
        else
        {
            pathField.setText(_userPrefs.getInstallDir());
        }
    }

    /**
     * This
     * method
     * is
     * called
     * from
     * within
     * the
     * constructor
     * to
     * initialize
     * the
     * form.
     * WARNING:
     * Do
     * NOT
     * modify
     * this
     * code.
     * The
     * content
     * of
     * this
     * method
     * is
     * always
     * regenerated
     * by
     * the
     * Form
     * Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        headerLabel = new javax.swing.JLabel();
        descLabel = new javax.swing.JLabel();
        pathField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        acceptButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(450, 290));

        headerLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        headerLabel.setText("Dota 2 Directory");

        descLabel.setText("<html>Please locate your Dota 2 install directory. It's typically located at:<br><br><b>Windows:</b><br> C:/Program Files/Steam/steamapps/common/dota 2 beta<br><b>Mac OS X:</b><br>~/Library/Application Support/Steam/SteamApps/common/dota 2 beta/<br><b>Linux:</b><br>~/local/share/Steam/steamapps/common/dota 2 beta</html>");

        pathField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pathFieldActionPerformed(evt);
            }
        });

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                browseButtonActionPerformed(evt);
            }
        });

        acceptButton.setText("Accept");
        acceptButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                acceptButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(descLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(headerLabel))
                        .addGap(0, 89, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pathField)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(acceptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(browseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                    .addComponent(cancelButton))
                .addContainerGap(81, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pathFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pathFieldActionPerformed
    {//GEN-HEADEREND:event_pathFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pathFieldActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_browseButtonActionPerformed
    {//GEN-HEADEREND:event_browseButtonActionPerformed

        userPrefs.setInstallDir();
        if (userPrefs.getSuccess())
        {
            pathField.setText(userPrefs.getInstallDir());
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                    "Dota 2 Install not found. Searched for pak01_dir.vpk, and could not find it.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_acceptButtonActionPerformed
    {//GEN-HEADEREND:event_acceptButtonActionPerformed

        //If the install path was already validated & successful, move on
        if (userPrefs.getSuccess() && !pathField.getText().isEmpty())
        {
            userPrefs.setInstallKeys();
            this.hostingDialog.dispose();
        }
        //Otherwise, make userPrefs set the path and do the validation now
        //TODO: Refactor this so we don't validate twice if we had a failed
        //validation on the Browse
        else
        {
            userPrefs.setInstallDir(pathField.getText());
            if (userPrefs.getSuccess())
            {
                userPrefs.setInstallKeys();
                this.hostingDialog.dispose();
            }
            //Validation completely failed.
            else
            {
                JOptionPane.showMessageDialog(this,
                        "Dota 2 Install not found. Searched for pak01_dir.vpk, and could not find it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        this.hostingDialog.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel descLabel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JTextField pathField;
    // End of variables declaration//GEN-END:variables
}
