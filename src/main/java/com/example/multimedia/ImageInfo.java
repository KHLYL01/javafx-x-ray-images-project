package com.example.multimedia;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    private String name;
    private String size;
    private String lastModified;
}
