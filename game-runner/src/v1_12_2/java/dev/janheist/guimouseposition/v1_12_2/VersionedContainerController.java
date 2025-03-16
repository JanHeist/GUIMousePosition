package dev.janheist.guimouseposition.v1_12_2;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.window.Window;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.models.Implements;
import net.labymod.v1_12_2.client.gui.screen.VersionedScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import dev.janheist.guimouseposition.core.SaveGuiMousePosAddon;
import dev.janheist.guimouseposition.core.controller.ContainerController;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.Display;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
@Implements(ContainerController.class)
public class VersionedContainerController extends ContainerController {

    private double lastX = -1;
    private double lastY = -1;
    private boolean inGui = false;

    @Inject
    public VersionedContainerController() {
    }

    @Override
    public void guiOpened(ScreenDisplayEvent e, SaveGuiMousePosAddon addon) {
        if (e.getScreen() == null && inGui) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                double[] xPos = new double[1];
                double[] yPos = new double[1];

                Window window = Laby.labyAPI().minecraft().minecraftWindow();
                GLFW.glfwGetCursorPos(window.getPointer(), xPos, yPos);

                lastX = xPos[0];
                lastY = yPos[0];
            });

            inGui = false;
        }

        if (e.getScreen() instanceof VersionedScreenWrapper wrapper
                && wrapper.getVersionedScreen() instanceof GuiScreen si
                && si instanceof GuiContainer) {

            if (!Laby.labyAPI().minecraft().isIngame() || !Display.isActive()) {
                return;
            }

            inGui = true;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        double x = lastX, y = lastY;

                        Window window = Laby.labyAPI().minecraft().minecraftWindow();

                        if (x == -1.0) {
                            x = window.getRawWidth() / 2.0;
                        }

                        if (y == -1.0) {
                            y = window.getRawHeight() / 2.0;
                        }

                        x = Math.max(Math.min(x, window.getRawWidth()), 0.0);
                        y = Math.max(Math.min(y, window.getRawHeight()), 0.0);

                        GLFW.glfwSetCursorPos(window.getPointer(), x, y);

                    });
                }
            }, 1);

        }
    }

}
