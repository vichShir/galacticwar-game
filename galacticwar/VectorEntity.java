/*******************************************************
 * Vector class for handling game entities
 *******************************************************/

import java.awt.*;

public class VectorEntity extends BaseGameEntity
{
    // Variables
    private Shape shape;

    // Acessor methods
    public Shape getShape() { return shape; }

    // Mutator methods
    public void setShape(Shape shape) { this.shape = shape; }

    // Default constructor
    public VectorEntity()
    {
        setShape(null);
    }
}
