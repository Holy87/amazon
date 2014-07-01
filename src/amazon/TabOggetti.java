/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Francesco
 */
public final class TabOggetti extends javax.swing.JPanel {

    /**
     * Creates new form TabUtenti
     */
    public TabOggetti() {
        initComponents();
        impostaTabella();
    }
    /**
     * Dichiarazione degli attributi
     * 
     */
    private ResultSet rs;
    private DBTableModel modelloTabella;
    private int cursore = 1;
    private String ultimaRicerca;
    private List colonne; //lista delle colonne della tabella
    private final int EDIT = 1;
    private final int ADDN = 2;
    private EditForm finestraEdit;
    private String nomeTabella = "";
    private MainWindow mainWindow;
    
    /**
     * Impostazione del modello della tabella, settaggi e selezione.
     */
    @SuppressWarnings("Convert2Lambda")
    public void impostaTabella() {
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
        //impostaPulsanteServizi();
    }
        
    public void impostaInterfaccia(String entita, EditForm finestra, MainWindow mainW) {
        nomeTabella = entita;
        finestraEdit = finestra;
        mainWindow = mainW;
        setService();
    }
    
    private void setService() {
        switch (nomeTabella) {
            case "UTENTI": serviceButton1.setText("Imposta utente");
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {impostaUtente(evt);}});
                break;
            //case "LIBRI": ALTRE AZIONI
                
