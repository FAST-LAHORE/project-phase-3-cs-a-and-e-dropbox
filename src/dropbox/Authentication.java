package dropbox;

import dropbox.GUI.GUI;
import java.sql.SQLException;
import java.util.HashMap;


public class Authentication 
{
    private static Authentication auth_obj = null;
    public static User online_user = null;
    
    private Authentication(){}
    
    public static Authentication getInstance()
    {
        if(auth_obj == null)
            auth_obj = new Authentication();
        return auth_obj;
    }

    public void login(String email, String password) throws SQLException 
    {
        if(checkLoginConstraints(email , password))
        {
            if(!Storage.getInstance().isUserRegistered(email))
            {
               Toast t = new Toast("You are not registered", 495, 505);
               t.showtoast();
            }
            else
            {
                HashMap userInfo = Storage.getInstance().loadUserOnly(email);
                if(matchPasswords((String)userInfo.get("password") , password))
                {
                    online_user = new User((String)userInfo.get("username") , email , (String)userInfo.get("password"));
                    online_user.loadAccount((String)userInfo.get("accountID"));
                    GUI.getForm().loadPanel("profile");
                    online_user.getUserAccount().displayProfile();
                }
                else
                {
                    Toast t = new Toast("Invalid Password", 495, 505);
                    t.showtoast();
                }
            }
        }    
    }
    
    private boolean matchPasswords(String passwordInStorage , String passwordEntered)
    {
        return (passwordInStorage.equals(passwordEntered));
    }
    
    private boolean checkLoginConstraints(String email , String password)
    {
        if(email.isEmpty() || !email.contains("@"))
        {
            Toast t = new Toast("Enter valid email!", 495, 505);
            t.showtoast(); 
            return false;
        }
        
        if(password.isEmpty())
        {
            Toast t = new Toast("Enter a password!", 495, 505);
            t.showtoast();
            return false;
        }
        
        return true;
    }
            
    
}