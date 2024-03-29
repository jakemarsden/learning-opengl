package jakemarsden.gradle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GitHooksPlugin implements Plugin<Project> {

  private static final Set<String> VALID_HOOKS =
      Set.of(
          "applypatch-msg",
          "commit-msg",
          "fsmonitor-watchman",
          "post-applypatch",
          "post-checkout",
          "post-commit",
          "post-merge",
          "post-receive",
          "post-rewrite",
          "post-update",
          "pre-applypatch",
          "pre-auto-gc",
          "pre-commit",
          "pre-push",
          "pre-rebase",
          "pre-receive",
          "prepare-commit-msg",
          "push-to-checkout",
          "update",
          "sendemail-validate");

  @Override
  public void apply(Project project) {
    final var ext = project.getExtensions().create("gitHooks", GitHooksExtension.class);
    project.afterEvaluate(proj -> writeHooks(ext));
  }

  private void writeHooks(GitHooksExtension ext) {
    final var hooks = ext.hooks.get();
    final var hooksDir = ext.hooksDir.getAsFile().get().toPath();
    final var gradleScript = ext.gradleScript.get();

    this.validateHooks(hooks);
    hooks.forEach((name, task) -> this.writeHook(hooksDir, gradleScript, name, task));
  }

  private void writeHook(Path dir, String gradleScript, String name, String task) {
    final var path = dir.resolve(name);
    final var script = this.genHookScript(gradleScript, task);
    try {
      Files.writeString(path, script);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (!path.toFile().setExecutable(true, false))
      throw new UnsupportedOperationException(
          "Unable to add set executable permission for " + name + " git hook: " + path);
  }

  private String genHookScript(String gradleScript, String task) {
    return ("#!/bin/bash" + "\n\n")
        + ("# Generated by " + GitHooksPlugin.class.getName() + "\n")
        + (gradleScript + " " + task + "\n");
  }

  private void validateHooks(Map<String, String> hooks) {
    if (hooks.isEmpty()) throw new IllegalArgumentException("No git hooks configured");

    final var invalid =
        hooks.keySet().stream()
            .filter(name -> !VALID_HOOKS.contains(name))
            .collect(Collectors.toSet());
    if (!invalid.isEmpty()) {
      throw new IllegalArgumentException("Some configured git hooks are invalid: " + invalid);
    }
  }
}
