package Exy.relivingthemoment.client.guistuff;

import Exy.relivingthemoment.Relivingthemoment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class BeltTooltipComponent implements TooltipComponent {
    public static final Identifier TEXTURE = new Identifier(Relivingthemoment.MODID,"textures/gui/container/belt.png");
    private final DefaultedList<ItemStack> inventory;

    public BeltTooltipComponent(BeltTooltipData data) {
        this.inventory = data.getInventory();

    }

    @Override
    public int getHeight() {
        return this.getRows() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumns() * 18 + 2;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
         int n = x  + 1;
         int o = y  + 1;
         this.drawSlot(n, o, 0, context, textRenderer);
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        this.draw(context, x, y, BeltTooltipComponent.Sprite.SLOT);
        if (!(index >= this.inventory.size())) {
            ItemStack itemStack = this.inventory.get(index);
            this.draw(context, x, y, BeltTooltipComponent.Sprite.SLOT);
            context.drawItem(itemStack, x + 1, y + 3, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 3);
        }
    }


    private void draw(DrawContext context, int x, int y, BeltTooltipComponent.Sprite sprite) {
        context.drawTexture(TEXTURE, x, y, 0, sprite.u, sprite.v, sprite.width, sprite.height, 128, 128);
    }



    private int getColumns() {
        return Math.max(2, (int)Math.ceil(Math.sqrt(1.0)));
    }

    private int getRows() {
        return (int)Math.ceil((1.0) / this.getColumns());
    }

    @Environment(EnvType.CLIENT)
     enum Sprite {
        SLOT(0, 0, 18, 20);

        public final int u;
        public final int v;
        public final int width;
        public final int height;

         Sprite(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }
    }
}
