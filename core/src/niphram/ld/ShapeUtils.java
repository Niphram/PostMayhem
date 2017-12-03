package niphram.ld;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Niphram on 02.12.2017.
 */
public class ShapeUtils {

    public static Shape createRectangle(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        return shape;
    }

    public static Shape createRandomRectangle(float minWidth, float minHeight, float maxWidth, float maxHeight) {
        return createRectangle(MathUtils.random(minWidth, maxWidth), MathUtils.random(minHeight, maxHeight));
    }

    public static Shape createRandomRectangle(float maxWidth, float maxHeight) {
        return createRandomRectangle(0, maxWidth, 0, maxHeight);
    }
    
    public static Shape createCircle(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        return shape;
    }
}
