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
 * @author Francesco
 */
public class FinestraListaDesideri extends javax.swing.JDialog {

    /**
     * Creazione della finestra
     * @param parent
     * @param modal
     * @param utenteID 
     */
    public FinestraListaDesideri(java.awt.Frame parent, boolean modal, int utenteID) {
        super(parent, modal);
        initComponents();
        this.utenteID = utenteID;
        impostaComboPrivacy();
        impostaTabellaListeDesideri();
        impostaTabellaArticoli();
    }
    
    private int utenteID, listaID, prodID, curLista = 1, curArticoli = 1;
    private DBTableModel modelloListe, modelloArticoli;
    private ResultSet rsListe, rsArticoli;
    private boolean modificaPrivacy = false;
    
    /**
     * Imposta la prima tabella
     */
    @SuppressWarnings("Convert2Lambda")
    private void impostaTabellaListeDesideri() {
        modelloListe = new DBTableModel(rsListe);
        tabellaListe.setModel(modelloListe); //metto il modellotabella nel
        tabellaListe.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaListe.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listeSelectionChanged();
            }
        } 
                
        );
        aggiornaTabellaListeDesideri();
    }
    
    /**
     * Imposta la seconda tabella
     */
    @SuppressWarnings("Convert2Lambda")
    private void impostaTabellaArticoli() {
        modelloArticoli = new DBTableModel(rsArticoli);
        tabellaArticoli.setModel(modelloArticoli); //metto il modellotabella nel
        tabellaArticoli.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaArticoli.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                articoliSelectionChanged();
            }
        } 
                
        );
        aggiornaTabellaArticoli();
    }
    
    /**
     * Aggiorna la tabella lista desideri con il nuovo resultSet
     */
    private void aggiornaTabellaListeDesideri() {
        try {
            rsListe = DBConnection.visualizzaListeUtente(utenteID);
            modelloListe.setRS(rsListe);
            rsListe.absolute(curLista);
            tabellaListe.getColumnModel().getColumn(0).setMinWidth(0);
            tabellaListe.getColumnModel().getColumn(0).setMaxWidth(0);
            impostaListaSelezionata();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiorna l'ID della lista selezionata e aggiorna la tabella degli articoli
     * per la nuova selezione
     */
    private void impostaListaSelezionata() {
        try {
            curLista = rsListe.getRow();
            //tabellaListe.getSelectionModel().setSelectionInterval(curLista - 1,curLista - 1);
            tabellaListe.setRowSelectionInterval(curLista - 1, curLista - 1);
            listaID = rsListe.getInt(1);
            if (modelloListe.getRowCount() == 0)
                abilitaPulsantiListeDesideri(false);
            else
                abilitaPulsantiListeDesideri(true);
            aggiornaComboPrivacy();
            aggiornaTabellaArticoli();
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      } catch (NullPointerException ex) {
          abilitaPulsantiListeDesideri(false);
      }
    }
    
    /**
     * Evento che viene chiamato quando cambia la selezione della lista desideri
     */
    private void listeSelectionChanged() {
        try {
            rsListe.absolute(tabellaListe.getSelectionModel().getMinSelectionIndex() + 1);                    
            impostaListaSelezionata();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiornamento della tabella articoli con il nuovo resultSet
     */
    private void aggiornaTabellaArticoli() {
        try {
            rsArticoli = DBConnection.visualizzaArticoliListaUtente(listaID);
            curArticoli = 1;
            modelloArticoli.setRS(rsArticoli);
            rsArticoli.absolute(curArticoli);
            tabellaArticoli.getColumnModel().getColumn(0).setMinWidth(0);
            tabellaArticoli.getColumnModel().getColumn(0).setMaxWidth(0);
            tabellaArticoli.getColumnModel().getColumn(1).setMinWidth(0);
            tabellaArticoli.getColumnModel().getColumn(1).setMaxWidth(0);
            impostaArticoloSelezionato();
            //abilitaPulsantiArticolo(true);
        } catch (SQLException ex) {
            mostraErrore(ex);
        } catch (NullPointerException ex) {
            abilitaPulsantiArticolo(false);
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Impostazione dell'articolo selezionato con prodID
     */
    private void impostaArticoloSelezionato() {
        try {
            curArticoli = rsArticoli.getRow();
            tabellaArticoli.setRowSelectionInterval(curArticoli - 1, curArticoli - 1);
            prodID = rsArticoli.getInt(1);
            if (modelloArticoli.getRowCount() == 0)
                abilitaPulsantiArticolo(false);
            else
                abilitaPulsantiArticolo(true);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    /**
     * 
     */
    private void articoliSelectionChanged() {
        try {
            rsArticoli.absolute(tabellaArticoli.getSelectionModel().getMinSelectionIndex() + 1);                    
            impostaArticoloSelezionato();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void impostaComboPrivacy() {
        modificaPrivacy = false;
        cPrivacyLista.removeAllItems();
        cPrivacyLista.addItem("Privata");
        cPrivacyLista.addItem("Condivisa");
        cPrivacyLista.addItem("Pubblica");
        cPrivacyLista.setSelectedIndex(0);
    }
    
    /**
     * Converte la privacy in ID
     * @return 0 se privata, 1 se condivisa, 2 se pubblica.
     */
    private int ottieniPrivacy() {
        try {
            switch(rsListe.getString(3)) {
                case "Privata": return 0;
                case "Condivisa": return 1;
                case "Pubblica": return 2;
                default: return 0;
            }
        } catch (SQLException ex) {
            mostraErrore(ex);
            return -1;
        }
    }
    
    private void aggiornaComboPrivacy() {
        modificaPrivacy = false;
        cPrivacyLista.setSelectedIndex(ottieniPrivacy());
        modificaPrivacy = true;
    }
    
    private void abilitaPulsantiArticolo(boolean stato) {
        bRimuoviArticolo.setEnabled(stato);
        bAggiungiCarrello.setEnabled(stato);
    }
    
    private void abilitaPulsantiListeDesideri(boolean stato) {
        bElimina.setEnabled(stato);
        bRinomina.setEnabled(stato);
        cPrivacyLista.setEnabled(stato);
    }
    
    private void rimuoviArticolo() {
        try {
            DBConnection.rimuoviArticoloLista(listaID, prodID);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabellaArticoli();
    }
    
    private void aggiungiCarrello() {
        int quantita;
        try {
            String stringa = JOptionPane.showInputDialog(this, "Inserisci la quantità:");
            System.out.println(stringa);
            quantita = Integer.parseInt(JOptionPane.showInputDialog(this, "Inserisci la quantità:"));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Il valore inserito per la quantità non è valido", null, ERROR_MESSAGE);
            return;
        }
        try {
            DBConnection.inserisciArticoloCarrello(utenteID, prodID, quantita);
            JOptionPane.showMessageDialog(this, "Articolo aggiunto al carrello.");
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void eliminaListaDesideri() {
        try {
            DBConnection.eliminaListaDesideri(listaID);
            aggiornaTabellaListeDesideri();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void aggiungiListaDesideri() {
        String nomeLista = JOptionPane.showInputDialog(this, "Inserisci il nuovo nome per la lista dei desideri:");
        try {
            DBConnection.creaListaDesideri(utenteID, nomeLista, 0);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabellaListeDesideri();
    }
    
    private void rinominaLista() {
        String nuovoNome = JOptionPane.showInputDialog(this, "Inserisci il nuovo nome per la lista dei desideri:");
        try {
            DBConnection.rinominaListaDesideri(listaID, nuovoNome);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabellaListeDesideri();
    }
    
    private void modificaPrivacyLista() {
        if (modificaPrivacy == false)
            return;
        try {
            DBConnection.modificaListaDesideri(listaID, cPrivacyLista.getSelectedIndex());
            aggiornaTabellaListeDesideri();
        } catch (SQLException ex) {
            mostraErrore(ex);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaListe = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaArticoli = new javax.swing.JTable();
        bAggiungiCarrello = new javax.swing.JButton();
        bRimuoviArticolo = new javax.swing.JButton();
        bRinomina = new javax.swing.JButton();
        bElimina = new javax.swing.JButton();
        bAggiungi = new javax.swing.JButton();
        cPrivacyLista = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);

        tabellaListe.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabellaListe);

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

        bAggiungiCarrello.setText("Aggiungi articolo al carrello");
        bAggiungiCarrello.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAggiungiCarrelloActionPerformed(evt);
            }
        });

        bRimuoviArticolo.setText("Rimuovi articolo");
        bRimuoviArticolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRimuoviArticoloActionPerformed(evt);
            }
        });

        bRinomina.setText("Rinomina lista desideri");
        bRinomina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRinominaActionPerformed(evt);
            }
        });

        bElimina.setText("Elimina lista desideri");
        bElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEliminaActionPerformed(evt);
            }
        });

        bAggiungi.setText("Nuova lista desideri");
        bAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAggiungiActionPerformed(evt);
            }
        });

        cPrivacyLista.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cPrivacyLista.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cPrivacyListaItemStateChanged(evt);
            }
        });

        jLabel1.setText("Imposta privacy per questa lista:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bRimuoviArticolo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAggiungiCarrello))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(bRinomina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bElimina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bAggiungi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cPrivacyLista, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cPrivacyLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRinomina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bElimina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAggiungi)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAggiungiCarrello)
                    .addComponent(bRimuoviArticolo)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAggiungiCarrelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAggiungiCarrelloActionPerformed
        aggiungiCarrello();
    }//GEN-LAST:event_bAggiungiCarrelloActionPerformed

    private void bRimuoviArticoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRimuoviArticoloActionPerformed
        rimuoviArticolo();
    }//GEN-LAST:event_bRimuoviArticoloActionPerformed

    private void bEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEliminaActionPerformed
        eliminaListaDesideri();
    }//GEN-LAST:event_bEliminaActionPerformed

    private void bAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAggiungiActionPerformed
        aggiungiListaDesideri();
    }//GEN-LAST:event_bAggiungiActionPerformed

    private void bRinominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRinominaActionPerformed
        rinominaLista();
    }//GEN-LAST:event_bRinominaActionPerformed

    private void cPrivacyListaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cPrivacyListaItemStateChanged
        modificaPrivacyLista();
    }//GEN-LAST:event_cPrivacyListaItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAggiungi;
    private javax.swing.JButton bAggiungiCarrello;
    private javax.swing.JButton bElimina;
    private javax.swing.JButton bRimuoviArticolo;
    private javax.swing.JButton bRinomina;
    private javax.swing.JComboBox cPrivacyLista;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabellaArticoli;
    private javax.swing.JTable tabellaListe;
    // End of variables declaration//GEN-END:variables
}
