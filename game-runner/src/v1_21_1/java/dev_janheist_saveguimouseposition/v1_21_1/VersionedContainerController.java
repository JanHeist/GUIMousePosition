package dev_janheist_saveguimouseposition.v1_21_1;

import dev.janheist.guimouseposition.core.SaveGuiMousePosAddon;
import dev.janheist.guimouseposition.core.SaveGuiMousePosAddonConfig.SaveScope;
import dev.janheist.guimouseposition.core.controller.ContainerController;
import net.labymod.api.Laby;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_1.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

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
  public void guiOpened(ScreenDisplayEvent e, SaveGuiMousePosAddon addon) {
    if (processing) {
      return;
    }
    processing = true;

    if (e.getScreen() == null && inGui) {
      savePosition(Laby.labyAPI().minecraft().mouse().getX(),
          Laby.labyAPI().minecraft().mouse().getY(), addon);

      inGui = false;
      processing = false;
      return;
    }

    if (!(e.getScreen() instanceof VersionedScreenWrapper wrapper)
        || !(wrapper.getVersionedScreen() instanceof Screen si)
        || !(si instanceof AbstractContainerScreen<?> acs)) {
      processing = false;
      return;
    }

    if (!Laby.labyAPI().minecraft().isIngame() || !Minecraft.getInstance().isWindowActive()) {
      processing = false;
      return;
    }

    if (inGui) {
      savePosition(Laby.labyAPI().minecraft().mouse().getX(),
          Laby.labyAPI().minecraft().mouse().getY(), addon);
    }

    lastTitle = acs.getTitle().getString();
    inGui = true;

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        double[] pos = getPosition(lastTitle, addon);
        Minecraft.getInstance()
            .execute(() -> Laby.labyAPI().minecraft().setCursorPosition(pos[0], pos[1]));
        processing = false;
      }
    }, 5);

  }

  private void savePosition(double x, double y, SaveGuiMousePosAddon addon) {
    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      lastX = Laby.labyAPI().minecraft().mouse().getX();
      lastY = Laby.labyAPI().minecraft().mouse().getY();
    } else if (addon.configuration().getSaveScope().get() == SaveScope.GUITITLE) {
      guiPositions.put(lastTitle, new double[]{x, y});
    }
  }

  private double[] getPosition(String title, SaveGuiMousePosAddon addon) {
    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      return new double[]{lastX, lastY};
    }

    return guiPositions.getOrDefault(title, new double[]{-1, -1});
  }

}
