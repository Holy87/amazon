/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.exceptions.NoFormatSelectedException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Francesco
 */
public class FinestraCorriere extends EditForm {

    /**
     * Creates new form FinestraUtente
     * @param parent
     * @param modal
     */
    public FinestraCorriere(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    public boolean d1, d2, d3;
    public int corriereID;
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tCorriere_ID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tCorriere_Nome = new javax.swing.JTextField();
        bEsc = new javax.swing.JButton();
        bOk = new javax.swing.JButton();
        jCheckBox_d1 = new javax.swing.JCheckBox();
        jCheckBox_d2 = new javax.swing.JCheckBox();
        jCheckBox_d3 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifica Autore");

        jLabel1.setText("Corriere_ID");

        tCorriere_ID.setEnabled(false);

        jLabel2.setText("Nome Corrieri");

        tCorriere_Nome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tCorriere_NomeActionPerformed(evt);
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

        jCheckBox_d1.setText("1 Giorno");
        jCheckBox_d1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_d1ActionPerformed(evt);
            }
        });

        jCheckBox_d2.setText("2-3 Giorni");
        jCheckBox_d2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_d2ActionPerformed(evt);
            }
        });

        jCheckBox_d3.setText("3-5 Giorni");
        jCheckBox_d3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_d3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Modalità di Spedizione a disposizione");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bEsc))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tCorriere_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tCorriere_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jCheckBox_d1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCheckBox_d2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jCheckBox_d3))
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tCorriere_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tCorriere_Nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_d1)
                    .addComponent(jCheckBox_d2)
                    .addComponent(jCheckBox_d3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bEsc)
                    .addComponent(bOk))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        eseguiOk();
    }//GEN-LAST:event_bOkActionPerformed

    private void bEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEscActionPerformed
        chiudiFinestra();
    }//GEN-LAST:event_bEscActionPerformed

    private void tCorriere_NomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tCorriere_NomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tCorriere_NomeActionPerformed

    private void jCheckBox_d1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_d1ActionPerformed
        d1=jCheckBox_d1.isSelected();
    }//GEN-LAST:event_jCheckBox_d1ActionPerformed

    private void jCheckBox_d3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_d3ActionPerformed
        d3=jCheckBox_d3.isSelected();
    }//GEN-LAST:event_jCheckBox_d3ActionPerformed

    private void jCheckBox_d2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_d2ActionPerformed
        d2=jCheckBox_d2.isSelected();
    }//GEN-LAST:event_jCheckBox_d2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEsc;
    private javax.swing.JButton bOk;
    private javax.swing.JCheckBox jCheckBox_d1;
    private javax.swing.JCheckBox jCheckBox_d2;
    private javax.swing.JCheckBox jCheckBox_d3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField tCorriere_ID;
    private javax.swing.JTextField tCorriere_Nome;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillContents() {
        tCorriere_ID.setText((String)dati.get(0));
        tCorriere_Nome.setText((String)dati.get(1));
    }

    @Override
    protected void cleanContents() {
        tCorriere_ID.setText("");
        tCorriere_Nome.setText("");
    }
    
    private void eseguiOk()
    {
        
        try {
            tuttiDisattivati();
            setVisible(false);
            if (mode == ADDN)   {
                corriereID = DBConnection.creaCorriere(tCorriere_Nome.getText());
            System.out.println(d1);
            System.out.println(d2);
            System.out.println(d3);
            System.out.println(corriereID);
            }
            else
                DBConnection.aggiornaCorriere(Integer.parseInt(tCorriere_ID.getText()), tCorriere_Nome.getText());
            chiudiFinestra();
            aggiornaModSped();
        }
        catch(SQLException ex){
            mostraErrore(ex);
            setVisible(true);
        } catch (NoFormatSelectedException ex) {
            JOptionPane.showMessageDialog(this, "Devi selezionare almeno un formato", "Errore!", JOptionPane.ERROR_MESSAGE, null);
        }
    }
    
    private void aggiornaModSped() throws SQLException {
        if (mode == EDIT)
            DBConnection.aggiornaModSpedizione(Integer.parseInt(tCorriere_ID.getText()), d1, d2, d3);
        else
            DBConnection.aggiungiModSpedizione(corriereID, d1, d2, d3);
    }
        

    private void tuttiDisattivati() throws NoFormatSelectedException {
        if (jCheckBox_d1.isSelected() && jCheckBox_d2.isSelected() && jCheckBox_d3.isSelected())
            throw new NoFormatSelectedException();
    }
    
    @Override
    protected String titoloNuovo () {
        return "Nuovo Corriere";
    }
    
    @Override
    protected String titoloModifica () {
        return "Modifica Corriere";
    }
}
