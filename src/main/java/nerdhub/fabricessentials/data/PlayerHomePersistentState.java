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

import java.util.*;

public class PlayerHomePersistentState extends PersistentState {

    private Map<UUID, PlayerHomeDataObj> playerHomesData = Maps.newHashMap();

    public PlayerHomePersistentState() {
        super("FabricEssentialsHomePersistentState");
    }

    public boolean doesPlayerHaveEntry(UUID uuid) {
        return playerHomesData.containsKey(uuid);
    }

    public void addPlayerHome(UUID uuid, String home, BlockPos pos) {
        this.markDirty();
        this.playerHomesData.computeIfAbsent(uuid, playerHomesData -> new PlayerHomeDataObj()).addHome(home, pos);
    }

    public BlockPos getPlayerHome(UUID uuid, String name) {
        this.markDirty();
        return doesPlayerHaveEntry(uuid) ? playerHomesData.get(uuid).getHome(name) : null;
    }

    public Set<String> getHomes(UUID uuid) {
        return doesPlayerHaveEntry(uuid) ? playerHomesData.get(uuid).getHomes().keySet() : Collections.EMPTY_SET;
    }

    public boolean doesPlayerHaveHome(UUID uuid, String home) {
        return doesPlayerHaveEntry(uuid) ? getPlayerHome(uuid, home) != null : false;
    }

    public void clearPlayerHomes(UUID uuid) {
        if (doesPlayerHaveEntry(uuid)) {
            playerHomesData.get(uuid).getHomes().clear();
            this.markDirty();
        }
    }

    public void deletePlayerHome(UUID uuid, String name) {
        if(doesPlayerHaveEntry(uuid)) {
            playerHomesData.get(uuid).removeHome(name);
            this.markDirty();
        }
    }

    public static PlayerHomePersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(PlayerHomePersistentState::new, "FabricEssentialsHomePersistentState");
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("playerHomeDataList", NbtType.COMPOUND);
        playerHomesData.clear();

        for (Iterator<Tag> it = listTag.iterator(); it.hasNext(); ) {
            CompoundTag tag = (CompoundTag) it.next();
            UUID uuid = TagHelper.deserializeUuid(tag.getCompound("uuid"));
            playerHomesData.put(uuid, PlayerHomeDataObj.deserialize(tag.getCompound("data")));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for (UUID uuid : playerHomesData.keySet()) {
            CompoundTag tag = new CompoundTag();
            tag.put("uuid", TagHelper.serializeUuid(uuid));
            tag.put("data", PlayerHomeDataObj.serialize(playerHomesData.get(uuid)));
            listTag.add(tag);
        }
        compoundTag.put("playerHomeDataList", listTag);

        return compoundTag;
    }
}
