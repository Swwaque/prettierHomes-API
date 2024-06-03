package com.ph.payload.request;

import com.ph.domain.entities.Category;
import com.ph.domain.entities.CategoryPropertyKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
//toBuilder()  yontemi ile mevcut nesnenin bir kopyasini olusturmak icin kullanilir. nesne uzerinden belirli kisimlar degisitirilerek yeni bir nesne olusturulabilir.
public class CategoryRequest implements Supplier<Category>, Serializable {

    @NotNull(message = "{validation.category.title.notnull}")
    @Size(max = 150, message = "{validation.category.title.size}")
    @NotBlank(message = "{validation.category.title.notblank}")
    private String title;

    @NotNull(message = "{validation.category.icon.notnull}")
    @Size(max = 50, message = "{validation.category.icon.size}")
    @NotBlank(message = "{validation.category.icon.notblank}")
    private String icon;

    @NotNull(message = "{validation.category.seq.notnull}")
    private Integer seq;

    @NotNull(message = "{validation.category.is-active.notnull}")
    private Boolean active;

    @Override
    public Category get() {
        return Category.builder()
                .title(getTitle())
                .icon(getIcon())
                .seq(getSeq())
                .active(getActive())
                .build();
    }

}
