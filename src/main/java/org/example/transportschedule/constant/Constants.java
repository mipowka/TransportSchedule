package org.example.transportschedule.constant;

public class Constants {

    private Constants() {
    }

    // Префикс ключа автобуса для Redis
    public static final String BUS_CACHE_KEY_PREFIX = "Bus:";

    // Префикс для ключа автобусов по странице для Redis
    public static final String BUSES_CACHE_KEY_PREFIX_WITH_PAGE = "busesPage:";

    // Префикс ключа поезда для Redis
    public static final String TRAIN_CACHE_KEY_PREFIX = "Train:";

    // Префикс для ключа поездов по странице для Redis
    public static final String TRAINS_CACHE_KEY_PREFIX_WITH_PAGE = "trainsPage:";
}
