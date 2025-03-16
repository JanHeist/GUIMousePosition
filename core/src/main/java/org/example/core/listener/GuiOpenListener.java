package org.example.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import org.example.core.ExampleAddon;
import org.example.core.controller.ContainerController;

public class GuiOpenListener {

  private final ExampleAddon addon;
  private final ContainerController controller;

  public GuiOpenListener(ExampleAddon addon, ContainerController controller) {
    this.addon = addon;
    this.controller = controller;
  }

  @Subscribe
  public void onGuiOpenEvent(ScreenDisplayEvent event) {
    if (this.addon.configuration().enabled().get())
      controller.guiOpened(event, addon);
  }

}
