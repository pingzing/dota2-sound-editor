/* 
 * The MIT License
 *
 * Copyright 2015 Neil McAlister.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.Utility;
import javax.swing.JDialog;

public class AboutPanel extends javax.swing.JPanel
{

    private JDialog hostingDialog = null;
    private final String VERSION = "v1.3";
    public AboutPanel(JDialog _hostingDialog)
    {        
        this.hostingDialog = _hostingDialog;
        Utility.setFrameIcon(hostingDialog);
        initComponents();
        versionLabel.setText(VERSION);
    }

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

        applicationIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dotaSoundEditor/resources/editorIconSmall.png"))); // NOI18N
        applicationIcon.setText("jLabel1");
        applicationIcon.setMaximumSize(new java.awt.Dimension(50, 50));
        applicationIcon.setMinimumSize(new java.awt.Dimension(50, 50));
        applicationIcon.setName(""); // NOI18N
        applicationIcon.setPreferredSize(new java.awt.Dimension(50, 50));

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
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applicationIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createdByLabel)
                    .addComponent(versionLabel)
                    .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(okayButton)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(contactLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(applicationIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                        .addGap(209, 209, 209))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(versionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(createdByLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contactLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okayButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
