package dev.janheist.saveguimouseposition.core.controller;

import dev.janheist.saveguimouseposition.core.SaveGuiMousePositionAddon;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ContainerController {

  public ContainerController() {
  }

  public void guiOpened(ScreenDisplayEvent event, SaveGuiMousePositionAddon addon) {
  }

}