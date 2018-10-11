package scaper;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Scraper {
   
    public static ArrayList<String> stringArray = new ArrayList();
    public static ArrayList<String> wellFormattedSessions = new ArrayList();
    public static ArrayList<sessionDetails> sessionDetails = new ArrayList();
    public static ArrayList<String> unformattedDetails = new ArrayList();
     
    public static void main(String[] args) {
        
        //queryWebsite();
        //formatSessions();
        uploadSessions();
    }
    
    public static void uploadSessions(){
        
        Connection conn = null;
        String connectionString = "jdbc:sqlserver://mattripia.database.windows.net:1433;database=autscraper;user=mattripia@mattripia;password=Hello1234;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        String query = ""; 
        
        // the driver for microsofts sql database
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        // the connection to the database using the connectionString
        try {
        conn = DriverManager.getConnection(connectionString); 
        } catch (SQLException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // if the connection failed, this wont execute
        // this writes the data from text file into the database
        if(conn != null)
        {
            System.out.println("Connection successful! Reading and writing data now!");            
            stringArray.clear();
            openFile("formattedSessions.txt");

            // for each string in the string arraylist, insert into the DB
            for(String aString : stringArray)
            {
                String stringArray[] = aString.split(",");
                query = "insert into sessionDetails values('"+stringArray[0]+"','"+ stringArray[1]+"','"+stringArray[2]+"',"+ stringArray[3]+",'"+stringArray[4]+"','"+ stringArray[5]+"','"+ stringArray[6]+"','"+stringArray[7]+"')";
                
                try {
                Statement statement = conn.createStatement();
                statement.executeUpdate(query);
                    
                } catch (SQLException ex) {
                    System.out.println("Duplicate detected - " + stringArray[0] + ',' +stringArray[1] + ',' +stringArray[2] + ',' +stringArray[3] + ',' +stringArray[4] + ',' +stringArray[5] + ',' +stringArray[6] + ',' +stringArray[7]);
                }
            }
        }
    }
    
    // goes into the unformatted arraylist document and removes the dead streams with no sessions
    // this overwrites what was in the output text document with the new data
    // this function also formats the data into comma seperated variables which
    // can be accessed very easily for the database aspect of the program
    public static void formatSessions() {
        
        openFile("unformattedSessions.txt");
        
        int i = 0;
        for(String aString: stringArray)
        {
            if(aString.length() > 45)
            {
                wellFormattedSessions.add(aString);
            }
            i++;
        }
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("formattedSessions.txt"));
            
            for(String aString : wellFormattedSessions)
            {
                // paper code
                String paperCodeString = aString.substring(0, 7);
                
                // paper stream
                String paperStreamString = aString.substring(8, 10);
                
                // paper semester
                String paperSemester = "";
                switch (paperStreamString.charAt(0)) {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                        paperSemester = "S1";
                        break;
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                        paperSemester = "S2";
                        break;
                    case '9':
                        paperSemester = "SS";
                        break;
                    case 'M':
                        if(paperStreamString.charAt(1) == '1' || paperStreamString.charAt(1) == '2' || paperStreamString.charAt(1) == '3' || paperStreamString.charAt(1) == '4')
                        {
                            paperSemester = "S1";
                        }
                        else
                        {
                            paperSemester = "S2";
                        }   break;
                    default:
                            paperSemester = "Unknown";
                        break;
                }
                
                aString = aString.substring(13);
                
                if(aString.charAt(0) == 'A' || aString.charAt(0) == 'B' || aString.charAt(0) == 'C' || aString.charAt(0) == 'D')
                {
                    aString = aString.substring(2);
                }
                
                // paper year
                String year = aString.substring(7, 11);
                aString = aString.substring(24);
                
                // session day
                String day = aString.substring(0, 3);
                aString = aString.substring(4);
                
                // session start time
                String startTime = aString.substring(0, 7);
                
                if(startTime.endsWith("."))
                {
                    startTime = startTime.substring(0, 6);
                }
                
                startTime = startTime + "m";
                startTime = startTime.replaceAll(" ", "");
                aString = aString.substring(12);
  
                if(aString.charAt(0) == ' ')
                {
                    aString = aString.substring(1);
                }
                
                // session end time
                String endTime = aString.substring(0, 7);
                
                if(endTime.endsWith("."))
                {
                    endTime = endTime.substring(0, 6);
                }
                
                endTime = endTime + "m";
                endTime = endTime.replaceAll(" ", "");
                
                // session room
                aString = aString.substring(10);
                
                if(aString.charAt(0) == ' ')
                {
                    aString = aString.substring(1);
                }
                
                System.out.println(paperCodeString + "," + paperStreamString + ',' + paperSemester + ',' + year + ',' + day + ',' + startTime + ',' + endTime + ',' + aString);
                writer.write(paperCodeString + "," + paperStreamString + ',' + paperSemester + ',' + year + ',' + day + ',' + startTime + ',' + endTime + ',' + aString);
                writer.newLine();
            }
            
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // opens a file and adds the contents into a string ArrayList
    private static void openFile(String fileName) {
 
        String cLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            
        while((cLine = reader.readLine()) != null)
        {
            stringArray.add(cLine);
        }

        reader.close();
        
        } catch (Exception ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // this is the main scraper of the arion website
    // removes dead info and adds the data i want to unformatted arrayList
    private static void getShortUrlFromHtml(){
        
    ArrayList<String> urlStrings = new ArrayList();
    ArrayList<String> updatedUrlStrings = new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("urls.txt"));
            
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("formattedUrls.txt"));
            String cLine = "";
            
        try {
            while((cLine = reader.readLine()) != null)
            {
                for(int i = 0; i < 6; i++)
                {
                    cLine = reader.readLine();
                }
                if(cLine != null)
                {
                    urlStrings.add(cLine);
                    System.out.println(cLine);
                }
            }
            
            for(String string : urlStrings)
            {
                String newUrlString = string.substring(144);
                updatedUrlStrings.add(newUrlString);
                System.out.println(newUrlString);
            }
            
            urlStrings.clear();
            
            for(String urlString : updatedUrlStrings)
            {
                String[] stringArray = urlString.split("=");
                System.out.println(stringArray[1]);
                urlStrings.add(stringArray[1]);
            }
            
            updatedUrlStrings.clear();
            
            for(String string : urlStrings)
            {
                updatedUrlStrings.add(string.substring(0, 5));
                System.out.println(string.substring(0, 5));
            }

            for(String string : updatedUrlStrings)
            {
                writer.write(string);
                writer.newLine();
            }
            
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (Exception ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void checkShortUrl() {
    int i = 0;
    for(String string : stringArray)
    {
        i++;
        System.out.println(string);
    }
       System.out.println(i);
    }

    private static void queryWebsite() {
        
    // opens the specified file, adds each line in the file to a string
    // then adds the strings to an arrayList of strings which can be iterated over.
    openFile("testurls.txt");
    Document doc = null;

    for(String url : stringArray)
    {
        try {
            doc = Jsoup.connect("https://arion.aut.ac.nz/ArionMain/CourseInfo/Information/Qualifications/Details/PaperDetails.aspx?actiontype=1&id=" + url).get();   

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Elements lightBackground = doc.getElementsByClass("BackgroundLight");
        Element firstElement = lightBackground.get(0);
        String paperNameString = firstElement.text();

        // removes the header info / first 4 lines of the output
        for(int i = 0; i < 4; i ++)
        {
            lightBackground.remove(0);
        }

        // tries to remove the course info by checking if it contains the paper code
        loop:
        while(lightBackground.size() > 0)
        {
            if(lightBackground.get(0).text().contains(paperNameString) == false)
            {
                lightBackground.remove(0);
                lightBackground.remove(0);
            }
            else
            {
                break loop;
            }
        }

        // loops the rest of the elements, this is relevent data that i can use
        // adds the details that i want into unformatttedDetails arrayList
        int index = lightBackground.size();
        String paperCodeAndStream = "";
        for(int i = 0; i < index; i ++)
        {
            Element theElements = lightBackground.get(i);
            String aString = theElements.text();
            
            // if true, its a new session
            if(aString.contains(paperNameString))
            {
                 System.out.println(aString);
                 paperCodeAndStream = aString.substring(0, 13);
                 unformattedDetails.add(aString);
            }
            // if false, its part of the same session
            else
            {
                System.out.println(paperCodeAndStream + aString);
                unformattedDetails.add(paperCodeAndStream + aString);
            }
        }
    }
    
    saveFile("unformattedSessions.txt");
}
    
    // saves the unformattedDetails arrayList into an output doc
    private static void saveFile(String fileName) {
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            
            for(String aString : unformattedDetails)
            {
                writer.write(aString);
                writer.newLine();
            }
            
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Scraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}