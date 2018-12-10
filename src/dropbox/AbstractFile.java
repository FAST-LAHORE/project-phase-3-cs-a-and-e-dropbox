package dropbox;

import static dropbox.GUI.FilesUI.pathLabel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public abstract class AbstractFile extends FileSystemElement
{
    protected String url;
    
    public AbstractFile(String id , String name , String parentFolderID , String creationDate , String url)
    {
        super(id ,  name ,  parentFolderID ,  creationDate);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    
    public static String getFileContent(String fileID) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader("./src/CloudStorage/" + fileID + ".txt"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        return stringBuilder.toString();        
    }
    
    public static boolean editFileName(String oldName , String fileName ,String fileID ,String currFolder) throws SQLException
    {
        
        if(fileName.isEmpty())
        {
             Toast t = new Toast("Enter file name!" , 495 , 505);
             t.showtoast();
             return false;
        }
        
        if(!oldName.equals(fileName))
        {
            ResultSet conatinerIDs = Storage.getInstance().loadContainerUsingFileName(fileName);
            while(conatinerIDs.next())
            {
                if(currFolder.equals(conatinerIDs.getString("container_id")))
                {
                    Toast t = new Toast("Current folder has file with same name!" , 495 , 505);
                    t.showtoast();
                    return false;
                }
            }
        }
        if(!oldName.equals(fileName))
        {
            Storage.getInstance().updateFileName(fileID , fileName);
            
            HashMap  folderInfo = Storage.getInstance().loadFolder(pathLabel.getName());
            Folder f = new Folder((String)folderInfo.get("id") , (String)folderInfo.get("name") , (String)folderInfo.get("container_id") , (String)folderInfo.get("creation_date"));
            Authentication.online_user.getUserAccount().displayFiles(f);
        }
        return true;
        
    }
    
    
    
    public abstract void upload(String uploadPath) throws Exception;
    
    public static boolean shareFile(String receiver , String fileID) throws SQLException
    {
        if(receiver.isEmpty())
        {
            Toast t = new Toast("Enter receiver's email" , 495 , 505);
            t.showtoast();
            return false;
        }
        
        if(receiver.equals(Authentication.online_user.getEmail()))
        {
            Toast t = new Toast("Sender and receiver can't be same" , 495 , 505);
            t.showtoast();
            return false;
        }
        
        
        if(!Storage.getInstance().isUserRegistered(receiver))
        {
            Toast t = new Toast("Receiver's email is not found on DropBox" , 495 , 505);
            t.showtoast();
            return false;
        }
        
        Storage.getInstance().addShareFile(Authentication.online_user.getEmail() , receiver , fileID);
        return true;
    }
    
      
    
    
}
