package unchartedborders.world;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.math.Vec3;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import unchartedborders.UnchartedBorders;

public class Tile extends WorldObject{
    public Tile(Vec3 position, Node shape, UnchartedBorders main) {
        super(position, shape, main);
        shape.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            main.setTileSize(main.getTileSize() + deltaY);
        });
    }
}
