package com.jakewharton.twirl;

import java.io.File;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

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
}
