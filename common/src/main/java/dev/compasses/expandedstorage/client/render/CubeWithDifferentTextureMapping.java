package dev.compasses.expandedstorage.client.render;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.Set;

public class CubeWithDifferentTextureMapping extends ModelPart.Cube {
    public CubeWithDifferentTextureMapping(int texCoordU, int texCoordV, int texWidth, int texHeight, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, Set<Direction> visibleFaces) {
        super(texCoordU, texCoordV, originX, originY, originZ, dimensionX, dimensionY, dimensionZ, 0, 0, 0, false, texWidth, texHeight, visibleFaces);

        int i = 0;
        polygons = new ModelPart.Polygon[visibleFaces.size()];

        if (visibleFaces.contains(Direction.DOWN)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ + dimensionZ), 0, 0),
                    },
                    texCoordU + dimensionZ + dimensionX,
                    texCoordV,
                    texCoordU + dimensionZ + dimensionX + dimensionX,
                    texCoordV + dimensionZ,
                    texWidth,
                    texHeight,
                    true,
                    Direction.DOWN
            );
        }

        if (visibleFaces.contains(Direction.UP)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                    },
                    texCoordU + dimensionZ,
                    texCoordV,
                    texCoordU + dimensionZ + dimensionX,
                    texCoordV + dimensionZ,
                    texWidth,
                    texHeight,
                    false,
                    Direction.UP
            );
        }

        if (visibleFaces.contains(Direction.WEST)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ), 0, 0)
                    },
                    texCoordU + dimensionZ,
                    texCoordV + dimensionZ,
                    texCoordU,
                    texCoordV + dimensionZ + dimensionY,
                    texWidth,
                    texHeight,
                    true,
                    Direction.WEST
            );
        }

        if (visibleFaces.contains(Direction.EAST)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ), 0, 0),
                    },
                    texCoordU + dimensionZ + dimensionX,
                    texCoordV + dimensionZ,
                    texCoordU + dimensionZ + dimensionX + dimensionZ,
                    texCoordV + dimensionZ + dimensionY,
                    texWidth,
                    texHeight,
                    false,
                    Direction.EAST
            );
        }

        if (visibleFaces.contains(Direction.SOUTH)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ), 0, 0),
                    },
                    texCoordU + dimensionZ + dimensionX + dimensionZ,
                    texCoordV + dimensionZ,
                    texCoordU + dimensionZ + dimensionX + dimensionZ + dimensionX,
                    texCoordV + dimensionZ + dimensionY,
                    texWidth,
                    texHeight,
                    false,
                    Direction.SOUTH
            );
        }

        if (visibleFaces.contains(Direction.NORTH)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY + dimensionY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX, originY, originZ + dimensionZ), 0, 0),
                            new ModelPart.Vertex(new Vector3f(originX + dimensionX, originY, originZ + dimensionZ), 0, 0),
                    },
                    texCoordU + dimensionZ,
                    texCoordV + dimensionZ,
                    texCoordU + dimensionZ + dimensionX,
                    texCoordV + dimensionZ + dimensionY,
                    texWidth,
                    texHeight,
                    false,
                    Direction.NORTH
            );
        }
    }
}
