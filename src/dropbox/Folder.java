package dropbox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Folder extends FileSystemElement 
{
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    private ArrayList<String> foldersIDs;   // proxy placeholder for folder 
    private ArrayList<AbstractFile> filesList;
    
    public Folder(String id , String name , String parentFolderID) 
    {
        super(id , name , parentFolderID , dateFormat.format(new Date()).toString());
        foldersIDs = null;
        filesList = null;
    }
    
    public Folder(String id , String name , String parentFolderID , String creationDate)
    {
        super(id , name , parentFolderID , creationDate);
        foldersIDs = null;
        filesList = null;
    }
    
    public void loadFilesList() throws SQLException
    {
        if(filesList != null)
        {
            filesList.clear();
        }
        
        filesList = new ArrayList<AbstractFile>();
        
        
        ResultSet files = Storage.getInstance().loadFiles(this.id);
        while(files.next())
        {
            String fileType = files.getString("type").toLowerCase();
            String id = files.getString("id");
            String name = files.getString("name");
            String parentFolderID = files.getString("container_id");
            String creationDate = files.getString("creation_date");
            String url = files.getString("url");
            
            if(fileType.equals("text"))
            {
                filesList.add(AbstractFileFactory.getInstance().getFile(AbstractFileFactory.FileType.TEXT , id , name , parentFolderID , creationDate , url));
                
            }
            else if(fileType.equals("image"))
            {
                filesList.add(AbstractFileFactory.getInstance().getFile(AbstractFileFactory.FileType.IMAGE , id , name , parentFolderID , creationDate , url));
            }
            else if(fileType.equals("pdf"))
            {
                filesList.add(AbstractFileFactory.getInstance().getFile(AbstractFileFactory.FileType.PDF , id , name , parentFolderID , creationDate , url));
          
            }
            else if(fileType.equals("video"))
            {
                filesList.add(AbstractFileFactory.getInstance().getFile(AbstractFileFactory.FileType.VIDEO , id , name , parentFolderID , creationDate , url));
            }
                        
        }   
    }

    public ArrayList<String> getFoldersIDs()
    {
        return foldersIDs;
    }

    public ArrayList<AbstractFile> getFilesList() 
    {
        return filesList;
    }
    
    
    
    public void loadFoldersIDs() throws SQLException
    {
        if(foldersIDs != null)
        {
            foldersIDs.clear();
        }
        
        foldersIDs = new ArrayList<String>();
        
        
        ResultSet folderIDs = Storage.getInstance().loadFoldersIDs(this.id);
        while(folderIDs.next())
        {
            foldersIDs.add(folderIDs.getString("id"));
        }   
    }
    
}
