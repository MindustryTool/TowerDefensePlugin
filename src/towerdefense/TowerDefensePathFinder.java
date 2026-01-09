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
                        || ((PathTile.team(tile) == 0 || PathTile.team(tile) == team) && PathTile.solid(tile)))
                                ? impassable
                                : 1 + (PathTile.deep(tile) ? notPath : 0) + (PathTile.damages(tile) ? 50 : 0)
                                        + (PathTile.nearSolid(tile) ? 50 : 0) + (PathTile.nearLiquid(tile) ? 10 : 0));

        costTypes.set(costLegs,
                (team, tile) -> (PathTile.allDeep(tile) || PathTile.legSolid(tile)) ? impassable
                        : 1 + (PathTile.deep(tile) ? notPath : 0) + (PathTile.damages(tile) ? 50 : 0)
                                + (PathTile.nearLegSolid(tile) ? 50 : 0) + (PathTile.nearSolid(tile) ? 10 : 0));

        costTypes.set(costNaval,
                (team, tile) -> (PathTile.solid(tile) || !PathTile.liquid(tile) ? notPath : 1)
                        + (PathTile.damages(tile) ? 50 : 0) + (PathTile.nearSolid(tile) ? 10 : 0)
                        + (PathTile.nearGround(tile) ? 10 : 0));
    }

    // @Override
    // public int packTile(Tile tile) {
    //     boolean nearLiquid = false, nearDeep = false, nearSolid = false, nearLegSolid = false, nearGround = false,
    //             allDeep = tile.floor().isDeep();

    //     for (int i = 0; i < 4; i++) {
    //         var other = tile.nearby(i);
    //         if (other == null)
    //             continue;

    //         if (other.floor().isLiquid)
    //             nearLiquid = true;
    //         if (other.solid() || !isPath(other))
    //             nearSolid = true;
    //         if (other.legSolid() || !isPath(other))
    //             nearLegSolid = true;
    //         if (!other.floor().isLiquid)
    //             nearGround = true;
    //         if (!other.floor().isDeep())
    //             allDeep = false;
    //         if (other.floor().isDeep())
    //             nearDeep = true;
    //     }

    //     return PathTile.get(0, //
    //             tile.getTeamID(), //
    //             tile.solid(), //
    //             tile.floor().isLiquid, //
    //             tile.legSolid(), //
    //             nearLiquid,
    //             nearGround, //
    //             nearSolid, //
    //             nearLegSolid, //
    //             tile.floor().isDeep() || !isPath(tile), //
    //             tile.floor().damageTaken > 0f || !isPath(tile), //
    //             allDeep, //
    //             nearDeep, 
    //             tile.block().teamPassable);
    // }

    public static boolean isPath(Tile tile) {
        return tile.floor() == Blocks.darkPanel4 || tile.floor() == Blocks.darkPanel5;
    }
}
