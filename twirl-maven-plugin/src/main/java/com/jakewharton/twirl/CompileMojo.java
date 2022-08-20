package com.jakewharton.twirl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import play.japi.twirl.compiler.TwirlCompiler;
import play.twirl.api.HtmlFormat;
import play.twirl.api.JavaScriptFormat;
import play.twirl.api.TxtFormat;
import play.twirl.api.XmlFormat;
import scala.io.Codec;

import static com.google.common.collect.Collections2.transform;
import static java.util.Collections.singletonList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

@SuppressWarnings("UnusedDeclaration") // Used reflectively by Maven.
@Mojo(name = "compile", defaultPhase = GENERATE_SOURCES, threadSafe = true)
public final class CompileMojo extends AbstractMojo {
  private static final Map<String, String> FORMATTERS = ImmutableMap.<String, String>builder()
      .put("html", HtmlFormat.class.getCanonicalName())
      .put("txt", TxtFormat.class.getCanonicalName())
      .put("xml", XmlFormat.class.getCanonicalName())
      .put("js", JavaScriptFormat.class.getCanonicalName())
      .build();
  private static final Set<String> JAVA_IMPORTS = ImmutableSet.<String>builder()
      .add("java.lang._")
      .add("java.util._")
//    .add("scala.collection.JavaConversions._") removed in 2.13
      .add("scala.collection.JavaConverters._")
      .build();

  /** Directory from which to compile templates. */
  @Parameter(defaultValue = "${project.basedir}/src/main/twirl")
  private File templateDirectory;

  /** Directory to which compiled template scala files are placed. */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/twirl")
  private File outputDirectory;

  /**
   * A set of inclusion filters for the compiler.
   * <p>
   * ex :
   * <pre>
   * &lt;includes&gt;
   *   &lt;include&gt;SomeFile.scala&lt;/include&gt;
   * &lt;/includes&gt;
   * </pre>
   */
  @Parameter
  private Set<String> includes = new LinkedHashSet<>();

  /**
   * A set of exclusion filters for the compiler.
   * <p>
   * ex :
   * <pre>
   * &lt;excludes&gt;
   *   &lt;exclude&gt;SomeBadFile.scala&lt;/exclude&gt;
   * &lt;/excludes&gt;
   * </pre>
   */
  @Parameter
  private Set<String> excludes = new LinkedHashSet<>();

  /**
   * A set of additional imports for the templates.
   * <p>
   * ex :
   * <pre>
   * &lt;imports&gt;
   *   &lt;import&gt;foo.bar.Baz&lt;/import&gt;
   * &lt;/imports&gt;
   * </pre>
   */
  @Parameter
  private Set<String> imports = new LinkedHashSet<>();

  /** Whether to add the output directory as a compilation source root. */
  @Parameter
  @SuppressWarnings("FieldCanBeLocal") // Mojo parameter.
  private boolean addSourceRoot = true;

  /** Whether to automatically add template {@linkplain #imports} which ease use from Java. */
  @Parameter
  @SuppressWarnings("FieldCanBeLocal") // Mojo parameter.
  private boolean useJavaHelpers = true;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  private final Map<String, Set<String>> importCache = new LinkedHashMap<>();

  @Override public void execute() throws MojoExecutionException, MojoFailureException {
    Log log = getLog();

    if (includes.isEmpty()) {
      // Add default globs for all supported extensions.
      for (String extension : FORMATTERS.keySet()) {
        includes.add("**/*.scala." + extension);
      }
    }
    if (useJavaHelpers) {
      imports.addAll(JAVA_IMPORTS);
    }

    log.debug("templateDirectory: " + templateDirectory);
    log.debug("outputDirectory: " + outputDirectory);
    log.debug("includes: " + includes);
    log.debug("excludes: " + excludes);
    log.debug("imports: " + imports);
    log.debug("addSourceRoot: " + addSourceRoot);
    log.debug("useJavaHelpers: " + useJavaHelpers);

    String[] templatePaths = findFiles(templateDirectory, includes, excludes);
    if (templatePaths.length == 0) {
      log.info("No templates to compile.");
      return;
    }

    log.info(String.format("Compiling %s templates...", templatePaths.length));
    Stopwatch stopwatch = Stopwatch.createStarted();
    for (String templatePath : templatePaths) {
      File template = new File(templateDirectory, templatePath);

      String extension = extensionOf(template);
      String formatter = FORMATTERS.get(extension);
      Set<String> imports = getImports(extension);
      List<String> constructorAnnotations = singletonList("@javax.inject.Inject()");

      log.debug(String.format("Compiling '%s'...", templatePath));

      TwirlCompiler.compile(template, templateDirectory, outputDirectory, formatter, imports,
          constructorAnnotations, Codec.UTF8(), false);
    }

    log.info(String.format("Completed in %sms", stopwatch.elapsed(MILLISECONDS)));

    if (addSourceRoot) {
      project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }
  }

  private Set<String> getImports(String extension) {
    return importCache.computeIfAbsent(extension, s -> ImmutableSet.<String>builder()
        .addAll(TwirlCompiler.DEFAULT_IMPORTS)
        .addAll(transform(imports, i -> i.replace("%format%", extension)))
        .build());
  }

  private static String[] findFiles(File dir, Set<String> includes, Set<String> excludes) {
    if (!dir.exists()) {
      return new String[0];
    }

    DirectoryScanner scanner = new DirectoryScanner();
    scanner.setBasedir(dir);
    scanner.setIncludes(includes.toArray(new String[includes.size()]));
    scanner.setExcludes(excludes.toArray(new String[excludes.size()]));
    scanner.addDefaultExcludes();
    scanner.scan();
    return scanner.getIncludedFiles();
  }

  private static String extensionOf(File file) {
    String[] parts = file.getName().split("\\.", -1);
    return parts[parts.length - 1];
  }
}
