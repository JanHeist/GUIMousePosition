package org.example.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class ExampleConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  @DropdownSetting
  private final ConfigProperty<SaveScope> saveScope = new ConfigProperty<>(SaveScope.GLOBAL);

  public ConfigProperty<SaveScope> getSaveScope() {
    return this.saveScope;
  }

  public enum SaveScope {
    GLOBAL(0),
    GUITITLE(1);

    public final int id;

    SaveScope(final int id) {
      this.id = id;
    }
  }
}
