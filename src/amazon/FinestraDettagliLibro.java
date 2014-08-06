/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.modelliTabelle.DBTableModel;
import amazon.utility.ListaDesideri;
import java.awt.Color;
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
public class FinestraDettagliLibro extends javax.swing.JDialog {

    /**
     * Creates new form FinestraDettagliLibro
     * @param parent la finestra chiamante
     * @param modal sempre false
     * @param isbn il codice del libro da visualizzare
     * @param padre è la classe che crea la finestra
     * @param idUtente è l'ID dell'utente attivo per l'acquisto
     */
    public FinestraDettagliLibro(java.awt.Frame parent, boolean modal, String isbn, java.awt.Dialog padre, int idUtente) {
        super(parent, modal);
        this.padre = padre;
        this.idUtente = idUtente;
        this.padre.setVisible(false);
        try {
            libro = DBConnection.visualizzaInfoLibro(isbn);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        this.isbn = isbn;
        initComponents();
        assegnaDettagliLibro();
        impostaTabella();
        aggiornaListeDesideri();
    }
    
    private String isbn, titolo, autore, editore, formato, stato, genere, data, descrizione, prezzo, peso;
    private int disponibilita, pagine, idUtente, formatoId, venditoreId, prodId;
    private long voto; 
    private java.awt.Dialog padre;
    
    private LinkedList<ListaDesideri> listeDesideriUtente;
    private ResultSet rs, rs2, libro;//libro è il resultset a 1 riga
    private DBTableModel modelloTabellaVenditori;
    private DBTableModel modelloTabellaFormati;
    private int cursore = 1;    //cursore della prima tabella
    private int cursore2 = 1;   //cursore della seconda tabella       
    
    @SuppressWarnings("Convert2Lambda")
    public final void impostaTabella() {
        modelloTabellaVenditori = new DBTableModel(rs);
        tabellaVenditori.setModel(modelloTabellaVenditori); //metto il modellotabella nel
        tabellaVenditori.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaVenditori.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        //nasconde l'ID del venditore
        modelloTabellaFormati = new DBTableModel(rs2);
        tabellaFormati.setModel(modelloTabellaFormati);
        tabellaFormati.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaFormati.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                tableSelectionChanged2();
            }
        });
        
        aggiornaTabella();
        aggiornaTabella2();
    }
    
    /**
     * Operazione di stampa di tutte le informazioni del libro sulla
     * finestra.
     */
    private void assegnaDettagliLibro() {
        try {
            libro.first();
            titolo = libro.getString(1);
            autore = libro.getString(2) + " " + libro.getString(3);
            editore = libro.getString(4);
            descrizione = libro.getString(6);
            genere = libro.getString(7);
            try {
                pagine = Integer.parseInt(libro.getString(8));
            } catch(NumberFormatException ex) {
                System.out.println("PAGINE   " + ex);
            }
            try {
                peso = libro.getString(9);
            } catch(NumberFormatException ex) {
                System.out.println("PESO   " + ex);
            }
            data = libro.getNString(10);
            try {
            voto = Long.parseLong(libro.getString(11));
            } catch(NumberFormatException ex) {
                voto = 0;
            }
            tTitolo.setText(titolo);
            tAutore.setText("di "+autore);
            tEditore.setText("Editore: " + editore);
            tDescrizione.setText(descrizione);
            tGenere.setText("Genere: " + genere);
            tPagine.setText("Pagine: " + pagine);
            tPeso.setText("Peso: " + peso);
            tAnno.setText("Data uscita: " + data);
            tVoto.setText("Voto medio: " + voto + "/5");
            tCodice.setText("ISBN: "+isbn);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    private void aggiornaTabella()
    {
        try {
            rs = DBConnection.visualizzaVenditoriLibro(isbn);
            modelloTabellaVenditori.setRS(rs);
            rs.absolute(cursore);
            mostraDati();
            tabellaVenditori.getColumnModel().getColumn(0).setMinWidth(0);
            tabellaVenditori.getColumnModel().getColumn(0).setMaxWidth(0);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che aggiunge alla combo box tutte le liste dei desideri
     */
    private void aggiornaListeDesideri() {
        listeDesideriUtente = new LinkedList();
        listeDesideri.removeAllItems();
        
        try {
            ResultSet liste = DBConnection.visualizzaListeUtente(idUtente);
            while (liste.next()) {
                ListaDesideri lista = new ListaDesideri(liste.getInt(1), liste.getString(2));
                listeDesideriUtente.add(lista);
                listeDesideri.addItem(lista.getNomeLista());
            }
            listeDesideri.addItem("Nuova lista desideri");
            listeDesideri.setSelectedIndex(0);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiunge l'oggetto alla lista desideri selezionata.
     */
    private void aggiungiListaDesideri() {
        if (listeDesideri.getSelectedIndex() == listeDesideri.getItemCount()-1) {
            String nomeLista = JOptionPane.showInputDialog(this, "Dai un nome alla nuova lista desideri");
            if (!"".equals(nomeLista)) {
                try {
                    DBConnection.creaListaDesideri(idUtente, nomeLista, prodId);
                    DBConnection.inserisciArticoloLista(DBConnection.ultimaListaDesideri(), prodId, prezzo);
                    JOptionPane.showMessageDialog(this, titolo + " aggiunto a " + nomeLista);
                } catch (SQLException ex) {
                    mostraErrore(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Errore, inserisci un nome valido!");
            }
        } else {
            try {
                ListaDesideri lista = listeDesideriUtente.get(listeDesideri.getSelectedIndex());
                DBConnection.inserisciArticoloLista(lista.getIdLista(), prodId, prezzo);
                JOptionPane.showMessageDialog(this, titolo + " aggiunto a " + lista.getNomeLista());
            } catch (SQLException ex) {
                mostraErrore(ex);
            }
        }
    }
    
    /**
     * Metodo di aggiornamento della tabella dei formati del libro disponibili
     * per il venditore. Viene chiamato all'avvio della finestra e ogni volta
     * che si seleziona un venditore.
     */
    private void aggiornaTabella2() {
        try {
            rs2 = DBConnection.visualizzaFormatoLibroVenditore(isbn,
                    Integer.parseInt(modelloTabellaVenditori.getValueAt(cursore-1, 0).toString()));
            modelloTabellaFormati.setRS(rs2);
            cursore2 = 1;
            rs2.absolute(cursore2);
            mostraDati2();
            tabellaFormati.getColumnModel().getColumn(0).setMinWidth(0);
            tabellaFormati.getColumnModel().getColumn(0).setMaxWidth(0);
            tabellaFormati.getColumnModel().getColumn(1).setMinWidth(0);
            tabellaFormati.getColumnModel().getColumn(1).setMaxWidth(0);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        
    }
    
    /**
     * Aggiorna le informazioni variabili sul formato. Viene chiamato ogni
     * volta che si cambia la selezione del formato.
     */
    private void aggiornaDatiLibro() {
        tPrezzo.setText("€"+prezzo);
        tFormato.setText("Formato: " + formato);
        tCondizioni.setText("Condizioni: " + stato);
        tDisponibile.setText("Disponibilità: " + getDisponibilita());
    }
    
    /**
     * Viene chiamato all'aggiornamento della selezione del formato. Restituisce
     * una stringa da mostrare in base alla disponibilità
     * @return stringa variabile sulla disponibilità. Se è maggiore di 10 non
     * indica il numero.
     * Il metodo cambia anche colore del testo a seconda della disponibilità del
     * prodotto.
     */
    private String getDisponibilita() {
        if (disponibilita < 0) {
            tDisponibile.setForeground(Color.GREEN);
            tQuantita.setEnabled(false);
            tQuantita.setText("1");
            return "Illimitata";
        } else if (disponibilita > 10) {
            tDisponibile.setForeground(Color.GREEN);
            tQuantita.setEnabled(true);
            return "più di 10 disponibili";}
          else  if (disponibilita <= 10 && disponibilita > 0) {
            tDisponibile.setForeground(Color.ORANGE);
            tQuantita.setEnabled(true);
            return disponibilita + " disponibili";
        } else {
            tDisponibile.setForeground(Color.RED);
            tQuantita.setEnabled(true);
            return "Non disponibile";
          }
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void mostraDati() {
      try {
          cursore = rs.getRow();
          tabellaVenditori.getSelectionModel().setSelectionInterval(cursore - 1,cursore - 1);
          tabellaVenditori.setRowSelectionInterval(cursore - 1, cursore - 1);
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          System.out.println("mostraDati: " + ex.getMessage());
      }
    }
    
    private void mostraDati2() {
        try {
          cursore2 = rs2.getRow();
          int cur = cursore2 - 1;
          tabellaFormati.getSelectionModel().setSelectionInterval(cur, cur);
          tabellaFormati.setRowSelectionInterval(cur, cur);
          prezzo = modelloTabellaFormati.getValueAt(cur, 5).toString();
          prodId = Integer.parseInt(modelloTabellaFormati.getValueAt(cur, 0).toString());
          if (modelloTabellaFormati.getValueAt(cur, 3) == null) {
              disponibilita = -1;
          } else
              disponibilita = Integer.parseInt(modelloTabellaFormati.getValueAt(cur, 4).toString());
          stato = modelloTabellaFormati.getValueAt(cur, 3).toString();
          formato = modelloTabellaFormati.getValueAt(cur, 2).toString();
          formatoId = Integer.parseInt(modelloTabellaFormati.getValueAt(cur, 1).toString());
          aggiornaDatiLibro();
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
      }
    }
    
    /**
     * Mostra l'errore della connessione al database in finestra popup.
     * @param ex eccezione da mostrare
     */
    private void mostraErrore(SQLException ex) {
        String errore = "(Finestra dettagli libro) Errore di connessione al database";
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
            rs.absolute(tabellaVenditori.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati();
            venditoreId = Integer.parseInt(modelloTabellaVenditori.getValueAt(cursore - 1, 0).toString());
            aggiornaTabella2();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Metodo che viene chiamato all'evento della selezione della tabella dei
     * formati.
     * A sua volta aggiorna le informaizoni su schermo del formato, prezzo e disp.
     */
    private void tableSelectionChanged2() {
        try {
            rs2.absolute(tabellaFormati.getSelectionModel().getMinSelectionIndex() + 1);
            mostraDati2();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Aggiunge il libro al carrello
     */
    private void aggiungiACarrello() {
        if (getQuantita() > disponibilita && disponibilita >= 0)
            JOptionPane.showMessageDialog(this, "Attenzione: Il numero inserito è maggiore degli articoli disponibili.\nVerrà comunque aggiunto al carrello.");
        setVisible(false);
        try {
            DBConnection.inserisciArticoloCarrello(idUtente, prodId, getQuantita());
            dispose();
        } catch (SQLException ex) {
            mostraErrore(ex);
            setVisible(true);
        }
    }
    
    /**
     * Questo metodo è necessario per evitare che l'utente immetta valori
     * errati nella quantità, come numeri negativi o lettere.
     * @return quantità scelta dell'oggetto
     */
    private int getQuantita() {
        int prezzo = 0;
        try {
            prezzo = Integer.parseInt(tQuantita.getText());
            if (prezzo < 0) {
                prezzo = 1;
                tQuantita.setText("1");
            }
        } catch (NumberFormatException ex) {
            System.out.println(tQuantita.getText()+" non è un valore accettabile");
            tQuantita.setText("1");
        }
        return prezzo;
    }
    
    /**
     * Override per far tornare visibile la finestra della scelta dei libri alla
     * chiusura di questa finestra.
     */
    @Override
    public void dispose() {
        super.dispose();
        padre.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tTitolo = new javax.swing.JLabel();
        tAutore = new javax.swing.JLabel();
        tVoto = new javax.swing.JLabel();
        tDisponibile = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        bCarrello = new javax.swing.JButton();
        tPrezzo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tQuantita = new javax.swing.JTextField();
        tEditore = new javax.swing.JLabel();
        tFormato = new javax.swing.JLabel();
        tCodice = new javax.swing.JLabel();
        tAnno = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaVenditori = new javax.swing.JTable();
        tCondizioni = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabellaFormati = new javax.swing.JTable();
        tGenere = new javax.swing.JLabel();
        tPagine = new javax.swing.JLabel();
        tPeso = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tDescrizione = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        listeDesideri = new javax.swing.JComboBox();
        bAggiungi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

        tTitolo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        tTitolo.setForeground(new java.awt.Color(153, 153, 0));
        tTitolo.setText("TITOLO");

        tAutore.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        tAutore.setText("Autore");

        tVoto.setText("Voto: 5/5");

        tDisponibile.setText("Disponibilità: Disponibile");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Dettagli:");

        bCarrello.setBackground(new java.awt.Color(240, 100, 0));
        bCarrello.setText("Aggiungi al carrello");
        bCarrello.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCarrelloActionPerformed(evt);
            }
        });

        tPrezzo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        tPrezzo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tPrezzo.setText("€10.00");
        tPrezzo.setToolTipText("");

        jLabel3.setText("Quantità:");

        tQuantita.setText("1");

        tEditore.setText("Editore:");

        tFormato.setText("Formato:");

        tCodice.setText("ISBN:");

        tAnno.setText("Anno:");

        tabellaVenditori.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabellaVenditori);

        tCondizioni.setText("Condizioni: Nuovo");

        tabellaFormati.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabellaFormati);

        tGenere.setText("Genere:");

        tPagine.setText("Pagine:");

        tPeso.setText("Peso:");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        tDescrizione.setColumns(20);
        tDescrizione.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tDescrizione.setLineWrap(true);
        tDescrizione.setRows(5);
        jScrollPane3.setViewportView(tDescrizione);

        jLabel2.setText("Descrizione:");

        jLabel4.setText("Aggiungi alla lista desideri...");

        listeDesideri.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        bAggiungi.setText("Aggiungi");
        bAggiungi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAggiungiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(tPagine, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tPeso, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(tEditore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(tFormato, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(tCodice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(tAnno, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tCondizioni, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                            .addComponent(tGenere, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(147, 147, 147)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tTitolo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tAutore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tVoto, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tDisponibile, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addGap(71, 71, 71)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bCarrello, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(tPrezzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(listeDesideri, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(bAggiungi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tTitolo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tAutore))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCarrello, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tVoto)
                    .addComponent(tDisponibile, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tQuantita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tEditore)
                    .addComponent(tCodice)
                    .addComponent(tCondizioni)
                    .addComponent(listeDesideri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tFormato)
                    .addComponent(tAnno)
                    .addComponent(tGenere)
                    .addComponent(bAggiungi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tPagine)
                    .addComponent(tPeso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCarrelloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCarrelloActionPerformed
        aggiungiACarrello();
    }//GEN-LAST:event_bCarrelloActionPerformed

    private void bAggiungiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAggiungiActionPerformed
        aggiungiListaDesideri();
    }//GEN-LAST:event_bAggiungiActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAggiungi;
    private javax.swing.JButton bCarrello;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox listeDesideri;
    private javax.swing.JLabel tAnno;
    private javax.swing.JLabel tAutore;
    private javax.swing.JLabel tCodice;
    private javax.swing.JLabel tCondizioni;
    private javax.swing.JTextArea tDescrizione;
    private javax.swing.JLabel tDisponibile;
    private javax.swing.JLabel tEditore;
    private javax.swing.JLabel tFormato;
    private javax.swing.JLabel tGenere;
    private javax.swing.JLabel tPagine;
    private javax.swing.JLabel tPeso;
    private javax.swing.JLabel tPrezzo;
    private javax.swing.JTextField tQuantita;
    private javax.swing.JLabel tTitolo;
    private javax.swing.JLabel tVoto;
    private javax.swing.JTable tabellaFormati;
    private javax.swing.JTable tabellaVenditori;
    // End of variables declaration//GEN-END:variables
}
