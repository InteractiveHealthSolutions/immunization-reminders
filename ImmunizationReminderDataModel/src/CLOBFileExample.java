/*// -----------------------------------------------------------------------------
// CLOBFileExample.java
// -----------------------------------------------------------------------------


 * =============================================================================
 * Copyright (c) 1998-2011 Jeffrey M. Hunter. All rights reserved.
 * 
 * All source code and material located at the Internet address of
 * http://www.idevelopment.info is the copyright of Jeffrey M. Hunter and
 * is protected under copyright laws of the United States. This source code may
 * not be hosted on any other site without my express, prior, written
 * permission. Application to host any of the material elsewhere can be made by
 * contacting me at jhunter@idevelopment.info.
 *
 * I have made every effort and taken great care in making sure that the source
 * code and other content included on my web site is technically accurate, but I
 * disclaim any and all responsibility for any loss, damage or destruction of
 * data or any other property which may arise from relying on it. I will in no
 * case be liable for any monetary damages arising from such loss, damage or
 * destruction.
 * 
 * As with any code, ensure to test this code in a development environment 
 * before attempting to run it in production.
 * =============================================================================
 
 
import java.sql.*;
import java.io.*;
import java.util.*;

// Needed since we will be using Oracle's CLOB, part of Oracle's JDBC extended
// classes. Keep in mind that we could have included Java's JDBC interfaces
// java.sql.Clob which Oracle does implement. The oracle.sql.CLOB class 
// provided by Oracle does offer better performance and functionality.
import oracle.sql.*;

// Needed for Oracle JDBC Extended Classes
import oracle.jdbc.*;


*//**
 * -----------------------------------------------------------------------------
 * Used to test the functionality of how to load and unload text data from an
 * Oracle CLOB.
 * 
 * This example uses an Oracle table with the following definition:
 * 
 *      CREATE TABLE test_clob (
 *            id               NUMBER(15)
 *          , document_name    VARCHAR2(1000)
 *          , xml_document     CLOB
 *          , timestamp        DATE
 *      );
 * -----------------------------------------------------------------------------
 * @version 1.0
 * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
 * @author  http://www.idevelopment.info
 * -----------------------------------------------------------------------------
 *//*
 
public class CLOBFileExample  {

    private String          inputTextFileName   = null;
    private File            inputTextFile       = null;

    private String          outputTextFileName1  = null;
    private File            outputTextFile1      = null;

    private String          outputTextFileName2  = null;
    private File            outputTextFile2      = null;
    
    private String          dbUser              = "SCOTT";
    private String          dbPassword          = "TIGER";
    private Connection      conn                = null;
    

    *//**
     * Default constructor used to create this object. Responsible for setting
     * this object's creation date, as well as incrementing the number instances
     * of this object.
     * @param args Array of string arguments passed in from the command-line.
     * @throws java.io.IOException
     *//*
    public CLOBFileExample(String[] args) throws IOException {
        
        inputTextFileName  = args[0];
        inputTextFile = new File(inputTextFileName);
        
        if (!inputTextFile.exists()) {
            throw new IOException("File not found. " + inputTextFileName);
        }

        outputTextFileName1 = inputTextFileName + ".getChars.out";
        outputTextFileName2 = inputTextFileName + ".Streams.out";
        
    }


    *//**
     * Obtain a connection to the Oracle database.
     * @throws java.sql.SQLException
     *//*
    public void openOracleConnection()
            throws    SQLException
                    , IllegalAccessException
                    , InstantiationException
                    , ClassNotFoundException {

        String driver_class  = "oracle.jdbc.driver.OracleDriver";
        String connectionURL = null;

        try {
            Class.forName (driver_class).newInstance();
            connectionURL = "jdbc:oracle:thin:@melody:1521:JEFFDB";
            conn = DriverManager.getConnection(connectionURL, dbUser, dbPassword);
            conn.setAutoCommit(false);
            System.out.println("Connected.\n");
        } catch (IllegalAccessException e) {
            System.out.println("Illegal Access Exception: (Open Connection).");
            e.printStackTrace();
            throw e;
        } catch (InstantiationException e) {
            System.out.println("Instantiation Exception: (Open Connection).");
            e.printStackTrace();
            throw e;
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception: (Open Connection).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Open Connection).");
            e.printStackTrace();
            throw e;
        }
            
    }
    
    
    *//**
     * Close Oracle database connection.
     * @throws java.sql.SQLException
     *//*
    public void closeOracleConnection() throws SQLException {
        
        try {
            conn.close();
            System.out.println("Disconnected.\n");
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Closing Connection).");
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                    System.out.println("Caught SQL (Rollback Failed) Exception.");
                    e2.printStackTrace();
                }
            }
            throw e;
        }

    }
    
    
    *//**
     * Method used to print program usage to the console.
     *//*
    static public void usage() {
        System.out.println("\nUsage: java CLOBFileExample \"Text File Name\"\n");
    }


    *//**
     * Validate command-line arguments to this program.
     * @param args Array of string arguments passed in from the command-line.
     * @return Boolean - value of true if correct arguments, false otherwise.
     *//*
    static public boolean checkArguments(String[] args) {
        
        if (args.length == 1) {
            return true;
        } else {
            return false;
        }

    }


    *//**
     * Override the Object toString method. Used to print a version of this 
     * object to the console.
     * @return String - String to be returned by this object.
     *//*
    public String toString() {
    
        String retValue;

        retValue  = "Input File         : " + inputTextFileName    + "\n" +
                    "Output File (1)    : " + outputTextFileName1  + "\n" +
                    "Output File (2)    : " + outputTextFileName2  + "\n" +
                    "Database User      : " + dbUser;
        return retValue;
    
    }


    *//**
     * Method used to write text data contained in a file to an Oracle CLOB
     * column. The method used to write the data to the CLOB uses the putChars()
     * method. This is one of two types of methods used to write text data to
     * a CLOB column. The other method uses Streams.
     * 
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     *//*
    public void writeCLOBPut() 
            throws IOException, SQLException {
        
        FileInputStream     inputFileInputStream    = null;
        InputStreamReader   inputInputStreamReader  = null;
        BufferedReader      inputBufferedReader     = null;
        String              sqlText                 = null;
        Statement           stmt                    = null;
        ResultSet           rset                    = null;
        Clob                xmlDocument             = null;
        int                 chunkSize;
        char[]              textBuffer;
        long                position;
        int                 charsRead               = 0;
        int                 charsWritten            = 0;
        int                 totCharsRead            = 0;
        int                 totCharsWritten         = 0;
        
        try {

            stmt = conn.createStatement();
            
            inputTextFile = new File(inputTextFileName);
            inputFileInputStream = new FileInputStream(inputTextFile);
            inputInputStreamReader = new InputStreamReader(inputFileInputStream);
            inputBufferedReader = new BufferedReader(inputInputStreamReader);
        
            sqlText =
                "INSERT INTO test_clob (id, document_name, xml_document, timestamp) " +
                "   VALUES(1, '" + inputTextFile.getName() + "', EMPTY_CLOB(), SYSDATE)";
            stmt.executeUpdate(sqlText);
            
            sqlText = 
                "SELECT xml_document " +
                "FROM   test_clob " +
                "WHERE  id = 1 " +
                "FOR UPDATE";
            rset = stmt.executeQuery(sqlText);
            rset.next();
            xmlDocument = ((ResultSet) rset).getClob("xml_document");
            
            chunkSize = xmlDocument.getChunkSize();
            textBuffer = new char[chunkSize];
            
            position = 1;
            while ((charsRead = inputBufferedReader.read(textBuffer)) != -1) {
                charsWritten = xmlDocument.putChars(position, textBuffer, charsRead);
                position        += charsRead;
                totCharsRead    += charsRead;
                totCharsWritten += charsWritten;
            }
            
            inputBufferedReader.close();
            inputInputStreamReader.close();
            inputFileInputStream.close();

            conn.commit();
            rset.close();
            stmt.close();
            
            System.out.println(
                "==========================================================\n" +
                "  PUT METHOD\n" +
                "==========================================================\n" +
                "Wrote file " + inputTextFile.getName() + " to CLOB column.\n" +
                totCharsRead + " characters read.\n" +
                totCharsWritten + " characters written.\n"
            );

        } catch (IOException e) {
            System.out.println("Caught I/O Exception: (Write CLOB value - Put Method).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Write CLOB value - Put Method).");
            System.out.println("SQL:\n" + sqlText);
            e.printStackTrace();
            throw e;
        }

    }

    
    *//**
     * Method used to write the contents (data) from an Oracle CLOB column to
     * an O/S file. This method uses one of two ways to get data from the CLOB
     * column - namely the getChars() method. The other way to read data from an
     * Oracle CLOB column is to use Streams.
     * 
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     *//*
    public void readCLOBToFileGet()
            throws IOException, SQLException {

        FileOutputStream    outputFileOutputStream      = null;
        OutputStreamWriter  outputOutputStreamWriter    = null;
        BufferedWriter      outputBufferedWriter        = null;
        String              sqlText                     = null;
        Statement           stmt                        = null;
        ResultSet           rset                        = null;
        CLOB                xmlDocument                 = null;
        long                clobLength;
        long                position;
        int                 chunkSize;
        char[]              textBuffer;
        int                 charsRead                   = 0;
        int                 charsWritten                = 0;
        int                 totCharsRead                = 0;
        int                 totCharsWritten             = 0;

        try {

            stmt = conn.createStatement();

            outputTextFile1 = new File(outputTextFileName1);
            outputFileOutputStream = new FileOutputStream(outputTextFile1);
            outputOutputStreamWriter = new OutputStreamWriter(outputFileOutputStream);
            outputBufferedWriter = new BufferedWriter(outputOutputStreamWriter);

            sqlText = 
                "SELECT xml_document " +
                "FROM   test_clob " +
                "WHERE  id = 1 " +
                "FOR UPDATE";
            rset = stmt.executeQuery(sqlText);
            rset.next();
            xmlDocument = ((OracleResultSet) rset).getCLOB("xml_document");
            
            clobLength = xmlDocument.length();
            chunkSize = xmlDocument.getChunkSize();
            textBuffer = new char[chunkSize];
            
            for (position = 1; position <= clobLength; position += chunkSize) {
                
                // Loop through while reading a chunk of data from the CLOB
                // column using the getChars() method. This data will be stored
                // in a temporary buffer that will be written to disk.
                charsRead = xmlDocument.getChars(position, chunkSize, textBuffer);

                // Now write the buffer to disk.
                outputBufferedWriter.write(textBuffer, 0, charsRead);
                
                totCharsRead += charsRead;
                totCharsWritten += charsRead;

            }

            outputBufferedWriter.close();
            outputOutputStreamWriter.close();
            outputFileOutputStream.close();
            
            conn.commit();
            rset.close();
            stmt.close();
            
            System.out.println(
                "==========================================================\n" +
                "  GET METHOD\n" +
                "==========================================================\n" +
                "Wrote CLOB column data to file " + outputTextFile1.getName() + ".\n" +
                totCharsRead + " characters read.\n" +
                totCharsWritten + " characters written.\n"
            );

        } catch (IOException e) {
            System.out.println("Caught I/O Exception: (Write CLOB value to file - Get Method).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Write CLOB value to file - Get Method).");
            System.out.println("SQL:\n" + sqlText);
            e.printStackTrace();
            throw e;
        }

    }
    
    
    *//**
     * Method used to write text data contained in a file to an Oracle CLOB
     * column. The method used to write the data to the CLOB uses Streams.
     * This is one of two types of methods used to write text data to
     * a CLOB column. The other method uses the putChars() method.
     * 
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     *//*
    public void writeCLOBStream()
            throws IOException, SQLException {

        FileInputStream     inputFileInputStream    = null;
        OutputStream        clobOutputStream        = null;
        String              sqlText                 = null;
        Statement           stmt                    = null;
        ResultSet           rset                    = null;
        CLOB                xmlDocument             = null;
        int                 bufferSize;
        byte[]              byteBuffer;
        int                 bytesRead               = 0;
        int                 bytesWritten            = 0;
        int                 totBytesRead            = 0;
        int                 totBytesWritten         = 0;

        try {

            stmt = conn.createStatement();

            inputTextFile = new File(inputTextFileName);
            inputFileInputStream = new FileInputStream(inputTextFile);
            
            sqlText =
                "INSERT INTO test_clob (id, document_name, xml_document, timestamp) " +
                "   VALUES(2, '" + inputTextFile.getName() + "', EMPTY_CLOB(), SYSDATE)";
            stmt.executeUpdate(sqlText);
            
            sqlText = 
                "SELECT xml_document " +
                "FROM   test_clob " +
                "WHERE  id = 2 " +
                "FOR UPDATE";
            rset = stmt.executeQuery(sqlText);
            rset.next();
            xmlDocument = ((OracleResultSet) rset).getCLOB("xml_document");
            
            bufferSize = xmlDocument.getBufferSize();
            
            // Notice that we are using an array of bytes as opposed to an array
            // of characters. This is required since we will be streaming the
            // content (to either a CLOB or BLOB) as a stream of bytes using
            // using an OutputStream Object. This requires that a byte array to
            // be used to temporarily store the contents that will be sent to
            // the LOB. Note that they use of the byte array can be used even
            // when reading contents from an ASCII text file that will be sent
            // to a CLOB.
            byteBuffer = new byte[bufferSize];
            
            clobOutputStream = xmlDocument.getAsciiOutputStream();
            
            while ((bytesRead = inputFileInputStream.read(byteBuffer)) != -1) {
            
                // After reading a buffer from the text file, write the contents
                // of the buffer to the output stream using the write()
                // method.
                clobOutputStream.write(byteBuffer, 0, bytesRead);
                
                totBytesRead += bytesRead;
                totBytesWritten += bytesRead;

            }

            // Keep in mind that we still have the stream open. Once the stream
            // gets open, you cannot perform any other database operations
            // until that stream has been closed. This even includes a COMMIT
            // statement. It is possible to loose data from the stream if this 
            // rule is not followed. If you were to attempt to put the COMMIT in
            // place before closing the stream, Oracle will raise an 
            // "ORA-22990: LOB locators cannot span transactions" error.

            inputFileInputStream.close();
            clobOutputStream.close();
            
            conn.commit();
            rset.close();
            stmt.close();

            System.out.println(
                "==========================================================\n" +
                "  OUTPUT STREAMS METHOD\n" +
                "==========================================================\n" +
                "Wrote file " + inputTextFile.getName() + " to CLOB column.\n" +
                totBytesRead + " bytes read.\n" +
                totBytesWritten + " bytes written.\n"
            );

        } catch (IOException e) {
            System.out.println("Caught I/O Exception: (Write CLOB value - Stream Method).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Write CLOB value - Stream Method).");
            System.out.println("SQL:\n" + sqlText);
            e.printStackTrace();
            throw e;
        }

    }
    
    
    *//**
     * Method used to write the contents (data) from an Oracle CLOB column to
     * an O/S file. This method uses one of two ways to get data from the CLOB
     * column - namely using Streams. The other way to read data from an
     * Oracle CLOB column is to use getChars() method.
     * 
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     *//*
    public void readCLOBToFileStream()
            throws IOException, SQLException {

        FileOutputStream    outputFileOutputStream      = null;
        InputStream         clobInputStream             = null;
        String              sqlText                     = null;
        Statement           stmt                        = null;
        ResultSet           rset                        = null;
        CLOB                xmlDocument                 = null;
        int                 chunkSize;
        byte[]              textBuffer;
        int                 bytesRead                   = 0;
        int                 bytesWritten                = 0;
        int                 totBytesRead                = 0;
        int                 totBytesWritten             = 0;

        try {

            stmt = conn.createStatement();

            outputTextFile2 = new File(outputTextFileName2);
            outputFileOutputStream = new FileOutputStream(outputTextFile2);

            sqlText = 
                "SELECT xml_document " +
                "FROM   test_clob " +
                "WHERE  id = 2 " +
                "FOR UPDATE";
            rset = stmt.executeQuery(sqlText);
            rset.next();
            xmlDocument = ((OracleResultSet) rset).getCLOB("xml_document");

            // Will use a Java InputStream object to read data from a CLOB (can
            // also be used for a BLOB) object. In this example, we will use an
            // InputStream to read ASCII characters from a CLOB.
            clobInputStream = xmlDocument.getAsciiStream();
            
            chunkSize = xmlDocument.getChunkSize();
            textBuffer = new byte[chunkSize];
            
            while ((bytesRead = clobInputStream.read(textBuffer)) != -1) {
                
                // Loop through while reading a chunk of data from the CLOB
                // column using an InputStream. This data will be stored
                // in a temporary buffer that will be written to disk.
                outputFileOutputStream.write(textBuffer, 0, bytesRead);
                
                totBytesRead += bytesRead;
                totBytesWritten += bytesRead;

            }

            outputFileOutputStream.close();
            clobInputStream.close();
            
            conn.commit();
            rset.close();
            stmt.close();
            
            System.out.println(
                "==========================================================\n" +
                "  INPUT STREAMS METHOD\n" +
                "==========================================================\n" +
                "Wrote CLOB column data to file " + outputTextFile2.getName() + ".\n" +
                totBytesRead + " characters read.\n" +
                totBytesWritten + " characters written.\n"
            );

        } catch (IOException e) {
            System.out.println("Caught I/O Exception: (Write CLOB value to file - Streams Method).");
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: (Write CLOB value to file - Streams Method).");
            System.out.println("SQL:\n" + sqlText);
            e.printStackTrace();
            throw e;
        }
        
    }
    
    
    *//**
     * Sole entry point to the class and application.
     * @param args Array of string arguments passed in from the command-line.
     *//*
    public static void main(String[] args) {
    
        CLOBFileExample cLOBFileExample = null;
        
        if (checkArguments(args)) {

            try {
                
                cLOBFileExample = new CLOBFileExample(args);
                
                System.out.println("\n" + cLOBFileExample + "\n");
                
                cLOBFileExample.openOracleConnection();
                
                cLOBFileExample.writeCLOBPut();
                cLOBFileExample.readCLOBToFileGet();
                
                cLOBFileExample.writeCLOBStream();
                cLOBFileExample.readCLOBToFileStream();
                
                cLOBFileExample.closeOracleConnection();

            } catch (IllegalAccessException e) {
                System.out.println("Caught Illegal Accecss Exception. Exiting.");
                e.printStackTrace();
                System.exit(1);
            } catch (InstantiationException e) {
                System.out.println("Instantiation Exception. Exiting.");
                e.printStackTrace();
                System.exit(1);
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found Exception. Exiting.");
                e.printStackTrace();
                System.exit(1);
            } catch (SQLException e) {
                System.out.println("Caught SQL Exception. Exiting.");
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Caught I/O Exception. Exiting.");
                e.printStackTrace();
                System.exit(1);
            }

        } else {
            System.out.println("\nERROR: Invalid arguments.");
            usage();
            System.exit(1);
        }
        
        System.exit(0);
    }
    
}
*/