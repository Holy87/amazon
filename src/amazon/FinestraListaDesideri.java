/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.modelliTabelle.DBTableModel;
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
public class FinestraListaDesideri extends javax.swing.JDialog {

    /**
     * Creates new form FinestraListaDesideri
     */
    public FinestraListaDesideri(java.awt.Frame parent, boolean modal, int utente) {
        super(parent, modal);
        utenteID = utente;
        initComponents();
        impostaTabella();           //liste desideri
        impostaTabellaArticoli();   //articoli della lista
        controllaSeUltima();
    }
    
    private int utenteID;
    private ResultSet rsDesideri, rsArticoli; //ResultSet su cui si basano i dati della tabella
    private DBTableModel modelloTabella; //modello della tabella per i dati
    private DBTableModel modelloArticoli;
    private int cursoreDesideri = 1; //memorizza la riga selezionata
    private int cursoreArticoli = 1; //memorizza la riga selezionata negli art.
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabella = new DBTableModel(rsDesideri);//inserire il resultset nel costr.
        tabellaDesideri.setModel(modelloTabella); //metto il modellotabella nel
        tabellaDesideri.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaDesideri.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        //infine aggiorno il resultset della tabella
        aggiornaTabella();
    }
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabellaArticoli() {
        modelloTabella = new DBTableModel(rsArticoli);//inserire il resultset nel costr.
        tabellaArticoli.setModel(modelloArticoli); //metto il modellotabella nel
        tabellaArticoli.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaArticoli.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableArticoliSelectionChanged();
            }
        } 
                
        );
        //infine aggiorno il resultset della tabella
        aggiornaTabellaArticoli();
    }
    
    /**
     * Aggiorna i dati della tabella desideri con i dati dell'utente.
     */
    public void aggiornaTabella()
    {
        try {
            rsDesideri = ottieniDati(); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts.
            modelloTabella.setRS(rsDesideri);   //non credo serva, ma il prof lo mette..
            rsDesideri.absolute(cursoreDesideri);   //attiva la riga del cursore attuale
            mostraDati();           //imposta la selezione a riga singola
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiorna i dati della tabella articoli a seconda della lista selezionata.
     */
    public void aggiornaTabellaArticoli()
    {
        try {
            rsArticoli = ottieniArticoli(idLista()); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts.
            modelloArticoli.setRS(rsArticoli);   //non credo serva, ma il prof lo mette..
            rsArticoli.absolute(cursoreArticoli);   //attiva la riga del cursore attuale
            mostraDatiArticoli();           //imposta la selezione a riga singola
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    
    
    /**
     * Questo metodo restituisce il resultset necessario alla tabella
     * Modificatelo a seconda dei dati che volete visualizzare sulla tabella
     * @return resultset dei dati da mettere in tabella 
     */
    private ResultSet ottieniDati() throws SQLException {
        return DBConnection.visualizzaListeUtente(utenteID);
    }
    
    private ResultSet ottieniArticoli(int idLista) throws SQLException {
        return null;// DBConnection.visualizzaArticoliListaUtente(idLista);
    }
    
    private String nomeLista() {
        return modelloTabella.getValueAt(cursoreDesideri - 1, 1).toString();
    }
    
    private int idLista() {
        return Integer.parseInt(modelloTabella.getValueAt(cursoreDesideri - 1, 0).toString());
    }
    
    /**
     * Metodo che viene chiamato quando si clica il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableSelectionChanged() 
    {
        try {
            rsDesideri.absolute(tabellaDesideri.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
            aggiornaTabellaArticoli();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che viene chiamato quando si clicca il mouse sulla tabella
     * cambiando la selezione
     */
    private void tableArticoliSelectionChanged() 
    {
        try {
            rsArticoli.absolute(tabellaArticoli.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDatiArticoli();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursoreDesideri = rsDesideri.getRow();
          tabellaDesideri.getSelectionModel().setSelectionInterval(cursoreDesideri - 1,cursoreDesideri - 1);
          tabellaDesideri.setRowSelectionInterval(cursoreDesideri - 1, cursoreDesideri - 1);
          tabellaDesideri.getColumnModel().getColumn(0).setMinWidth(0);
          tabellaDesideri.getColumnModel().getColumn(0).setMaxWidth(0);
          aggiornaCheckBox();
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDatiArticoli() {
      try {
          cursoreArticoli = rsArticoli.getRow();
          tabellaArticoli.getSelectionModel().setSelectionInterval(cursoreArticoli - 1,cursoreArticoli - 1);
          tabellaArticoli.setRowSelectionInterval(cursoreArticoli - 1, cursoreArticoli - 1);
          tabellaArticoli.getColumnModel().getColumn(0).setMinWidth(0);
          tabellaArticoli.getColumnModel().getColumn(0).setMaxWidth(0);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    private void eliminaLista() {
        try {
            DBConnection.eliminaListaDesideri(idLista());
            aggiornaTabella();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        controllaSeUltima();
    }
    
    private void rinominaLista() {
        String nuovoNome = JOptionPane.showInputDialog(this, "Inserisci il nuovo nome per la lista");
        try {
            DBConnection.rinominaListaDesideri(idLista(), nuovoNome);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabella();
    }
    
    private void aggiungiLista() {
        String nome = JOptionPane.showInputDialog(this, "Inserisci il nome per la nuova lista");
        try {
            DBConnection.creaListaDesideri(utenteID, nome, false);
            controllaSeUltima();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void modificaPrivacy() {
        boolean selezione = checkPubblica.isSelected();
        try {
            DBConnection.modificaListaDesideri(idLista(), selezione);
        } catch (SQLException ex) {
            checkPubblica.setSelected(selezione);
            mostraErrore(ex);
        }
    }
    
    private void controllaSeUltima() {
        if (modelloTabella.getColumnCount() <= 1) {
            bElimina.setEnabled(false);
        } else {
            bElimina.setEnabled(true);
        }
    }
    
    private void aggiornaCheckBox() {
        
    }
    
    /**
     * Questo metodo stampa l'errore SQL. È facoltativo.
     * @param ex è l'eccezione da stampare
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
        tabellaDesideri = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaArticoli = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        bElimina = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        checkPubblica = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Liste dei desideri");
        setLocationByPlatform(true);

        tabellaDesideri.setModel(new javax.swing.table.DefaultTableModel(
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
        tabellaDesideri.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabellaDesideriMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tabellaDesideri);

        tabellaArticoli.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabellaArticoli);

        jLabel1.setText("Liste desideri:");

        jLabel2.setText("Articoli nella lista:");

        jButton1.setText("Nuova lista desideri");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Rinomina lista desideri");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        bElimina.setText("Elimina lista desideri");
        bElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEliminaActionPerformed(evt);
            }
        });

        jButton4.setText("Rimuovi articolo");

        checkPubblica.setText("Rendi la lista pubblica");
        checkPubblica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPubblicaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bElimina, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(checkPubblica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkPubblica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bElimina)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tabellaDesideriMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaDesideriMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tabellaDesideriMouseReleased

    private void bEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEliminaActionPerformed
        eliminaLista();
    }//GEN-LAST:event_bEliminaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        rinominaLista();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        aggiungiLista();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void checkPubblicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPubblicaActionPerformed
        modificaPrivacy();
    }//GEN-LAST:event_checkPubblicaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bElimina;
    private javax.swing.JCheckBox checkPubblica;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabellaArticoli;
    private javax.swing.JTable tabellaDesideri;
    // End of variables declaration//GEN-END:variables
}
