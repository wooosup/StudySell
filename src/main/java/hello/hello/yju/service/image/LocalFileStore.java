package hello.hello.yju.service.image;

import hello.hello.yju.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Profile("local")
public class LocalFileStore implements FileStore {

    private final Path storageLocation;

    public LocalFileStore(@Value("${file.upload-dir}") String uploadDir) {
        this.storageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new FileStorageException("로컬 저장소 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = FileUtils.createFileName(originalFileName);

        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("잘못된 파일 경로: " + originalFileName);
            }
            Path targetLocation = this.storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("파일 업로드에 실패했습니다: " + originalFileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path filePath = this.storageLocation.resolve(fileName).normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException("파일 삭제에 실패했습니다: " + fileName, e);
        }
    }

    @Override
    public String generateFileUrl(String fileName) {
        return "/upload-images/" + fileName;
    }
}