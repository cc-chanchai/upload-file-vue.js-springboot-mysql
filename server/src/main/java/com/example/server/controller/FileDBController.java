package com.example.server.controller;

import com.example.server.message.ResponseFile;
import com.example.server.message.ResponseMessage;
import com.example.server.model.FileDB;
import com.example.server.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8081")
public class FileDBController {
    @Autowired
    FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file){
        String message = "";

        try{
            fileStorageService.store(file);

            message = "Upload file success: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception error){
            message = "could not upload file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getFiles(){
        List<ResponseFile> files = fileStorageService.getAllFile().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length
            );
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id){
        // Load file from database
        FileDB fileDB = fileStorageService.getFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDB.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "Attachment; filename=\""+ fileDB.getName() + "\"")
                .body(new ByteArrayResource(fileDB.getData()));
    }
}
