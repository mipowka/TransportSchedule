package org.example.transportschedule.constant;

public class Constants {
    // Префикс ключа автобуса для Redis
    public static final String BUS_CACHE_KEY_PREFIX = "Bus:";

    // Префикс для ключа автобусов по странице для Redis
    public static final String BUSES_CACHE_KEY_PREFIX_WITH_PAGE = "busesPage:";

    // Размер страницы для пагинации
    public static final int PAGE_SIZE = 10;
}
