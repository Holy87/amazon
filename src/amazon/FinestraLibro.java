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
public class FinestraLibro extends EditForm {

    /**
     * Creates new form FinestraUtente
     * @param parent finestra principale
     * @param modal 1 se è un'aggiunta, 2 se è una modifica
     */
    public FinestraLibro(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    private String oldISBN;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tGenere = new javax.swing.JTextField();
        bEsc = new javax.swing.JButton();
        bOk = new javax.swing.JButton();
        tNomeLibro = new javax.swing.JTextField();
        tNEdizione = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tNPagine = new javax.swing.JTextField();
        tPesoSped = new javax.swing.JTextField();
        tDataUscita = new javax.swing.JTextField();
        tISBN = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tDescrizione = new javax.swing.JTextArea();

        jLabel9.setText("Descrizione");

        jLabel12.setText("N. Pagine");

        jLabel3.setText("ID");

        tID.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modifica Libro");

        jLabel1.setText("Nome libro");

        jLabel2.setText("N. Edizione");

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

        tNEdizione.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tNEdizioneActionPerformed(evt);
            }
        });

        jLabel7.setText("ISBN");

        jLabel8.setText("Descrizione");

        jLabel10.setText("Genere");

        jLabel11.setText("N. Pagine");

        jLabel13.setText("Peso Sped.");

        jLabel14.setText("Data Uscita");

        tDescrizione.setColumns(20);
        tDescrizione.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tDescrizione.setLineWrap(true);
        tDescrizione.setRows(5);
        jScrollPane1.setViewportView(tDescrizione);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bOk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bEsc))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tNPagine)
                            .addComponent(tPesoSped, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tDataUscita, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(tGenere)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2))
                                .addGap(6, 6, 6))
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tNEdizione, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(tNomeLibro, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tISBN))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tNomeLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tNEdizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(tGenere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(tNPagine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(tPesoSped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(tDataUscita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
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

    private void tNEdizioneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNEdizioneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tNEdizioneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEsc;
    private javax.swing.JButton bOk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tDataUscita;
    private javax.swing.JTextArea tDescrizione;
    private javax.swing.JTextField tGenere;
    private javax.swing.JTextField tID;
    private javax.swing.JTextField tISBN;
    private javax.swing.JTextField tNEdizione;
    private javax.swing.JTextField tNPagine;
    private javax.swing.JTextField tNomeLibro;
    private javax.swing.JTextField tPesoSped;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillContents() {
        oldISBN=dati.get(2).toString();
        tNomeLibro.setText((String)dati.get(0));
        tNEdizione.setText((String)dati.get(1));
        tISBN.setText((String)dati.get(2));
        tDescrizione.setText((String)dati.get(3));
        tGenere.setText((String)dati.get(4));
        tNPagine.setText((String)dati.get(5));
        tPesoSped.setText((String)dati.get(6));
        tDataUscita.setText((String)dati.get(7));
    }

    @Override
    protected void cleanContents() {
        tNomeLibro.setText("");
        tNEdizione.setText("");
        tISBN.setText("");
        tDescrizione.setText("");
        tGenere.setText("");
        tNPagine.setText("");
        tPesoSped.setText("");
        tDataUscita.setText("");
    }
    
    private void eseguiOk()
    {
        
        try {
            setVisible(false);
            if (mode == ADDN)
                DBConnection.creaLibro(tNomeLibro.getText(), Integer.parseInt(tNEdizione.getText()), tISBN.getText(), tDescrizione.getText(), tGenere.getText(), Integer.parseInt(tNPagine.getText()), Integer.parseInt(tPesoSped.getText()), tDataUscita.getText());
            else
                DBConnection.aggiornaLibro(oldISBN, tNomeLibro.getText(), Integer.parseInt(tNEdizione.getText()), tISBN.getText(), tDescrizione.getText(), tGenere.getText(), Integer.parseInt(tNPagine.getText()), Integer.parseInt(tPesoSped.getText()), tDataUscita.getText());
            chiudiFinestra();
        }
        catch(SQLException ex){
            mostraErrore(ex);
            setVisible(true);
        }
        
    }
    
    @Override
    protected String titoloNuovo () {
        return "Nuovo Libro";
    }
    
    @Override
    protected String titoloModifica () {
        return "Modifica Libro";
    }
}
