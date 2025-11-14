package com.mobilestore.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = buildEMF();

    private static EntityManagerFactory buildEMF() {
        try {
            return Persistence.createEntityManagerFactory("mobileStorePU");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize EntityManagerFactory", e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
