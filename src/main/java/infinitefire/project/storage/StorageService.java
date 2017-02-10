package infinitefire.project.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import infinitefire.project.web.UserController;

@Service
public class StorageService {
	@Value("${file.upload_path}")
	private String basicDir;
	
	@Value("${file.upload_path_profile}")
	private String profileDir;
	
	@Value("${file.upload_Path_attachment}")
	private String attachmentDir;
	
	private Path rootLocation;
	private Path profilePath;
	private Path attachmentPath;
	
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(basicDir);
        this.profilePath = Paths.get(basicDir + profileDir);
        this.attachmentPath = Paths.get(basicDir + attachmentDir);
        LogManager.getRootLogger().setLevel(Level.DEBUG);
        
        try {
        	Files.createDirectories(rootLocation);
        	Files.createDirectories(profilePath);
        	Files.createDirectories(attachmentPath);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        log.info("basicDir : " + basicDir);
    }

    public void store(MultipartFile file, String newFileName, FileType type) {
    	if (type.isProfile()) {
		    try {
	            if (file.isEmpty()) {
	                throw new StorageException("Failed to store empty file " + newFileName);
	            }
	            Files.copy(file.getInputStream(), this.profilePath.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            throw new StorageException("Failed to store file " + newFileName, e);
	        }
    	}
    	if (type.isAttachment()) {
    		try {
    			if (file.isEmpty()) {
    				throw new StorageException("Failed to store empty file " + newFileName);
    			}
    			Files.copy(file.getInputStream(), this.attachmentPath.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
    		} catch (IOException e) {
	            throw new StorageException("Failed to store file " + newFileName, e);
	        }
    	}
    }
    
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename, FileType type) {
    	log.debug("filename : " + filename);
    	Resource resource = null;
    	
    	if (type.isProfile()) {
	        try {
	        	Path file = Paths.get(basicDir + profileDir + filename);
	            log.debug("file: " + file);
	            resource = new UrlResource(file.toUri());
	            if(resource.exists() || resource.isReadable()) {
	                return resource;
	            }
	            else {
	                throw new StorageFileNotFoundException("Could not read file : " + filename);
	
	            }
	        } catch (MalformedURLException e) {
	            throw new StorageFileNotFoundException("Could not read file : " + filename, e);
	        }
    	}
    	if (type.isAttachment()) {
    		//Path file = Paths.get(basicDir + attachmentDir + filename);
    	}
    	return resource;
    }

    public void deleteFile(String path) {
    	File file = new File(path);
    	if (file.exists())
    		file.delete();
    }
    
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
