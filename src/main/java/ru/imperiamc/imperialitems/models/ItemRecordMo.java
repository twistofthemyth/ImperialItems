package ru.imperiamc.imperialitems.models;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemRecordMo {
    @Nullable
    private List<String> suitableLore;
    @Nullable
    private List<String> notSuitableLore;
    @NotNull
    private ItemMo itemMo;
}
