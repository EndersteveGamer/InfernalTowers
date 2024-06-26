package core.world.loaders;

import api.EventManager;
import api.Position;
import api.events.occupants.OccupantSpawnEvent;
import api.world.World;
import api.world.WorldLoader;
import core.ImplPosition;
import core.entities.MultiTile;
import core.entities.Occupant;
import core.utils.builders.TxtBuilder;
import core.entities.builders.TxtEntityBuilder;
import core.entities.builders.TxtMultiTileBuilder;
import core.world.ImplWorld;

import java.io.*;

public class TxtWorldLoader implements WorldLoader {
    @Override
    public World loadWorld(String filePath, EventManager eventManager) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException("Unable to find world at path " + filePath);
        World world = new ImplWorld(eventManager);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            readWorld(reader, world);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to find world at path " + filePath);
        }
        return world;
    }

    private void readWorld(BufferedReader reader, World world) throws IOException {
        TxtBuilder<Occupant> entityBuilder = new TxtEntityBuilder();
        TxtBuilder<MultiTile> multiTileBuilder = new TxtMultiTileBuilder();
        Position pos = new ImplPosition(world, 0, 0, 0);
        String line;
        while ((line = reader.readLine()) != null) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i += 2) {
                final int finalI = i;
                entityBuilder.build(chars[i], pos.clone()).ifPresentOrElse(
                    o -> {
                        world.addOccupant(o);
                        world.getEventManager().callDeferredEvent(EventManager.WORLDLOAD_REGISTRY, new OccupantSpawnEvent(o));
                    },
                    () -> multiTileBuilder.build(chars[finalI], pos.clone()).ifPresent(
                        m -> {
                            world.addMultiTile(m);
                            m.getOccupants().forEach(
                                o -> world.getEventManager()
                                    .callDeferredEvent(EventManager.WORLDLOAD_REGISTRY, new OccupantSpawnEvent(o))
                            );
                        }
                    )
                );
                pos.add(1, 0, 0);
            }
            pos.setX(0);
            pos.add(0, 0, 1);
        }
    }
}
