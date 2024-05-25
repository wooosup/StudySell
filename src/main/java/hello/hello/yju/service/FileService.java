package hello.hello.yju.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Service s3Service;

    public String uploadFile(String originalFileName, byte[] fileData) throws IOException {
        MultipartFile multipartFile = new InMemoryMultipartFile(originalFileName, fileData);
        return s3Service.uploadFile(multipartFile);
    }

    public void deleteFile(String filePath) {
        s3Service.deleteFile(filePath);
    }

    public String generateFileUrl(String filePath) {
        return s3Service.getFileUrl(filePath);
    }

    private static class InMemoryMultipartFile implements MultipartFile {

        private final String originalFileName;
        private final byte[] fileData;

        public InMemoryMultipartFile(String originalFileName, byte[] fileData) {
            this.originalFileName = originalFileName;
            this.fileData = fileData;
        }

        @Override
        public String getName() {
            return originalFileName;
        }

        @Override
        public String getOriginalFilename() {
            return originalFileName;
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public boolean isEmpty() {
            return fileData == null || fileData.length == 0;
        }

        @Override
        public long getSize() {
            return fileData.length;
        }

        @Override
        public byte[] getBytes() {
            return fileData;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(fileData);
        }

        @Override
        public void transferTo(java.io.File dest) {
            throw new UnsupportedOperationException("transferTo() 메서드는 지원되지 않습니다.");
        }
    }
}