package jakemarsden.opengl.engine.display;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Objects.requireNonNull;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fissore.slf4j.FluentLogger;
import org.lwjgl.glfw.GLFWErrorCallback;

public class GlfwDisplay implements Display {

  private final FluentLogger LOGGER;
  private final long id;

  private int posX;
  private int posY;
  private int width;
  private int height;
  private boolean visible;
  private String title;

  private @Nullable MoveCallback moveCallback;
  private @Nullable ResizeCallback resizeCallback;

  public static @NonNull GlfwDisplay create(int width, int height, String title) {
    glfwSetErrorCallback(GLFWErrorCallback.createThrow());
    if (!glfwInit()) throw new IllegalStateException("Unable to initialise GLFW");

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

    final long windowId = glfwCreateWindow(width, height, title, NULL, NULL);
    if (windowId == NULL) {
      destroy(NULL);
      throw new IllegalStateException("Unable to create GLFW window");
    }

    // centre window on screen
    final var videoMode = requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));
    final int posX = (videoMode.width() - width) / 2;
    final int posY = (videoMode.height() - height) / 2;
    glfwSetWindowPos(windowId, posX, posY);

    glfwMakeContextCurrent(windowId);
    glfwSwapInterval(1);

    final var display = new GlfwDisplay(windowId, posX, posY, width, height, false, title);
    glfwSetWindowPosCallback(windowId, (winId, newX, newY) -> display.onMove(newX, newY));
    glfwSetWindowSizeCallback(windowId, (winId, newW, newH) -> display.onResize(newW, newH));

    return display;
  }

  private static void destroy(long windowId) {
    if (windowId != NULL) {
      requireNonNull(glfwSetWindowPosCallback(windowId, null)).free();
      requireNonNull(glfwSetWindowSizeCallback(windowId, null)).free();
      glfwFreeCallbacks(windowId);
      glfwDestroyWindow(windowId);
    }

    glfwTerminate();
    requireNonNull(glfwSetErrorCallback(null)).free();
  }

  private GlfwDisplay(
      long id, int posX, int posY, int width, int height, boolean visible, String title) {
    this.LOGGER = getLogger(GlfwDisplay.class.getName() + "#" + id);
    this.id = id;
    this.posX = posX;
    this.posY = posY;
    this.width = width;
    this.height = height;
    this.visible = visible;
    this.title = title;
  }

  @Override
  public int getPosX() {
    return posX;
  }

  @Override
  public int getPosY() {
    return posY;
  }

  @Override
  public void setPos(int posX, int posY) {
    LOGGER.debug().log("#setPos: posX={}, posY={}", posX, posY);
    if (posX == this.posX && posY == this.posY) return;
    glfwSetWindowPos(this.id, posX, posY);
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void setSize(int width, int height) {
    LOGGER.debug().log("#setSize: width={}, height={}", width, height);
    if (width == this.width && height == this.height) return;
    glfwSetWindowSize(this.id, width, height);
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible(boolean visible) {
    LOGGER.debug().log("#setVisible: visible={}", visible);
    if (visible == this.visible) return;
    if (visible) {
      glfwShowWindow(this.id);
    } else {
      glfwHideWindow(this.id);
    }
    this.visible = visible;
  }

  @Override
  public @NonNull String getTitle() {
    return title;
  }

  @Override
  public void setTitle(@NonNull String title) {
    LOGGER.debug().log("#setTitle: title={}", title);
    if (title.equals(this.title)) return;
    glfwSetWindowTitle(this.id, title);
    this.title = title;
  }

  @Override
  public boolean isCloseRequested() {
    return glfwWindowShouldClose(this.id);
  }

  @Override
  public void setCloseRequested(boolean closeRequested) {
    LOGGER.debug().log("#setCloseRequested: closeRequested={}", closeRequested);
    glfwSetWindowShouldClose(this.id, closeRequested);
  }

  @Override
  public void setMoveCallback(@Nullable MoveCallback cb) {
    this.moveCallback = cb;
  }

  @Override
  public void setResizeCallback(@Nullable ResizeCallback cb) {
    this.resizeCallback = cb;
  }

  @Override
  public void processPendingInputEvents() {
    glfwPollEvents();
  }

  @Override
  public void swapDrawBuffers() {
    glfwSwapBuffers(this.id);
  }

  @Override
  public void destroy() {
    LOGGER.info().log("#destroy");
    this.moveCallback = null;
    this.resizeCallback = null;
    destroy(this.id);
  }

  @Override
  public String toString() {
    return "GlfwDisplay{" + this.id + "}";
  }

  private void onMove(int posX, int posY) {
    LOGGER.debug().every(500, MILLIS).log("#onMove: posX={}, posY={}", posX, posY);
    this.posX = posX;
    this.posY = posY;
    if (this.moveCallback != null) this.moveCallback.onMove(posX, posY);
  }

  private void onResize(int width, int height) {
    LOGGER.debug().every(500, MILLIS).log("#onResize: width={}, height={}", width, height);
    this.width = width;
    this.height = height;
    if (this.resizeCallback != null) this.resizeCallback.onResize(width, height);
  }
}
