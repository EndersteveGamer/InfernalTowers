package core.world.loaders;

import api.EventManager;
import api.events.occupants.OccupantSpawnEvent;
import api.world.World;
import api.world.WorldLoader;
import core.entities.builders.JsonEntityBuilder;
import core.entities.builders.JsonMultiTileBuilder;
import core.utils.JsonParser;
import core.utils.builders.JsonBuilder;
import core.world.ImplWorld;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class JsonWorldLoader implements WorldLoader {
    private final boolean debugMode;

    public JsonWorldLoader(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public World loadWorld(String filePath, EventManager eventManager) throws IOException {
        JsonParser parser = new JsonParser(filePath);
        World world = parser.<String>getObjectAtPath("world").map(
            path -> {
                try {
                    return new FileWorldLoader(debugMode).loadWorld(path, eventManager);
                }
                catch (IOException e) {
                    System.out.println("Failed to load world at " + path);
                    return new ImplWorld(eventManager);
                }
            }
        ).orElse(new ImplWorld(eventManager));
        loadEntities(parser, "occupants", new JsonEntityBuilder(world, debugMode),
            o -> {
                world.addOccupant(o);
                world.getEventManager().callDeferredEvent(EventManager.WORLDLOAD_REGISTRY, new OccupantSpawnEvent(o));
            }
        );
        loadEntities(parser, "multiTiles", new JsonMultiTileBuilder(world, debugMode),
            m -> {
                world.addMultiTile(m);
                m.getOccupants().forEach(
                    o -> world.getEventManager()
                        .callDeferredEvent(EventManager.WORLDLOAD_REGISTRY, new OccupantSpawnEvent(o))
                );
            }
        );
        return world;
    }

    private <T> void loadEntities(JsonParser parser, String jsonPath, JsonBuilder<T> builder, Consumer<T> consumer) {
        Optional<List<Object>> obj = parser.getObjectAtPath(jsonPath);
        if (obj.isPresent()) {
            List<Map<String, Object>> occupantsList = convertToMapList(obj.get());
            for (Map<String, Object> map : occupantsList) {
                try {
                    builder.build(map).ifPresent(consumer);
                }
                catch (IllegalArgumentException e) {
                    if (debugMode) {
                        System.out.println("Error while loading:");
                        System.out.println("Json: " + parser.displayJson(map));
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> convertToMapList(List<Object> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        list.forEach(
            m -> {
                if (!(m instanceof Map<?, ?>)) return;
                Map<?, ?> map = (Map<?, ?>) m;
                Map<String, Object> json = new HashMap<>();
                map.forEach((k, v) -> json.put(k.toString(), v));
                mapList.add(json);
            }
        );
        return mapList;
    }
}
