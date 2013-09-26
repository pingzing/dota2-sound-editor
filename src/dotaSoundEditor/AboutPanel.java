/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

import javax.swing.JDialog;

/**
 *
 * @author
 * Image
 * 17
 */
public class AboutPanel extends javax.swing.JPanel
{

    private JDialog hostingDialog = null;
    private final String VERSION = "v0.3.5";
    public AboutPanel(JDialog _hostingDialog)
    {        
        this.hostingDialog = _hostingDialog;
        Utility.setFrameIcon(hostingDialog);
        initComponents();
        versionLabel.setText(VERSION);
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

        applicationIcon = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        createdByLabel = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        okayButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(370, 275));

        applicationIcon.setText("jLabel1");

        headerLabel.setText("<html><h1>Dota 2 Sound Editor</h1></html>");

        versionLabel.setText("versionLabel");

        descriptionLabel.setText("<html>The Dota 2 Sound Editor is freeware and may be<br>reditributed freely, so long as it has not been<br>tampered with.</html>");

        createdByLabel.setText("Created by PingZing");

        contactLabel.setText("Contact: psychozagal@gmail.com");

        okayButton.setText("Okay");
        okayButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applicationIcon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contactLabel)
                    .addComponent(createdByLabel)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(versionLabel)
                    .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okayButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applicationIcon)
                    .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(versionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(createdByLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okayButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okayButtonActionPerformed
    {//GEN-HEADEREND:event_okayButtonActionPerformed
        this.hostingDialog.dispose();
    }//GEN-LAST:event_okayButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel applicationIcon;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JLabel createdByLabel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JButton okayButton;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
