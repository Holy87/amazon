/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.modelliTabelle.DBTableModel;
import amazon.utility.Autore;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
public class FinestraAutoriLibro extends javax.swing.JDialog {

    /**
     * Creates new form EsempioTabella
     * @param parent va inserita la finestra chiamante (un jFrame)
     * @param modal va sempre in false
     * @param isbn ISBN del libro di cui visualizzare gli autori
     */
    public FinestraAutoriLibro(java.awt.Frame parent, boolean modal, String isbn) {
        super(parent, modal);
        this.isbn = isbn;
        initComponents();
        impostaTabella();   // aggiungere al costruttore questo metodo in modo
                            // da impostare il set di dati
    }
    
    private final String isbn; //ISBN selezionato in precedenta da TabOggetti
    private int autoreID;
    private ResultSet rs; //ResultSet su cui si basano i dati della tabella
    private DBTableModel modelloTabella; //modello della tabella per i dati
    private int cursore = 1; //memorizza la riga selezionata
    
    /**
     * Inizializza i dati della tabella, assegnandogli il modello e il rs.
     */
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabella = new DBTableModel(rs);//inserire il resultset nel costr.
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
        //infine aggiorno il resultset della tabella
        aggiornaTabella();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            rs = ottieniDati(); //chiama il metodo in basso
                                //non è proprio necessario chiamare un metodo
                                //si può anche direttamente passare il reslts.
            modelloTabella.setRS(rs);   //non credo serva, ma il prof lo mette..
            rs.absolute(cursore);   //attiva la riga del cursore attuale
            mostraDati();           //imposta la selezione a riga singola
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
        return DBConnection.visualizzaAutoriLibro(isbn);
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
            System.out.println("ok");
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
          autoreID = rs.getInt(1);
          if (modelloTabella.getRowCount() == 0)
            abilitaPulsanteElimina(false);
          else
            abilitaPulsanteElimina(true);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }
    
    /*private void impostaAutoreSelezionato() {
        try {
            curAutori = rs.getRow();
            tabella.setRowSelectionInterval(curAutori - 1, curAutori - 1);
            autoreID = rs.getInt(1);
            if (modelloTabella.getRowCount() == 0)
                abilitaPulsanteElimina(false);
            else
                abilitaPulsanteElimina(true);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
      }
    }*/
    
    private void rimuoviAutore() {
        try {
            DBConnection.rimuoviAutoreLibro(autoreID, isbn);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTabella();
    }
    
    private void aggiungiAutore() {
        try {
            
            ResultSet autori = DBConnection.eseguiQuery("SELECT * FROM AUTORI");
            int[] autoriInLibro = new int[modelloTabella.getRowCount()];
            for (int i = 0; i < autoriInLibro.length; i++) {
                autoriInLibro[i] = Integer.parseInt(modelloTabella.getValueAt(cursore - 1, 0).toString());
            }
            LinkedList<Autore> listaAutori = new LinkedList();
            while (autori.next()) {
                if (autoreNonPresente(autoriInLibro, autori.getInt(1)))
                    listaAutori.add(new Autore(autori.getInt(1),autori.getString(2), autori.getString(3)));
            }
            Autore[] elencoAutori = listaAutori.toArray(new Autore[listaAutori.size()]);
            Autore risposta = (Autore)JOptionPane.showInputDialog(this, "Seleziona l'autore da aggiungere al libro", "Aggiungi autore", JOptionPane.QUESTION_MESSAGE, null, elencoAutori, JOptionPane.OK_CANCEL_OPTION);
            //AGGIUNTA DELL'AUTORE AL LIBRO
        } catch (SQLException ex) {
            Logger.getLogger(FinestraAutoriLibro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean autoreNonPresente(int[] idAutori, int autore) {
        for (int i = 0; i < idAutori.length; i++) {
            if (idAutori[i] == autore)
                return false;
        }
        return true;
    }
    
    private void abilitaPulsanteElimina(boolean stato) {
        bDeleteAuthor.setEnabled(stato);
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
        tabella = new javax.swing.JTable();
        bAddAuthor = new javax.swing.JButton();
        bDeleteAuthor = new javax.swing.JButton();

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

        bAddAuthor.setText("Aggiungi autore");
        bAddAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddAuthorActionPerformed(evt);
            }
        });

        bDeleteAuthor.setText("Elimina autore");
        bDeleteAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDeleteAuthorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bAddAuthor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDeleteAuthor)
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAddAuthor)
                    .addComponent(bDeleteAuthor)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDeleteAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDeleteAuthorActionPerformed
        rimuoviAutore();
    }//GEN-LAST:event_bDeleteAuthorActionPerformed

    private void bAddAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddAuthorActionPerformed
        aggiungiAutore();
    }//GEN-LAST:event_bAddAuthorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddAuthor;
    private javax.swing.JButton bDeleteAuthor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabella;
    // End of variables declaration//GEN-END:variables
}
