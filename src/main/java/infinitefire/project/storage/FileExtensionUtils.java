package infinitefire.project.storage;

public class FileExtensionUtils {
	public static final String PNG = "png";
	public static final String JPG = "jpg";
	public static final String TXT = "txt";
	public static final String HTML = "html";
	public static final String JS = "js";
	public static final String ZIP = "zip";
	
	
	public static String checkFileExtension(String fullFilename) {
		if (fullFilename.isEmpty())
			return "none";

		String[] sFileName = fullFilename.split("\\.");
		if (sFileName.equals("") || sFileName == null)
			return "none";
		if (sFileName.length < 1)
			return "none";
		String extension = sFileName[1];
		
		if (extension == null) 
			return "none";
		if (extension.equals(JPG))
			return JPG;
		if (extension.equals(PNG))
			return PNG;
		if (extension.equals(TXT))
			return TXT;
		if (extension.equals(HTML))
			return HTML;
		if (extension.equals(JS))
			return JS;
		if (extension.equals(ZIP))
			return ZIP;
		
		return "none";
	}
	
	public static String getFileExtension(String fullFilename) {
		return "." + checkFileExtension(fullFilename);
	}
}
