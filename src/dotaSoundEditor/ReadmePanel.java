package dotaSoundEditor;

public class ReadmePanel extends javax.swing.JFrame
{                
    public ReadmePanel()
    {                        
        initComponents();
        this.setSize(530, 450);
        jEditorPane1.setCaretPosition(0);
        Utility.setFrameIcon(this);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sound Editor Help");
        setLocationByPlatform(true);
        setMaximumSize(new java.awt.Dimension(535, 450));
        setMinimumSize(new java.awt.Dimension(530, 450));

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setMaximumSize(new java.awt.Dimension(530, 450));

        jEditorPane1.setEditable(false);
        jEditorPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jEditorPane1.setContentType("text/html"); // NOI18N
        jEditorPane1.setText("<html>"
            + "<h1>How to use:</h1><br>"
            + "<h2>Setting up</h2><br>"
            + "1. Open Steam and click on Library.<br>"
            + "2. Right click on Dota 2, and select Properties.<br>"
            + "3. Select \"Set Launch Options\".<br>"
            + "4. Add \"-override_vpk\" (without quotes) to the launch options.<br>"
            + "5. Click Ok.<br><br>"
            + ""
            + "<h2>Using Dota 2 Sound Editor</h2><br>"
            + "The Dota 2 Sound editor allows you to modify what sounds are played when heroes or items in Dota 2 perform actions."
            + " To begin, select a tab from the top of the window. Then, if applicable, select an entry from the dropdown box and "
            + "expand the list of sounds associated with a particular action. "
            + "From there, you can view each sound that may occur when a hero or item performs that action. For each sound, you can:<br><br>"
            + ""
            + "<b>Replace</b><br>"
            + "This allows you to change what sound plays when the selected action is performed.<br><br>"
            + ""
            + "<b>Revert</b><br>"
            + "This restores the selected action to its default sound.<br><br>"
            + ""
            + "<b>Revert All</b><br>"
            + "<ul>"
            + "<li><b>Heroes Spells:</b> This restores all actions for the current hero to their default sound.</li>"
            + "<li><b>Items:</b> This restores all item actions to their default sound. </li>"
            + "</ul><br>"
            + "<b>Advanced/Basic</b><br>"
            + "The Advanced button allows you to view the current hero's entire sound script as a tree. Triple-click any node to allow manual editing.<br>"
            + "While in Advanced mode, Replace, Revert and Play Sound will only work on nodes that contain a path to a sound file.<br>"
            + "Clicking the <b>Basic</b> button will return the editor to Basic mode.</html>");
        jEditorPane1.setMargin(new java.awt.Insets(6, 6, 6, 6));
        jEditorPane1.setMaximumSize(new java.awt.Dimension(530, 450));
        jScrollPane2.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 461, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
