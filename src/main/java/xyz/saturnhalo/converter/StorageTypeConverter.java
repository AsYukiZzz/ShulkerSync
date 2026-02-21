package xyz.saturnhalo.converter;

import jakarta.persistence.Converter;
import xyz.saturnhalo.enums.StorageType;

/**
 * StorageType 枚举转换器
 */
@Converter(autoApply = true)
class StorageTypeConverter extends AbstractEnumConverter<StorageType> {
    public StorageTypeConverter() {
        super(StorageType.class);
    }
}