package org.example.core.controller;

import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.reference.annotation.Referenceable;
import org.example.core.ExampleAddon;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ContainerController {

  public ContainerController() {
  }

  public void guiOpened(ScreenDisplayEvent event, ExampleAddon addon) {
  }

}