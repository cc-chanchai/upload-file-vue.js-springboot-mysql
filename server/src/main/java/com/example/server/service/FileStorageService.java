package com.example.server.service;

import com.example.server.model.FileDB;
import com.example.server.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    private FileDBRepository fileDBRepository;

    //receives MultipartFile object, transform to FileDB object and save it to Database
    public FileDB store(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(fileDB);
    }

    //returns a FileDB object by provided Id
    public FileDB getFile(String id){
        return fileDBRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found with id: " + id));
    }

    //returns all stored files as list of code>FileDB objects
    public Stream<FileDB> getAllFile(){
        return fileDBRepository.findAll().stream();
    }
}
