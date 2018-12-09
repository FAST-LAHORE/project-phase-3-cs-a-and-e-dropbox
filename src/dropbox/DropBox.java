package dropbox;

import dropbox.GUI.GUI;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBox
{
    public static void main(String[] args) throws IOException
    {
       
        GUI gui = GUI.getForm();
        gui.loadPanel("splash");
        gui.openForm();
        
        long t= System.currentTimeMillis();
        long end = t+2000;
        while(System.currentTimeMillis() < end); 
        
        gui.loadPanel("login");
    }
    
}
