package dropbox;

import static dropbox.GUI.FilesUI.fileNameFieldEdit;
import static dropbox.GUI.FilesUI.pathLabel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public abstract class FileSystemElement
{
    protected String id;
    protected String name;
    protected String creationDate;
    protected String parentFolderID;
    
    public FileSystemElement(String id , String name , String parentFolderID , String creationDate)
    {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.parentFolderID = parentFolderID;
    }
    

    public String getId()
    {
        return id;
    }

    public String getName() 
    {
        return name;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public String getParentFolderID() 
    {
        return parentFolderID;
    }
    
   
    
}
