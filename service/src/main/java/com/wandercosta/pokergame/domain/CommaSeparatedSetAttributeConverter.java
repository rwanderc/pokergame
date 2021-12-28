package com.wandercosta.pokergame.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Attribute converter to inline {@link Set}s of {@link Object}s separated by comma.
 */
@Converter
public class CommaSeparatedSetAttributeConverter implements AttributeConverter<Set<Float>, String> {

    private static final String COMMA = ",";

    @Override
    public String convertToDatabaseColumn(final Set<Float> attribute) {
        if (attribute == null) {
            return "";
        }
        final List<String> strRepresentations = attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        return String.join(COMMA, strRepresentations);
    }

    @Override
    public Set<Float> convertToEntityAttribute(final String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Collections.emptySet();
        }
        final List<String> strings = Arrays.asList(dbData.split(COMMA));
        return strings.stream()
                .map(Float::valueOf)
                .collect(Collectors.toSet());
    }
}
