package com.jakewharton.twirl;

import java.io.File;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldProjectTest {
  @Rule public MojoRule rule = new MojoRule();
  @Rule public TestResources resources = new TestResources();

  @Test public void sourcesAreGenerated() throws Exception {
    File basedir = resources.getBasedir("hello-world");

    Mojo mojo = rule.lookupConfiguredMojo(basedir, "compile");
    mojo.execute();

    File expected = new File(basedir, "target/twirl/foo/bar/html/baz.template.scala");
    assertThat(expected).exists();
  }
}
