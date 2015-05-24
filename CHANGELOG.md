Change Log
==========

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
