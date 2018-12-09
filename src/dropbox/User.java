package dropbox;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;


public class User 
{
    private String userName;
    private String email;
    private String password;
    private Account userAccount;

    public User(String userName, String email, String password , String accType)
    {
        this.userName = userName;
        this.email = email;
        this.password = password;
        
        if(accType.toLowerCase().trim().equals("basic"))
           this.userAccount = new Basic(UUID.randomUUID().toString());
        
        else if(accType.toLowerCase().trim().equals("dropboxplus"))
            this.userAccount = new DropBoxPlus(UUID.randomUUID().toString());
    }
   
    public User(String userName , String email , String password)
    {
        this.userName = userName;
        this.email = email;
        this.password = password;
        userAccount = null;
    }        
    

    public Account getUserAccount()
    {
        return this.userAccount;
    }

    public String getUserName() 
    {
        return this.userName;
    }

    public String getEmail() 
    {
        return this.email;
    }

    public String getPassword() 
    {
        return this.password;
    }

    public void loadAccount(String accountID) throws SQLException
    {
        HashMap accountInfo = Storage.getInstance().loadAccount(accountID);
   
        String accType = (String)accountInfo.get("account_type");
        
        if(accType.toLowerCase().trim().equals("basic"))
           this.userAccount = new Basic((String)accountInfo.get("id") , (String)accountInfo.get("root_folder_id")  , (int)accountInfo.get("no_of_files"));
        
        else if(accType.toLowerCase().trim().equals("dropboxplus"))
            this.userAccount = new DropBoxPlus((String)accountInfo.get("id") , (String)accountInfo.get("root_folder_id")  , (int)accountInfo.get("no_of_files"));
   
    }

    public boolean updateEmail(String newEmail , String currentPassword) throws SQLException
    {
        if(currentPassword.equals(Authentication.online_user.getPassword()))
        {
            if(newEmail.contains("@") && !newEmail.isEmpty())
            {
                if(!Storage.getInstance().isUserRegistered(newEmail))
                {
                    Storage.getInstance().updateEmail(newEmail, Authentication.online_user.email);
                    Authentication.online_user.email = newEmail;        
                    return true;
                }
                else
                {
                    Toast t = new Toast("Email already in use!", 495, 505);
                    t.showtoast(); 
                    return false;    
                }
            }
            else
            {
                Toast t = new Toast("Invalid Email!", 495, 505);
                t.showtoast();
                return false;
            }
        }
        else
        {
            Toast t = new Toast("Wrong password!", 495, 505);
            t.showtoast();
            return false;
        }
               
    }
    
    public boolean updatePassword(String newPassword , String currentPassword) throws SQLException
    {
        if(currentPassword.equals(Authentication.online_user.getPassword()))
        {
            if(newPassword.length() >= 6)
            {
                Storage.getInstance().updatePassword(newPassword, Authentication.online_user.email);
                Authentication.online_user.password = newPassword;        
                return true;
            }
            else
            {
                Toast t = new Toast("Password should consist of atleast six characters!", 495, 505);
                t.showtoast();
                return false;
            }
        }
        else
        {
            Toast t = new Toast("Wrong password!", 495, 505);
            t.showtoast();
            return false;
        }
    }
    
    public boolean updateUserName(String newUserName , String currentPassword) throws SQLException
    {
        if(currentPassword.equals(Authentication.online_user.getPassword()))
        {
            if(!newUserName.isEmpty())
            {
                Storage.getInstance().updateUserName(newUserName, Authentication.online_user.email);
                Authentication.online_user.userName = newUserName;        
                return true;
            }
            else
            {
                Toast t = new Toast("Enter username!", 495, 505);
                t.showtoast();
                return false;
            }
        }
        else
        {
            Toast t = new Toast("Wrong password!", 495, 505);
            t.showtoast();
            return false;
        }
    }
    
    
    
    
}
