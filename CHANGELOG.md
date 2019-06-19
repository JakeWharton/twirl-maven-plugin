Change Log
==========

Version 1.2.0 *(2019-06-19)*
----------------------------

 * Update to Twirl 1.4.1 and Scala 2.12.8 which adds support for Java 11 and other new versions.


Version 1.1.0 *(2017-07-02)*
----------------------------

 * Update to Twirl 1.3.3 which adds support for injectable templates.
 * Java 8 is now required to run Twirl and thus is also required for this plugin.


Version 1.0.5 *(2016-05-02)*
----------------------------

 * Add m2e lifecycle mapping file so compilation works by default in Eclipse.


Version 1.0.4 *(2015-05-24)*
----------------------------

 * Update Twirl dependencies to the latest version (1.1.1).


Version 1.0.3 *(2015-05-11)*
----------------------------

 * Fix: Do not crash when the template directory is missing.


Version 1.0.2 *(2014-07-29)*
----------------------------

 * `useJavaHelpers` configuration parameter controls whether or not the generated Scala code
   includes imports for `scala.collection.JavaConversions._` and
   `scala.collection.JavaConverters._`. Enabled by default.


Version 1.0.1 *(2014-07-25)*
----------------------------

 * `addSourceRoot` configuration parameter controls whether or not the output directory is added
   as a compilation source root. This is useful to disable when your output directory is an
   existing source root such as `src/main/scala/`. Enabled by default.


Version 1.0.0 *(2014-07-25)*
----------------------------

Initial version.
