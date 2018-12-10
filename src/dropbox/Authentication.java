package dropbox;

import dropbox.GUI.GUI;
import java.sql.SQLException;
import java.util.HashMap;


public class Authentication 
{
    private static Authentication auth_obj = null;
    public static User online_user = null;
    public static Admin online_admin = null;
    
    
    private Authentication(){}
    
    public static Authentication getInstance()
    {
        if(auth_obj == null)
            auth_obj = new Authentication();
        return auth_obj;
    }
    
    public void signup(String userName , String email , String password , String accountType) throws SQLException
    {
        Storage storage = Storage.getInstance();
        if(checkSignupConstraints(userName , email , password , accountType))
        {
            if(!storage.isUserRegistered(email))
            {
                User user = new User(userName , email , password , accountType);
                storage.saveUserInitial(user);
                online_user = user;
                GUI.getForm().loadPanel("profile");
                online_user.getUserAccount().displayProfile();
            }
            else
            {
                Toast t = new Toast("Email already in use!", 495, 505);
                t.showtoast(); 
            }
        }
    }
  
    private boolean checkSignupConstraints(String userName , String email , String password , String accountType)
    {   
        if(!userName.isEmpty())
        {
            if(email.contains("@") && !email.isEmpty())
            {
                if(password.length() >= 6)
                {
                    
                    if(accountType.isEmpty())
                    {
                        Toast t = new Toast("Select account type!", 495, 505);
                        t.showtoast(); 
                        return false;
                    }
                    
                    return true;
                }
                else
                {
                   Toast t = new Toast("Password should consist of atleast six characters!", 425, 505);
                   t.showtoast();
                   return false;
                }
            }   
            else 
            {
                 Toast t = new Toast("Enter valid email!", 495, 505);
                 t.showtoast(); 
                 return false;
            }
        }
        else
        {
            
            Toast t = new Toast("Please enter username!", 495, 505);
            t.showtoast(); 
            return false;
        }    
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
    
     public void Adminlogin(String email, String password) throws SQLException 
    {

        if(checkLoginConstraints(email , password))
        { 
            if(email.equals("admin@gmail.com") &&  password.equals("123456") )
            {      System.out.println(password + "   "+  email);

              online_user=null;
              online_admin= new Admin("admin" , email , password);
            GUI.getForm().loadPanel("admin");
            
            }
            else if (email=="admin@gmail.com")
             {
              Toast t = new Toast("Invalid Password", 495, 505);
               t.showtoast();
             }
            else
            {
                Toast t = new Toast("You are not admin", 495, 505);
               t.showtoast();
            }
        }    
    }
    
}