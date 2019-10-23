/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geterrorgroupings;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author vpanneerselvam
 */
public class GetErrorGroupings {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
        FileReader reader=new FileReader("GetErrorGroupings.properties");  

            Properties p=new Properties();  
            p.load(reader);  

            ParseXmlFile parse = new ParseXmlFile();
            
            parse.XMLReaderFeedComparator_ReportSpecific(p.getProperty("JtlFilePath"), p.getProperty("OutFilePath"));
                    
            return;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    
}
