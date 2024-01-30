package sunuser.mylife.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sunuser.mylife.exception.StorageException;
import sunuser.mylife.property.StorageProperties;

@Service
public class FileStorageService {

	private Path rootLocation;

	public FileStorageService(StorageProperties storageProperties) {
		this.rootLocation = Paths.get(storageProperties.getLocation());
	}

	public String store(MultipartFile file) {
		if (file.isEmpty()) {
			throw new StorageException("File should not be empty.");
		}
		String filename = file.getOriginalFilename();

		Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
				.normalize().toAbsolutePath();

		if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
			throw new StorageException("Incorrent store path");
		}

		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new StorageException("exception occured when storing a file");
		}

		return filename;
	}

	public Resource loadAsResource(String filename) {
		try {
			Path filePath = rootLocation.resolve(filename).normalize().toAbsolutePath();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new RuntimeException("File not found: " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new RuntimeException("File not found: " + filename);
		}
	}
}
