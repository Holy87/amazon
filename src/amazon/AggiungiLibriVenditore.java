/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author frbos_000
 */
public class AggiungiLibriVenditore extends javax.swing.JDialog {

    /**
     * Creates new form AggiungiLibriVenditore
     */
    public AggiungiLibriVenditore(java.awt.Frame parent, boolean modal, int idVenditore) {
        super(parent, modal);
        this.idVenditore = idVenditore;
        initComponents();
        impostaTabella();
    }
    
    private ResultSet rs;
    private DBTableModel modelloTabella;
    private int cursore = 1;
    private final int NUOVO = 1;
    private final int USATO = 2;
    private final int RENEW = 3;
    private final int RIGID = 2002;
    private final int FLESS = 2001;
    private final int KINDL = 2003;
    private int idVenditore;
    
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabella = new DBTableModel(rs);
        tabella.setModel(modelloTabella); //metto il modellotabella nel
        tabella.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabella.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        aggiornaTabella();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            System.out.println(""+getFormato());
            rs = DBConnection.visualizzaListinoLibri(getFormato());
            modelloTabella.setRS(rs);
            rs.absolute(cursore);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabella.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabella.setRowSelectionInterval(cursore - 1, cursore - 1);
          tPrezzo.setText(modelloTabella.getValueAt(cursore - 1, 2).toString());
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          //System.out.println("IllegalArgumentException "+ex.getMessage());
      }
    }
    
    private void mostraErrore(SQLException ex) {
        String errore = "Errore di connessione al database";
        errore += "\nCodice: " + ex.getErrorCode();
        errore += "\nMessaggio: " + ex.getMessage();
        errore += "\n\n" + ex.getSQLState();
        JOptionPane.showMessageDialog(this, "Errore: " + errore, null, ERROR_MESSAGE);
    }
    
    /**
     * Metodo che viene chiamato quando si clica il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableSelectionChanged() 
    {
        try {
            rs.absolute(tabella.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private int getFormato() {
        if (tRigida.isSelected())
            return RIGID;
        else if (tFlessibile.isSelected())
            return FLESS;
        else
            return KINDL;
    }
    
    private int getStato() {
        if (tNuovo.isSelected())
            return NUOVO;
        else if (tUsato.isSelected())
            return USATO;
        else
            return RENEW;
    }
    
    private String getCondizione() {
        switch (getStato()) {
            case NUOVO: return "Nuovo";
            case USATO: return "Usato";
            case RENEW: return "Ricondizionato";
            default: return "";
        }
    }
    
    private void impostaAbilitazione(boolean value) {
        tQuantita.setEnabled(value);
        tNuovo.setEnabled(value);
        tUsato.setEnabled(value);
        tRicondizionato.setEnabled(value);
        aggiornaTabella();
        
    }
    
    private String isbn() {
        System.out.println(modelloTabella.getValueAt(cursore-1, 1).toString());
        return modelloTabella.getValueAt(cursore-1, 1).toString();
    }
    
    private void aggiungiLibro() {
        try {
            setVisible(false);
            DBConnection.inserisciLibroMagazzino(idVenditore, isbn(), getFormato(), getCondizione(), Integer.parseInt(tQuantita.getText()), Double.parseDouble(tPrezzo.getText()));
            dispose();
        } catch (SQLException ex) {
            mostraErrore(ex);
            setVisible(true);
        }
    }
    
    private void verificaValore(javax.swing.JTextField campo) {
        int prezzo = 0;
        try {
           prezzo = Integer.parseInt(campo.getText());
        } catch (NumberFormatException ex) {
            campo.setText("1");
        }
        if (prezzo == 0)
            campo.setText("1");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gruppoFormato = new javax.swing.ButtonGroup();
        gruppoStato = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabella = new javax.swing.JTable();
        tRigida = new javax.swing.JRadioButton();
        tFlessibile = new javax.swing.JRadioButton();
        tKindle = new javax.swing.JRadioButton();
        tNuovo = new javax.swing.JRadioButton();
        tUsato = new javax.swing.JRadioButton();
        tRicondizionato = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        tQuantita = new javax.swing.JTextField();
        bAnnulla = new javax.swing.JButton();
        bAggiungi = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tPrezzo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabella.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tabella);

        gruppoFormato.add(tRigida);
        tRigida.setSelected(true);
        tRigida.setText("Copertina rigida");
        tRigida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tRigidaActionPerformed(evt);
            }
        });

        gruppoFormato.add(tFlessibile);
        tFlessibile.setText("Copertina flessibile");
        tFlessibile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tFlessibileActionPerformed(evt);
            }
        });

        gruppoFormato.add(tKindle);
        tKindle.setText("Kindle");
        tKindle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tKindleActionPerformed(evt);
            }
        });

        gruppoStato.add(tNuovo);
        tNuovo.setSelected(true);
        tNuovo.setText("Nuovo");

        gruppoStato.add(tUsato);
        tUsato.setText("Usato");

        gruppoStato.add(tRicondizionato);
        tRicondizionato.setText("Ricondizionato");

        jLabel1.setText("Quantità:");

        tQuantita.setText("1");
        tQuantita.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tQuantitaFocusLost(evt);
            }
        });

        bAnnulla.setText("Annulla");
        bAnnulla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAnnullaActionPerformed(evt);
            }
        });

        bAggiungi.setText("Aggiungi");
        bAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAggiungiActionPerformed(evt);
            }
        });

        jLabel2.setText("Prezzo:");

        tPrezzo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tPrezzoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tRigida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tFlessibile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tKindle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tNuovo)
                                .addGap(69, 69, 69)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tUsato)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tPrezzo)
                            .addComponent(tQuantita, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
                    .addComponent(tRicondizionato))
                .addGap(18, 18, 18)
                .addComponent(bAggiungi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bAnnulla)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tRigida)
                            .addComponent(tNuovo)
                            .addComponent(jLabel1)
                            .addComponent(tQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tFlessibile)
                            .addComponent(tUsato)
                            .addComponent(jLabel2)
                            .addComponent(tPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tRicondizionato)
                            .addComponent(tKindle))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bAggiungi)
                            .addComponent(bAnnulla))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tKindleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tKindleActionPerformed
        impostaAbilitazione(false);
    }//GEN-LAST:event_tKindleActionPerformed

    private void tRigidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tRigidaActionPerformed
        impostaAbilitazione(true);
    }//GEN-LAST:event_tRigidaActionPerformed

    private void tFlessibileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tFlessibileActionPerformed
        impostaAbilitazione(true);
    }//GEN-LAST:event_tFlessibileActionPerformed

    private void bAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAggiungiActionPerformed
        aggiungiLibro();
    }//GEN-LAST:event_bAggiungiActionPerformed

    private void tQuantitaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tQuantitaFocusLost
        verificaValore(tQuantita);
    }//GEN-LAST:event_tQuantitaFocusLost

    private void tPrezzoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPrezzoFocusLost
        verificaValore(tPrezzo);
    }//GEN-LAST:event_tPrezzoFocusLost

    private void bAnnullaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAnnullaActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_bAnnullaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAggiungi;
    private javax.swing.JButton bAnnulla;
    private javax.swing.ButtonGroup gruppoFormato;
    private javax.swing.ButtonGroup gruppoStato;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton tFlessibile;
    private javax.swing.JRadioButton tKindle;
    private javax.swing.JRadioButton tNuovo;
    private javax.swing.JTextField tPrezzo;
    private javax.swing.JTextField tQuantita;
    private javax.swing.JRadioButton tRicondizionato;
    private javax.swing.JRadioButton tRigida;
    private javax.swing.JRadioButton tUsato;
    private javax.swing.JTable tabella;
    // End of variables declaration//GEN-END:variables
}
