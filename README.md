Twirl Maven Plugin
==================

A Maven plugin which compiles [Twirl templates][1] into Scala source files.

Twirl lets you write type-safe, compiled templates in Scala:
```scala
// Hello.scala.html
@(name: String)
<h1>Hello, @name!</h1>
```
which you then render in code:
```java
html.Hello.render("Jake")
```
to yield the final result:
```
<h1>Hello, Jake!</h1>
```



Usage
-----

Add the plugin in your `pom.xml`:

```xml
<plugin>
  <groupId>com.jakewharton.twirl</groupId>
  <artifactId>twirl-maven-plugin</artifactId>
  <version>1.2.0</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>compile</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

and specify a dependency on the Twirl API:

```xml
<dependency>
  <groupId>com.typesafe.play</groupId>
  <artifactId>twirl-api_2.12</artifactId>
  <version>1.4.1</version>
</dependency>
```

By default the plugin looks for templates in `src/main/twirl/` and compiles to
`target/generated-sources/twirl/`. The output folder is automatically added as a
source root on the project.

Custom template formatters can also be specified:

```xml
<plugin>
  <groupId>com.jakewharton.twirl</groupId>
  <artifactId>twirl-maven-plugin</artifactId>
    ...
  <configuration>
    <customFormatters>custom:fully.qualified.name.CustomTwirlFormat</customFormatters>
  </configuration>
```

The generated code is a Scala source file for each template which still need
to be compiled using something like the [scala-maven-plugin][2].

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].



License
-------

    Copyright 2014 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://github.com/playframework/twirl
 [2]: https://github.com/davidB/scala-maven-plugin
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
