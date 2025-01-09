package hello.hello.yju.service.image;

import hello.hello.yju.exception.FileStorageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileStore fileStore;

    @InjectMocks
    private FileService fileService;

    private final String originalFileName = "test.jpg";
    private final byte[] fileData = new byte[0];

    @DisplayName("파일 업로드 테스트")
    @Test
    void uploadFile() throws Exception {
        //given
        String storedFileName = "test.jpg";
        when(fileStore.uploadFile(any(MultipartFile.class))).thenReturn(storedFileName);

        //when
        String result = fileService.uploadFile(originalFileName, fileData);

        //then
        assertThat(result).isEqualTo(storedFileName);
        verify(fileStore, times(1)).uploadFile(any(MultipartFile.class));
    }

    @DisplayName("파일 업로드 중 예외가 발생하면 FileStorageException을 던진다.")
    @Test
    void uploadFileException() throws Exception {
        //given
        when(fileStore.uploadFile(any(MultipartFile.class))).thenThrow(new IOException("파일 저장에 실패했습니다."));

        // expect
        assertThatThrownBy(() -> fileService.uploadFile(originalFileName, fileData))
                .isInstanceOf(FileStorageException.class)
                .hasMessageContaining("파일 업로드에 실패했습니다");

        verify(fileStore, times(1)).uploadFile(any(MultipartFile.class));
    }

    @DisplayName("파일 삭제 테스트")
    @Test
    void deleteFile() throws Exception {
        //given
        String fileName = "test.jpg";

        //when
        fileService.deleteFile(fileName);

        //then
        verify(fileStore, times(1)).deleteFile(fileName);
    }

    @DisplayName("파일 삭제 중 예외가 발생하면 FileStorageException을 던진다.")
    @Test
    void deleteFileException() throws Exception {
        //given
        String fileName = "test.jpg";
        doThrow(new IOException("파일 삭제에 실패했습니다.")).when(fileStore).deleteFile(fileName);

        // expect
        assertThatThrownBy(() -> fileService.deleteFile(fileName))
                .isInstanceOf(FileStorageException.class)
                .hasMessageContaining("파일 삭제에 실패했습니다");

        verify(fileStore, times(1)).deleteFile(fileName);
    }

    @DisplayName("파일 URL 생성 테스트")
    @Test
    void generateFileUrl() throws Exception {
        //given
        String fileName = "test.jpg";
        String fileUrl = "http://test.com/test.jpg";
        when(fileStore.generateFileUrl(fileName)).thenReturn(fileUrl);

        //when
        String result = fileService.generateFileUrl(fileName);

        //then
        assertThat(result).isEqualTo(fileUrl);
        verify(fileStore, times(1)).generateFileUrl(fileName);
    }
}