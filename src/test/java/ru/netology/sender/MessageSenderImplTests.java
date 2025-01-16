package ru.netology.sender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageSenderImplTests {
    private MessageSenderImpl messageSender;
    private GeoService geoService;
    private LocalizationService localizationService;

    static Stream<Arguments> headersProvider() {
        return Stream.of(
                Arguments.of(Map.of("x-real-ip", "127.0.0.1"), new Location(null, null, null, 0)),
                Arguments.of(Map.of("x-real-ip", "172.0.32.11"), new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of(Map.of("x-real-ip", "96.44.183.149"), new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of(Map.of("x-real-ip", "172.0.45.158"), new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of(Map.of("x-real-ip", "96.4.415.1"), new Location("New York", Country.USA, null, 0))
        );
    }

    @BeforeEach
    void setUp() {
        geoService = mock(GeoService.class);
        localizationService = mock(LocalizationService.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @ParameterizedTest
    @MethodSource("headersProvider")
    void testSend_WhenIpRussian(Map<String, String> headers, Location location) {
        // Arrange
        when(geoService.byIp(headers.get("x-real-ip"))).thenReturn(location);
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Сообщение отправлено на русском");
        when(localizationService.locale(argThat(country -> country != Country.RUSSIA))).thenReturn("Сообщение отправлено на английском");
        // Act
        String message = messageSender.send(headers);
        // Assert
        if (location.getCountry() == Country.RUSSIA) {
            assertEquals("Сообщение отправлено на русском", message);
        } else {
            assertEquals("Сообщение отправлено на английском", message);
        }
    }
}
