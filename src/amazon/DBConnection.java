/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        
        //createTables();
    }
    
    private static void createTables() {
        ListaTipi tipi = new ListaTipi();
        //tabelle = tipi.getTipi();
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
      
   public static ResultSet visualizzaCarrello(String idUtente) throws SQLException {
       //Visualizza l'attuale carrello dell'utente dato il suo ID
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VIEW_INFO.LIBRO_NOME, VIEW_INFO.FORMATO_NOME, VIEW_INFO.PREZZOVENDITA, VIEW_INFO.VENDITORE_NOME, QUANTITÀ FROM COMPARTICOLI JOIN VIEW_INFO ON VIEW_INFO.ISBN = COMPARTICOLI.ISBN AND VIEW_INFO.FORMATO_ID = COMPARTICOLI.FORMATO_ID AND VIEW_INFO.VENDITORE_ID = COMPARTICOLI.VENDITORE_ID WHERE UTENTE_ID=? AND ORDINE_ID=0;",
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
       **Esempio query :INSERT INTO "GRUPPO26"."RECENSIONI" (UTENTE_ID, COMMENTO, ISBN, VOTO)
       **               VALUES ('423572', 'Un libro meraviglioso, con forti spunti di riflessione. Da consigliare a tutti', '1', ‘5’)
       **Se la variabile booleana libroRec è TRUE, inserisce una recensione di un prodotto, altrimenti di un venditore.
       */
       PreparedStatement pstmt;
       
       if ( libroRec )
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, ISBN, VOTO) VALUES ('?', '?', '?', '?')");
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
       
       pstmt = conn.prepareStatement("UPDATE LIBRI SET LIBRO_NOME = ?, EDIZIONE_N = ?, ISBN = ?, DESCRIZIONE = ?, GENERE = ?, PAGINE_N = ?, PESOSPED = ?, DATAUSCITA = ? WHERE ISBN = ?");
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
   
   

   /*public static ResultSet visualizzaMagazzino(String idVenditore) throws SQLException   {
       //Vista sull'inventario di un magazzino di un venditore
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEWMAGAZZINO WHERE VENDITORE_ID = ?",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idVenditore));
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME    AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            VENDITORE_ID
                          Fight Club	Chuck       Palahniuk       Mondadori	9788804508359	6318
       
   }*/
   
   public static ResultSet visualizzaMagazzino (String venditoreID) throws SQLException {
       //Lista dei libri dettagliata che il venditore ha a disposizione
       
       //Esempio: VENDITORE_ID = 6317;
       /*RISULTATO QUERY:
                        LIBRO_NOME                      FORMATO_NOME            TIPOCONDIZIONE  PEZZIDISPONIBILI    PREZZOVENDITA
                        I racconti di Nené              Copertina Flessibile	Ricondizionato	2                   2,99
                        I racconti di Nené              Copertina Flessibile	Nuovo           10                  4,99
                        Avatar                          Copertina Flessibile	Nuovo           0                   8,99
                        Soffocare                       Copertina Flessibile	Usato           1                   5,99
                        Invito alla Biologia            Copertina Flessibile	Usato           1                   8,47
                        Hunger Games                    Copertina Flessibile	Nuovo           20                  12,99
                        La Danza delle Stelle           Copertina Rigida	Nuovo           10                  9,99
                        La Solitudine dei Numeri Primi	Copertina Rigida	Nuovo           10                  11,99
                        Fight Club                      Copertina Rigida	Nuovo           10	            7,4
       
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA FROM VIEW_INFO WHERE VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       pstmt.setInt(1, Integer.parseInt(venditoreID));
       
       return pstmt.executeQuery();
   }
   //serve per la ricerca su un nome
   public static ResultSet visualizzaMagazzino(String idVenditore, String query) throws SQLException   {
       //Vista sull'inventario di un magazzino di un venditore
       PreparedStatement pstmt;
       query.replace('%', ' ');
       pstmt = conn.prepareStatement("SELECT * FROM VIEWMAGAZZINO WHERE VENDITORE_ID = ? AND LIBRI_NOME LIKE %?%",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(idVenditore));
       pstmt.setString(2, query);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME    AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            VENDITORE_ID
                          Fight Club	Chuck       Palahniuk       Mondadori	9788804508359	6318
       */
   }
   
   public static ResultSet visualizzaListinoLibri() throws SQLException {
        //Lista completa di tutti i libri presenti nell'archivio completo (non nei magazzini dei venditori)
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN FROM LIBRI",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       return pstmt.executeQuery();
       
   }
   
   public static ResultSet visualizzaListinoLibri(String query) throws SQLException {
       //Con una stringa possiamo cercare il nome di un libro presente nell'archivio
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN FROM LIBRI WHERE LIBRI.LIBRO_NOME LIKE ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       pstmt.setString(1,query);
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaInfoLibro (String isbn) throws SQLException {
       //Compaiono le informazioni dettagliate dei libri presenti nell'archivio
       
        //Esempio: ISBN = 9788804632238;
       /*RISULTATO QUERY:
            LIBRO_NOME          AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            DESCRIZIONE                                                                                                                         GENERE          PAGINE_N    PESOSPED    DATAUSCITA      VOTOPROD_MEDIA
            Hunger Games        Suzanne     Collins         Mondadori	9788804632238	Quando Katniss urla "Mi offro volontaria, mi offro volontaria come tributo!" sa di aver appena firmato la sua condanna a morte.     Fantascienza    370         399         14-MAG-13       (null)
       */
       System.out.println(""+Long.parseLong(isbn));
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_INFOLIBRO WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        pstmt.setLong(1, Long.parseLong(isbn));
       
       return pstmt.executeQuery();
       
   }
   
   public static ResultSet visualizzaVenditoriLibro(String isbn) throws SQLException   {
       //In questo campo compaiono i venditori che hanno a disposizione il libro scelto, qualsiasi formato abbiano a disposizione
       
       //Esempio: ISBN = 9788804508359;
       /*RISULTATO QUERY: 
                        VENDITORE_NOME    PREZZOVENDITA_MINIM
                        C.U.S.            4,99
                        Libri.it          7,40
       
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VENDITORE_ID, VENDITORE_NOME, PREZZOVENDITA_MINIMO FROM VIEW_LIBRIDISPONIBILI NATURAL JOIN VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(isbn));
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaFormatoLibroVenditore(String isbn, String venditoreID) throws SQLException   {
       //In questo campo compaiono i formati che il venditore ha a disposizione per il libro in questione

       //Esempio: ISBN = 9788804508359 AND VENDITORE_ID = 6318;
       /*RISULTATO QUERY: FORMATO_NOME           PREZZOVENDITA   TIPOCONDIZIONE
                          Copertina Rigida	 7,65            Nuovo
                          Copertina Rigida	 4,99            Usato
                          Kindle        	 5,99            Nuovo
       */
       PreparedStatement pstmt;
       //ho inserito anche ISBN e VENDITORE_ID nei risultati perché c'è bisogno di loro quando seleziono il libro
       pstmt = conn.prepareStatement("SELECT ISBN, VENDITORE_ID, FORMATO_NOME, PREZZOVENDITA, TIPOCONDIZIONE FROM MAGAZZINO_LIBRI NATURAL JOIN VENDITORI NATURAL JOIN IMPOSTAZIONI WHERE ISBN = ? AND VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setLong(1, Long.parseLong(isbn));
       pstmt.setInt(2, Integer.parseInt(venditoreID));
       
       return pstmt.executeQuery();   
   }
   
   public static void inserisciArticoloCarrello(String utenteId, String isbn, String formatoId, String venditoreId, String tipoCondizione, String quantita) throws SQLException {
        //Viene effettuato l'inserimento nel carrello di un articolo
 
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPARTICOLI(UTENTE_ID, ISBN, Formato_ID, Venditore_ID, Tipocondizione, Quantità) VALUES(?,?,?,?,?,?)",
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, Integer.parseInt(utenteId));
        pstmt.setInt(2, Integer.parseInt(isbn));
        pstmt.setInt(3, Integer.parseInt(formatoId));
        pstmt.setInt(4, Integer.parseInt(venditoreId));
        pstmt.setString(5, tipoCondizione);
        pstmt.setInt(6, Integer.parseInt(quantita));   
        
        pstmt.executeQuery(); 
 }
   
   public static void inserisciArticoloLista(String utenteId, String listaNome, String isbn, String formatoId, String venditoreId, String tipoCond, String dataPrezzo) throws SQLException {
        //Viene effettuato l'inserimento nel carrello di un articolo
 
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPLISTADESIDERI(UTENTE_ID, LISTA_NOME, ISBN, Formato_ID, Venditore_ID, TIPOCONDIZIONE, DATAAGGIUNTA_PREZZO) VALUES(?,?,?,?,?,?,?)",
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, Integer.parseInt(utenteId));
        pstmt.setString(2, listaNome);
        pstmt.setInt(3, Integer.parseInt(isbn));
        pstmt.setInt(4, Integer.parseInt(formatoId));
        pstmt.setInt(5, Integer.parseInt(venditoreId));
        pstmt.setString(6, tipoCond);
        pstmt.setString(7, dataPrezzo);   
        
        pstmt.executeQuery(); 
    }
   
   
   public static void inserisciLibro(String venditoreID, String isbn, String formatoID, String tipoCondizione, String pezziDisp, String prezzo) throws SQLException {
       //Inserisce in un determinato venditore un libro selezionato precedentemente con determinate informazioni
       
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO MAGAZZINO_LIBRI VALUES(?, ?, ?, ?, ?, ?)");
       pstmt.setString(1, venditoreID);
       pstmt.setString(2, isbn);
       pstmt.setString(3, formatoID);
       pstmt.setString(4, tipoCondizione);
       pstmt.setString(5, pezziDisp);
       pstmt.setString(6, prezzo);
       
       pstmt.executeUpdate();
   }
   
   public static ResultSet visualizzaLibriDisponibili() throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_LIBRIDISPONIBILI",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       return pstmt.executeQuery();
   }
   
      public static ResultSet visualizzaFormatoLibro(String venditoreID, String isbn) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT FORMATO_ID, PEZZIDISPONIBILI, PREZZOVENDITA FROM MAGAZZINO_LIBRI WHERE VENDITORE_ID = ? AND ISBN = ?",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, venditoreID);
       pstmt.setLong(2, Long.parseLong(isbn));
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaListeUtente(String utenteId) throws SQLException   {
       //In questo campo compaiono le liste dei desideri di un singolo utente
       
       //Esempio: UTENTE_ID = 423570;
       /*RISULTATO QUERY: 
                        LISTA_NOME              LISTA_PRIVACY
                        CD da Acquistare	Pubblica
                        Per mia Moglie          Privata
       
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LISTA_NOME, LISTA_PRIVACY FROM LISTA_DESIDERI WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(utenteId));
       
       return pstmt.executeQuery();
   }
   
   public static ResultSet visualizzaArticoliListaUtente(String utenteId, String listaNome) throws SQLException   {
       //In questo campo compaiono i libri di una lista di un utente
       
       //Esempio: UTENTE_ID = 423572, LISTA_NOME LIKE 'Default';
       /*RISULTATO QUERY: 
                        LIBRO_NOME              FORMATO_NOME            TIPOCONDIZIONE      VENDITORE_NOME      PREZZOVENDITA
                        Invito alla Biologia	Copertina Flessibile	Usato               Amazon.it           5,99
       
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, VENDITORE_NOME, PREZZOVENDITA FROM COMPLISTA_DESIDERI NATURAL JOIN VIEW_INFO WHERE UTENTE_ID = ? AND LISTA_NOME LIKE '?'",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, Integer.parseInt(utenteId));
       pstmt.setInt(2, Integer.parseInt(listaNome));
       
       return pstmt.executeQuery();
   }
}

