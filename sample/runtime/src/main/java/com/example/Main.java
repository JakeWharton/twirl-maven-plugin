package com.example;

import com.example.html.Hello;

public final class Main {
  public static void main(String... args) {
    String render = Hello.render("Jake!").toString();
    System.out.println(render);
  }
}
