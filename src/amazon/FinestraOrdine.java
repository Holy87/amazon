/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.exceptions.CodeAlreadyUsedException;
import amazon.exceptions.CodeNotValidException;
import amazon.exceptions.TooMuchDealsException;
import amazon.modelliTabelle.DBTableModel;
import amazon.modelliTabelle.ScontiModel;
import amazon.utility.BoxUtility;
import amazon.utility.Contatto;
import amazon.utility.ModPagamento;
import amazon.utility.Scontotemp;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author Francesco
 */
public class FinestraOrdine extends javax.swing.JDialog {

    /**
     * 
     * @param parent
     * @param modal true per bloccare la finestra precedente
     * @param idUtente 
     */
    public FinestraOrdine(java.awt.Frame parent, boolean modal, int idUtente) {
        super(parent, modal);
        this.idUtente = idUtente;
        initComponents();
        impostaTabella();
        impostaTabellaSconti();
        impostaIndirizzi();
        impostaMetodiPagamento();
        aggiornaTotale();
        aggiornaPagamentoSelezionato();
        aggiornaIndirizzoSelezionato();
    }
    
    private ResultSet rsArticoli;
    private DBTableModel modelloTabellaArticoli;
    private ScontiModel modelloTabellaSconti;
    
    private int cursoreArticoli = 1;
    private int cursoreSconti = 1;
    private int indirizzoSelezionato, pagamentoSelezionato;
    private LinkedList<Scontotemp> sconti = new LinkedList();
    
    private final int idUtente;
    private int prodID, prodNum;
    private double totale = 0;
    static double scontoCompl = 0;
    
    private final int SPEDECO = 1;
    private final int SPEDMID = 2;
    private final int SPEDRAP = 3;
    
