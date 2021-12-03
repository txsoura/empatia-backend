package br.com.empatia.app.enums;

import br.com.empatia.app.exceptions.InvalidAttributeException;

public enum PostType {
    TEXT,
    AUDIO,
    IMAGE,
    VIDEO;

    public static PostType cast(String x) {
        switch (x) {
            case "text":
                return TEXT;
            case "audio":
                return AUDIO;
            case "image":
                return IMAGE;
            case "video":
                return VIDEO;
        }

        throw new InvalidAttributeException("Invalid type");
    }
}
