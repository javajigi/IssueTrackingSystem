package infinitefire.project.storage;

public interface FileExtensionUtils {
	public static final String PNG = "png";
	public static final String TXT = "txt";
	public static final String HTML = "html";
	public static final String JS = "js";
	
	public static String checkFileExtension(String fullFilename) {
		String[] sFileName = fullFilename.split(".");
		String extension = sFileName[1];
		
		if (extension == null) 
			return "none";
		if (extension.equals(PNG))
			return PNG;
		if (extension.equals(TXT))
			return TXT;
		if (extension.equals(HTML))
			return HTML;
		if (extension.equals(JS))
			return JS;
		
		return "none";
	}
}