    private void impostaMetodiPagamento() {
        try {
            BoxUtility.impostaModPagamento(cPagamento, idUtente);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void impostaIndirizzi() {
        try {
            BoxUtility.impostaPerIndirizzi(cSpedizione, idUtente);
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    /**
     * Restituisce l'ID del metodo di pagamento selezionato
     * @return 
     */
    private int metodoPagamentoSelezionato() {
        return pagamentoSelezionato;
    }
    
    private int indirizzoSelezionato() {
        return indirizzoSelezionato;
    }
    
    private void inserisciCodice() {
        String codice = codiceSconto.getText().toUpperCase();
        codiceSconto.setText("");
        try {
            controllaSeGiaInserito(codice);
            double sconto = DBConnection.verificaSconto(codice);
            scontoCompl = sommaSconti(sconto);
            sconti.add(new Scontotemp(codice, sconto));
            modelloTabellaSconti.setSconti(sconti);
            aggiornaTotale();
        } catch (SQLException ex) {
            mostraErrore(ex);
        } catch (CodeAlreadyUsedException ex) {
            JOptionPane.showMessageDialog(this, "Questo codice è già stato inserito nel carrello!", null, ERROR_MESSAGE);
        } catch (CodeNotValidException ex) {
            JOptionPane.showMessageDialog(this, "Il codice sconto inserito è già stato usato o non è valido.", null, ERROR_MESSAGE);
        } catch (TooMuchDealsException ex) {
            JOptionPane.showMessageDialog(this, "Il totale dei codici sconto supera il prezzo degli articoli.", null, ERROR_MESSAGE);
        }
    }
    
    private double sommaSconti() throws TooMuchDealsException {
        double scontoTotale = 0.0;
        for (Scontotemp sconto : sconti) {
            scontoTotale += sconto.getSconto();
        }
        if (scontoTotale > netto())
            throw new TooMuchDealsException();
        return scontoTotale;
    }
    
    private double sommaSconti(double nuovoSconto) throws TooMuchDealsException {
        double scontoTotale = sommaSconti();
        if (scontoTotale + nuovoSconto > netto())
            throw new TooMuchDealsException();
        return scontoTotale;
    }
    
    private void controllaSeGiaInserito(String codice) throws CodeAlreadyUsedException {
        for (Scontotemp sconto : sconti) {
            if (sconto.getcodPromo().equals(codice)) {
                throw new CodeAlreadyUsedException();
            }
        }
    }
    
    @SuppressWarnings("Convert2Lambda")
    private void impostaTabella() {
        modelloTabellaArticoli = new DBTableModel(rsArticoli);
        tabellaArticoli.setModel(modelloTabellaArticoli); //metto il modellotabella nel
        tabellaArticoli.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaArticoli.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        } 
                
        );
        aggiornaTabella();
        
    }
    
    @SuppressWarnings("Convert2Lambda")
    private void impostaTabellaSconti(){
        modelloTabellaSconti = new ScontiModel(sconti);
        tabellaSconti.setModel(modelloTabellaSconti);
        tabellaSconti.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //non possono essere selezionati record multipli
        tabellaSconti.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            //implemento un evento che chiama tableSelectionChanged quando cambia la selezione della tabella
            @Override
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChangedSconti();
            }
        }
                
        );
    }
    
    
    /**
     * Aggiorna i dati della tabella con tutti i dati del database.
     */
    public void aggiornaTabella()
    {
        try {
            rsArticoli = DBConnection.visualizzaCarrello(idUtente);
            modelloTabellaArticoli.setRS(rsArticoli);
            rsArticoli.absolute(cursoreArticoli);
            aggiornaSelezione();
            tabellaArticoli.getColumnModel().getColumn(0).setMinWidth(0);
            tabellaArticoli.getColumnModel().getColumn(0).setMaxWidth(0);
            tabellaArticoli.getColumnModel().getColumn(1).setMinWidth(0);
            tabellaArticoli.getColumnModel().getColumn(1).setMaxWidth(0);
            tabellaArticoli.getColumnModel().getColumn(8).setMinWidth(0);
            tabellaArticoli.getColumnModel().getColumn(8).setMaxWidth(0);
            controlloAbilitazioneAcquisto();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
        aggiornaTotale();
        
    }
    
    /**
     * Aggiorna il testo del totale
     */
    private void aggiornaTotale() {
        try {
            totale = 0;
            totale += netto();
            tNetto.setText(netto()+"€");
            totale += costoSpedizione();
            tSpedizione.setText(costoSpedizione()+"€");
            totale -= sommaSconti();
            BigDecimal bd = new BigDecimal(totale);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            totale=bd.doubleValue();
            tSconto.setText("-"+sommaSconti()+"€");
            tTotale.setText(totale + "€");   
        } catch (TooMuchDealsException ex) {
            System.out.println(ex.toString());
        }
    }
    
    private double netto() {
        return modelloTabellaArticoli.getColumnSum(5, 7);
    }
    
    private double costoSpedizione() {
        switch (getSelectedSpedition()) {
            case SPEDECO: return 0;
            case SPEDMID: return 4.0;
            case SPEDRAP: return 8.0;
            default: return 0;
        }
    }
    
    private int getSelectedSpedition() {
        if (rb1.isSelected())
            return SPEDECO;
        else if (rb2.isSelected())
            return SPEDMID;
        else
            return SPEDRAP;
    }
    
    /**
     * Mostra infine i dati sulla tabella dopo un aggiornamento
     */
    private void aggiornaSelezione() {
      try {
          cursoreArticoli = rsArticoli.getRow();
          //tabellaArticoli.getSelectionModel().setSelectionInterval(cursoreArticoli - 1,cursoreArticoli - 1);
          tabellaArticoli.setRowSelectionInterval(cursoreArticoli - 1, cursoreArticoli - 1);
          prodID = rsArticoli.getInt(9);
          prodNum = rsArticoli.getInt(8);
          abilitazioneComandi(true);
          //if (SwingUtilities.isRightMouseButton(null))
      } catch (SQLException ex) {
          mostraErrore(ex);
      } catch (java.lang.IllegalArgumentException ex) {
          abilitazioneComandi(false);
      }
    }
    
    private void abilitazioneComandi(boolean abi) {
        jButton1.setEnabled(abi);
        jButton3.setEnabled(abi);
    }
    
    private void controlloAbilitazioneAcquisto() {
        if (cPagamento.getSelectedItem() == null || cSpedizione.getSelectedItem() == null || modelloTabellaArticoli.getRowCount() == 0)
            jButton2.setEnabled(false);
        else
            jButton2.setEnabled(true);
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
            rsArticoli.absolute(tabellaArticoli.getSelectionModel().getMinSelectionIndex() + 1);
            aggiornaSelezione();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void tableSelectionChangedSconti() {
        cursoreSconti = tabellaArticoli.getSelectedRow();
        tabellaArticoli.getSelectionModel().setSelectionInterval(cursoreSconti - 1,cursoreSconti - 1);
        tabellaArticoli.setRowSelectionInterval(cursoreSconti - 1, cursoreSconti - 1);
    }
    
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void aggiornaIndirizzoSelezionato() {
        try {
            Contatto indirizzo = (Contatto)cSpedizione.getSelectedItem();
            indirizzoSelezionato = indirizzo.getId();
        } catch (Exception ex) {
            indirizzoSelezionato = 0;
        }
        controlloAbilitazioneAcquisto();
    }
    
    private void completaAcquisto() throws TooMuchDealsException {
        try {
            System.out.println(idUtente);
            System.out.println(costoSpedizione());
            System.out.println(metodoPagamentoSelezionato());
            System.out.println(indirizzoSelezionato());
            DBConnection.creaOrdine(idUtente, (int)costoSpedizione(), sommaSconti(), metodoPagamentoSelezionato(), indirizzoSelezionato(), sconti);
            setVisible(false);
            JOptionPane.showMessageDialog(this, "L'ordine è stato effettuato.");
            dispose();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void aggiornaPagamentoSelezionato() {
        try {
            ModPagamento pagamento = (ModPagamento) cPagamento.getSelectedItem();
            pagamentoSelezionato = pagamento.getId();
            tIntestatario.setText(pagamento.getNomeIntestatario() + " " + pagamento.getCognomeIntestatario());
            tScadenzaCarta.setText(pagamento.getData());
            
        } catch (NullPointerException ex) {}
        controlloAbilitazioneAcquisto();
    }
    
    private void clickDestro(java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }
    
    private void rimuoviArticolo() {
        try {
            System.out.println(prodID);
            DBConnection.eliminaArticoloCarrello(idUtente, prodID);
            aggiornaTabella();
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }
    
    private void modificaQuantita() {
        try {
            String risposta = JOptionPane.showInputDialog(this, "Scegli la quantità", prodNum);
            if (risposta != null) {
                try {
                    int quantita = Integer.parseInt(risposta);
                    if (quantita <= 0)
                        DBConnection.eliminaArticoloCarrello(idUtente, prodID);
                    else
                        DBConnection.modificaQuantitaArticolo(idUtente, prodID, quantita);
                    aggiornaTabella();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Il valore inserito non è corretto");
                }
            }
        } catch (SQLException ex) {
            mostraErrore(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        popupMenu = new javax.swing.JPopupMenu();
        elimina = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabellaArticoli = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cSpedizione = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        rb1 = new javax.swing.JRadioButton();
        rb2 = new javax.swing.JRadioButton();
        rb3 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        codiceSconto = new javax.swing.JTextField();
        pulsanteCodiceSconto = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabellaSconti = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        tNetto = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tSpedizione = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tSconto = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tTotale = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cPagamento = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        tIntestatario = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tScadenzaCarta = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable3);

        elimina.setText("Rimuovi");
        popupMenu.add(elimina);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabellaArticoli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tabellaArticoli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabellaArticoliMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaArticoliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabellaArticoli);

        jLabel1.setText("Indirizzo di spedizione:");

        cSpedizione.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cSpedizione.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cSpedizioneItemStateChanged(evt);
            }
        });
        cSpedizione.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cSpedizioneActionPerformed(evt);
            }
        });

        jLabel2.setText("Tipo di spedizione:");

        buttonGroup1.add(rb1);
        rb1.setSelected(true);
        rb1.setText("3-5 Giorni lavorativi: GRATIS");
        rb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb2);
        rb2.setText("2-3 Giorni lavorativi: €4,00");
        rb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb3);
        rb3.setText("1 Giorno: €8,00");
        rb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb3ActionPerformed(evt);
            }
        });

        jLabel3.setText("Inserisci codice promozionale se presente:");

        codiceSconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codiceScontoActionPerformed(evt);
            }
        });

        pulsanteCodiceSconto.setText("Aggiungi");
        pulsanteCodiceSconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pulsanteCodiceScontoActionPerformed(evt);
            }
        });

        tabellaSconti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabellaSconti.setColumnSelectionAllowed(true);
        tabellaSconti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabellaScontiMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabellaSconti);
        tabellaSconti.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Prezzo netto:");

        tNetto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tNetto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tNetto.setText("10€");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Costo spedizione:");

        tSpedizione.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tSpedizione.setText("0€");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Sconto:");

        tSconto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tSconto.setText("0€");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("TOTALE:");

        tTotale.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        tTotale.setForeground(new java.awt.Color(0, 0, 153));
        tTotale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tTotale.setText("10€");

        jButton2.setBackground(new java.awt.Color(0, 255, 204));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 153, 153));
        jButton2.setText("Completa l'acquisto");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel9.setText("Seleziona il metodo di pagamento:");

        cPagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cPagamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cPagamentoItemStateChanged(evt);
            }
        });
        cPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cPagamentoActionPerformed(evt);
            }
        });

        jLabel5.setText("Intestato a:");

        tIntestatario.setText(" ");

        jLabel7.setText("Scadenza:");

        tScadenzaCarta.setText(" ");

        jButton1.setText("Rimuovi dal carrello");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Modifica quantità");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addComponent(tScadenzaCarta, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tIntestatario, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cSpedizione, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(codiceSconto, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pulsanteCodiceSconto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rb1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rb2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rb3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tNetto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tSpedizione, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tSconto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tTotale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(2, 2, 2)
                        .addComponent(cSpedizione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rb3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codiceSconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pulsanteCodiceSconto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tNetto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(tSpedizione)
                            .addComponent(jLabel5)
                            .addComponent(tIntestatario))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(tSconto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(tTotale, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 4, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tScadenzaCarta))
                        .addGap(121, 121, 121))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb1ActionPerformed
        aggiornaTotale();
    }//GEN-LAST:event_rb1ActionPerformed

    private void tabellaScontiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaScontiMouseClicked
        // AGGIUNGERE L'EVENTO CON IL TASTO DESTRO DEL MOUSE
    }//GEN-LAST:event_tabellaScontiMouseClicked

    private void pulsanteCodiceScontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pulsanteCodiceScontoActionPerformed
        inserisciCodice();
    }//GEN-LAST:event_pulsanteCodiceScontoActionPerformed

    private void codiceScontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codiceScontoActionPerformed
        inserisciCodice();
    }//GEN-LAST:event_codiceScontoActionPerformed

    private void rb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb2ActionPerformed
        aggiornaTotale();
    }//GEN-LAST:event_rb2ActionPerformed

    private void rb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb3ActionPerformed
        aggiornaTotale();
    }//GEN-LAST:event_rb3ActionPerformed

    private void cSpedizioneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cSpedizioneActionPerformed
        aggiornaIndirizzoSelezionato();
    }//GEN-LAST:event_cSpedizioneActionPerformed

    private void cPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cPagamentoActionPerformed
        aggiornaPagamentoSelezionato();
    }//GEN-LAST:event_cPagamentoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            completaAcquisto();
        } catch (TooMuchDealsException ex) {
            Logger.getLogger(FinestraOrdine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tabellaArticoliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaArticoliMouseClicked

    }//GEN-LAST:event_tabellaArticoliMouseClicked

    private void tabellaArticoliMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabellaArticoliMouseReleased
        clickDestro(evt);
    }//GEN-LAST:event_tabellaArticoliMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        rimuoviArticolo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cPagamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cPagamentoItemStateChanged
        aggiornaPagamentoSelezionato();
    }//GEN-LAST:event_cPagamentoItemStateChanged

    private void cSpedizioneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cSpedizioneItemStateChanged
        aggiornaIndirizzoSelezionato();
    }//GEN-LAST:event_cSpedizioneItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        modificaQuantita();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cPagamento;
    private javax.swing.JComboBox cSpedizione;
    private javax.swing.JTextField codiceSconto;
    private javax.swing.JMenuItem elimina;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JButton pulsanteCodiceSconto;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JRadioButton rb3;
    private javax.swing.JLabel tIntestatario;
    private javax.swing.JLabel tNetto;
    private javax.swing.JLabel tScadenzaCarta;
    private javax.swing.JLabel tSconto;
    private javax.swing.JLabel tSpedizione;
    private javax.swing.JLabel tTotale;
    private javax.swing.JTable tabellaArticoli;
    private javax.swing.JTable tabellaSconti;
    // End of variables declaration//GEN-END:variables
}
