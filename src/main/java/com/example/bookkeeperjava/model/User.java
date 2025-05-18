package com.example.bookkeeperjava.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
//明示的に紐づけるテーブル名を指定する（クラス名とテーブル名が違う場合等）
@Table(name = "user_table")
public class User {
    @Id
    //主キーを自動生成する
    @GeneratedValue
    private Long userId;
    private String userName;
    private String password;

    //１つのuserが複数のbookと関連づけられることを宣言する
    @OneToMany(mappedBy = "user")
    private List<Book> books;
}
