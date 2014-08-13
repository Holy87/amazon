/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author frbos_000
 */
public class FinestraRecensioneLibro extends javax.swing.JDialog {

    /**
     * Creates new form FinestraRecensioneLibro
     */
    public FinestraRecensioneLibro(java.awt.Frame parent, boolean modal, int idUtente, String isbn, FinestraListaRecensioniLibri finestraRecensioni) {
        super(parent, modal);
        this.idUtente = idUtente;
        this.isbn = isbn;
        this.finestraRecensioni = finestraRecensioni;
        initComponents();
    }
    
    public FinestraRecensioneLibro(java.awt.Dialog parent, boolean modal, int idUtente, String isbn, FinestraListaRecensioniLibri finestraRecensioni) {
        super(parent, modal);
        this.idUtente = idUtente;
        this.isbn = isbn;
        this.finestraRecensioni = finestraRecensioni;
        initComponents();
    }
    
    private int idUtente;
    private String isbn;
    private FinestraListaRecensioniLibri finestraRecensioni;
    
    private void inserisciRecensione() {
        setVisible(false);
        try {
            DBConnection.creaRecensione(idUtente, tCommento.getText(), true, isbn, sVoto.getValue());
            finestraRecensioni.aggiornaTabella();
            dispose();
        } catch (SQLException ex) {
            mostraErrore(ex);
            setVisible(true);
        }
    }
    
    private void annullaRecensione() {
        setVisible(false);
        dispose();
    }
    
    /**
     * Mostra il messaggio d'errore
     * @param ex eccezione da mostrare
     */
    private void mostraErrore(SQLException ex) {
        String errore = "Errore di connessione al database";
        errore += "\nCodice: " + ex.getErrorCode();
        errore += "\nMessaggio: " + ex.getMessage();
        errore += "\n\n" + ex.getSQLState();
        JOptionPane.showMessageDialog(this, "Errore: " + errore, null, ERROR_MESSAGE);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tCommento = new javax.swing.JTextArea();
        sVoto = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        bInserisci = new javax.swing.JButton();
        bAnnulla = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inserisci recensione");
        setLocationByPlatform(true);
        setResizable(false);

        tCommento.setColumns(20);
        tCommento.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tCommento.setLineWrap(true);
        tCommento.setRows(5);
        tCommento.setToolTipText("Inserisci la tua recensione");
        jScrollPane1.setViewportView(tCommento);
        tCommento.getAccessibleContext().setAccessibleName("");

        sVoto.setMajorTickSpacing(1);
        sVoto.setMaximum(5);
        sVoto.setMinimum(1);
        sVoto.setMinorTickSpacing(1);
        sVoto.setPaintLabels(true);
        sVoto.setPaintTicks(true);
        sVoto.setToolTipText("");
        sVoto.setValue(3);

        jLabel1.setText("Voto:");

        bInserisci.setText("Inserisci");
        bInserisci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInserisciActionPerformed(evt);
            }
        });

        bAnnulla.setText("Annulla");
        bAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAnnullaActionPerformed(evt);
            }
        });

        jLabel2.setText("Recensione (max 255 caratteri)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sVoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(13, 180, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bInserisci)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAnnulla))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sVoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bInserisci)
                    .addComponent(bAnnulla))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bInserisciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInserisciActionPerformed
        inserisciRecensione();
    }//GEN-LAST:event_bInserisciActionPerformed

    private void bAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAnnullaActionPerformed
        annullaRecensione();
    }//GEN-LAST:event_bAnnullaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAnnulla;
    private javax.swing.JButton bInserisci;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider sVoto;
    private javax.swing.JTextArea tCommento;
    // End of variables declaration//GEN-END:variables
}
