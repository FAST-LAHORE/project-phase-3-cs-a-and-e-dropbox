package dropbox;


public class AbstractFileFactory
{
    
    private static AbstractFileFactory instance = null;
    
    
    public enum FileType 
    { 
        IMAGE , VIDEO , TEXT , PDF; 
    } 
    
    private AbstractFileFactory(){}
    
    
    public static AbstractFileFactory getInstance()
    {
        if(instance == null)
            instance = new AbstractFileFactory();
        
        return instance;        
    }
    
    
    public AbstractFile getFile(FileType type , String randomID , String name , String parentFolderID , String date , String url)
    {
        if(type.equals(FileType.VIDEO))
            return new VideoFile(randomID , name , parentFolderID , date , url);

        else if(type.equals(FileType.TEXT))
            return new TextFile(randomID , name , parentFolderID , date , url);

        else if(type.equals(FileType.PDF))
            return  new PdfFile(randomID , name , parentFolderID , date , url);

        else if(type.equals(FileType.IMAGE))
            return new ImageFile(randomID , name , parentFolderID , date , url);

        return null;
   }
    
    
    
}
