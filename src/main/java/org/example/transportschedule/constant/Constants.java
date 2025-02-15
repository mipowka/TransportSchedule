package org.example.transportschedule.constant;

public class Constants {

    private Constants() {
    }

    // Префикс ключа автобуса для Redis
    public static final String BUS_CACHE_KEY_PREFIX = "Bus:";

    // Префикс для ключа автобусов по странице для Redis
    public static final String BUSES_CACHE_KEY_PREFIX_WITH_PAGE = "busesPage:";

    public static final String TRAIN_CACHE_KEY_PREFIX = "Train:";

    public static final String TRAINS_CACHE_KEY_PREFIX_WITH_PAGE = "trainsPage:";

    // Размер страницы для пагинации
    public static final int PAGE_SIZE = 10;
}
