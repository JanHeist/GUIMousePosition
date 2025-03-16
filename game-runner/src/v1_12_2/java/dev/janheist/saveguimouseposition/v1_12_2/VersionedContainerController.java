package dev.janheist.saveguimouseposition.v1_12_2;

import dev.janheist.saveguimouseposition.core.SaveGuiMousePositionAddon;
import dev.janheist.saveguimouseposition.core.SaveGuiMousePositionAddonConfig.SaveScope;
import dev.janheist.saveguimouseposition.core.controller.ContainerController;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.window.Window;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.labymod.v1_12_2.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Implements(ContainerController.class)
public class VersionedContainerController extends ContainerController {

  private double lastX = -1, lastY = -1;
  private boolean inGui = false, processing = false;
  private String lastTitle = null;
  private HashMap<String, double[]> guiPositions = new HashMap<>();

  @Inject
  public VersionedContainerController() {
  }

  @Override
  public void guiOpened(ScreenDisplayEvent e, SaveGuiMousePositionAddon addon) {
    if (processing) {
      return;
    }
    processing = true;

    if (e.getScreen() == null && inGui) {
      savePosition(addon);

      inGui = false;
      processing = false;
      return;
    }

    if (!(e.getScreen() instanceof VersionedScreenWrapper wrapper)
        || !(wrapper.getVersionedScreen() instanceof GuiScreen si)
        || !(si instanceof GuiContainer gc)) {
      processing = false;
      return;
    }

    if (!Laby.labyAPI().minecraft().isIngame() || !Display.isActive()) {
      processing = false;
      return;
    }

    if (inGui) {
      savePosition(addon);
    }

    lastTitle = gc.getClass().getName();
    inGui = true;

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        Minecraft.getMinecraft().addScheduledTask(() -> {
          double[] pos = getPosition(lastTitle, addon);
          double x = pos[0], y = pos[1];

          Window window = Laby.labyAPI().minecraft().minecraftWindow();

          if (x == -1.0) {
            x = window.getRawWidth() / 2.0;
          }

          if (y == -1.0) {
            y = window.getRawHeight() / 2.0;
          }

          x = Math.max(Math.min(x, window.getRawWidth()), 0.0);
          y = Math.max(Math.min(y, window.getRawHeight()), 0.0);

          GLFW.glfwSetCursorPos(window.getPointer(), x, y);

        });
        processing = false;
      }
    }, 5);

  }

  private void savePosition(SaveGuiMousePositionAddon addon) {
    double[] xPos = new double[1];
    double[] yPos = new double[1];

    Minecraft.getMinecraft().addScheduledTask(() -> {
      Window window = Laby.labyAPI().minecraft().minecraftWindow();
      GLFW.glfwGetCursorPos(window.getPointer(), xPos, yPos);
    });

    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      lastX = xPos[0];
      lastY = yPos[0];
    } else if (addon.configuration().getSaveScope().get() == SaveScope.GUITITLE) {
      guiPositions.put(lastTitle, new double[]{xPos[0], yPos[0]});
    }
  }

  private double[] getPosition(String title, SaveGuiMousePositionAddon addon) {
    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      return new double[]{lastX, lastY};
    }

    return guiPositions.getOrDefault(title, new double[]{-1, -1});
  }

}
