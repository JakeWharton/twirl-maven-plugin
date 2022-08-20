package com.jakewharton.twirl;

import com.google.common.io.Files;
import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public final class IntegrationTest {
  @Rule public MojoRule rule = new MojoRule();
  @Rule public TestResources resources = new TestResources();

  @Test public void helloWorld() throws Exception {
    File basedir = resources.getBasedir("hello-world");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
  }

  @Test public void javaImports() throws Exception {
    File basedir = resources.getBasedir("java-imports");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
    String fooBarBaz = Files.toString(fooBarBazTemplate, UTF_8);
    assertThat(fooBarBaz).contains("import java.util._")
        .contains("import java.lang._")
//        .contains("import scala.collection.JavaConversions._")
        .contains("import scala.collection.JavaConverters._");
  }

  @Test public void javaImportsDisabled() throws Exception {
    File basedir = resources.getBasedir("java-imports-disabled");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
    String fooBarBaz = Files.toString(fooBarBazTemplate, UTF_8);
    assertThat(fooBarBaz).doesNotContain("import java.util._")
        .doesNotContain("import java.lang._")
        .doesNotContain("import scala.collection.JavaConversions._")
        .doesNotContain("import scala.collection.JavaConverters._");
  }

  @Test public void outputDirectoryAddedAsSourceRoot() throws Exception {
    File basedir = resources.getBasedir("hello-world");

    MavenProject project = rule.readMavenProject(basedir);
    MavenSession session = rule.newMavenSession(project);
    MojoExecution execution = rule.newMojoExecution("compile");
    rule.executeMojo(session, project, execution);

    String expected = new File(basedir, "target/generated-sources/twirl").getAbsolutePath();
    assertThat(project.getCompileSourceRoots()).contains(expected);
  }

  @Test public void doNotAddSourceRootWhenConfiguredAsSuch() throws Exception {
    File basedir = resources.getBasedir("hello-world-not-adding-source-root");

    MavenProject project = rule.readMavenProject(basedir);
    MavenSession session = rule.newMavenSession(project);
    MojoExecution execution = rule.newMojoExecution("compile");
    rule.executeMojo(session, project, execution);

    String expected = new File(basedir, "target/generated-sources/twirl").getAbsolutePath();
    assertThat(project.getCompileSourceRoots()).doesNotContain(expected);
  }

  @Test public void helloWorldFormats() throws Exception {
    File basedir = resources.getBasedir("hello-world-formats");

    rule.executeMojo(basedir, "compile");

    File fooBarBazHtmlTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazHtmlTemplate).exists();
    File fooBarBazJsTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/js/baz.template.scala");
    assertThat(fooBarBazJsTemplate).exists();
    File fooBarBazTxtTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/txt/baz.template.scala");
    assertThat(fooBarBazTxtTemplate).exists();
    File fooBarBazXmlTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/xml/baz.template.scala");
    assertThat(fooBarBazXmlTemplate).exists();
  }

  @Test public void templateDirectory() throws Exception {
    File basedir = resources.getBasedir("template-directory");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
  }

  @Test public void outputDirectory() throws Exception {
    File basedir = resources.getBasedir("output-directory");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/twirl-templates/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
  }

  @Test public void imports() throws Exception {
    File basedir = resources.getBasedir("imports");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
    String fooBarBaz = Files.toString(fooBarBazTemplate, UTF_8);
    assertThat(fooBarBaz).contains("import my.Thing").contains("import my.other.Thing");
  }

  @Test public void importReplacement() throws Exception {
    File basedir = resources.getBasedir("import-replacement");

    rule.executeMojo(basedir, "compile");

    File fooBarBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/bar/html/baz.template.scala");
    assertThat(fooBarBazTemplate).exists();
    String fooBarBaz = Files.toString(fooBarBazTemplate, UTF_8);
    assertThat(fooBarBaz).contains("import my.other.html.Thing");
  }

  @Test public void excludes() throws Exception {
    File basedir = resources.getBasedir("excludes");

    rule.executeMojo(basedir, "compile");

    File fooBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/html/baz.template.scala");
    assertThat(fooBazTemplate).exists();
    File barBazTemplate = new File(basedir, "target/generated-sources/twirl/bar/html/baz.template.scala");
    assertThat(barBazTemplate).doesNotExist();
  }

  @Test public void includes() throws Exception {
    File basedir = resources.getBasedir("includes");

    rule.executeMojo(basedir, "compile");

    File fooBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/html/baz.template.scala");
    assertThat(fooBazTemplate).exists();
    File barBazTemplate = new File(basedir, "target/generated-sources/twirl/bar/html/baz.template.scala");
    assertThat(barBazTemplate).doesNotExist();
  }

  @Test public void includesAndExcludes() throws Exception {
    File basedir = resources.getBasedir("includes-and-excludes");

    rule.executeMojo(basedir, "compile");

    File barBazTemplate = new File(basedir, "target/generated-sources/twirl/bar/html/baz.template.scala");
    assertThat(barBazTemplate).exists();
    File barFooBazTemplate = new File(basedir, "target/generated-sources/twirl/bar/foo/html/baz.template.scala");
    assertThat(barFooBazTemplate).doesNotExist();
    File fooBazTemplate = new File(basedir, "target/generated-sources/twirl/foo/html/baz.template.scala");
    assertThat(fooBazTemplate).doesNotExist();
  }

  @Test public void missingFolderDoesNotThrow() throws Exception {
    File basedir = resources.getBasedir("missing-folder");

    rule.executeMojo(basedir, "compile");
    // Not throwing an exception is considered passing.
  }
}
