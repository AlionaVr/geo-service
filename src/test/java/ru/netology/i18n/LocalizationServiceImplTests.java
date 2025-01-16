package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationServiceImplTests {
    @ParameterizedTest
    @EnumSource(Country.class)
    void testLocale(Country country) {
        // Arrange
        LocalizationServiceImpl localeService = new LocalizationServiceImpl();
        // Act
        String hello = localeService.locale(country);
        // Assert
        if (country == Country.RUSSIA) {
            assertEquals("Добро пожаловать", hello);
        } else {
            assertEquals("Welcome", hello);
        }
    }
}
