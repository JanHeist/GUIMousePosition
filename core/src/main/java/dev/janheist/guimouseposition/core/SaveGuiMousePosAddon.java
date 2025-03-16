package dev.janheist.guimouseposition.core;

import dev_janheist_saveguimouseposition.core.generated.DefaultReferenceStorage;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import dev.janheist.guimouseposition.core.controller.ContainerController;
import dev.janheist.guimouseposition.core.controller.DefaultContainerController;
import dev.janheist.guimouseposition.core.listener.GuiOpenListener;


@AddonMain
public class SaveGuiMousePosAddon extends LabyAddon<SaveGuiMousePosAddonConfig> {

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
  protected Class<SaveGuiMousePosAddonConfig> configurationClass() {
    return SaveGuiMousePosAddonConfig.class;
  }

  private DefaultReferenceStorage references() {
    return this.referenceStorageAccessor();
  }

}
