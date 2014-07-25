Twirl Maven Plugin
==================

A Maven plugin which compiles [Twirl templates][1] into Scala source files.



Usage
-----

Add the plugin in your `pom.xml`:

```xml
<plugin>
  <groupId>com.jakewharton.twirl</groupId>
  <artifactId>twirl-maven-plugin</artifactId>
  <version>1.0.0</version>
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
  <artifactId>twirl-api_2.11</artifactId>
  <version>1.0.2</version>
</dependency>
```

By default the plugin looks for templates in `src/main/twirl/` and compiles to
`target/twirl/`. The output folder is automatically added as a source root
on the project.

The generated code is a Scala source file for each template which still need
to be compiled using something like the [scala-maven-plugin][2].



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
