package dropbox;


import dropbox.GUI.FilesUI;
import static dropbox.GUI.FilesUI.contentArea;
import static dropbox.GUI.FilesUI.filePanel;
import static dropbox.GUI.FilesUI.pathLabel;
import dropbox.GUI.GUI;
import dropbox.GUI.ProfileUI;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.awt.font.TextAttribute.FONT;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar.Separator;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;


public abstract class Account
{
    private String id;
    private Folder rootFolder;
    private int noOfFiles;
    ProfileUI profilePanel;
   
    public Account(String id , String rootFolderId , int noOfFiles) throws SQLException
    {
        this.id = id;
        this.noOfFiles = noOfFiles;
        loadRootFolderInfo(rootFolderId);
    }

    public Account(String id)
    {
        this.id = id;
        this.noOfFiles = 0;
        this.rootFolder = new Folder(UUID.randomUUID().toString() , "root" , null); 
    }
    
    public String getId()
    {
        return id;
    }

    public Folder getRootFolder()
    {
        return rootFolder;
    }
    
    public int getNoOfFiles()
    {
        return noOfFiles;
    }
    
    public void displayProfile()
    {
        profilePanel = (ProfileUI) GUI.getForm().get_panel();
        profilePanel.setProfileAttributes(Authentication.online_user.getUserName());
               
    }
    
    private void loadRootFolderInfo(String rootFolderId) throws SQLException
    {
        HashMap rootFolderInfo = Storage.getInstance().loadFolder(rootFolderId);
        
        String id = (String) rootFolderInfo.get("id");
        String creationDate = (String) rootFolderInfo.get("creation_date");
        
        this.rootFolder = new Folder(id , "root" , null , creationDate);
                 
    } 
    
    
    public void displayFiles(Folder folder) throws SQLException
    {
        if(FilesUI.filePanel.getComponentCount() > 0)
            FilesUI.filePanel.removeAll();
        
        FilesUI.pathLabel.setText(folder.getName());
        FilesUI.pathLabel.setName(folder.getId());
        
        folder.loadFilesList();
        folder.loadFoldersIDs();
        
        int rows = folder.getFoldersIDs().size() + folder.getFilesList().size(); 
        rows *= 2;
        JPanel subPanel = new JPanel();
        if(rows < 10)
            rows = 10;
        subPanel.setLayout(new GridLayout(rows , 2 , 20 , 0));
        
        for(String folderID : folder.getFoldersIDs())
        {
            JPanel panelContainer = new JPanel(new GridLayout(1 , 4));
            panelContainer.setBackground(Color.WHITE);

            JLabel icon = new JLabel();    
            icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/folder_logo.PNG")));
            panelContainer.add(icon);

            JLabel name = new JLabel(Storage.getInstance().loadFolderName(folderID));
            name.setName(folderID);
            panelContainer.add(name);

            
            panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));

            panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));

            
            panelContainer.setBackground(Color.WHITE);
            subPanel.add(panelContainer);

            JLabel line = new JLabel("  __________________________________________________________________________  ");
            line.setForeground(new Color(230 , 232 , 235));
            subPanel.add(line);

        }
        
        
        
        for(AbstractFile file : folder.getFilesList())
        {
            String type = file.getClass().getSimpleName().toLowerCase();
            
            if(type.equals("pdffile"))
            {
                JPanel panelContainer = new JPanel(new GridLayout(1 , 4));
                panelContainer.setBackground(Color.WHITE);
        
                JLabel icon = new JLabel();    
                icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/pdf_logo.PNG")));
                panelContainer.add(icon);
                
                JLabel name = new JLabel(file.getName() + ".pdf");
                name.setName(file.getId());
                panelContainer.add(name);
              
                
                panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));
                
                JMenuBar bar = new JMenuBar();
                bar.setPreferredSize(new Dimension(10 , 10));
                bar.setLocation(10 ,10 );
                bar.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
                bar.setBackground(Color.WHITE);
                
                JMenu optionsMenu = new JMenu();
                optionsMenu.setBackground(Color.WHITE);
                optionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/option_logo.PNG")));
                
                JMenuItem editItem = new JMenuItem("Edit");
                editItem.setActionCommand(file.getId());
                editItem.setBackground(Color.WHITE);
                editItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            String fileID = e.getActionCommand();
                            FilesUI.displayEditNameDialog(fileID);
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                optionsMenu.add(editItem);
               
                
                bar.add(optionsMenu);
                panelContainer.add(bar);
              
                panelContainer.setBackground(Color.WHITE);
                subPanel.add(panelContainer);
                
                JLabel line = new JLabel("  __________________________________________________________________________  ");
                line.setForeground(new Color(230 , 232 , 235));
                subPanel.add(line);
                
            }
            ///
            else if(type.equals("textfile"))
            {
                JPanel panelContainer = new JPanel(new GridLayout(1,4));
                panelContainer.setBackground(Color.WHITE);
        
                JLabel icon = new JLabel();    
                icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/text_logo.PNG")));
                panelContainer.add(icon);
               
                
                JLabel name = new JLabel(file.getName() + ".txt");
                name.setName(file.getId());
                panelContainer.add(name);
                
                panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));
                
                JMenuBar bar = new JMenuBar();
                bar.setPreferredSize(new Dimension(10 , 10));
                bar.setLocation(10 ,10 );
                bar.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
                bar.setBackground(Color.WHITE);
                
                JMenu optionsMenu = new JMenu();
                optionsMenu.setBackground(Color.WHITE);
                optionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/option_logo.PNG")));
                
                JMenuItem editItem = new JMenuItem("Edit");
                editItem.setActionCommand(file.getId());
                editItem.setBackground(Color.WHITE);
                editItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            String fileID = e.getActionCommand();
                            String contents = AbstractFile.getFileContent(fileID);
                            FilesUI.displayContents(contents , fileID);
                            
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                optionsMenu.add(editItem);
                
                
                
                bar.add(optionsMenu);
                panelContainer.add(bar);
                
                panelContainer.setBackground(Color.WHITE);
                subPanel.add(panelContainer);
                
                JLabel line = new JLabel("  __________________________________________________________________________  ");
                line.setForeground(new Color(230 , 232 , 235));
                subPanel.add(line);
        
            }
            else if(type.equals("videofile"))
            {                
                JPanel panelContainer = new JPanel(new GridLayout(1 , 4));
                panelContainer.setBackground(Color.WHITE);
        
                JLabel icon = new JLabel();    
                icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/video_file.PNG")));
                panelContainer.add(icon);
                
                JLabel name = new JLabel(file.getName() + ".mp4");
                name.setName(file.getId());
                panelContainer.add(name);
                
                
                panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));
                
                JMenuBar bar = new JMenuBar();
                bar.setPreferredSize(new Dimension(10 , 10));
                bar.setLocation(10 ,10 );
                bar.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
                bar.setBackground(Color.WHITE);
                
                JMenu optionsMenu = new JMenu();
                optionsMenu.setBackground(Color.WHITE);
                optionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/option_logo.PNG")));
                
                JMenuItem editItem = new JMenuItem("Edit");
                editItem.setActionCommand(file.getId());
                editItem.setBackground(Color.WHITE);
                editItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            String fileID = e.getActionCommand();
                            FilesUI.displayEditNameDialog(fileID);
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                optionsMenu.add(editItem);
                
                bar.add(optionsMenu);
                panelContainer.add(bar);
               
                panelContainer.setBackground(Color.WHITE);
                subPanel.add(panelContainer);
                
                JLabel line = new JLabel("  __________________________________________________________________________  ");
                line.setForeground(new Color(230 , 232 , 235));
                subPanel.add(line);
            }
            else if(type.equals("imagefile"))
            {
                
                JPanel panelContainer = new JPanel(new GridLayout(1 , 4));
                panelContainer.setBackground(Color.WHITE);
        
                JLabel icon = new JLabel();    
                icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/img_file.PNG")));
                panelContainer.add(icon);
                
                JLabel name = new JLabel(file.getName() + ".png");
                name.setName(file.getId());
                panelContainer.add(name);
                
                
                panelContainer.add(Box.createRigidArea(new Dimension(5 , 0)));
                
                JMenuBar bar = new JMenuBar();
                bar.setPreferredSize(new Dimension(10 , 10));
                bar.setLocation(10 ,10 );
                bar.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
                bar.setBackground(Color.WHITE);
                
                JMenu optionsMenu = new JMenu();
                optionsMenu.setBackground(Color.WHITE);
                optionsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dropbox/images/option_logo.PNG")));
                
                JMenuItem editItem = new JMenuItem("Edit");
                editItem.setActionCommand(file.getId());
                editItem.setBackground(Color.WHITE);
                editItem.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        try
                        {
                            String fileID = e.getActionCommand();
                            FilesUI.displayEditNameDialog(fileID);
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
                optionsMenu.add(editItem);
                
                
                
                
                bar.add(optionsMenu);
                panelContainer.add(bar);
               
                panelContainer.setBackground(Color.WHITE);
                subPanel.add(panelContainer);
                
                JLabel line = new JLabel("  __________________________________________________________________________  ");
                line.setForeground(new Color(230 , 232 , 235));
                subPanel.add(line);
            }
            
        }
        
        JScrollPane scroll = new JScrollPane(subPanel , ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 0));
        subPanel.setBackground(Color.WHITE);
        
        scroll.setBounds(0, 0, 534, 418);
        FilesUI.filePanel.setPreferredSize(new Dimension(534, 418));
        FilesUI.filePanel.add(scroll);
        FilesUI.filePanel.revalidate();
    }
    
    
    
    
    
    
}
