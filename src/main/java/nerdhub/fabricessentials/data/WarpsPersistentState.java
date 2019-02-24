package nerdhub.fabricessentials.data;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.Iterator;
import java.util.Map;

public class WarpsPersistentState extends PersistentState {

    public Map<String, BlockPos> warps = Maps.newHashMap();

    public WarpsPersistentState() {
        super("FabricEssentialsWarpsPersistentState");
    }

    public boolean hasWarp(String name) {
        return this.warps.containsKey(name);
    }

    public void addWarp(String name, BlockPos pos) {
        this.warps.put(name, pos);
    }

    public void removeWarp(String name) {
        this.warps.remove(name);
    }

    public BlockPos getWarp(String name) {
        return warps.get(name);
    }

    public static WarpsPersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(WarpsPersistentState::new, "FabricEssentialsWarpsPersistentState");
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("data", NbtType.COMPOUND);

        for (Iterator<Tag> it = listTag.iterator(); it.hasNext(); ) {
            CompoundTag tag = (CompoundTag) it.next();
            warps.put(tag.getString("name"), TagHelper.deserializeBlockPos(tag.getCompound("blockpos")));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for (String name : warps.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", name);
            tag.put("blockpos", TagHelper.serializeBlockPos(warps.get(name)));
            listTag.add(tag);
        }

        compoundTag.put("data", listTag);

        return compoundTag;
    }
}
