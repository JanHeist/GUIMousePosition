package dev.janheist.guimouseposition.main;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

public class GUIMousePosition extends LabyModAddon {

    private boolean active = true;
    private Robot r;
    private Point lastPos;
    private boolean inGui;

    @Override
    public void onEnable() {

        // init vars
        lastPos = null;
        inGui = false;

        try {
            r = new Robot();
        } catch (AWTException e) {
            r = null;
            System.out.println("[GUIMousePosition] Unknown Exception");
            e.printStackTrace();
        }


        // register events
        this.getApi().registerForgeListener(this);

    }

    @SubscribeEvent
    public void GUIEvent(GuiOpenEvent e) {

        if (!active || r == null)
            return;

        if (e.getGui() == null) // closed eq null
            inGui = false;
        else if (
          e.getGui() instanceof net.minecraft.client.gui.inventory.GuiInventory ||
          e.getGui() instanceof net.minecraft.client.gui.inventory.GuiContainer
        ) {
            inGui = true;
            if (lastPos == null)
                return;

            // needs 1ms delay to avoid default mousemove to center
            Point tmp = lastPos;
            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                r.mouseMove(tmp.x, tmp.y); // move to saved pos
            }).start();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!active || !inGui || r == null)
            return;

        lastPos = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void loadConfig() {
        this.active = !getConfig().has("active") || getConfig().get("active").getAsBoolean();
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        getSubSettings().add(new BooleanElement("Active ?", this, new ControlElement.IconData(Material.LEVER), "active", this.active));
    }
}
