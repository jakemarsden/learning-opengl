package jakemarsden.opengl.engine.display;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Display {

  int getPosX();

  int getPosY();

  void setPos(int posX, int posY);

  int getWidth();

  int getHeight();

  void setSize(int width, int height);

  boolean isVisible();

  void setVisible(boolean visible);

  @NonNull
  String getTitle();

  void setTitle(@NonNull String title);

  boolean isCloseRequested();

  void setCloseRequested(boolean closeRequested);

  void setMoveCallback(@Nullable MoveCallback cb);

  void setResizeCallback(@Nullable ResizeCallback cb);

  void processPendingInputEvents();

  void swapDrawBuffers();

  void destroy();

  @FunctionalInterface
  interface MoveCallback {

    void onMove(int newPosX, int newPosY);
  }

  @FunctionalInterface
  interface ResizeCallback {

    void onResize(int newWidth, int newHeight);
  }
}
