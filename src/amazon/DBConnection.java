/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.*;
/**
 *
 * @author Francesco
 */
public class DBConnection {
    private static OracleDataSource ods;
    public static Connection conn = null;
    public static String tempUser, tempPass, tempHost, tempPort, tempSchema;
    /**
    * Indica che il frame si trova nello stato di inserimento di un nuovo record
    * o dei parametri di ricerca.
    */
   final static public int APPEND_QUERY = 1;
   /**
    * Indica che il frame si trova nello stato di navigazione (ricerca
    * gi&agrave; effettuata).
    */
   final static public int BROWSE = 2;
   /**
    * Indica che il frame si trova nello stato di modifica dei dati.
    */
   final static public int UPDATE = 3;
   /**
    * indica che l'eccezione &egrave; stata sollevata eseguendo un comando
    * <i>SELECT</i>.
    */
   final static public int CONTESTO_ESEGUI_QUERY = 1;
   
   private static Hashtable<String, LinkedList> tabelle;
    
    /**
     * 
     * @throws SQLException 
     * @param usernm nome utente per il collegamento
     * @param passwd password da immettere per il collegamento
     * Avvia la connessione con il server
     */
    public static void StartConnection() throws SQLException {
        ods = new OracleDataSource();
        ods.setDriverType("thin");
        ods.setServerName(tempHost);
        ods.setPortNumber(Integer.parseInt(tempPort));
        ods.setUser(tempUser);
        ods.setPassword(tempPass);
        ods.setDatabaseName("xe");
        //ods.setURL("jdbc:oracle:thin:@//"+domain+":"+port+"/xe");//143.225.117.238:1521/xe");
        //Stampa Versione Driver
        conn = ods.getConnection();
        
        createTables();
    }
    
    private static void createTables() {
        ListaTipi tipi = new ListaTipi();
        tabelle = tipi.getTipi();
    }
    
    
    
    
    public static void reConnect() throws SQLException {
        if (conn != null){
            StartConnection();
        }
    }
    
    /**
     * Chiude la connessione con il server
     * @throws SQLException 
     */
    public static void CloseConnection() throws SQLException {
        if (conn != null) conn.close();
    }
    
    public static boolean connected() {
        return conn != null;
    }
    
    /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object getValue(String query) {
      Object obj;
      Statement st;
      ResultSet rs;
      obj = null;
      try {
         st = conn.createStatement();
         rs = st.executeQuery(query);
         rs.next();
         obj = rs.getObject(1);
      } catch (SQLException e) {  //nessuna azione
      }
      return obj;
   }

   /**
    * Effettua una query e restituisce il primo valore.
    * 
    * @param query String contenente l'interrogazione con segnaposto
    * @param codice int per rimpiazzare il segnaposto
    * @return oggetto contenente la prima colonna della prima riga del risultato
    */
   static public Object getValue(String query, int codice) {
      Object ret;
      PreparedStatement st;
      ResultSet rs;
      ret = null;
      System.out.println(query + codice); //debug
      try {
         st = conn.prepareStatement(query);
         st.setInt(1, codice);
         rs = st.executeQuery();
         rs.next();
         ret = rs.getObject(1);
      } catch (SQLException e) {
      }
      return ret;
   }
   
   /**
    * Restituisce un ResultSet di una query specificata in imput da stringa.
    * @param query
    * @return
    * @throws SQLException 
    */
   public static ResultSet eseguiQuery(String query) throws SQLException
   {
       Statement pstmt;
       pstmt = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

       ResultSet rs = pstmt.executeQuery(query);
       return rs;
   }
   
   public static void visualizzaListeDesideri(String idUtente) {
       /*Visualizza le liste desideri di un utente, dato il suo ID
       **QUERY DI BASE= SELECT NOMELISTA, PROD_ID, LIBRO_NOME FROM COMPLISTA_DESIDERI INNER JOIN LIBRI ON COMPLISTA_DESIDERI.PROD_ID=LIBRI.PROD_ID WHERE UTENTE_ID=?;
       **NOTA = Se possibile, visualizzare anche il prezzo di ogni articolo aggiunto
       */
   }
      
   public static void visualizzaCarrello(String idUtente) {
       //Visualizza l'attuale carrello dell'utente dato il suo ID
   }
   
   public static void visualizzaOrdini(String idUtente) {
       //Visualizza gli ordini effettuati dall'utente dato il suo ID
   }
   
   public static void creaOrdine (int idUtente, int costospedin, int scontocomplin, int idContatto) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       int idOrder; //id dell'ordine da usare per l'aggiunta in COMPARTICOLI e SPEDIZIONE
       int countCourier;
       
