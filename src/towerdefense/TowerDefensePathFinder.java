package towerdefense;

import mindustry.ai.Pathfinder;
import mindustry.content.Blocks;
import mindustry.gen.PathTile;
import mindustry.world.Tile;

public class TowerDefensePathFinder extends Pathfinder {

    public static final int impassable = -1, notPath = 999999;

    public TowerDefensePathFinder() {
        costTypes.set(costGround,
                (team, tile) -> (PathTile.allDeep(tile)
                        || ((PathTile.team(tile) == team && !PathTile.teamPassable(tile)) || PathTile.team(tile) == 0)
                                && PathTile.solid(tile)) ? impassable
                                        : 1 + (PathTile.deep(tile) ? notPath : 0) + (PathTile.damages(tile) ? 50 : 0)
                                                + (PathTile.nearSolid(tile) ? 50 : 0)
                                                + (PathTile.nearLiquid(tile) ? 10 : 0)
                                                + PathTile.health(tile) * 999999);

        costTypes.set(costLegs,
                (team, tile) -> PathTile.legSolid(tile) ? impassable
                        : 1 + (PathTile.deep(tile) ? notPath : 0) + (PathTile.damages(tile) ? 50 : 0)
                                + (PathTile.nearLegSolid(tile) ? 50 : 0) + (PathTile.nearSolid(tile) ? 10 : 0)
                                + PathTile.health(tile) * 999999);

        costTypes.set(costNaval,
                (team, tile) -> (PathTile.solid(tile) || !PathTile.liquid(tile) ? notPath : 1)
                        + (PathTile.damages(tile) ? 50 : 0) + (PathTile.nearSolid(tile) ? 10 : 0)
                        + (PathTile.nearGround(tile) ? 10 : 0) + PathTile.health(tile) * 999999);

        costTypes.set(costHover,
                (team, tile) -> (((PathTile.team(tile) == team && !PathTile.teamPassable(tile))
                        || PathTile.team(tile) == 0) && PathTile.solid(tile)) ? impassable
                                : 1 +
                                        PathTile.health(tile) * 5 +
                                        (PathTile.nearSolid(tile) ? 2 : 0)
                                        + PathTile.health(tile) * 999999);

    }

    @Override
    public int packTile(Tile tile) {
        boolean nearLiquid = false, nearDeep = false, nearSolid = false, nearLegSolid = false, nearGround = false,
                allDeep = tile.floor().isDeep();

        for (int i = 0; i < 4; i++) {
            var other = tile.nearby(i);
            if (other == null)
                continue;

            var isOtherPath = isPath(other);

            if (other.floor().isLiquid)
                nearLiquid = true;
            if (other.solid() || !isOtherPath)
                nearSolid = true;
            if (other.legSolid() || !isOtherPath)
                nearLegSolid = true;
            if (!other.floor().isLiquid)
                nearGround = true;
            if (!other.floor().isDeep())
                allDeep = false;
            if (other.floor().isDeep())
                nearDeep = true;
        }

        var isTilePath = isPath(tile);

        return PathTile.get(isTilePath ? 0 : 10000, //
                tile.getTeamID(), //
                tile.solid() || !isTilePath, //
                tile.floor().isLiquid, //
                tile.legSolid() || !isTilePath, //
                nearLiquid,
                nearGround, //
                nearSolid, //
                nearLegSolid, //
                tile.floor().isDeep() || !isTilePath, //
                tile.floor().damageTaken > 0f || !isTilePath, //
                allDeep, //
                nearDeep,
                tile.block().teamPassable);
    }

    public static boolean isPath(Tile tile) {
        return Blocks.darkPanel5 == tile.floor();
    }
}
