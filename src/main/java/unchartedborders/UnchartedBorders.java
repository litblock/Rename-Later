package unchartedborders;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import de.articdive.jnoise.JNoise;
import de.articdive.jnoise.interpolation.InterpolationType;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import unchartedborders.world.NoiseGenerator;
import unchartedborders.world.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnchartedBorders extends GameApplication {
    private int WIDTH = 750;
    private int HEIGHT= 750;
    private int TILE_SIZE = 25;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initGame() {
        //Entity player = FXGL.entityBuilder().at(300, 300)
        //        .view(new Rectangle(25, 25, Color.BLUE))
         //       .buildAndAttach();
        List<List<Tile>> tiles = new ArrayList<>();
        NoiseGenerator noise = new NoiseGenerator(new Random().nextInt());
        noise.SetNoiseType(NoiseGenerator.NoiseType.Perlin);
        for(int x = 0; x < WIDTH; x+=25){
            List<Tile> column = new ArrayList<>();
            for(int y = 0; y < HEIGHT; y+=25){
                float value = noise.GetNoise(x, y);
                column.add(new Tile(new Vec2(x, y), new Rectangle(TILE_SIZE, TILE_SIZE, Color.gray(normalize(value, -1, 1)))));
            }
            tiles.add(column);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Calculates a value between 0 and 1, given the precondition that value
     * is between min and max. 0 means value = max, and 1 means value = min.
     */
    double normalize(double value, double min, double max) {
        return 1 - ((value - min) / (max - min));
    }
}
