package com.example.html;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NameListTest {
  @Test public void helloWorld() {
    List<String> names = Arrays.asList("One", "Two");
    String actual = NameList.render(names).toString();
    assertThat(actual.replaceAll("[\r\n]", "")).isEqualTo("<h1>Hello, One!</h1><h1>Hello, Two!</h1>");
  }
}
