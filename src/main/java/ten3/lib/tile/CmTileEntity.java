package ten3.lib.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import ten3.TConst;
import ten3.core.network.Network;
import ten3.core.network.check.PTCCheckPack;
import ten3.core.network.check.PTSCheckPack;
import ten3.init.BlockInit;
import ten3.init.ContInit;
import ten3.init.TileInit;
import ten3.lib.capability.item.InventoryCm;
import ten3.lib.wrapper.IntArrayCm;
import ten3.lib.wrapper.SlotCm;
import ten3.util.TranslateKeyUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class CmTileEntity extends BlockEntity implements MenuProvider {

    public static CmTileEntity ofType(BlockEntityType<?> type, BlockPos... pos) {
        return (CmTileEntity) type.create(pos.length > 0 ? pos[0] : BlockPos.ZERO,
                BlockInit.getBlock(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type).getPath()).defaultBlockState());
    }

    public IntArrayCm data = ContInit.createDefaultIntArr();
    public Component component;
    public String id;
    public ArrayList<SlotCm> slots = new ArrayList<>();
    public InventoryCm inventory = ContInit.createDefaultInv(slots);

    public Level world;
    public BlockPos pos;

    boolean init;

    public void addSlot(SlotCm s) {
        slots.add(s);
    }

    public CmTileEntity(String key, BlockPos pos, BlockState state) {
        super(TileInit.getType(key), pos, state);
        component = TranslateKeyUtil.translated(TConst.modid + "." + key);
        id = key;
    }

    public int @NotNull [] getItemFirstTransferSlot(Item i) {
        return new int[] {};
    }

    public void rdt(CompoundTag nbt) {
    }

    public void wdt(CompoundTag nbt) {
    }

    public void load(CompoundTag nbt) {

        rdt(nbt);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.of(nbt.getCompound("item" + i)));
        }
        init = nbt.getBoolean("init");
        loaded = true;

        super.load(nbt);

    }

    public void saveAdditional(CompoundTag compound) {

        wdt(compound);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            compound.put("item" + i, inventory.getItem(i).copy().save(new CompoundTag()));
        }
        compound.putBoolean("init", init);

        super.saveAdditional(compound);

    }

    boolean loaded;

    public List<ItemStack> drops() {

        List<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!canDrop(inventory.getItem(i), i))
                continue;
            stacks.add(inventory.getItem(i));
        }

        return stacks;

    }

    protected boolean canDrop(ItemStack stack, int slot) {
        return true;
    }

    public static void sendCheckPack() {

        Network.sendToClient(new PTCCheckPack());

    }

    @Deprecated
    public static int UPDATE_INITIAL_TIME = 30;

    private boolean init_rerun;
    protected int globalTimer = 0;

    public int getTileAliveTime() {
        return globalTimer;
    }

    public void serverTick() {

        world = level;
        pos = worldPosition;

        if (level == null)
            return;

        if (!level.isClientSide()) {
            globalTimer++;
            if (!init) {
                init = true;
                init();
            }
            if (!init_rerun) {
                if (loaded) {
                    if (PTSCheckPack.GET) {
                        init_rerun = true;
                        packets();
                    } else {
                        sendCheckPack();
                    }
                }
            } else {
                update();
                endTick();
            }
        }

        updateRemote();

    }

    public void endTick() {}

    public void init() {}

    public void packets() {}

    public void updateRemote() {}

    public void update() {}

    public Component getDisplayName() {

        return component;

    }

    protected IntArrayCm createData() {
        return data;
    }

    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

}
