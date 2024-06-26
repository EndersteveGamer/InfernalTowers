package api.events.gui;

import api.EventManager;
import api.events.Event;
import core.utils.display.CameraState;

/**
 * An event called every frame for the graphical interface
 */
public class GuiDisplayGameEvent extends Event {
    private final CameraState cameraState;

    public GuiDisplayGameEvent(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    public CameraState getCameraState() {
        return cameraState;
    }

    @Override
    public String toString() {
        return EventManager.SILENT_EVENT;
    }
}