       pstmt = conn.prepareStatement("INSERT INTO ORDINI(UTENTE_ID, DATAORDINE, PREZZONETTO, COSTOSPED, SCONTOCOMPL) VALUES(?, SYSDATE, (SELECT SUM(PREZZOVENDITA) FROM COMPARTICOLI WHERE (?=UTENTE_ID AND ORDINE_ID=NULL)), ?, ?)");
       pstmt.setInt(1, idUtente);
       pstmt.setInt(2, idUtente);
       pstmt.setInt(3, costospedin);
       pstmt.setInt(4, scontocomplin);
       
       pstmt.executeUpdate();
       pstmt.close();
       
       //Query per visualizzare l'ultimo record inserito
       PreparedStatement lastorder;
       lastorder = conn.prepareStatement("SELECT ORDINE_ID FROM UTENTI WHERE ROWNUM <=1 ORDER BY UTENTE_ID DESC;");
       rs = pstmt.executeQuery();
       rs.next();
       idOrder = rs.getInt(1);
       lastorder.close();
       
       //Aggiornamento di COMPARTICOLI
       PreparedStatement updateCompArticoli;
       updateCompArticoli = conn.prepareStatement("UPDATE COMPARTICOLI SET ORDINE_ID=? WHERE (UTENTE_ID=? AND ORDINE_ID=NULL);");
       updateCompArticoli.setInt(1, idOrder);
       updateCompArticoli.setInt(2, idUtente);
       updateCompArticoli.executeUpdate();
       updateCompArticoli.close();
       
       //Calcolo di PREZZO TOTALE
       PreparedStatement totalPrice;
       totalPrice = conn.prepareStatement("UPDATE ORDINI SET PREZZOTOTALE=(PREZZONETTO+COSTOSPED)-SCONTOCOMPL WHERE (ORDINE_ID=?);");
       totalPrice.setInt(1, idOrder);
       totalPrice.executeUpdate();
       totalPrice.close();
       
       //Immissione corriere casuale (provare ad usare un solo tipo Statement)
       PreparedStatement countDelivery; //conta quanti corrieri hanno quel tipo di spedizione
       countDelivery = conn.prepareStatement("SELECT COUNT(*) AS NUMCORRIERI FROM MOD_SPEDIZIONE WHERE COSTOSPED=?;");
       countDelivery.setInt(1, costospedin);
       ResultSet numDelivery = countDelivery.executeQuery();
       numDelivery.next();
       countCourier = numDelivery.getInt(1);
       countDelivery.close();
       
       PreparedStatement showDelivery; //sceglie il corriere da uno casuale;
       ResultSet choiceDelivery;
       showDelivery = conn.prepareStatement("SELECT CORRIERE_ID FROM MOD_SPEDIZIONE WHERE COSTOSPED=?");
       showDelivery.setInt(1, costospedin);
       choiceDelivery = showDelivery.executeQuery();
       int randCourier = (int) ((Math.random() * countCourier) + 1);
       int i = 0; //indice contatore del while
       int idCourier = 0;
       
       while (choiceDelivery.next() && i <= randCourier) {
           idCourier = choiceDelivery.getInt(1);
           i++;
       }
       showDelivery.close();
       
       //Inserimento spedizioni
       PreparedStatement insertDelivery;
       insertDelivery = conn.prepareStatement("INSERT INTO SPEDIZIONI(CORRIERE_ID, ORDINE_ID, CONTACT_ID, DATACONSEGNA) VALUES (?, ?, ?, ?);");
       insertDelivery.setInt(1, idCourier);
       insertDelivery.setInt(2, idOrder);
       insertDelivery.setInt(3, idContatto);
       if (costospedin == 8)
            insertDelivery.setString(4, "SYSDATE + 1");
       else if (costospedin == 4)
            insertDelivery.setString(4, "SYSDATE + 3");
       else
            insertDelivery.setString(4, "SYSDATE + 5");
       
