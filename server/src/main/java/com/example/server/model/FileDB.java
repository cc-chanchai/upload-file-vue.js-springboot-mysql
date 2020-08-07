package com.example.server.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="files")
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String type;

    @Lob
    private byte[] data;
    //    LOB is datatype for storing large object data. There’re two kinds of LOB: BLOB and CLOB:
    //    BLOB is for storing binary data
    //    CLOB is for storing text data

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }


}
