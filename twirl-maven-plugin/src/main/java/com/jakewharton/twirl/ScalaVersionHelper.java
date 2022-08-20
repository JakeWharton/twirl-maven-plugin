package com.jakewharton.twirl;

import com.google.common.collect.ImmutableSet;
import scala.util.matching.Regex;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ScalaVersionHelper {

    static String getRunningScalaVersion() {
        try {
            //stainsby's way
            Properties props = new java.util.Properties();
            props.load(ScalaVersionHelper.class.getResourceAsStream("/library.properties"));
            String line = props.getProperty("version.number");
            Pattern version = Pattern.compile("(\\d\\.\\d+\\.\\d).*");
            Matcher matcher = version.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return "2.12";
            }
        } catch (Throwable e) {
            return "2.12"; //or some other default version, or re-raise
        }
    }

    static ImmutableSet<String> getJavaImport() {
        if (getRunningScalaVersion().startsWith("2.13")) {
            return JAVA_IMPORTS_2_13;
        } else {
            return JAVA_IMPORTS_2_12;
        }
    }

    static
    private final ImmutableSet<String> JAVA_IMPORTS_2_13 = ImmutableSet.<String>builder().add(
            "java.lang._",
            "java.util._",
            "scala.collection.JavaConverters._"
    ).build();

    static
    private final ImmutableSet<String> JAVA_IMPORTS_2_12 = ImmutableSet.<String>builder().add(
            "java.lang._", "java.util._", "scala.collection.JavaConversions._", "scala.collection.JavaConverters._"
    ).build();
}
