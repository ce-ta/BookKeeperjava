package com.example.bookkeeperjava.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 50, message = "タイトルは50文字以内で入力してください")
    private String title;

    @NotBlank(message = "著者名は必須です")
    @Size(max = 30, message = "著者名は30文字以内で入力してください")
    private String author;
    
    @JsonProperty("genre")
    @NotNull(message = "ジャンルは必須です")
    private Long genreId;

    @PastOrPresent(message = "未来の日付は選択できません")
    @NotNull(message = "出版日は必須です")
    private LocalDate publishedDate;
}
