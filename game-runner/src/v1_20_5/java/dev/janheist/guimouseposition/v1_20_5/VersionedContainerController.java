package dev.janheist.guimouseposition.v1_20_5;

import net.labymod.api.Laby;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.labymod.v1_20_5.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import dev.janheist.guimouseposition.core.SaveGuiMousePosAddon;
import dev.janheist.guimouseposition.core.controller.ContainerController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Implements(ContainerController.class)
public class VersionedContainerController extends ContainerController {

  private double lastX = -1;
  private double lastY = -1;
  private boolean inGui = false;

  @Inject
  public VersionedContainerController() {
  }

  @Override
  public void guiOpened(ScreenDisplayEvent e, SaveGuiMousePosAddon addon) {
    if (e.getScreen() == null && inGui) {
      lastX = Laby.labyAPI().minecraft().mouse().getX();
      lastY = Laby.labyAPI().minecraft().mouse().getY();

      inGui = false;
    }

    if (e.getScreen() instanceof VersionedScreenWrapper wrapper
        && wrapper.getVersionedScreen() instanceof Screen si
        && si instanceof AbstractContainerScreen<?>) {

      if (!Laby.labyAPI().minecraft().isIngame() || !Minecraft.getInstance().isWindowActive()) {
        return;
      }

      inGui = true;

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          Minecraft.getInstance().execute(() -> Laby.labyAPI().minecraft().setCursorPosition(lastX, lastY));
        }
      }, 1);

    }
  }

}
