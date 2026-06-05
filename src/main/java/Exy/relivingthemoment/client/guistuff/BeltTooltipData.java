package Exy.relivingthemoment.client.guistuff;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class BeltTooltipData implements TooltipData {
    private final DefaultedList<ItemStack> inventory;
    private final int bundleOccupancy;

    public BeltTooltipData(DefaultedList<ItemStack> inventory, int bundleOccupancy) {
        this.inventory = inventory;
        this.bundleOccupancy = bundleOccupancy;
    }

    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    public int getBundleOccupancy() {
        return this.bundleOccupancy;
    }
}