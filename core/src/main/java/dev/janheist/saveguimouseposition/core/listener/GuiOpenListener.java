package dev.janheist.saveguimouseposition.core.listener;

import dev.janheist.saveguimouseposition.core.SaveGuiMousePositionAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import dev.janheist.saveguimouseposition.core.controller.ContainerController;

public class GuiOpenListener {

  private final SaveGuiMousePositionAddon addon;
  private final ContainerController controller;

  public GuiOpenListener(SaveGuiMousePositionAddon addon, ContainerController controller) {
    this.addon = addon;
    this.controller = controller;
  }

  @Subscribe
  public void onGuiOpenEvent(ScreenDisplayEvent event) {
    if (this.addon.configuration().enabled().get())
      controller.guiOpened(event, addon);
  }

}
