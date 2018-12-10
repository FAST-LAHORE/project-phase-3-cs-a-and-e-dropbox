package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImageFile extends AbstractFile 
{
    public ImageFile(String id , String name , String parentFolderID , String creationDate , String url)
    {
        super(id ,  name ,  parentFolderID ,  creationDate , url);  
    }
    
    
    public void upload(String uploadPath) throws Exception
    {
        Storage.getInstance().saveFile(id , name , "image" , parentFolderID , url , creationDate , Authentication.online_user.getEmail());

        InputStream is = new FileInputStream(new File(uploadPath));
        OutputStream os = new FileOutputStream(new File("./src/CloudStorage/" + id + ".png"));

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

    public void download(String receivedID)
    {
        try
        {
            Storage.getInstance().saveFile(id, name,"image" , parentFolderID , url ,  creationDate, Authentication.online_user.getEmail());
            InputStream is = new FileInputStream(new File("./src/CloudStorage/" + receivedID + ".png"));
            OutputStream os = new FileOutputStream(new File("./src/CloudStorage/" + id + ".png"));

            byte[] buffer = new byte[3096];
            int length;
            while ((length = is.read(buffer)) > 0) {
              os.write(buffer, 0, length);
            }
            is.close();
            os.close();

        }
        catch (SQLException ex)
        {
            Logger.getLogger(TextFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
