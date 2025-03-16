package dev.janheist.saveguimouseposition.core;

import dev.janheist.saveguimouseposition.core.generated.DefaultReferenceStorage;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import dev.janheist.saveguimouseposition.core.controller.ContainerController;
import dev.janheist.saveguimouseposition.core.controller.DefaultContainerController;
import dev.janheist.saveguimouseposition.core.listener.GuiOpenListener;


@AddonMain
public class SaveGuiMousePositionAddon extends LabyAddon<SaveGuiMousePositionAddonConfig> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    ContainerController controller = this.references().getContainerController();
    if (controller == null) {
      controller = new DefaultContainerController();
    }

    this.registerListener(new GuiOpenListener(this, controller));

    this.logger().info("Enabled the Addon");
  }

  @Override
  protected Class<SaveGuiMousePositionAddonConfig> configurationClass() {
    return SaveGuiMousePositionAddonConfig.class;
  }

  private DefaultReferenceStorage references() {
    return this.referenceStorageAccessor();
  }

}
