/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazon;

import amazon.utility.Scontotemp;
import amazon.exceptions.CodeNotValidException;
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
     * Avvia la connessione con il server
     */
    public static void StartConnection() throws SQLException {
        ods = new OracleDataSource();
        ods.setDriverType("thin");
        ods.setServerName(tempHost);
        ods.setPortNumber(Integer.parseInt(tempPort));
        ods.setUser(tempUser);//143.225.117.238:1521/xe
        ods.setPassword(tempPass);
        ods.setDatabaseName("xe");
        conn = ods.getConnection();
    }
    
    /**
     * chiude la connessione e riconnette l'applicazione al database con l'ultima
     * configurazione di ID e password
     * @throws SQLException 
     */
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
    
    /**
     * Verifica se la connessione è attiva.
     * @return true se è connesso, false altrimenti.
     */
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
      } catch (SQLException e) {}
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
   
   /**
    * Ottiene l'ID del prdotto dal magazzino libri passando l'ISBN, venditore e formato
    * @param isbn stringa, isbn dell'entità libri
    * @param venditoreId intero id del venditore
    * @param formatoId intero formato del libro
    * @return ID del prodotto PROD_ID
    * @throws SQLException 
    */
   public static int ottieniIdProdotto(String isbn, int venditoreId, int formatoId) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT PROD_ID FROM MAGAZZINO_LIBRI WHERE VENDITORE_ID = ?, ISBN = ?, FORMATO_ID = ?");
       pstmt.setInt(1, venditoreId);
       pstmt.setString(2, isbn);
       pstmt.setInt(3, formatoId);
       ResultSet rs = pstmt.executeQuery();
       return Integer.parseInt(rs.getString(1));
   }
      
   /**
    * Visualizza l'attuale carrello dell'utente dato il suo ID
    * @param idUtente
    * @return ISBN, FORMATO_ID, VENDITORE_ID, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PREZZOVENDITA, VENDITORE_NOME, QUANTITÀ
    * @throws SQLException 
    */
   public static ResultSet visualizzaCarrello(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT VIEW_INFO.ISBN, VIEW_INFO.FORMATO_ID, VIEW_INFO.VENDITORE_ID, VIEW_INFO.LIBRO_NOME, VIEW_INFO.FORMATO_NOME, VIEW_INFO.TIPOCONDIZIONE, VIEW_INFO.PREZZOVENDITA, VIEW_INFO.VENDITORE_NOME, QUANTITÀ FROM COMPARTICOLI JOIN VIEW_INFO ON VIEW_INFO.ISBN = COMPARTICOLI.ISBN AND VIEW_INFO.FORMATO_ID = COMPARTICOLI.FORMATO_ID AND VIEW_INFO.TIPOCONDIZIONE LIKE COMPARTICOLI.TIPOCONDIZIONE AND VIEW_INFO.VENDITORE_ID = COMPARTICOLI.VENDITORE_ID WHERE UTENTE_ID = ? AND ORDINE_ID = 0",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       return pstmt.executeQuery();
   }
   
   /**
    * Elimina un articolo dal carrello in base ai dati in ingresso specificati nel metodo
    * @param idUtente
    * @param prodID
    * @throws SQLException 
    */
   public static void eliminaArticoloCarrello(int idUtente, int prodID) throws SQLException {
       PreparedStatement pstmt;
        pstmt = conn.prepareStatement("DELETE FROM COMPARTICOLI WHERE UTENTE_ID=? AND PROD_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, idUtente);
        pstmt.setInt(2, prodID);
   }
   
   /**
    * Visualizza gli ordini effettuati dall'utente dato il suo ID
    * @param idUtente
    * @return ORDINE_ID, DATAORDINE, PREZZOTOTALE
    * @throws SQLException 
    */
   public static ResultSet visualizzaOrdiniUtente(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT ORDINE_ID, DATAORDINE, PREZZOTOTALE FROM ORDINI WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Visualizza gli articoli presenti in un ordine di un utente
    * @param idOrdine
    * @return LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, QUANTITÀ, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaArticoliOrdine(int idOrdine) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT VIEW_INFO.LIBRO_NOME, VIEW_INFO.FORMATO_NOME, VIEW_INFO.TIPOCONDIZIONE, COMPARTICOLI.QUANTITÀ, COMPARTICOLI.PREZZOVENDITA FROM COMPARTICOLI JOIN VIEW_INFO ON VIEW_INFO.PROD_ID = COMPARTICOLI.PROD_ID WHERE ORDINE_ID=?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idOrdine);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Restituisce il valore di sconto del codice, lancia eccezione se non valido
    * @param codice
    * @return SCONTO
    * @throws SQLException
    * @throws CodeNotValidException se il codice è già stato usato o non è valido
    */
   public static double verificaSconto(String codice) throws SQLException, CodeNotValidException {
        ResultSet rs;
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("SELECT SCONTO FROM SCONTO_CODICI WHERE CODPROMO=? AND ORDINE_ID IS NULL",
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, codice);

        rs=pstmt.executeQuery();
        rs.last();
        double ritorno = 0.0;
        try {
            rs.first();
            ritorno = rs.getDouble(1);
        }
        catch(SQLException ex) {
            //MANDA UNA ECCEZIONE DI CODICE GIA USATO.
            throw new CodeNotValidException();
        }     
        return ritorno;
   }
   
   /**
    * Collega i codici promozionali utilizzati all'ordine dell'utente.
    * @param sconti Array di sconti(codice sconto, sconto)
    * @param ordineId id dell'ordine
    * @throws SQLException 
    */
   public static void applicaScontoOrdine(Scontotemp sconti[], int ordineId) throws SQLException {
        PreparedStatement pstmt;
        int contatore=0;
        String codPromo;
        
        while(sconti[contatore]!=null)    {
            codPromo=sconti[contatore].getcodPromo();
            
            pstmt = conn.prepareStatement("UPDATE CODICI_SCONTO SET ORDINE_ID=? WHERE CODPROMO=?");
            pstmt.setInt(1, ordineId);
            pstmt.setString(2, codPromo);
            
            pstmt.executeUpdate();
            contatore++;
            pstmt.close();
        }  
   }
   
   /**
    * Converte il carrello dell'utente in un ordine da spedire
    * @param idUtente id dell'utente, intero
    * @param sped costo previsto dalla modalità di spedizione
    * @param sconto valore complessivo dello sconto
    * @param modpagamento id del metodo di pagamento
    * @param sconti codici di sconto
    * @throws SQLException 
    */
   public static void creaOrdine (int idUtente, String sped, double sconto, int modpagamento, Scontotemp sconti[]) throws SQLException {
       //NOTA = sistemare i "parse" ove necessario
       //NOTA2 = gestire i pezzi disponibili. Checkare e sottrarre solo se il formato ID è 2001 o 2002.
       
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       ResultSet rs; //Variabile dove inserire i risultati della Query
       int idOrder; //id dell'ordine da usare per l'aggiunta in COMPARTICOLI e SPEDIZIONE
       
       pstmt = conn.prepareStatement("INSERT INTO ORDINI(UTENTE_ID, DATAORDINE, COSTOSPED, SCONTOCOMPL, MOD_PAGAMENTO_ID) VALUES(?,SYSDATE,?,?,?)");
       pstmt.setInt(1, idUtente);
       pstmt.setString(2, sped);
       pstmt.setDouble(3, sconto);
       pstmt.setInt(4, modpagamento);
       
       rs=pstmt.executeQuery();
       idOrder=rs.getInt(2);//Ottenimento ORDINE_ID riga inserita
       pstmt.close();
       
       PreparedStatement pstmt2; //Statement per il richiamo della funzione per il completamento
       
       pstmt2 = conn.prepareStatement("BEGIN CREA_ORDINE_PART_2(?,?,?,?); END;");
       pstmt2.setInt(1, idUtente);
       pstmt2.setInt(2, idOrder);
       pstmt2.setString(3, sped);
       pstmt2.setInt(4, modpagamento);
       
       if (sconti!=null)
           applicaScontoOrdine(sconti, idOrder);
       
       pstmt2.close();
       
   }
   
   /**
    * Su un sottomenu a tendina compaiono le modalità di pagamento disponibili per l'utente attivo
    * @param utenteId id dell'utente
    * @return MOD_PAGAMENTO_ID, TITOLARECARTA_NOME, TITOLARECARTA_COGNOME, TIPOCARTA, NUMEROCARTACREDITO, DATASCADENZA
    * @throws SQLException 
    */
   public static ResultSet sceltaModPagamento (int utenteId) throws SQLException {
       //Esempio: UTENTE_ID = 423575;
       /*RISULTATO QUERY:
                    TITOLARECARTA_NOME      TITOLARECARTA_COGNOME   TIPOCARTA   NUMEROCARTACREDITO      DATASCADENZA
                    Roberto                 Di Carlo                Mastercard	4172836483428572	01-GEN-24
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT SELECT MOD_PAGAMENTO_ID, TITOLARECARTA_NOME, TITOLARECARTA_COGNOME, TIPOCARTA, NUMEROCARTACREDITO, DATASCADENZA FROM MOD_PAGAMENTO NATURAL JOIN MOD_PAGAMENTO_CC NATURAL JOIN RUBRICA_INDIRIZZI WHERE UTENTE_ID=? FROM MOD_PAGAMENTO WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, utenteId);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Si crea la recensione postata da un utente con un certo ID su un certo libro/venditore
    * Se la variabile booleana libroRec è TRUE, inserisce una recensione di un prodotto, altrimenti di un venditore.
    * @param idUtente utente che recensisce
    * @param commento stringa, testo della recensione
    * @param libroRec true: libro, false: venditore
    * @param target id dell'elemento da recensire, venditore o libro
    * @param voto valore da 1 a 5
    * @throws SQLException 
    */
   public static void creaRecensione(int idUtente, String commento, boolean libroRec, String target, int voto) throws SQLException {
       /*Esempio query :INSERT INTO "GRUPPO26"."RECENSIONI" (UTENTE_ID, COMMENTO, ISBN, VOTO)
       **               VALUES ('423572', 'Un libro meraviglioso, con forti spunti di riflessione. Da consigliare a tutti', '1', ‘5’)
       */
       
       if (voto < 1)
           voto = 1;
       else if (voto > 5)
           voto = 5;
       PreparedStatement pstmt;
       
       if ( libroRec )
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, ISBN, VOTO) VALUES (?, ?, ?, ?)");
       else
           pstmt = conn.prepareStatement("INSERT INTO RECENSIONI (UTENTE_ID, COMMENTO, VENDITORE_ID, VOTO) VALUES (?, ?, ?, ?)");
       
       pstmt.setInt(1, idUtente);
       pstmt.setString(2, commento);
       pstmt.setString(3, target);
       pstmt.setInt(4, voto);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Creazione di un nuovo utente
    * @param nome
    * @param cognome
    * @param mail
    * @param password
    * @param cellulare
    * @throws SQLException 
    */
   public static void creaUtente(String nome, String cognome, String mail, String password, int cellulare) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO UTENTI(NOME, COGNOME, EMAIL, PSW, NUMCELLULARE) VALUES(?, ?, ?, ?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setInt(5, cellulare);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Aggiornamento dei dati dell'utente
    * @param id dell'utente da modificare, non cambia
    * @param nome
    * @param cognome
    * @param mail
    * @param password
    * @param cellulare
    * @throws SQLException 
    */
   public static void aggiornaUtente(int id, String nome, String cognome, String mail, String password, int cellulare) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE UTENTI SET NOME = ?, COGNOME = ?, EMAIL = ?, PSW = ?, NUMCELLULARE = ? WHERE UTENTE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setString(3, mail);
       pstmt.setString(4, password);
       pstmt.setInt(5, cellulare);
       pstmt.setInt(6, id);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Visualizza i dati di spedizione dell'utente
    * @param idUtente
    * @return CONTACT_ID, CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono
    * @throws SQLException 
    */
   public static ResultSet visualizzaRubricaUtente(int idUtente) throws SQLException {
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT CONTACT_ID, CONTACT_NOME, CONTACT_COGNOME, INDIRIZZOR1, INDIRIZZOR2, CAP, città, Provincia, Paese, Numtelefono FROM RUBRICA_INDIRIZZI WHERE UTENTE_ID=?",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idUtente);
       
       return pstmt.executeQuery();
       
       /*VISUALIZZA:
        1234567	Roberto	Fasullo     Via Roma, 323       80100   Napoli  NA      Italia  5551029387
        1234568	Marco   Carrozzo    Via Dei Sub, 41     80100	Napoli	NA	Italia	5559876543
       */
   }
   
   /**
    * Eliminazione di un record da una tabella secondo i parametri
    * @param id identificatore del record
    * @param tabella nome della tabella da cui risiede il record
    * @param nomeColonna nome dell'attributo dell'ID
    * @throws SQLException 
    */
   public static void eliminaRecord(String id, String tabella, String nomeColonna) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       pstmt = conn.prepareStatement("DELETE FROM " + tabella + " WHERE " + nomeColonna + " = ?");
       pstmt.setString(1, id);
       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di creazione dell'autore
    * @param nome
    * @param cognome
    * @throws SQLException 
    */
   public static void creaAutore(String nome, String cognome) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO AUTORI(AUT_NOME, AUT_COGNOME) VALUES(?, ?)");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);

       
       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di modifica di un autore
    * @param id identificativo
    * @param nome nuovo nome
    * @param cognome nuovo cognome
    * @throws SQLException 
    */
   public static void aggiornaAutore(int id, String nome, String cognome) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE AUTORI SET AUT_NOME = ?, AUT_COGNOME = ? WHERE AUTORE_ID = ?");
       pstmt.setString(1, nome);
       pstmt.setString(2, cognome);
       pstmt.setInt(3, id);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Creazione di un libro nell'entità LIBRI
    * @param nomeLibro
    * @param nEdizione
    * @param isbn attenzione, è l'identificativo
    * @param descrizione
    * @param genere
    * @param nPagine
    * @param pesoSped
    * @param dataUscita DD-MES-AAAA
    * @throws SQLException 
    */
   public static void creaLibro(String nomeLibro, int nEdizione, String isbn, String descrizione, String genere, int nPagine, int pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       //Quando si crea un libro bisogna aggiungere: COD_AUTORE, EDITORE, LINGUA, PREZZO
       
       pstmt = conn.prepareStatement("INSERT INTO LIBRI(LIBRO_NOME, EDIZIONE_N, ISBN, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, DATAUSCITA) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
       pstmt.setString(1, nomeLibro);
       pstmt.setInt(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setInt(6, nPagine);
       pstmt.setInt(7, pesoSped);
       pstmt.setString(8, dataUscita);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Metodo di modifica del libro
    * @param oldISBN identificatore del libro
    * @param nomeLibro
    * @param nEdizione
    * @param isbn nuovo ISBN
    * @param descrizione
    * @param genere
    * @param nPagine
    * @param pesoSped
    * @param dataUscita
    * @throws SQLException 
    */
   public static void aggiornaLibro(String oldISBN, String nomeLibro, int nEdizione, String isbn, String descrizione, String genere, int nPagine, int pesoSped, String dataUscita) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE LIBRI SET LIBRO_NOME = ?, EDIZIONE_N = ?, ISBN = ?, DESCRIZIONE = ?, GENERE = ?, PAGINE_N = ?, PESOSPED = ?, DATAUSCITA = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS.SSSSS') WHERE ISBN LIKE ?");
       pstmt.setString(1, nomeLibro);
       pstmt.setInt(2, nEdizione);
       pstmt.setString(3, isbn);
       pstmt.setString(4, descrizione);
       pstmt.setString(5, genere);
       pstmt.setInt(6, nPagine);
       pstmt.setInt(7, pesoSped);
       pstmt.setString(8, dataUscita);
       pstmt.setString(9, oldISBN);
       
       pstmt.executeUpdate();
   }
   
   public static void creaEditore(String nomeEditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO EDITORI(EDI_NOME) VALUES(?)");
       pstmt.setString(1, nomeEditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaEditore(int idEditore, String nomeEditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE EDITORI SET EDI_NOME = ? WHERE EDI_ID = ?");
       pstmt.setString(1, nomeEditore);
       pstmt.setInt(2, idEditore);
       
       pstmt.executeUpdate();
   }
   
      public static void creaVenditore(String nomeVenditore) throws SQLException
   {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("INSERT INTO VENDITORI(VENDITORE_NOME) VALUES(?)");
       pstmt.setString(1, nomeVenditore);

       
       pstmt.executeUpdate();
   }
   
   public static void aggiornaVenditore(int idVenditore, String nomeVenditore) throws SQLException {
       PreparedStatement pstmt; //Statement inserimento nuova riga in ordini
       
       pstmt = conn.prepareStatement("UPDATE VENDITORI SET VENDITORE_NOME = ? WHERE VENDITORE_ID = ?");
       pstmt.setString(1, nomeVenditore);
       pstmt.setInt(2, idVenditore);
       
       pstmt.executeUpdate();
   }
   
   public static ResultSet visualizzaLibriAutore(int idAutore) throws SQLException {
       /*Metodo che gestisce l'Autore e visualizza 
       **tutti i libri di quell'autore
       */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idAutore);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Soffocare
       */
    }
   
   public static ResultSet cercaLibriAutore(int idAutore, String chiave) throws SQLException {
       PreparedStatement pstmt;
       String query = "SELECT LIBRI.LIBRO_NOME FROM AUTORI_LIB, LIBRI WHERE AUTORI_LIB.ISBN = LIBRI.ISBN AND AUTORE_ID = ? AND LIBRI.LIBRO_NOME LIKE ?";
       pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idAutore);
       pstmt.setString(2, chiave);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME
                          Fight Club
       */
   }
   
   public static ResultSet visualizzaLibriEditore(int idEditore) throws SQLException {
        /*Metodo che gestisce l'Editore e visualizza 
        **tutti i libri di quell'editore
        */
       
       PreparedStatement pstmt;
       
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI, EDITORI_LIB WHERE EDITORI_LIB.ISBN = LIBRI.ISBN AND EDI_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idEditore);
       
       return pstmt.executeQuery();
    }
   
   /**
    * Cerca il libro di un editore dalla query
    * @param idEditore editore
    * @param query titolo del libro
    * @return LIBRO_NOME
    * @throws SQLException 
    */
   public static ResultSet cercaLibroEditore(int idEditore, String query) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME FROM LIBRI, EDITORI_LIB WHERE EDITORI_LIB.ISBN = LIBRI.ISBN AND EDI_ID = ? AND LIBRO_NOME LIKE ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idEditore);
       pstmt.setString(2,query);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Lista dei libri dettagliata che il venditore ha a disposizione
    * @param venditoreID
    * @return LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaMagazzino (int venditoreID) throws SQLException {       
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
       
       pstmt.setInt(1, venditoreID);
       
       return pstmt.executeQuery();
   }

   /**
    * Vista sull'inventario di un magazzino di un venditore
    * serve per la ricerca su un nome
    * @param idVenditore
    * @param query parola da cercare
    * @return VIEWMAGAZZINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaMagazzino(int idVenditore, String query) throws SQLException   {
       PreparedStatement pstmt;
       query = query.replace('%', ' ');
       pstmt = conn.prepareStatement("SELECT * FROM VIEWMAGAZZINO WHERE VENDITORE_ID = ? AND LIBRI_NOME LIKE %?%",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, idVenditore);
       pstmt.setString(2, query);
       
       return pstmt.executeQuery();
       /*RISULTATO QUERY: LIBRO_NOME    AUT_NOME    AUT_COGNOME     EDI_NOME    ISBN            VENDITORE_ID
                          Fight Club	Chuck       Palahniuk       Mondadori	9788804508359	6318
       */
   }
   
   /**
    * Visualizza tutti i libri disponibili per l'acquisto
    * @return result set LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri() throws SQLException {
        //Lista completa di tutti i libri presenti nell'archivio completo (non nei magazzini dei venditori)
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI NATURAL JOIN IMPOSTAZIONI",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       
       return pstmt.executeQuery();
       
   }
   
   /**
    * Lista completa di tutti i libri presenti nell'archivio completo (non nei magazzini dei venditori)
    * @param formato
    * @return
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri(int formato) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LIBRO_NOME, ISBN, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI WHERE FORMATO_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, formato);
       return pstmt.executeQuery();
       
   }
   
   /**
    * Visualizza il listino dei libri secondo una ricerca avanzata dall'utente
    * @param query          parola cercata (% vengono messe automaticamente)
    * @param formato        2001, 2002 o 2003
    * @param prezzoMin      prezzo minimo richiesto (DOUBLE)
    * @param prezzoMax      limite massimo del prezzo (DOUBLE)
    * @return LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO
    * @throws SQLException 
    */
   public static ResultSet visualizzaListinoLibri(String query, int formato, double prezzoMin, double prezzoMax) throws SQLException {
       //Con una stringa possiamo cercare il nome di un libro presente nell'archivio
       boolean cercaParola = false;
       boolean cercaFormato = false;
       boolean cercaPrezzoMin = false;
       boolean cercaPrezzoMax = false;
       int controllo = 0;
       String ricerca = "SELECT LIBRO_NOME, ISBN, FORMATO_NOME, PREZZOLISTINO FROM LIBRI NATURAL JOIN LISTINO_PREZZI NATURAL JOIN IMPOSTAZIONI WHERE ";
       if (!"".equals(query)) {
           ricerca += "LIBRI.LIBRO_NOME LIKE ?";
           cercaParola = true;
       } else
           ricerca += "1=1";
       if (formato > 0) {
           cercaFormato = true;
           ricerca += " AND FORMATO_ID = ?";
       }
       if (prezzoMin > 0) {
           cercaPrezzoMin = true;
           ricerca += " AND PREZZOLISTINO >= ?";
       }
       if (prezzoMax > 0) {
           cercaPrezzoMax = true;
           ricerca += " AND PREZZOLISTINO <= ?";
       }
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement(ricerca,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       if (cercaParola == true) {
           controllo++;
           pstmt.setString(controllo, "%"+query+"%");
       }
       if (cercaFormato == true) {
           controllo++;
           pstmt.setInt(controllo, formato);
       }    
       if (cercaPrezzoMin == true) {
           controllo++;
           pstmt.setDouble(controllo, prezzoMin);
       }
       if (cercaPrezzoMax == true) {
           controllo++;
           pstmt.setDouble(controllo, prezzoMax);
       }
       return pstmt.executeQuery();
   }
   
   
   /**
    * Compaiono le informazioni dettagliate dei libri presenti nell'archivio
    * @param isbn
    * @return ISBN, NOME_LIBRO, PROD_ID, VENDITORE_ID, VENDITORE_NOME, AUTORE_ID, AUT_NOME, AUT_COGNOME, EDI_ID, EDI_NOME, FORMATO_ID, FORMATO NOME, TIPOCONDIZIONE, DESCRIZIONE, GENERE, PAGINE_N, PESOSPED, DATAUSCITA, 
    * @throws SQLException 
    */
   public static ResultSet visualizzaInfoLibro (String isbn) throws SQLException {
        //Esempio: ISBN = 9788804632238;
       /*RISULTATO QUERY:
            ISBN                NOME_LIBRO              PROD_ID     VENDITORE_ID  VENDITORE_NOME    AUTORE_DI   AUT_NOME    AUT_COGNOME     EDI_ID      EDI_NOME        FORMATO_ID  FORMATO NOME    TIPOCONDIZIONE      DESCRIZIONE                             GENERE      PAGINE_N    PESOSPED    DATAUSCITA
            5558845230455	La Danza delle Stelle	28	    6294	  Amazon.it         1006	Giuseppe    Senese          2779	Bompiani	2003        Kindle          Nuovo		Libro inedito che forse verrà scritto.	Fantasy     328         200         10-FEB-13
       */
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_INFO NATURAL JOIN LIBRI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       
       return pstmt.executeQuery();
       
   }
   
   /**
    * In questo campo compaiono i venditori che hanno a disposizione il libro scelto, qualsiasi formato abbiano.
    * VENDITORE_ID, VENDITORE_NOME, PREZZOVENDITA_MINIMO
    * @param isbn
    * @return ResultSet di informazioni
    * @throws SQLException 
    */
   public static ResultSet visualizzaVenditoriLibro(String isbn) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VENDITORE_ID, VENDITORE_NOME, PREZZOVENDITA_MINIMO FROM VIEW_LIBRIDISPONIBILI NATURAL JOIN VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       return pstmt.executeQuery();
   }
   
   /**
    * In questo campo compaiono i formati che il venditore ha a disposizione per il libro in questione
    * FORMATO_ID, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA
    * @param isbn libro
    * @param venditoreID id del venditore
    * @return ResultSet elenco dei formati con prezzo e tutto
    * @throws SQLException 
    */
   public static ResultSet visualizzaFormatoLibroVenditore(String isbn, int venditoreID) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT PROD_ID, FORMATO_ID, FORMATO_NOME, TIPOCONDIZIONE, PEZZIDISPONIBILI, PREZZOVENDITA, PERCSCONTO FROM MAGAZZINO_LIBRI NATURAL JOIN VENDITORI NATURAL JOIN IMPOSTAZIONI NATURAL JOIN VIEW_PERCSCONTO WHERE ISBN = ? AND VENDITORE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       pstmt.setInt(2, venditoreID);
       return pstmt.executeQuery();
   }
   
      /**
    * In questo campo compaiono i voti medi dei libri presenti nell'archivio
    * ISBN, VOTO_MEDIO
    * @param isbn libro
    * @return double valore del voto
    * @throws SQLException 
    */
   public static double visualizzaVotomedioLibro(String isbn) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VOTO_MEDIO FROM VOTO_LIBRI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, isbn);
       ResultSet rs = pstmt.executeQuery();
       rs.first();
       return rs.getDouble(1);
   }
   
     /**
    * In questo campo compaiono i voti medi dei venditori
    * ISBN, VOTO_MEDIO
    * @param venditoreId venditore
    * @return double valore del voto
    * @throws SQLException 
    */
   public static double visualizzaVotomedioVenditore(String venditoreId) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT VOTO_MEDIO FROM VOTO_VENDITORI WHERE ISBN = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setString(1, venditoreId);

       return pstmt.executeQuery().getDouble(2);
   }
   
   
   /**
    * Viene effettuato l'inserimento nel carrello di un articolo
    * @param utenteId id dell'utente
    * @param prodID id del prodotto
    * @param quantita quantità richiesta
    * @throws SQLException 
    */

   public static void inserisciArticoloCarrello(int utenteId, int prodID, int quantita) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPARTICOLI(UTENTE_ID, PROD_ID, Quantità) VALUES(?,?,?)");
        pstmt.setInt(1, utenteId);
        pstmt.setInt(2, prodID);
        pstmt.setInt(3, quantita);   
        
        pstmt.executeQuery(); 
 }
   
   /**
    * Creazione della lista dei desideri
    * @param utenteID intero, id del proprietario della lista
    * @param nomeLista stringa, nome della lista
    * @param privacy 0: privata, 1: condivisa, 2: pubblica
    * @throws SQLException 
    */
   public static void creaListaDesideri(int utenteID, String nomeLista, int privacy) throws SQLException {
       String nomePrivacy;
       switch (privacy) {
           case 0: nomePrivacy = "Privata";
           break;
           case 1: nomePrivacy = "Condivisa";
           break;
           case 2: nomePrivacy = "Pubblica";
           break;
           default: nomePrivacy = "Privata";
       }
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("INSERT INTO LISTA_DESIDERI (UTENTE_ID, LISTA_NOME, LISTA_PRIVACY) VALUES(?,?,?)");
       pstmt.setInt(1, utenteID);
       pstmt.setString(2, nomeLista);
       pstmt.setString(3, nomePrivacy);
       
       pstmt.executeQuery();
   }
   
   public static int ultimaListaDesideri() throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LISTA_ID FROM LISTA_DESIDERI",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       ResultSet rs = pstmt.executeQuery();
       rs.last();
       return rs.getInt(1);
   }
   
   /**
    * Modifica della privacy della lista dei desideri
    * @param listaID id lista da modificare
    * @param privacy 0: privata, 1: condivisa, 2: pubblica
    * @throws SQLException 
    */
   public static void modificaListaDesideri(int listaID, int privacy) throws SQLException {
       
       PreparedStatement pstmt;
       String nomePrivacy;
       switch (privacy) {
           case 0: nomePrivacy = "Privata";
           break;
           case 1: nomePrivacy = "Condivisa";
           break;
           case 2: nomePrivacy = "Pubblica";
           break;
           default: nomePrivacy = "Privata";
       }
       pstmt = conn.prepareStatement("UPDATE LISTA_DESIDERI SET LISTA_PRIVACY=? WHERE LISTA_ID = ?");
       pstmt.setString(1, nomePrivacy);
       pstmt.setInt(2, listaID);
       
       pstmt.executeUpdate();
   }
   
   /**
    * Eliminazione della lista dei desideri
    * @param listaID
    * @throws SQLException 
    */
   public static void eliminaListaDesideri(int listaID) throws SQLException {
       
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("DELETE FROM LISTA_DESIDERI WHERE LISTA_ID = ?");
       pstmt.setInt(1, listaID);
       
       pstmt.execute();
   }   
   
   /**
    * Rinomina della lista dei desideri
    * @param listaID
    * @param nuovoNome
    * @throws SQLException 
    */
   public static void rinominaListaDesideri(int listaID, String nuovoNome) throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("UPDATE LISTA_DESIDERI SET LISTA_NOME = ? WHERE LISTA_ID = ?");
       pstmt.setString(1, nuovoNome);
       pstmt.setInt(2, listaID);
       
       pstmt.executeQuery();
    }
   
   /**
    * Inserimento dell'articolo nella lista desideri
    * @param listaID intero, id della lista desideri
    * @param prodID id del prodotto
    * @param dataPrezzo stringa, in formato dd-MES-aaaa
    * @throws SQLException 
    */
   public static void inserisciArticoloLista(int listaID, int prodID, double dataPrezzo) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("INSERT INTO COMPLISTA_DESIDERI(LISTA_ID, PROD_ID, DATAAGGIUNTA_PREZZO) VALUES(?,?,?)",
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, listaID);
        pstmt.setInt(2, prodID);
        pstmt.setDouble(3, dataPrezzo);   
        
        pstmt.executeQuery(); 
    }
   
