package de.itscope.chartservice.util.funnelRangeUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class FunnelRangeConverter implements AttributeConverter<FunnelRange, String> {
    @Override
    public String convertToDatabaseColumn(FunnelRange attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public FunnelRange convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(FunnelRange.values())
                .filter(c -> c.name().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
