package com.ph.payload.request;

import com.ph.domain.entities.CategoryPropertyKey;
import com.ph.domain.enums.KeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryPropertyKeyRequest implements Supplier<CategoryPropertyKey>, Serializable {

    @NotNull(message = "{validation.category.name.notnull}")
    @NotBlank(message = "{validation.category.name.notblank}")
    @Size(min = 2, max = 80, message = "{validation.category.name.size}")
    private String name;

    @NotNull(message = "{validation.category.key-type.notnull}")
    private KeyType keyType;

    private String prefix;

    private String suffix;

    @Override
    public CategoryPropertyKey get() {
        return CategoryPropertyKey.builder()
                .name(getName())
                .keyType(getKeyType())
                .prefix(getPrefix())
                .suffix(getSuffix())
                .build();
    }

}
