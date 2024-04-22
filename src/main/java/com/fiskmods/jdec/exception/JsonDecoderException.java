package com.fiskmods.jdec.exception;

import com.google.gson.stream.JsonReader;

public class JsonDecoderException extends RuntimeException {
    public JsonDecoderException(Exception e, JsonReader reader) {
        super(getTrace(reader, e.getMessage()), e);
    }

    private static String getTrace(JsonReader in, String message) {
        String s = getTrace(in);
        return message != null ? message.endsWith(s) ? message : message + " -" + s : s.substring(1);
    }

    private static String getTrace(JsonReader in) {
        return in.toString().substring(3 + in.getClass().getSimpleName().length());
    }
}
