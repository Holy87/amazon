/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author frbos_000
 */
public class AnagraficaLibri extends javax.swing.JDialog {

    /**
     * Creates new form AnagraficaLibri
     */
    public AnagraficaLibri(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        impostaTabella();
    }
    
    private ResultSet rs;
    private DBTableModel modelloTabella;
    private int cursore = 1;
    
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
        aggiornaTabella();
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            rs = resultSetAggiorna();
            modelloTabella.setRS(rs);
            rs.absolute(cursore);
            mostraDati();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    private void eseguiRicerca(){
        searchBox.setText(null);
        searchButton.setEnabled(false);
        try {
            modelloTabella.setRS(resultSetRicerca(searchBox.getText()));
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private ResultSet resultSetRicerca(String query) throws SQLException {
        return DBConnection.visualizzaListinoLibri(query);
    }
    
    protected ResultSet resultSetAggiorna() throws SQLException {
        return DBConnection.visualizzaListinoLibri();//.visualizzaLibriDisponibili();
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
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println(ex.getMessage());
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
    
    private void apriLibro() {
        FinestraDettagliLibro finestraDettagli = new FinestraDettagliLibro(null, false, modelloTabella.getValueAt(cursore-1, 1).toString());
        finestraDettagli.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formatoLibri = new javax.swing.ButtonGroup();
        condizioniLibri = new javax.swing.ButtonGroup();
        searchBox = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabella = new javax.swing.JTable();
        rbFlessibile = new javax.swing.JRadioButton();
        rbEBook = new javax.swing.JRadioButton();
        rbTutti = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        rbNuovo = new javax.swing.JRadioButton();
        rbUsato = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        rbTutti2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        rbRicondizionato = new javax.swing.JRadioButton();
        rbRigida = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Listino");

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
        tabella.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabella);

        formatoLibri.add(rbFlessibile);
        rbFlessibile.setText("Copertina flessibile");

        formatoLibri.add(rbEBook);
        rbEBook.setText("eBook digitale");

        formatoLibri.add(rbTutti);
        rbTutti.setText("Tutti");

        condizioniLibri.add(rbNuovo);
        rbNuovo.setText("Nuovo");

        condizioniLibri.add(rbUsato);
        rbUsato.setText("Usato");

        jLabel1.setText("Formato");

        condizioniLibri.add(rbTutti2);
        rbTutti2.setText("Tutti");

        jLabel2.setText("Condizioni");

        condizioniLibri.add(rbRicondizionato);
        rbRicondizionato.setText("Ricondizionato");

        formatoLibri.add(rbRigida);
        rbRigida.setText("Copertina rigida");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbTutti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(117, 117, 117))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbFlessibile)
                            .addComponent(jLabel1)
                            .addComponent(rbNuovo)
                            .addComponent(rbUsato)
                            .addComponent(rbRicondizionato)
                            .addComponent(rbTutti2)
                            .addComponent(rbRigida)
                            .addComponent(rbEBook))
                        .addGap(0, 54, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchButton))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbFlessibile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbRigida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbEBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbTutti)
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(rbNuovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbUsato)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbRicondizionato)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbTutti2)
                        .addGap(1, 1, 1)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        eseguiRicerca();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBoxKeyTyped
        searchButton.setEnabled(searchBox.getText().length() > 0);
    }//GEN-LAST:event_searchBoxKeyTyped

    private void tabellaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaMouseClicked
        apriLibro();
    }//GEN-LAST:event_tabellaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup condizioniLibri;
    private javax.swing.ButtonGroup formatoLibri;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton rbEBook;
    private javax.swing.JRadioButton rbFlessibile;
    private javax.swing.JRadioButton rbNuovo;
    private javax.swing.JRadioButton rbRicondizionato;
    private javax.swing.JRadioButton rbRigida;
    private javax.swing.JRadioButton rbTutti;
    private javax.swing.JRadioButton rbTutti2;
    private javax.swing.JRadioButton rbUsato;
    private javax.swing.JTextField searchBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JTable tabella;
    // End of variables declaration//GEN-END:variables
}
