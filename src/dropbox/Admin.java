/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dropbox;
import dropbox.GUI.AllusersUI;
import dropbox.GUI.GUI;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author apple
 */
public class Admin {
    private String adminName;
    public  String email;
    private String password;
    private Account adminAccount;

   
    public Admin(String userName , String email , String password)
    {
        this.adminName = userName;
        this.email = email;
        this.password = password;
        adminAccount = null;
    }        
    

    public Account getAdminAccount()
    {
        return this.adminAccount;
    }

    public String getAdminName() 
    {
        return this.adminName;
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
        
        //if(accType.toLowerCase().trim().equals("basic"))
           this.adminAccount = new Basic((String)accountInfo.get("id") , (String)accountInfo.get("root_folder_id")  , (int)accountInfo.get("no_of_files"));
        
//        else if(accType.toLowerCase().trim().equals("dropboxplus"))
//            this.adminAccount = new DropBoxPlus((String)accountInfo.get("id") , (String)accountInfo.get("root_folder_id")  , (int)accountInfo.get("no_of_files"));
//   
    }

    public ResultSet loadUsers() throws SQLException
     {

               ResultSet users = Storage.getInstance().loadAllUsers();        
                return users;
     }
    
    
}
