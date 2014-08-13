/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.SQLException;

/**
 *
 * @author Francesco
 */
public class FinestraEditore extends EditForm {

    /**
     * Creates new form FinestraUtente
     * @param parent
     * @param modal
     */
    public FinestraEditore(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /**
     * Creates new form FinestraUtente
     * @param parent
     * @param modal
     */
    public FinestraEditore(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tEDI_ID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tEDI_Nome = new javax.swing.JTextField();
        bEsc = new javax.swing.JButton();
        bOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifica Autore");
        setLocationByPlatform(true);

        jLabel1.setText("EDI_ID");

        tEDI_ID.setEnabled(false);

        jLabel2.setText("Nome Editore");

        tEDI_Nome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tEDI_NomeActionPerformed(evt);
            }
        });

        bEsc.setText("Annulla");
        bEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEscActionPerformed(evt);
            }
        });

        bOk.setText("OK");
        bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tEDI_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tEDI_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 185, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bEsc)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tEDI_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tEDI_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bEsc)
                    .addComponent(bOk)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        eseguiOk();
    }//GEN-LAST:event_bOkActionPerformed

    private void bEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEscActionPerformed
        chiudiFinestra();
    }//GEN-LAST:event_bEscActionPerformed

    private void tEDI_NomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tEDI_NomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tEDI_NomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEsc;
    private javax.swing.JButton bOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField tEDI_ID;
    private javax.swing.JTextField tEDI_Nome;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillContents() {
        tEDI_ID.setText((String)dati.get(0));
        tEDI_Nome.setText((String)dati.get(1));
    }

    @Override
    protected void cleanContents() {
        tEDI_ID.setText("");
        tEDI_Nome.setText("");
    }
    
    private void eseguiOk()
    {
        setVisible(false);
        try {
            if (mode == ADDN)
                DBConnection.creaEditore(tEDI_Nome.getText());
            else
                DBConnection.aggiornaEditore(Integer.parseInt(tEDI_ID.getText()), tEDI_Nome.getText());
            chiudiFinestra();
        }
        catch(SQLException ex){
            mostraErrore(ex);
            setVisible(true);
        }
        
    }
    
    @Override
    protected String titoloNuovo () {
        return "Nuovo Editore";
    }
    
    @Override
    protected String titoloModifica () {
        return "Modifica Editore";
    }
}
