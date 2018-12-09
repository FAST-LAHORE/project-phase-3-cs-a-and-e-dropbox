package dropbox;

import java.sql.SQLException;


public class Basic extends Account
{
    public Basic(String id , String rootFolderId , int noOfFiles) throws SQLException
    {
        super(id , rootFolderId , noOfFiles);
    }
    
    public Basic(String id)
    {
        super(id);
    }
}
