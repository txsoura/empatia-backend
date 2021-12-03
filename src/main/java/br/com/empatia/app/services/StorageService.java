package br.com.empatia.app.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class StorageService {
    private final static String root = System.getProperty("user.dir") + "/storage/app/public";
    private static String storageDriver;
    private static String bucket;
    private static long urlExpiration;
    private static String key;
    private static String secret;
    private static String region;
    private final AmazonS3 amazonS3;

    @Autowired
    public StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public static Object get(String filename) {
        if (storageDriver.equalsIgnoreCase("local")) {
            return getLocal(filename);
        } else {
            return getCloud(filename);
        }
    }

    private static Resource getLocal(String filename) {
        try {
            Path file = Paths.get(root, filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    private static URL getCloud(String filename) {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(key, secret);
        AmazonS3 aws = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * urlExpiration;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, filename)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return aws.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @Value("${storage.default.driver}")
    public void setStorageDriver(String driver) {
        StorageService.storageDriver = driver;
    }

    @Value("${storage.aws.s3.bucket}")
    public void setBucket(String bucket) {
        StorageService.bucket = bucket;
    }

    @Value("${storage.aws.s3.url.expiration}")
    public void setBucket(long expiration) {
        StorageService.urlExpiration = expiration;
    }

    @Value("${storage.aws.s3.access.key.id}")
    public void setKey(String key) {
        StorageService.key = key;
    }

    @Value("${storage.aws.s3.secret.access.key}")
    public void setSecret(String secret) {
        StorageService.secret = secret;
    }

    @Value("${storage.aws.s3.default.region}")
    public void setRegion(String region) {
        StorageService.region = region;
    }

    public void init() {
        File directory = new File(root);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Could not initialize storage folder");
            }
        }
    }

    public String save(String directory, MultipartFile file) {
        if (storageDriver.equalsIgnoreCase("local")) {
            return this.saveLocal(directory, file);
        } else {
            return this.saveCloud(directory, file);
        }
    }

    public String save(String directory, MultipartFile file, String fileName) {
        if (storageDriver.equalsIgnoreCase("local")) {
            return this.saveLocal(directory, file, fileName);
        } else {
            return this.saveCloud(directory, file, fileName);
        }
    }

    private String saveLocal(String directory, MultipartFile file) {
        Path directoryPath = Paths.get(root, directory);
        Path filePath = directoryPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Could not save the file");
        }

        return directory + "/" + file.getOriginalFilename();
    }

    private String saveLocal(String directory, MultipartFile file, String fileName) {
        Path directoryPath = Paths.get(root, directory);
        String extension = this.getExtensionByStringHandling(file.getOriginalFilename()).orElseThrow(() -> new RuntimeException("Invalid file extension"));
        Path filePath = directoryPath.resolve(fileName + "." + extension);

        try {
            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Could not save the file");
        }

        return directory + "/" + fileName + "." + extension;
    }

    private String saveCloud(String directory, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String path = bucket + "/" + directory;
        String fileName = file.getOriginalFilename();

        try {
            amazonS3.putObject(path, fileName, file.getInputStream(), metadata);
        } catch (AmazonServiceException | IOException e) {
            throw new RuntimeException("Could not save the file");
        }

        return directory + "/" + fileName;
    }

    private String saveCloud(String directory, MultipartFile file, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String path = bucket + "/" + directory;
        String extension = this.getExtensionByStringHandling(file.getOriginalFilename()).orElseThrow(() -> new RuntimeException("Invalid file extension"));
        String filePath = fileName + "." + extension;

        try {
            amazonS3.putObject(path, filePath, file.getInputStream(), metadata);
        } catch (AmazonServiceException | IOException e) {
            throw new RuntimeException("Could not save the file");
        }

        return directory + "/" + filePath;
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
