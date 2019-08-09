package com.library.java.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class FileUtils {

    public static byte[] toByteArray(InputStream inputStream) throws IOException { //TODO is that ok?
        return IOUtils.toByteArray(inputStream);
    }
}
