package com.example.bookkeeperjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
//作成・更新日時を自動設定するために必要なアノテーション(正確には、エンティティの監査をするためのアノテーション)
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;

    @ManyToOne
    @JoinColumn(name = "genre", nullable = false)
    private Genre genre;
    private LocalDate publishedDate;

    //データ作成時に自動で設定されるようにするアノテーション
    @CreatedDate
    //nullableはNULLの許可するかどうか、updatedableは更新させるかどうか
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    //データが更新されるたびに自動で設定されるようにするアノテーション
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated_at;

    //複数のbookが１つのuserと紐づくことを宣言する
    @ManyToOne
    //リレーションを作成する際に結合する列名を指定
    @JoinColumn(name = "user_id")
    //JSONに変換するときはこのフィールドは無視する
    @JsonIgnore
    private User user;
}