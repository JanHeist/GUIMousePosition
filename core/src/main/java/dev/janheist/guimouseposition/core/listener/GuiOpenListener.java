package dev.janheist.guimouseposition.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import dev.janheist.guimouseposition.core.SaveGuiMousePosAddon;
import dev.janheist.guimouseposition.core.controller.ContainerController;

public class GuiOpenListener {

  private final SaveGuiMousePosAddon addon;
  private final ContainerController controller;

  public GuiOpenListener(SaveGuiMousePosAddon addon, ContainerController controller) {
    this.addon = addon;
    this.controller = controller;
  }

  @Subscribe
  public void onGuiOpenEvent(ScreenDisplayEvent event) {
    if (this.addon.configuration().enabled().get())
      controller.guiOpened(event, addon);
  }

}
