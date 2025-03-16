package org.example.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.example.core.controller.ContainerController;
import org.example.core.controller.DefaultContainerController;
import org.example.core.generated.DefaultReferenceStorage;
import org.example.core.listener.GuiOpenListener;


@AddonMain
public class ExampleAddon extends LabyAddon<ExampleConfiguration> {

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
  protected Class<ExampleConfiguration> configurationClass() {
    return ExampleConfiguration.class;
  }

  private DefaultReferenceStorage references() {
    return this.referenceStorageAccessor();
  }

}
