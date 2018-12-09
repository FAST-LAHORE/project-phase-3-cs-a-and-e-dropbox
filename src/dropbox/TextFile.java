package dropbox;

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


public class TextFile extends AbstractFile 
{
    public TextFile(String id , String name , String parentFolderID , String creationDate , String url)
    {
        super(id ,  name ,  parentFolderID ,  creationDate , url);  
    }
    
    public static boolean editTextFile(String oldName , String fileName , String currFolder , String fileID , String content) throws SQLException, IOException
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
        File cloudFile = new File("./src/CloudStorage/" + fileID + ".txt");
        if(cloudFile.exists())
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(cloudFile));
            writer.write(content);
            writer.close();
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
    
    public static boolean createTextFile(String fileName , String folderID)
    {
        try 
        {
            if(fileName.isEmpty())
            {
                Toast t = new Toast("Enter file name!" , 495 , 505);
                t.showtoast();
                return false;
            }
            
            ResultSet conatinerIDs = Storage.getInstance().loadContainerUsingFileName(fileName);
            while(conatinerIDs.next())
            {
                if(folderID.equals(conatinerIDs.getString("container_id")))
                {
                    Toast t = new Toast("Current folder has file with same name!" , 495 , 505);
                    t.showtoast();
                    return false;
                }
            }
            
            AbstractFile absFile = new TextFile(UUID.randomUUID().toString() , fileName , folderID , Folder.dateFormat.format(new Date()).toString() , "url");
            Storage.getInstance().saveFile(absFile.getId(), fileName, "text", absFile.getParentFolderID(), absFile.getUrl() , absFile.getCreationDate() , Authentication.online_user.getEmail());
        
            File cloudFile = new File("./src/CloudStorage/" + absFile.getId() + ".txt");
            if(!cloudFile.exists())
            {
                cloudFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(cloudFile));
                writer.write(contentArea.getText());
                writer.close();
            }
            
            HashMap  folderInfo = Storage.getInstance().loadFolder(folderID);
            Folder f = new Folder((String)folderInfo.get("id") , (String)folderInfo.get("name") , (String)folderInfo.get("container_id") , (String)folderInfo.get("creation_date"));
            Authentication.online_user.getUserAccount().displayFiles(f);
            return true;
            
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(FilesUI.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(FilesUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void upload(String uploadPath) throws Exception
    {
        Storage.getInstance().saveFile(id , name , "text" , parentFolderID , url , creationDate , Authentication.online_user.getEmail());

        InputStream is = new FileInputStream(new File(uploadPath));
        OutputStream os = new FileOutputStream(new File("./src/CloudStorage/" + id + ".txt"));

        byte[] buffer = new byte[3096];
        int length;
        while ((length = is.read(buffer)) > 0) {
          os.write(buffer, 0, length);
        }
        is.close();
        os.close();

        HashMap  folderInfo = Storage.getInstance().loadFolder(parentFolderID);
        Folder f = new Folder((String)folderInfo.get("id") , (String)folderInfo.get("name") , (String)folderInfo.get("container_id") , (String)folderInfo.get("creation_date"));
        Authentication.online_user.getUserAccount().displayFiles(f);

    }
    
    
    
}
