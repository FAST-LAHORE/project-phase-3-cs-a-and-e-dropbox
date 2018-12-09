package dropbox;

import java.sql.SQLException;


public class DropBoxPlus extends Account
{
 
    public DropBoxPlus(String id , String rootFolderId , int noOfFiles) throws SQLException
    {
        super(id , rootFolderId , noOfFiles);
    }
    
    public DropBoxPlus(String id)
    {
        super(id);
    }
    
}
