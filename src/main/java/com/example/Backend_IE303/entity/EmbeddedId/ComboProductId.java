package com.example.Backend_IE303.entity.EmbeddedId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComboProductId {
    private Integer comboId;
    private Integer productId;
}
