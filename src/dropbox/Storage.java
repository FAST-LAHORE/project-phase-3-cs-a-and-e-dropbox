package dropbox;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class Storage 
{
    private static String DB_URL = "jdbc:derby://localhost:1527/DropBox.db";
    private static String DB_USER_NAME = "user123";
    private static String PASSWORD = "user123";
    private static String USERS_TABLE = "users";
    private static String ACCOUNT_TABLE = "account";
    private static String FOLDER_TABLE = "folder";
    private static String FILE_TABLE = "file";
    
    
    
    
    private Connection dbCon;
    private Statement dbStatment;
    
    private static Storage storage;
    
    private Storage() throws SQLException 
    {
        dbCon = DriverManager.getConnection(DB_URL , DB_USER_NAME , PASSWORD);
        dbStatment = dbCon.createStatement();
    }
    
    public static Storage getInstance() throws SQLException
    {
        if(storage == null)
            storage = new Storage();
        
        return storage;
    }
    
    public boolean isUserRegistered(String email) throws SQLException
    {
        String sql = "SELECT email FROM " + Storage.USERS_TABLE + " WHERE email = \'" + email + "\'";
        ResultSet users = storage.dbStatment.executeQuery(sql);
         
        return (users.next());
    }
 
   
    public void saveUserInitial(User user) throws SQLException
    {
        String userName = user.getUserName();
        String email = user.getEmail();
        String password = user.getPassword();
        Account userAccount = user.getUserAccount();
        
        String sql = "INSERT INTO " + Storage.USERS_TABLE + " VALUES (" + "\'" + email + " \'" + "," + "\'" + userName + "\'" + "," + "\'" + password + "\'" + "," + "\'" + userAccount.getId() + "\')";
        storage.dbStatment.executeUpdate(sql);
        
        
        String rootFolderId = userAccount.getRootFolder().getId();
        int noOfFiles = userAccount.getNoOfFiles(); 
        String accountType = userAccount.getClass().getSimpleName();
       
        
        String sql1 = "INSERT INTO " + Storage.ACCOUNT_TABLE + " VALUES (" + "\'" + userAccount.getId() + " \'" + "," + "\'" + rootFolderId + "\'" + ","  + noOfFiles + "," + "\'" + accountType + "\')";
        storage.dbStatment.executeUpdate(sql1);

        String folderName = userAccount.getRootFolder().getName();
        String creationDate = userAccount.getRootFolder().getCreationDate();
        
        String sql2 = "INSERT INTO " + Storage.FOLDER_TABLE + " VALUES (" + "\'" + rootFolderId + " \'" + "," + "\'" + folderName + "\'" + "," + "\'" + creationDate + "\' , null)";
        storage.dbStatment.executeUpdate(sql2);
    
    }
    
    public HashMap loadUserOnly(String email) throws SQLException
    {
        String sql = "SELECT * FROM " + Storage.USERS_TABLE + " WHERE email = \'" + email + "\'";
        ResultSet userOnly = storage.dbStatment.executeQuery(sql); 
        userOnly.next();
        
        HashMap map = new HashMap();
        map.put("email" , userOnly.getString("email"));
        map.put("password" , userOnly.getString("password"));
        map.put("username" , userOnly.getString("username"));
        map.put("accountID" , userOnly.getString("account_id"));
        
        return map;
        
    } 
    
    public HashMap loadAccount(String accountID) throws SQLException
    {
        String sql = "SELECT * FROM " + Storage.ACCOUNT_TABLE + " WHERE id = \'" + accountID + "\'";
        ResultSet account = storage.dbStatment.executeQuery(sql); 
        account.next();
        
        HashMap map = new HashMap();
        map.put("root_folder_id" , account.getString("root_folder_id"));
        map.put("no_of_files" , account.getInt("no_of_files"));
        map.put("account_type" , account.getString("account_type"));
        map.put("id" , account.getString("id"));
        
        return map;
        
    }
    
    
    public HashMap loadFolder(String folderID) throws SQLException
    {
        String sql = "SELECT * FROM " + Storage.FOLDER_TABLE + " WHERE id = \'" + folderID + "\'";
        ResultSet folder = storage.dbStatment.executeQuery(sql); 
        folder.next();
        
        HashMap map = new HashMap();
        map.put("name" , folder.getString("name"));
        map.put("creation_date" , folder.getString("creation_date"));
        map.put("container_id" , folder.getString("container_id"));
        map.put("id" , folder.getString("id"));
        
        return map;
        
    }
    
    public void updateEmail(String setEmail , String whereEmail) throws SQLException           
    {
        String sql = "UPDATE " + Storage.USERS_TABLE + " SET email = \'" + setEmail + "\' WHERE email = \'" + whereEmail + "\'";
        storage.dbStatment.executeUpdate(sql);
    }
    
    
    public void updatePassword(String setPassword , String whereEmail) throws SQLException           
    {
        String sql = "UPDATE " + Storage.USERS_TABLE + " SET password = \'" + setPassword + "\' WHERE email = \'" + whereEmail + "\'";
        storage.dbStatment.executeUpdate(sql);
    }
    
    public void updateUserName(String setUserName , String whereEmail) throws SQLException           
    {
        String sql = "UPDATE " + Storage.USERS_TABLE + " SET username = \'" + setUserName + "\' WHERE email = \'" + whereEmail + "\'";
        storage.dbStatment.executeUpdate(sql);
    }
    
    public ResultSet loadFiles(String containerID) throws SQLException
    {
       String sql = "SELECT * FROM " + Storage.FILE_TABLE + " WHERE container_id = \'" + containerID + "\'";
       return storage.dbStatment.executeQuery(sql);
    }
    
    public ResultSet loadFoldersIDs(String containerID) throws SQLException
    {
       String sql = "SELECT id FROM " + Storage.FOLDER_TABLE + " WHERE container_id = \'" + containerID + "\'";
       return storage.dbStatment.executeQuery(sql);
    }
    
    public String loadFolderName(String folderID) throws SQLException
    {
        String sql = "SELECT name FROM " + Storage.FOLDER_TABLE + " WHERE id = \'" + folderID + "\'";
        ResultSet r = storage.dbStatment.executeQuery(sql);
        r.next();
        return r.getString("name");
    }
    
   
    public String loadContainerID(String folderID) throws SQLException
    {
        String sql = "SELECT container_id FROM " + Storage.FOLDER_TABLE + " WHERE id = \'" + folderID + "\'";
        ResultSet r = storage.dbStatment.executeQuery(sql);
        r.next();
        return r.getString("container_id");
    }
    
    public void saveFile(String id , String name , String type , String containerID , String url , String creationDate , String creator) throws SQLException
    {
        
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(Storage.FILE_TABLE);
        builder.append(" VALUES (");
       
        builder.append("\'");
        builder.append(id);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(name);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(type);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(containerID);
        builder.append("\' , ");
        
        
        builder.append("\'");
        builder.append(url);
        builder.append("\' , ");
        
        
        builder.append("\'");
        builder.append(creationDate);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(creator);
        builder.append("\')");
        
        
        storage.dbStatment.executeUpdate(builder.toString());
    }
    
    public void deleteFile(String fileID) throws SQLException           
    {   
        String type = loadFileType(fileID);
        String sql = "DELETE FROM " + Storage.FILE_TABLE + " WHERE id = \'" + fileID + "\'";
        storage.dbStatment.executeUpdate(sql);
        
        String path = "./src/CloudStorage/" + fileID;
        
        if(type.equals("text"))
            path += ".txt";
        
        else if(type.equals("pdf"))
            path += ".pdf";
        
        else if(type.equals("image"))
            path += ".png";
        
        else if(type.equals("video"))
            path += ".mp4";
        
        File file = new File(path);
        try
        {
            file.delete();
           // Toast t=new Toast("File Deleted Successfully",495,505);
           // t.showtoast();
        }
        catch(Exception e)
        {
            Toast t=new Toast(e.getMessage(),495,505);
            t.showtoast();
        }
    }
    
    public String loadFileType(String fileID) throws SQLException
    {
       String sql = "SELECT type FROM " + Storage.FILE_TABLE + " WHERE id = \'" + fileID + "\'";
       ResultSet type = storage.dbStatment.executeQuery(sql);
       type.next();
       return type.getString("type");
    }
    
    
    public String loadFileName(String fileID) throws SQLException
    {
       String sql = "SELECT name FROM " + Storage.FILE_TABLE + " WHERE id = \'" + fileID + "\'";
       ResultSet name = storage.dbStatment.executeQuery(sql);
       name.next();
       return name.getString("name");
    }
            
            
    
    public ResultSet loadContainerUsingFileName(String fileName) throws SQLException
    {
       String sql = "SELECT container_id FROM " + Storage.FILE_TABLE + " WHERE name = \'" + fileName + "\'";
       return storage.dbStatment.executeQuery(sql);
    }
    
    public void updateFileName(String fileID , String newName) throws SQLException
    {
        String sql = "UPDATE " + Storage.FILE_TABLE + " SET name = \'" + newName + "\' WHERE id = \'" + fileID + "\'";
        storage.dbStatment.executeUpdate(sql);
    }
    
    public void saveFolder(String id , String name , String creation_date , String container_id) throws SQLException
    {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(Storage.FOLDER_TABLE);
        builder.append(" VALUES (");
       
        builder.append("\'");
        builder.append(id);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(name);
        builder.append("\' , ");
        
        builder.append("\'");
        builder.append(creation_date);
        builder.append("\' , ");
        
        
        builder.append("\'");
        builder.append(container_id);
        builder.append("\')");
        
        storage.dbStatment.executeUpdate(builder.toString());
        
    }
    
}
