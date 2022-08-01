package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Data
@Accessors(chain = true)
public class ItemRecordMo {
    @NotNull
    private String loreSign;
    @NotNull
    private ItemMo itemMo;
}
