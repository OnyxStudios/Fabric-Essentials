package nerdhub.fabricessentials.data;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;

public class PlayerHomeDataObj {

    private Map<String, BlockPos> homes = Maps.newHashMap();

    public PlayerHomeDataObj() {
    }

    public Map<String, BlockPos> getHomes() {
        return homes;
    }

    public PlayerHomeDataObj addHome(String name, BlockPos pos) {
        this.homes.put(name, pos);
        return this;
    }

    public BlockPos getHome(String name) {
        return homes.get(name);
    }

    public void removeHome(String name) {
        this.homes.remove(name);
    }

    public static PlayerHomeDataObj deserialize(CompoundTag tag) {
        PlayerHomeDataObj playerHomeData = new PlayerHomeDataObj();
        ListTag homesListTag = tag.getList("homes", NbtType.COMPOUND);

        for (Iterator<Tag> it = homesListTag.iterator(); it.hasNext();) {
            CompoundTag compoundTag = (CompoundTag) it.next();
            BlockPos pos = TagHelper.deserializeBlockPos(compoundTag.getCompound("blockpos"));
            playerHomeData.addHome(compoundTag.getString("name"), pos);
        }

        return playerHomeData;
    }

    public static CompoundTag serialize(PlayerHomeDataObj data) {
        CompoundTag tag = new CompoundTag();
        ListTag homesList  = new ListTag();

        for (String name : data.homes.keySet()) {
            CompoundTag homesTag = new CompoundTag();
            homesTag.putString("name", name);
            homesTag.put("blockpos", TagHelper.serializeBlockPos(data.homes.get(name)));
            homesList.add(homesTag);
        }

        tag.put("homes", homesList);
        return tag;
    }
}