/**
 * Rimozione di un elemento nella lista desideri
 * @param listaID intero, id della lista
 * @param prodID ID del prodotto
 * @throws SQLException 
 */
   public static void rimuoviArticoloLista(int listaID, int prodID) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("DELETE FROM COMPLISTA_DESIDERI WHERE LISTA_ID=? AND PROD_ID=?");
        pstmt.setInt(1, listaID);
        pstmt.setInt(2, prodID);
        
        pstmt.executeQuery(); 
    }
   
   
   /**
    * Inserisce in un determinato venditore un libro selezionato precedentemente con determinate informazioni
    * @param venditoreID venditore
    * @param isbn libro
    * @param formatoID formato (2001, 2002 o 2003, rispettivamente copertina flessibile, copertina rigida o kindle)
    * @param tipoCondizione "Nuovo", "Usato" o "Ricondizionato"
    * @param pezziDisp null o -1 se kindle
    * @param prezzo dev'essere minore del listino (non è obbligatorio, ma una prassi)
    * @throws SQLException 
    */
    public static void inserisciLibroMagazzino(int venditoreID, String isbn, int formatoID, String tipoCondizione, int pezziDisp, double prezzo) throws SQLException {
        PreparedStatement pstmt;
 
        pstmt = conn.prepareStatement("INSERT INTO MAGAZZINO_LIBRI VALUES(?, ?, ?, ?, ?, ?)");
        pstmt.setInt(1, venditoreID);
        pstmt.setString(2, isbn);
        pstmt.setInt(3, formatoID);
        pstmt.setString(4, tipoCondizione);
        pstmt.setInt(5, pezziDisp);
        pstmt.setDouble(6, prezzo);
        // try { 
            pstmt.executeUpdate();
        // } catch (SQLException ex) {
            // if (ex.getErrorCode() == 9000)
            // aggiungiLibriMagazzino(venditoreID, isbn, formatoID, tipoCondizione, pezziDisp, prezzo)
        // } 
 }
   
   /**
    * Query (ridondante ma) utile per controllare la disponibilità di un libro nei magazzini
    * @return VIEW_LIBRIDISPONIBILI
    * @throws SQLException 
    */
   public static ResultSet visualizzaLibriDisponibili() throws SQLException {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT * FROM VIEW_LIBRIDISPONIBILI",
               ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
       return pstmt.executeQuery();
   }
   
   /**
    * Visualizza tutti i formati disponibili per il libro dal venditore
    * @param venditoreID
    * @param isbn
    * @return FORMATO_ID, PEZZIDISPONIBILI, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaFormatoLibro(int venditoreID, String isbn) throws SQLException {
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement("SELECT FORMATO_ID, PEZZIDISPONIBILI, PREZZOVENDITA FROM MAGAZZINO_LIBRI WHERE VENDITORE_ID = ? AND ISBN = ?",
           ResultSet.TYPE_SCROLL_INSENSITIVE,
           ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, venditoreID);
        pstmt.setString(2, isbn);
        return pstmt.executeQuery();
   }
   
      /**
       * In questo campo compaiono le liste dei desideri di un singolo utente
       * LISTA_ID, LISTA_NOME, LISTA_PRIVACY
       * @param utenteId intero, identificatore dell'utente
       * @return resultset delle liste desideri dell'utente LISTA_ID, LISTA_NOME, LISTA_PRIVACY
       * @throws SQLException 
       */
   public static ResultSet visualizzaListeUtente(int utenteId) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT LISTA_ID, LISTA_NOME, LISTA_PRIVACY FROM LISTA_DESIDERI WHERE UTENTE_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, utenteId);
       
       return pstmt.executeQuery();
   }
   
   /**
    * In questo campo compaiono i libri di una lista di un utente
    * @param listaID id della ista
    * @return PROD_ID, ISBN, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, VENDITORE_NOME, PREZZOVENDITA
    * @throws SQLException 
    */
   public static ResultSet visualizzaArticoliListaUtente(int listaID) throws SQLException   {
       PreparedStatement pstmt;
       pstmt = conn.prepareStatement("SELECT DISTINCT PROD_ID, ISBN, LIBRO_NOME, FORMATO_NOME, TIPOCONDIZIONE, VENDITORE_NOME, PREZZOVENDITA FROM COMPLISTA_DESIDERI NATURAL JOIN VIEW_INFO NATURAL JOIN MAGAZZINO_LIBRI WHERE LISTA_ID = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
       pstmt.setInt(1, listaID);
       
       return pstmt.executeQuery();
   }
   
   /**
    * Inserendo un result set, restituisce il numero di righe
    * @param resultSet
    * @return intero, numero di righe
    */
   public static int contaRigheResultSet(ResultSet resultSet) {
       int size;
    try {
        resultSet.last();
        size = resultSet.getRow();
        resultSet.beforeFirst();
    }
    catch(Exception ex) {
        return 0;
    }
    return size;
   }
}