            default: serviceButton1.setVisible(false);
            break;
        }
    }
    
    private void impostaUtente(java.awt.event.ActionEvent evt) {
        List utente = getDataCollection();
        int id = Integer.parseInt((String)utente.get(0));
        String nome = (String)utente.get(1) + " " + (String)utente.get(2);
        mainWindow.impostaUtente(id, nome);
    }
    
    /**
     * Viene chiamato quando il pane diventa visibile. 
     */
    private void setColumnList()
    {
        colonne = new LinkedList();
        try {
            for (int i = 0; i < modelloTabella.getColumnCount(); i++)
            {
                colonne.add(modelloTabella.getDBColumnName(i+1));
            }
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che viene chiamato alla prima connessione
     */
    public void connectTable() {
        aggiornaTabella();
        setColumnList();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        aggiornaTabella("SELECT * FROM " + getTableName());        
    }
    
    /**
     * Aggiorna i dati della tabella secondo una query
     * @param query stringa da inserire come query
     */
    private void aggiornaTabella(String query)
    {
        ultimaRicerca = query;
        try {
            rs = DBConnection.eseguiQuery(query);
            modelloTabella.setResultSet(rs);
            rs.absolute(cursore);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabella.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabella.setRowSelectionInterval(cursore - 1, cursore - 1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      }
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
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    /**
     * Metodo che viene chiamato quando è cliccato il pulsante cerca
     * Cerca il valore o parte in tutti i campi
     * Il % prima e dopo una stringa commette un LIKE
     */
    private void eseguiQuerySuTabella(String query){
        String preQuery = "SELECT * FROM " + getTableName() + " WHERE ";
        Iterator itr = colonne.iterator();
        while(itr.hasNext()){
            String colonna = (String)itr.next();
            if (colonna.indexOf("%") > 0)
                preQuery += colonna + " LIKE " + query;
            else
                preQuery += colonna + " = " + query;
        preQuery += " OR ";
        }
        preQuery = preQuery.substring(0, preQuery.length()-4);
        System.out.println(preQuery);
        aggiornaTabella(preQuery);
    }
    
    /**
     * Ottiene una stringa con l'ID del record selezionato dalla tabella.
     * @return 
     */
    private String getSelectedID() {
        return (String)tabella.getValueAt(cursore, 1);
    }
    
    /**
     * Elimina il record attualmente selezionato dalla tabella
     * Fare un override se l'eliminazione dev'essere modificata
     */
    private void eliminaRecord() {
       String id = (String)modelloTabella.getValueAt(cursore-1, 0).toString();
       int risposta = JOptionPane.showConfirmDialog(mainWindow, "Sei sicuro di voler eliminare il record?", "Eliminazione record", YES_NO_OPTION);
       if (risposta == 0) {
           try {
                DBConnection.eliminaUtente(id);
                aggiornaTabella(ultimaRicerca);
                
           } catch (SQLException e) {
               mostraErrore(e);
           }
       }
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
        JOptionPane.showMessageDialog(mainWindow, "Errore: " + errore, null, ERROR_MESSAGE);
    }
    
    /**
     * Restituisce il nome della tabella del database a cui si riferisce la
     * sottoclasse. Ad esempio, basta metterci dentro
     * return "UTENTI"
    **/
    private String getTableName()
    {
        return nomeTabella;
    }
    /**
     * Metodo che viene chiamato quando è cliccato il pulsante "Nuovo"
     */
    private void nuovoRecord()
    {
        finestraEdit.show(ADDN, null);
        aggiornaTabella(ultimaRicerca);
    }
    
    /**
     * Metodo che viene chiamato quando è cliccato il pulsante "Modifica"
     */
    private void modificaRecord()
    {
        System.out.println(getDataCollection().size());
        finestraEdit.show(EDIT, getDataCollection());
        aggiornaTabella(ultimaRicerca);
    }
    
    private List getDataCollection()
    {
        List dati = new LinkedList();
        for (int i = 0; i < modelloTabella.getColumnCount(); i++)
        {
            dati.add((String)modelloTabella.getValueAt(cursore-1, i).toString());
        }
        return dati;
    }
    
    private void pulsanteDiServizio()
    {
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabella = new javax.swing.JTable();
        newRecord = new javax.swing.JButton();
        updateTable = new javax.swing.JButton();
        editRecord = new javax.swing.JButton();
        deleteRecord = new javax.swing.JButton();
        searchBox = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        serviceButton1 = new javax.swing.JToggleButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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

        newRecord.setText("Nuovo");
        newRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRecordActionPerformed(evt);
            }
        });

        updateTable.setText("Aggiorna");
        updateTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTableActionPerformed(evt);
            }
        });

        editRecord.setText("Modifica");
        editRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRecordActionPerformed(evt);
            }
        });

        deleteRecord.setText("Elimina");
        deleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRecordActionPerformed(evt);
            }
        });

        searchBox.setToolTipText("Scrivi la query di ricerca");
        searchBox.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        searchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchBoxKeyTyped(evt);
            }
        });

        searchButton.setText("Cerca");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jButton1.setText("<<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(">");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(">>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Scorrimento:");

        serviceButton1.setText("serviceButton1");
        serviceButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newRecord)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateTable, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addComponent(serviceButton1))
                .addContainerGap(59, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(updateTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton)
                    .addComponent(serviceButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void newRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRecordActionPerformed
        nuovoRecord();
    }//GEN-LAST:event_newRecordActionPerformed

    private void editRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRecordActionPerformed
        modificaRecord();
    }//GEN-LAST:event_editRecordActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        String query = searchBox.getText();
        searchBox.setText("");
        searchButton.setEnabled(false);
        eseguiQuerySuTabella(query);
    }//GEN-LAST:event_searchButtonActionPerformed

    private void deleteRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRecordActionPerformed
        eliminaRecord();
    }//GEN-LAST:event_deleteRecordActionPerformed

    private void updateTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTableActionPerformed
        aggiornaTabella();
    }//GEN-LAST:event_updateTableActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
          rs.first();
       } catch (SQLException e) {
          mostraErrore(e);
       }
       mostraDati();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
          if (!rs.isFirst()) {
             rs.previous();
          }
       } catch (SQLException e) {
          mostraErrore(e);
       }
       mostraDati();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
          if (!rs.isLast()) {
             rs.next();
          }
       } catch (SQLException e) {
          mostraErrore(e);
       }
       mostraDati();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
          rs.last();
       } catch (SQLException e) {
          mostraErrore(e);
       }
       mostraDati();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBoxKeyTyped
        searchButton.setEnabled(searchBox.getText().length() > 0);
    }//GEN-LAST:event_searchBoxKeyTyped

    private void serviceButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceButton1ActionPerformed
        pulsanteDiServizio();
    }//GEN-LAST:event_serviceButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteRecord;
    private javax.swing.JButton editRecord;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newRecord;
    private javax.swing.JTextField searchBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JToggleButton serviceButton1;
    private javax.swing.JTable tabella;
    private javax.swing.JButton updateTable;
    // End of variables declaration//GEN-END:variables

}