       insertDelivery.close();
       
   }
   
   public static void creaRecensione() {
       //Si crea la recensione postata da un utente con un certo ID su un certo libro/venditore 
   }
   
   //Creazione di un nuovo utente
   public static void creaUtente(String nome, String cognome, String mail, String password, String cellulare) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       pstmt = conn.prepareStatement("INSERT INTO UTENTI(NOME, COGNOME, EMAIL, PSW, NUMCELLULARE) VALUES(?, ?, ?, ?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setString(5, cellulare);
       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaUtente(String id, String nome, String cognome, String mail, String password, String cellulare) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE UTENTI SET NOME = ?, COGNOME = ?, EMAIL = ?, PSW = ?, NUMCELLULARE = ? WHERE UTENTE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setString(5, cellulare);
       pstmt.setString(6, id);
       
       pstmt.executeUpdate();
   }
   
   public static void eliminaRecord(String id, String tabella, String nomeColonna) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       pstmt = conn.prepareStatement("DELETE FROM " + tabella + " WHERE " + nomeColonna + " = ?");
       pstmt.setString(1, id);
       pstmt.executeUpdate();
   }
   
   public static void creaAutore(String nome, String cognome) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       pstmt = conn.prepareStatement("INSERT INTO AUTORI(AUT_NOME, AUT_COGNOME) VALUES(?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaAutore(String id, String nome, String cognome) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE AUTORI SET AUT_NOME = ?, AUT_COGNOME = ? WHERE AUTORE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, id);
       
       pstmt.executeUpdate();
   }
   
   public static void creaLibro(String nomeLibro, String nEdizione, String isbn, String descrizione, String genere, String nPagine, String pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       //Quando si crea un libro bisogna aggiungere: COD_AUTORE, EDITORE, LINGUA, PREZZO
       
       pstmt = conn.prepareStatement("INSERT INTO LIBRI(LIBRO_NOME, EDIZIONE_N, ISBN, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, DATAUSCITA) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
       pstmt.setString(1, nomeLibro);
       pstmt.setString(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setString(6, nPagine);
       pstmt.setString(7, pesoSped);
       pstmt.setString(8, dataUscita);
       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaLibro(String id, String nomeLibro, String nEdizione, String isbn, String descrizione, String genere, String nPagine, String pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       pstmt = conn.prepareStatement("UPDATE LIBRI SET LIBRO_NOME = ?, EDIZIONE_N = ?, ISBN = ?, DESCRIZIONE = ?, GENERE = ?, PAGINE_N = ?, PESOSPED = ?, DATAUSCITA = ? WHERE PROD_ID = ?");
       pstmt.setString(1, nomeLibro);
       pstmt.setString(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setString(6, nPagine);
       pstmt.setString(7, pesoSped);
       pstmt.setString(8, dataUscita);
       pstmt.setString(9, id);
       
       pstmt.executeUpdate();
   }
   
   public static void creaEditore(String nomeEditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       pstmt = conn.prepareStatement("INSERT INTO EDITORI(EDI_NOME) VALUES(?)");
       pstmt.setString(1, nomeEditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaEditore(String idEditore, String nomeEditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE EDITORI SET EDI_NOME = ?, WHERE EDI_ID = ?");
       pstmt.setString(1, nomeEditore);
       pstmt.setString(2, idEditore);
       
       pstmt.executeUpdate();
   }
   
      public static void creaVenditore(String nomeVenditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       
       pstmt = conn.prepareStatement("INSERT INTO VENDITORI(VENDITORE_NOME) VALUES(?)");
       pstmt.setString(1, nomeVenditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaVenditore(String idVenditore, String nomeVenditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE VENDITORI SET VENDITORE_NOME = ?, WHERE VENDITORE_ID = ?");
       pstmt.setString(1, nomeVenditore);
       pstmt.setString(2, idVenditore);
       
       pstmt.executeUpdate();
   }
   
   public static ResultSet visualizzaLibriAutore(String idAutore) throws SQLException {
       /*Metodo che gestisce l'Autore e visualizza 
       **tutti i libri di quell'autore
        
       QUERY = SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB INNER JOIN LIBRI ON AUTORI_LIB.ISBN=LIBRI.ISBN WHERE AUTORE_ID=?;*/
       
       PreparedStatement pstmt;
       
       //pstmt = conn.prepareStatement("SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB INNER JOIN LIBRI ON AUTORI_LIB.ISBN=LIBRI.ISBN WHERE AUTORE_ID = ?");
       pstmt = conn.prepareStatement("SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       //pstmt.setString(1, idAutore);
       pstmt.setInt(1, Integer.parseInt(idAutore));
       
       return pstmt.executeQuery();
    }
   
   public static ResultSet cercaLibriAutore(String idAutore, String chiave) throws SQLException {
       PreparedStatement pstmt;
       String query = "SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ? AND LIBRI.LIBRO_NOME LIKE ?";
       pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, idAutore);
       pstmt.setString(2, chiave);
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaLibriEditore(String idEditore) throws SQLException {
        /*Metodo che gestisce l'Editore e visualizza 
        **tutti i libri di quell'editore
       
       QUERY SELECT LIBRO_NOME FROM LIBRI INNER JOIN EDITORI_LIB ON EDITORI_LIB.ISBN=LIBRI.ISBN WHERE EDI_ID=?; = */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI, EDITORI_LIB WHERE EDITORI_LIB.ISBN = LIBRI.ISBN AND EDI_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, idEditore);
       
       return pstmt.executeQuery();
    }
   
   public static ResultSet cercaLibroEditore(String idEditore, String query) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI, EDITORI_LIB WHERE EDITORI_LIB.ISBN = LIBRI.ISBN AND EDI_ID = ? AND LIBRO_NOME LIKE ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idEditore));
       pstmt.setString(2,query);
       
       return pstmt.executeQuery();
   }
   
   public void visualizzaInfoLibro () {
       /*A differenza degli altri metodi, invece di stampare i risultati in una tabella, li stampa in una finestra*/
   }
}

   
