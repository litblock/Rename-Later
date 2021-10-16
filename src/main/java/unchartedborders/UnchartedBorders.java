package unchartedborders;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.math.Vec3;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import de.articdive.jnoise.JNoise;
import de.articdive.jnoise.interpolation.InterpolationType;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import unchartedborders.world.NoiseGenerator;
import unchartedborders.world.Tile;
import unchartedborders.world.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnchartedBorders extends GameApplication {
    private int WIDTH = 750;
    private int HEIGHT= 750;
    private double TILE_SIZE = 25;
    List<List<Tile>> tiles = new ArrayList<>();
    NoiseGenerator noise;
    double zoomFactor = 1;
    Vec2 zoomOffset = new Vec2(0, 0);
    Vec2 prevZoomOffset = new Vec2(0, 0);

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setTitle("Basic Game App");
    }

    @Override
    protected void initGame() {
        noise = new NoiseGenerator(new Random().nextInt());
        noise.SetNoiseType(NoiseGenerator.NoiseType.Perlin);
        drawTerrain();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Calculates a value between 0 and 1, given the precondition that value
     * is between min and max. 0 means value = max, and 1 means value = min.
     */
    double normalize(double value, double min, double max) {
        return ((value - min) / (max - min));
    }

    void drawTerrain()
    {
        tiles.forEach(x -> x.forEach(WorldObject::destroy));
        tiles.clear();
        for(int x = 0; x < WIDTH; x+=TILE_SIZE * zoomFactor){
            List<Tile> column = new ArrayList<>();
            for(int y = 0; y < HEIGHT; y+=TILE_SIZE * zoomFactor){
                //System.out.println(prevZoomOffset.x + ", " + zoomOffset.x);
                Vec2 position = new Vec2(x, y);
                //position = position.add(midpoint(prevZoomOffset, zoomOffset));
                float height = noise.GetNoise((float) (position.x / zoomFactor), (float) (position.y / zoomFactor));
                height = (float) (Math.round(height * 100.0) / 100.0); // Round to hundredth's
                createTileWithHeightInfo(column, height, position, zoomFactor);
            }
            tiles.add(column);
        }
    }
    void createTileWithHeightInfo(List<Tile> column, float height, Vec2 position, double zoomFactor){
        int steppedHeight;
        Color color;
        if(height <= -0.2){
            steppedHeight = 0;
            color = Color.BLUE;
        }
        else if(height <= 0){
            steppedHeight = 1;
            color = Color.LIGHTGOLDENRODYELLOW;
        }
        else if(height <= 0.4){
            steppedHeight = 2;
            color = Color.LIGHTGREEN;
        }
        else if(height <= 0.6){
            steppedHeight = 3;
            color = Color.GREEN;
        }
        else{
            steppedHeight = 4;
            color = Color.DARKGREEN;
        }

        column.add(new Tile(new Vec3(position.x, position.y, steppedHeight), new Rectangle(TILE_SIZE * zoomFactor, TILE_SIZE * zoomFactor, color), this));
    }

    public void zoom(double zoomFactor, Vec2 mousePosition){
        setZoomFactor(this.zoomFactor *= zoomFactor);
        prevZoomOffset = zoomOffset;
        zoomOffset = mousePosition;
        drawTerrain();
    }

    public void setZoomFactor(double zoomFactor) {
        if(zoomFactor < 0.5) zoomFactor = 0.5;
        if(zoomFactor > 1.5) zoomFactor = 1.5;
        this.zoomFactor = zoomFactor;
    }

    private Vec2 midpoint(Vec2 startPoint, Vec2 endPoint)
    {
        return new Vec2((startPoint.x + endPoint.x) / 2, (startPoint.y + endPoint.y) / 2) ;
    }

    public double getTileSize(){ return TILE_SIZE; }
    public void setTileSize(double value){
        TILE_SIZE = value;
        //drawTerrain();
    }
}
