package xyz.saturnhalo.domain.entity.converter;

import jakarta.persistence.Converter;
import xyz.saturnhalo.enums.StorageType;

/**
 * StorageType 枚举转换器
 */
@Converter(autoApply = true)
public class StorageTypeConverter extends AbstractEnumConverter<StorageType> {
    public StorageTypeConverter() {
        super(StorageType.class);
    }
}