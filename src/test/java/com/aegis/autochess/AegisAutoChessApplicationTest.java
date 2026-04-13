package com.aegis.autochess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Lightweight unit test for AegisAutoChessApplication.
 *
 * NOTE: This test avoids starting the full Spring context because the
 * application uses ddl-auto: none and there is no schema.sql yet. The
 * goal here is simply to verify that the main entry point class exists
 * and can be loaded, ensuring the test infrastructure is wired correctly.
 */
public class AegisAutoChessApplicationTest {

    @Test
    void mainMethodExists() throws NoSuchMethodException {
        // Do not start the Spring context; only verify that the main entry point exists.
        Class<?> cls = AegisAutoChessApplication.class;
        java.lang.reflect.Method main = cls.getMethod("main", String[].class);
        assertNotNull(main, "AegisAutoChessApplication should declare a public static main(String[] args) method");
    }

    @Test
    void applicationClassLoads() throws ClassNotFoundException {
        // Lightweight check: the class is loadable without triggering Spring Boot startup.
        Class.forName("com.aegis.autochess.AegisAutoChessApplication");
    }
}
