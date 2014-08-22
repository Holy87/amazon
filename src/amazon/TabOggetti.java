/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.utility.ListaTipi;
import amazon.modelliTabelle.DBTableModel;
import java.sql.PreparedStatement;
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
 * Classe che serve per gli oggetti. Non riuscivo ad ereditare la classe,
 * quindi ho dovuto farne una unica che cambiasse comportamento a seconda
 * della tabella.
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
    private List colonne; //lista delle colonne della tabella
    PreparedStatement ultimaRicerca;
    private final int EDIT = 1; // definizione per l'apertura della finestra in modifica
    private final int ADDN = 2; // definizione per l'aggiunta
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
    
    /**
     * Imposta l'entità della tabella da cui prendere i dati.
     * @param entita stringa nome della tabella.
     * @param finestra finestra di modifica e aggiunta dei dati
     * @param mainW finestra su cui si basa il jPane.
     */
    public void impostaInterfaccia(String entita, EditForm finestra, MainWindow mainW) {
        nomeTabella = entita;
        finestraEdit = finestra;
        mainWindow = mainW;
        setService();
        setService2();
        setService3();
    }
    
//    public void impostaInterfaccia(int tipoResultSe) {
//        setting = tipoResultSet;
//        switch (tipoResultSet) {
//            case LIBAUT: impostaTabella(DBConnection.visualizzaLibriAutore())
//        }
//    }
    
    @SuppressWarnings("unchecked")
    private void setService() {
        switch (nomeTabella) {
            case "UTENTI": serviceButton1.setText("Imposta utente");
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {impostaUtente(evt);}});
                break;
            case "AUTORI": serviceButton1.setText("Visualizza libri autore");
                //GESTIONE METODO: Visualizza tutti i libri dell'autore selezionato in un'altra finestra
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaLibriAutore(evt);}});
                break;
            case "EDITORI": serviceButton1.setText("Visualizza libri editore");
                //GESTIONE METODO: Visualizza tutti i libri dell'editore selezionato in un'altra finestra
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaLibriEditore(evt);}});
                break;
            case "LIBRI": serviceButton1.setText("Visualizza recensioni libro");
                //GESTIONE METODO: Visualizza le recensioni del libro
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaRecensioniLibro(evt);}});
                break;
            case "VENDITORI": serviceButton1.setText("Visualizza libri venditore");
                //GESTIONE METODO: Visualizza TUTTE le info sul libro, comprese quelle non visibili nella tabella
                //GESTIONE METODO: Visualizza TUTTI i libri del Venditore, con relative informazioni
                serviceButton1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaLibriVenditore(evt);}});
                break;
            default: serviceButton1.setVisible(false);
            break;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setService2() {
        switch (nomeTabella) {
            case "UTENTI": serviceButton2.setText("Visualizza rubrica utente");
                serviceButton2.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaRubricaUtente(evt);}});
                break;
            case "VENDITORI": serviceButton2.setText("Aggiungi libro a venditore");
                serviceButton2.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {inserisciLibro(evt);}});
                break;
            case "LIBRI": serviceButton2.setText("Visualizza autori del libro");
                serviceButton2.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaAutoriLibro(evt);}});
                break;
            default: serviceButton2.setVisible(false);
        }
    }
    
    private void setService3() {
        switch (nomeTabella) {
            case "UTENTI": serviceButton3.setText("Visualizza Modalità pagamenti");
                serviceButton3.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaModPagamenti(evt);}});
                break;
            case "VENDITORI": serviceButton3.setText("Visualizza recensioni");
                serviceButton3.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaRecensioniVenditore(evt);}});
                break;
            case "LIBRI": serviceButton3.setText("Visualizza editori del libro");
                serviceButton3.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {visualizzaEditoriLibro(evt);}});
                break;
            default: serviceButton3.setVisible(false);
        }
    }
    
    private void impostaAbilitazionePulsanti(boolean abilitazione) {
        editRecord.setEnabled(abilitazione);
        deleteRecord.setEnabled(abilitazione);
        jButton2.setEnabled(abilitazione);
        jButton3.setEnabled(abilitazione);
        serviceButton1.setEnabled(abilitazione);
        serviceButton2.setEnabled(abilitazione);
        serviceButton3.setEnabled(abilitazione);
    }
    
    /**
     * Attivazione del secondo pulsante inizialmente impostato come disabilitato
     */
    public void attivaServiceButton2() {
        serviceButton2.setEnabled(true);
    }
    
    /**
     * Attivazione del terzo pulsante di servizio, inizialmente disabilitato
     */
    /*public void attivaServiceButton3() {
        serviceButton3.setEnabled(true);
    }*/
    
    private void impostaUtente(java.awt.event.ActionEvent evt) {
        List utente = getDataCollection();
        int id = Integer.parseInt((String)utente.get(0));
        String nome = (String)utente.get(1) + " " + (String)utente.get(2);
        mainWindow.impostaUtente(id, nome);
    }
    
    private void visualizzaRubricaUtente(java.awt.event.ActionEvent evt) {
        FinestraRubricaUtente rubricaUtente = new FinestraRubricaUtente(mainWindow, true, Integer.parseInt(getSelectedID()));
        rubricaUtente.setVisible(true);
    }
    
    private void visualizzaModPagamenti(java.awt.event.ActionEvent evt) {
        FinestraModPagamento modPagamento = new FinestraModPagamento(mainWindow, true, Integer.parseInt(getSelectedID()));
        modPagamento.setVisible(true);
    }
    
    private void visualizzaLibriAutore(java.awt.event.ActionEvent evt) {
        FinestraLibriAutore libriAutore = new FinestraLibriAutore(mainWindow, true, getSelectedID());
        libriAutore.setVisible(true);
    }
    
    private void visualizzaLibriEditore(java.awt.event.ActionEvent evt) {
        FinestraLibriEditore libriEditore = new FinestraLibriEditore(mainWindow, true, getSelectedID());
        libriEditore.setVisible(true);
    }
    
    private void visualizzaLibriVenditore(java.awt.event.ActionEvent evt) {
        FinestraMagazzino magazzino = new FinestraMagazzino(mainWindow, true, getSelectedID());
        magazzino.setVisible(true);
    }
    
    private void visualizzaRecensioniLibro(java.awt.event.ActionEvent evt) {
        FinestraListaRecensioniLibri listaRecensioni = new FinestraListaRecensioniLibri(mainWindow, true, modelloTabella.getValueAt(cursore-1, 2).toString(), mainWindow.utenteID);
        listaRecensioni.setVisible(true);
    }
    
    private void inserisciLibro(java.awt.event.ActionEvent evt) {
        AggiungiLibriVenditore finestra = new AggiungiLibriVenditore(mainWindow, true, Integer.parseInt(getSelectedID()));
        finestra.setVisible(true);
    }
    
    private void visualizzaAutoriLibro(java.awt.event.ActionEvent evt) {
        FinestraAutoriLibro autoriLibro = new FinestraAutoriLibro(mainWindow, true, modelloTabella.getValueAt(cursore-1, 2).toString());
        autoriLibro.setVisible(true);
    }
    
    private void visualizzaEditoriLibro(java.awt.event.ActionEvent evt) {
        FinestraEditoriLibro editoriLibro = new FinestraEditoriLibro(mainWindow, true, modelloTabella.getValueAt(cursore-1, 2).toString());
        editoriLibro.setVisible(true);
    }
    
    private void visualizzaRecensioniVenditore(java.awt.event.ActionEvent evt) {
        FinestraListaRecensioniVenditori finestraReceVenditori = new FinestraListaRecensioniVenditori(mainWindow, true, mainWindow.utenteID, getSelectedID());
        finestraReceVenditori.setVisible(true);
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
     * Metodo che viene chiamato alla prima connessione con il database
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
        try {
            if (!"LIBRI".equals(getTableName()))
                rs = DBConnection.eseguiQuery("SELECT * FROM " + getTableName());
            else
                rs = DBConnection.eseguiQuery("SELECT LIBRO_NOME, EDIZIONE_N, ISBN, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, TO_CHAR(DATAUSCITA, 'DD/MM/YYYY') AS DATA_USCITA FROM LIBRI");
            modelloTabella.setRS(rs);
            rs.first();
            //rs.absolute(cursore);
            ultimaRicerca = null;
            aggiornaCursore();
        } catch (SQLException ex) {
            System.out.println("aggiornaTabella");
            mostraErrore(ex);
        }
        
    }
    
    /**
     * Aggiorna la tabella secondo la ricerca dell'utente
     */
    public void aggiornaTabellaConQuery() {
        if (ultimaRicerca == null)
            aggiornaTabella();
        else
            aggiornaTabella(ultimaRicerca);
        try {
            modelloTabella.getValueAt(cursore - 1, 0);
        } catch (java.lang.IllegalArgumentException ex)
        {
            impostaAbilitazionePulsanti(false);
        }
    }
    
    /**
     * Aggiorna i dati della tabella secondo una query
     * @param query stringa da inserire come query
     */
    private void aggiornaTabella(PreparedStatement pstmt)
    {
        ultimaRicerca = pstmt;
        try {
            rs = pstmt.executeQuery();
            modelloTabella.setRS(rs);
            rs.first();
            //rs.absolute(cursore);
            aggiornaCursore();
        } catch (SQLException ex) {
            System.out.println("aggiornaTabella");
            mostraErrore(ex);
        }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void aggiornaCursore() {
      try {
          cursore = rs.getRow();
          tabella.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          //tabella.setRowSelectionInterval(cursore - 1, cursore - 1);
          impostaAbilitazionePulsanti(true);
      } catch (SQLException ex) {
          System.out.println("aggiornaCursore");
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          impostaAbilitazionePulsanti(false);
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
            aggiornaCursore();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che viene chiamato quando è cliccato il pulsante cerca
     * Cerca il valore o parte in tutti i campi
     * Il % prima e dopo una stringa commette un LIKE
     * Si adatta al ResultSet leggendo i nomi delle colonne.
     * @param query stringa da cercare
     */
    private void eseguiQuerySuTabella(String query){
        LinkedList tab = new ListaTipi().getTipi().get(getTableName());
        LinkedList tab2 = new LinkedList();
        String preQuery = "SELECT * FROM " + getTableName() + " WHERE ";
        Iterator itr = colonne.iterator();
        int num = 0;
        boolean ok = false;
        while(itr.hasNext()){
            String colonna = (String)itr.next();
            switch ((char)tab.get(num)) {
                case 'i': try {
                        int in = Integer.parseInt(query);
                        if (ok == true)
                            preQuery += " OR ";
                        preQuery += colonna + " = ?";
                        tab2.add('i');
                        ok = true;
                } catch (NumberFormatException e) {
                    System.out.println(query + " non valido su " + colonna);
                }
                    break;
                case 's': if (ok == true)
                            preQuery += " OR "; 
                    if (query.contains("%"))
                        preQuery += "UPPER(" + colonna + ") LIKE UPPER(?)";
                    else
                        preQuery += "UPPER(" + colonna + ") = UPPER(?)";
                    tab2.add('s');
                    ok = true;
                    break;
                case 'f': try {
                    Float.parseFloat(query);
                    if (ok == true)
                            preQuery += " OR ";
                    preQuery += colonna + " = ?";
                    tab2.add('f');
                    ok = true;
                } catch (NumberFormatException e) {
                    System.out.println(query + " non valido su " + colonna);
                }
                    break;
            }
            num ++;
            
        }
        if (" OR ".equals(preQuery.substring(preQuery.length()-4, preQuery.length())))
            preQuery = preQuery.substring(0, preQuery.length()-4);
        System.out.println(preQuery);
        try {
            PreparedStatement pstmt = DBConnection.conn.prepareStatement(
                    preQuery,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < tab2.size(); i++) {
                switch ((char)tab2.get(i)) {
                    case 'i': pstmt.setInt(i+1, Integer.parseInt(query.replace('%', ' ')));
                        break;
                    case 's': pstmt.setString(i+1, query);
                        break;
                    case 'f': pstmt.setFloat(i+1, Float.parseFloat(query.replace('%', ' ')));
                        break;
                }
            }
            aggiornaTabella(pstmt);
            
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Ottiene una stringa con l'ID del record selezionato dalla tabella.
     * @return 
     */
    private String getSelectedID() {
        int id = Integer.parseInt((String)tabella.getValueAt(cursore-1, 0).toString());
        return "" + (id);
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
                DBConnection.eliminaRecord(id, getTableName(), modelloTabella.getDBID());
                aggiornaTabellaConQuery();
                
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
        finestraEdit.show(ADDN, null, this);
    }
    
    /**
     * Metodo che viene chiamato quando è cliccato il pulsante "Modifica"
     */
    private void modificaRecord()
    {
        finestraEdit.show(EDIT, getDataCollection(), this);
    }
    
    private List<String> getDataCollection()
    {
        List<String> dati = new LinkedList();
        for (int i = 0; i < modelloTabella.getColumnCount(); i++)
        {
            try {
                dati.add((String)modelloTabella.getValueAt(cursore-1, i).toString());
            } catch (NullPointerException ex) {
                dati.add("");
            }
        }
        return dati;
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
        serviceButton2 = new javax.swing.JButton();
        serviceButton1 = new javax.swing.JButton();
        serviceButton3 = new javax.swing.JButton();

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
        searchBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        searchBox.setMaximumSize(new java.awt.Dimension(6, 20));
        searchBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBoxActionPerformed(evt);
            }
        });
        searchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchBoxKeyTyped(evt);
            }
        });

        searchButton.setText("Cerca");
        searchButton.setEnabled(false);
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

        serviceButton2.setText("serviceButton2");

        serviceButton1.setText("serviceButton1");

        serviceButton3.setText("serviceButton3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newRecord)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateTable, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serviceButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serviceButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceButton3)
                .addContainerGap(103, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(updateTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchButton)
                        .addComponent(serviceButton2)
                        .addComponent(serviceButton1)
                        .addComponent(serviceButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE))
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
       aggiornaCursore();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
          if (!rs.isFirst()) {
             rs.previous();
          }
       } catch (SQLException e) {
          mostraErrore(e);
       }
       aggiornaCursore();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
          if (!rs.isLast()) {
             rs.next();
          }
       } catch (SQLException e) {
          mostraErrore(e);
       }
       aggiornaCursore();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
          rs.last();
       } catch (SQLException e) {
          mostraErrore(e);
       }
       aggiornaCursore();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBoxKeyTyped
        searchButton.setEnabled(searchBox.getText().length() > 0);
    }//GEN-LAST:event_searchBoxKeyTyped

    private void searchBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBoxActionPerformed
        searchButtonActionPerformed(evt);
    }//GEN-LAST:event_searchBoxActionPerformed


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
    private javax.swing.JButton serviceButton1;
    private javax.swing.JButton serviceButton2;
    private javax.swing.JButton serviceButton3;
    private javax.swing.JTable tabella;
    private javax.swing.JButton updateTable;
    // End of variables declaration//GEN-END:variables

}
