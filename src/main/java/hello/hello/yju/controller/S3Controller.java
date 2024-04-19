//package hello.hello.yju.controller;
//
//import hello.hello.yju.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/file")
//public class S3Controller {
//
//    private final S3Service s3Service;
//
//
//   @PostMapping("/uploadFile")
//   public ResponseEntity<List<String>> uploadFile(@RequestParam("file") List<MultipartFile> multipartFiles){
//        return ResponseEntity.ok(s3Service.uploadFile(multipartFiles));
//    }
//
//    @DeleteMapping("/deleteFile")
//    public ResponseEntity<String> deleteFile(@RequestParam String fileName){
//        s3Service.deleteFile(fileName);
//        return ResponseEntity.ok(fileName);
//    }
//}
