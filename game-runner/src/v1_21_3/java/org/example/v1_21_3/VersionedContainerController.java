package org.example.v1_21_3;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.labymod.v1_21_3.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.example.core.ExampleAddon;
import org.example.core.ExampleConfiguration.SaveScope;
import org.example.core.controller.ContainerController;

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
  public void guiOpened(ScreenDisplayEvent e, ExampleAddon addon) {
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
        || !(si instanceof AbstractContainerScreen<?> acs)) { // ignore InventoryScreen because of double calls
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

  private void savePosition(double x, double y, ExampleAddon addon) {
    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      lastX = Laby.labyAPI().minecraft().mouse().getX();
      lastY = Laby.labyAPI().minecraft().mouse().getY();
    } else if (addon.configuration().getSaveScope().get() == SaveScope.GUITITLE) {
      guiPositions.put(lastTitle, new double[]{x, y});
    }
  }

  private double[] getPosition(String title, ExampleAddon addon) {
    if (addon.configuration().getSaveScope().get() == SaveScope.GLOBAL) {
      return new double[] {lastX, lastY};
    }

    return guiPositions.getOrDefault(title, new double[] {-1, -1});
  }

}
