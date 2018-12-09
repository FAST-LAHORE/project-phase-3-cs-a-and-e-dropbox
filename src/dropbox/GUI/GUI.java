package dropbox.GUI;
import static com.sun.org.apache.bcel.internal.util.SecuritySupport.getResourceAsStream;
import javax.swing.JFrame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


public class GUI
{
    JFrame frame;
    Container container;
    JPanel jp;
    private static GUI instance = null;

    private GUI()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = frame.getContentPane();
        container.setLayout(new GridLayout());
    }
    
    public static GUI getForm()
    {
        if(instance == null)
            instance = new GUI();
            
         return instance;
    }
   
    
    public void loadPanel(String panelType)
    {
        
        if(panelType.toLowerCase().equals("login"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            instance.jp=new LoginUI();
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        else if(panelType.toLowerCase().equals("signup"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            instance.jp=new SignupUI();
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        else if(panelType.toLowerCase().equals("profile"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            instance.jp=new ProfileUI();
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        else if(panelType.toLowerCase().equals("splash"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            instance.jp=new SplashUI();
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        else if(panelType.toLowerCase().equals("updateinfo"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            instance.jp=new UpdateAccountInfoUI();
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        else if(panelType.toLowerCase().equals("files"))
        {
            if(instance.container.getComponentCount() > 0)
                instance.container.removeAll();
         
            try {
                instance.jp=new FilesUI();
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            instance.container.add(instance.jp);
            instance.frame.setContentPane(instance.container);
            
        }
        
        
     
        
        
        
    }
    public JPanel get_panel()
    {
        return instance.jp;
    }
    
    public void openForm()
    {
        instance.frame.setSize(820, 660);
        instance.frame.setIconImage(Toolkit.getDefaultToolkit().getImage("./src/dropbox/images/dropbox_logo.PNG"));
        instance.frame.setResizable(false);
        instance.frame.setVisible(true);
    }
}
