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
   
   public static ResultSet visualizzaListeDesideri(String idUtente) throws SQLException {
       /*Visualizza le liste desideri di un utente, dato il suo ID
       **QUERY DI BASE= SELECT NOMELISTA, PROD_ID, LIBRO_NOME FROM COMPLISTA_DESIDERI INNER JOIN LIBRI ON COMPLISTA_DESIDERI.PROD_ID=LIBRI.PROD_ID WHERE UTENTE_ID=?;
       **NOTA = Se possibile, visualizzare anche il prezzo di ogni articolo aggiunto
       
       */
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT NOMELISTA, PROD_ID, LIBRO_NOME FROM COMPLISTA_DESIDERI INNER JOIN LIBRI ON COMPLISTA_DESIDERI.PROD_ID=LIBRI.PROD_ID WHERE UTENTE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idUtente));
       
       return pstmt.executeQuery();
   }
      
   public static ResultSet visualizzaCarrello(String idUtente) throws SQLException {
       //Visualizza l'attuale carrello dell'utente dato il suo ID
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, FORMATO_NOME, MAGAZZINO_LIBRI.PREZZOVENDITA, VENDITORE_NOME, QUANTITÀ FROM COMPARTICOLI NATURAL JOIN LIBRI NATURAL JOIN MAGAZZINO_LIBRI NATURAL JOIN IMPOSTAZIONI NATURAL JOIN VENDITORI WHERE UTENTE_ID=? AND ORDINE_ID=NULL;",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY); //INSERIRE QUERY
       pstmt.setInt(1, Integer.parseInt(idUtente));
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaOrdini(String idUtente) throws SQLException {
       //Visualizza gli ordini effettuati dall'utente dato il suo ID
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY); //INSERIRE QUERY
       pstmt.setInt(1, Integer.parseInt(idUtente));
       
       return pstmt.executeQuery();
   }
   
   public static void creaOrdine (String idUtente, int costospedin, String scontocomplin, String idContatto) throws SQLException {
       //NOTA = sistemare i "parse" ove necessario
       //NOTA2 = gestire i pezzi disponibile. Checkare e sottrarre solo se il formato ID è 2001 o 2002.
       
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       String idOrder; //id dell'ordine da usare per l'aggiunta in COMPARTICOLI e SPEDIZIONE
       int countCourier;
       
       pstmt = conn.prepareStatement("INSERT INTO ORDINI(UTENTE_ID, DATAORDINE, PREZZONETTO, COSTOSPED, SCONTOCOMPL) VALUES(?, SYSDATE, (SELECT SUM(PREZZOVENDITA) FROM COMPARTICOLI WHERE (?=UTENTE_ID AND ORDINE_ID=NULL)), ?, ?)");
       pstmt.setString(1, idUtente);
       pstmt.setString(2, idUtente);
       pstmt.setInt(3, costospedin);
       pstmt.setString(4, scontocomplin);
       
       pstmt.executeUpdate();
       pstmt.close();
       
       //Query per visualizzare l'ultimo record inserito
       PreparedStatement lastorder;
       lastorder = conn.prepareStatement("SELECT ORDINE_ID FROM UTENTI WHERE ROWNUM <=1 ORDER BY UTENTE_ID DESC;");
       rs = pstmt.executeQuery();
       rs.next();
       idOrder = rs.getString(1);
       lastorder.close();
       
       //Aggiornamento di COMPARTICOLI
       PreparedStatement updateCompArticoli;
       updateCompArticoli = conn.prepareStatement("UPDATE COMPARTICOLI SET ORDINE_ID=? WHERE (UTENTE_ID=? AND ORDINE_ID=NULL);");
       updateCompArticoli.setString(1, idOrder);
       updateCompArticoli.setString(2, idUtente);
       updateCompArticoli.executeUpdate();
       updateCompArticoli.close();
       
       //Calcolo di PREZZO TOTALE
       PreparedStatement totalPrice;
       totalPrice = conn.prepareStatement("UPDATE ORDINI SET PREZZOTOTALE=(PREZZONETTO+COSTOSPED)-SCONTOCOMPL WHERE (ORDINE_ID=?);");
       totalPrice.setString(1, idOrder);
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
       insertDelivery.setString(2, idOrder);
       insertDelivery.setString(3, idContatto);
       if (costospedin == 8)
            insertDelivery.setString(4, "SYSDATE + 1");
       else if (costospedin == 4)
            insertDelivery.setString(4, "SYSDATE + 3");
       else
            insertDelivery.setString(4, "SYSDATE + 5");
       
       insertDelivery.close();
       
       //UPDATE valore PREZZOVENDITA in comparticoli
       //UPDATE PEZZIDISPONIBILI per i libri del nuovo ordine
       
   }
   
   public static void creaRecensione(String idUtente, String commento, boolean libroRec, String target, String voto) throws SQLException {
       /*Si crea la recensione postata da un utente con un certo ID su un certo libro/venditore
       **Esempio query :INSERT INTO "GRUPPO26"."RECENSIONI" (UTENTE_ID, COMMENTO, PROD_ID, VOTO)
       **               VALUES ('423572', 'Un libro meraviglioso, con forti spunti di riflessione. Da consigliare a tutti', '1', ‘5’)
       **Se la variabile booleana libroRec è TRUE, inserisce una recensione di un prodotto, altrimenti di un venditore.
       */
       PreparedStatement pstmt;
       
       if ( libroRec )
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, PROD_ID, VOTO) VALUES ('?', '?', '?', '?')");
       else
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, VENDITORE_ID, VOTO) VALUES ('?', '?', '?', '?')");
       
       pstmt.setString(1, idUtente);
       pstmt.setString(2, commento);
       pstmt.setString(3, target);
       pstmt.setString(4, voto);
       
       pstmt.executeUpdate();
   }
   
   //Creazione di un nuovo utente
   public static void creaUtente(String nome, String cognome, String mail, String password, String cellulare) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
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
       */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idAutore));
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Soffocare
       */
    }
   
   public static ResultSet cercaLibriAutore(String idAutore, String chiave) throws SQLException {
       PreparedStatement pstmt;
       String query = "SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ? AND LIBRI.LIBRO_NOME LIKE ?";
       pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, idAutore);
       pstmt.setString(2, chiave);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Fight Club
       */
   }
   
   public static ResultSet visualizzaLibriEditore(String idEditore) throws SQLException {
        /*Metodo che gestisce l'Editore e visualizza 
        **tutti i libri di quell'editore
        */
       
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
   
   public static ResultSet visualizzaMagazzino(String idVenditore) throws SQLException   {
       //Vista sull'inventario di un magazzino di un venditore
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_MAGAZZINO WHERE MAGAZZINO_LIBRI.VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idVenditore));
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME    AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            VENDITORE_ID
                          Fight Club	Chuck       Palahniuk       Mondadori	9788804508359	6318
       */
   }
   
   public static ResultSet visualizzaListinoLibri() throws SQLException {
        //Lista completa di tutti i libri che i venditori hanno a disposizione
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM LIBRI",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       return pstmt.executeQuery();
       
   }
   
   public static ResultSet visualizzaListinoLibri(String query) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM LIBRI WHERE LIBRI.LIBRO_NOME LIKE ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       pstmt.setString(1,query);
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaInfoLibro (String isbn) throws SQLException {
       //A differenza degli altri metodi, invece di stampare i risultati in una tabella, li stampa in una finestra
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_INFOLIBRO WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(isbn));
       
       return pstmt.executeQuery();
       /*
       DA SISTEMARE
       */
   }
   
   public static ResultSet visualizzaLibriVenditore (String venditoreID) throws SQLException {
       //Quali libri vende quel venditore
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA FROM MAGAZZINO_LIBRI NATURAL JOIN LIBRI NATURAL JOIN VENDITORI NATURAL JOIN IMPOSTAZIONI WHERE VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       pstmt.setInt(1, Integer.parseInt(venditoreID));
       
       return pstmt.executeQuery();
       
       /*RISULTATO QUERY: LIBRO_NOME            FORMATO_NOME            TIPOCONDIZIONE  PEZZIDISPONIBILI    PREZZOVENDITA
                          I racconti di Nené	Copertina Flessibile	Nuovo           5                   5,99
       
       */
   }
   
   public static ResultSet visualizzaVenditoriLibro(String isbn) throws SQLException   {
       
       //In quali magazzini c'è quel libro
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VENDITORE_NOME, PREZZOVENDITA_MINIMO FROM VIEW_LIBRIDISPONIBILI NATURAL JOIN VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(isbn));
       
       return pstmt.executeQuery();
                
       
       //In questo campo viene selezionato il venditore dove reperire il prodotto, che viene aggiunto nel carrello con un bottone
       /*RISULTATO QUERY: VENDITORE_NOME    PREZZOVENDITA_MINIM
                          C.U.S.            4,99
       
       */
   }
   
   public static ResultSet visualizzaFormatoLibroVenditore(String isbn, String venditoreID) throws SQLException   {
       
       //Compaiono i formati del libro disponibili di quel venditore
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT FORMATO_NOME, PREZZOVENDITA, TIPOCONDIZIONE FROM MAGAZZINO_LIBRI NATURAL JOIN VENDITORI NATURAL JOIN IMPOSTAZIONI WHERE ISBN = ? AND VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(isbn));
       pstmt.setInt(2, Integer.parseInt(venditoreID));
       
       return pstmt.executeQuery();
                
       
       //In questo campo verrà selezionato il prodotto specifico che verrà aggiunto nel carrello
       /*RISULTATO QUERY: FORMATO_NOME           PREZZOVENDITA   TIPOCONDIZIONE
                          Copertina Rigida	 7,65            Nuovo
       
       */
   }
}   
