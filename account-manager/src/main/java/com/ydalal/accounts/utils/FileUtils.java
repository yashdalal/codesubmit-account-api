package com.ydalal.accounts.utils;

import java.io.File;

public class FileUtils {
    public static boolean fileIsEmpty(final File file) {
        return file.length() == 0;
    }
}
