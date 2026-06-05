package Exy.relivingthemoment;

import Exy.exthylanes_extra_stuff.util.itemUtils.ItemUtils;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Relivingthemoment implements ModInitializer {

    public static final String MODID = "relivingthemoment";

    public static final Item WretchLeather = new Item(new Item.Settings());
    public static final WretchLeatherBeltItem WretchLeatherBelt = new WretchLeatherBeltItem(new Item.Settings());

    public static final SoulDebtStatusEffect SOUL_DEBT = Registry.register(Registries.STATUS_EFFECT,new Identifier(MODID,"soul_debt"),new SoulDebtStatusEffect());

    private static final Identifier EVOKER_LOOT = EntityType.EVOKER.getLootTableId();

    @Override
    public void onInitialize() {
        MidnightConfig.init(MODID, RelivingTheMomentConfig.class);
        ItemUtils.register("wretchleather",WretchLeather,MODID);
        ItemUtils.register("wretchleatherbelt",WretchLeatherBelt,MODID);
        ItemUtils.shoveIntoItemGroup(WretchLeatherBelt, ItemGroups.COMBAT);
        ItemUtils.shoveIntoItemGroup(WretchLeather, ItemGroups.INGREDIENTS);

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (EVOKER_LOOT.equals(id) && source.isBuiltin()) {

                LootPool.Builder pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(
                                ItemEntry.builder(WretchLeather)
                                        .weight(1)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1, 2)
                                        ))
                        ).with(
                                EmptyEntry.builder()
                                        .weight(4)
                        );

                tableBuilder.pool(pool);
            }
        });

    }
}
