package com.jakewharton.twirl;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;


public class ScalaVersionHelperTest {

    @Test
    public void testVersion() throws Exception {
        assertThat(ScalaVersionHelper.getRunningScalaVersion(), CoreMatchers.startsWith("2.13"));
    }

}