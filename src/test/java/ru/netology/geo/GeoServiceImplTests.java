package ru.netology.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoServiceImplTests {
    static Stream<Arguments> provideInputForTestFindLocationById() {
        return Stream.of(
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.0.45.158", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.4.415.1", new Location("New York", Country.USA, null, 0))
        );
    }

    @Test
    void testFindLocationByCoordinates() {
        // Arrange
        GeoServiceImpl geoService = new GeoServiceImpl();
        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            geoService.byCoordinates(12.34, 56.78);
        });
    }

    @ParameterizedTest
    @MethodSource("provideInputForTestFindLocationById")
    void testFindLocationById(String ip, Location expectedLocation) {
        // Arrange
        GeoServiceImpl geoService = new GeoServiceImpl();
        //Act
        Location location = geoService.byIp(ip);
        //Assert
        assertEquals(expectedLocation.getCity(), location.getCity());
        assertEquals(expectedLocation.getCountry(), location.getCountry());
        assertEquals(expectedLocation.getStreet(), location.getStreet());
        assertEquals(expectedLocation.getBuiling(), location.getBuiling());
    }
}
