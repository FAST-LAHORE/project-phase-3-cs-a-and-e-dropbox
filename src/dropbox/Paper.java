package dropbox;

import dropbox.GUI.FilesUI;

import dropbox.GUI.FilesUI;
import static dropbox.GUI.FilesUI.contentArea;
import static dropbox.GUI.FilesUI.pathLabel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Paper 
{
    public  String id;
    public  String name;
    public  String creationDate;
    Paper(String id , String name , String creationDate)
    {
        this.id=id;
        this.name=name;
        this.creationDate=creationDate;
    }
    
    public Paper()
    {
    }
    public String getId()
    {
        return this.id;
    
    }
    public String getname()
    {
        return this.name;
    
    }
    public String getCDate()
    {
        return this.creationDate;
    
    }
    public boolean createPaper(String name) throws IOException
    {
        try 
        {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.creationDate = Folder.dateFormat.format(new Date()).toString();
            Storage.getInstance().savePaper(this.id, name,this.creationDate , Authentication.online_user.getEmail());
 
            File cloudFile = new File("./src/CloudStorage/" + this.id + ".docx");
            if(!cloudFile.exists())
            {
                cloudFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(cloudFile));
                writer.close();
            }
            
            
//            Authentication.online_user.getUserAccount().displayFiles(f);
            return true;
            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(FilesUI.class.getName()).log(Level.SEVERE, null, ex);
        } 
//        catch (IOException ex) {
//            Logger.getLogger(FilesUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return false;
    }
    
    
}
