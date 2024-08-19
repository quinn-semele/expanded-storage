package dev.compasses.expandedstorage.client.render;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

import java.util.Set;

public class CubeWithDifferentTextureMapping extends ModelPart.Cube {
    public CubeWithDifferentTextureMapping(int texCoordU, int texCoordV, int texWidth, int texHeight, float originX, float originY, float originZ, float dimensionX, float dimensionY, float dimensionZ, Set<Direction> visibleFaces) {
        super(texCoordU, texCoordV, originX, originY, originZ, dimensionX, dimensionY, dimensionZ, 0, 0, 0, false, texWidth, texHeight, visibleFaces);

        // Antipode means direct opposite, usually used in relation to spheres.
        // Using it here to refer to (maxX, maxY, maxZ), opposite of the origin.
        float antipodeX = originX + dimensionX;
        float antipodeZ = originZ + dimensionZ;
        float antipodeY = originY + dimensionY;
        
        // south = positiveZ = forward, east = positiveX = left ?
        var origin = new ModelPart.Vertex(originX, originY, originZ, 0, 0);
        var originForward = new ModelPart.Vertex(originX, originY, antipodeZ, 0, 0);
        var originLeft = new ModelPart.Vertex(antipodeX, originY, originZ, 0, 0);
        var originAcross = new ModelPart.Vertex(antipodeX, originY, antipodeZ, 0, 0);

        var antipodeAcross = new ModelPart.Vertex(originX, antipodeY, originZ, 0, 0);
        var antipodeRight = new ModelPart.Vertex(originX, antipodeY, antipodeZ, 0, 0);
        var antipodeBackward = new ModelPart.Vertex(antipodeX, antipodeY, originZ, 0, 0);
        var antipode = new ModelPart.Vertex(antipodeX, antipodeY, antipodeZ, 0, 0);

        int i = 0;

        if (visibleFaces.contains(Direction.DOWN)) {
            polygons[i++] = new ModelPart.Polygon(
                    new ModelPart.Vertex[]{
                            originLeft,
                            origin,
                            originForward,
                            originAcross,
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
                            antipodeBackward,
                            antipodeAcross,
                            antipodeRight,
                            antipode,
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
                            antipodeAcross,
                            antipodeRight,
                            originForward,
                            origin
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
                            antipodeBackward,
                            antipode,
                            originAcross,
                            originLeft,
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
                            antipodeAcross,
                            antipodeBackward,
                            originLeft,
                            origin,
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
                            antipode,
                            antipodeRight,
                            originForward,
                            originAcross,
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
