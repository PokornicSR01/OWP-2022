package com.ftn.owpProject2022.utilities;

import java.util.UUID;

public class IdGenerator {
    public static String generateId() {

        UUID newId = UUID.randomUUID();
        return newId.toString();

    }
}
