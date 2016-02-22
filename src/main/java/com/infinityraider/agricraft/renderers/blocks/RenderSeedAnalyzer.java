package com.infinityraider.agricraft.renderers.blocks;

import com.infinityraider.agricraft.container.ContainerSeedAnalyzer;
import com.infinityraider.agricraft.handler.config.ConfigurationHandler;
import com.infinityraider.agricraft.init.AgriCraftBlocks;
import com.infinityraider.agricraft.reference.Constants;
import com.infinityraider.agricraft.renderers.RenderUtil;
import com.infinityraider.agricraft.renderers.TessellatorV2;
import com.infinityraider.agricraft.renderers.models.ModelSeedAnalyzer;
import com.infinityraider.agricraft.renderers.models.ModelSeedAnalyzerBook;
import com.infinityraider.agricraft.tileentity.TileEntitySeedAnalyzer;
import com.infinityraider.agricraft.utility.AgriForgeDirection;
import com.infinityraider.agricraft.utility.icon.BaseIcons;
import com.infinityraider.agricraft.utility.icon.IconUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/*
 * TODO: Convert to new Renderer.
 */
@SideOnly(Side.CLIENT)
public class RenderSeedAnalyzer extends RenderBlockAgriCraft {

	private final ModelSeedAnalyzer modelSeedAnalyzer;
	private final ModelSeedAnalyzerBook modelBook;
	//dummy TileEntity for inventory rendering
	private final TileEntitySeedAnalyzer seedAnalyzerDummy;

	public RenderSeedAnalyzer() {
		super(AgriCraftBlocks.blockSeedAnalyzer, new TileEntitySeedAnalyzer(), true, true, false);
		this.modelSeedAnalyzer = new ModelSeedAnalyzer();
		this.modelBook = new ModelSeedAnalyzerBook();
		seedAnalyzerDummy = new TileEntitySeedAnalyzer();
		seedAnalyzerDummy.setOrientation(AgriForgeDirection.SOUTH);
	}

	@Override
	protected void doInventoryRender(TessellatorV2 tess, ItemStack item) {
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		this.doRenderTileEntity(tess, seedAnalyzerDummy);
		GL11.glPopMatrix();
	}

	/**
	 * Much Hack. Very Bad.
	 *
	 * @param tess
	 * @param te
	 */
	@Override
	protected void doRenderTileEntity(TessellatorV2 tess, TileEntity te) {
		if (te instanceof TileEntitySeedAnalyzer) {
			tess.draw();
			TileEntitySeedAnalyzer analyzer = (TileEntitySeedAnalyzer) te;
			//render the model
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 1.5F, 0.5F);
			GL11.glRotatef(180, 0F, 0F, 1F);
			this.modelSeedAnalyzer.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			if (analyzer.hasJournal() && ConfigurationHandler.renderBookInAnalyzer) {
				this.modelBook.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			}
			GL11.glPopMatrix();
			if (analyzer.hasSeed() || analyzer.hasTrowel()) {
				renderSeed(tess, analyzer);
			}
			tess.startDrawingQuads();
		}
	}

	//renders the seed
	private void renderSeed(TessellatorV2 tessellator, TileEntitySeedAnalyzer analyzer) {
		//grab the texture
		ItemStack stack = analyzer.getStackInSlot(ContainerSeedAnalyzer.seedSlotId);
		TextureAtlasSprite icon = IconUtil.getParticleIcon(stack); //TODO: find seed icon
		if (icon == null) {
			return;
		}
		//define rotation angle in function of system time
		float angle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);   //credits to Pahimar
		GL11.glPushMatrix();
		//translate to the desired position
		GL11.glTranslated(Constants.UNIT * 8, Constants.UNIT * 4, Constants.UNIT * 8);
		//resize the texture to half the size
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		//rotate the renderer
		GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-8 * Constants.UNIT, 0, 0);

		//TODO: render the seed
		/*
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        ItemRenderer.renderItemIn2D(tessellator, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), Constants.UNIT);
		 */
		GL11.glTranslatef(8 * Constants.UNIT, 0, 0);
		GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(2, 2, 2);
		GL11.glPopMatrix();
	}

	private static void renderBase(TessellatorV2 tess) {

		// Get Icons
		final TextureAtlasSprite trimIcon = BaseIcons.OAK_PLANKS.getIcon();
		final TextureAtlasSprite baseIcon = BaseIcons.IRON_BLOCK.getIcon();

		// Render Trimming
		// Forward-Back
		RenderUtil.drawScaledPrism(tess, 0, 0, 0, 16, 4, 4, trimIcon);
		RenderUtil.drawScaledPrism(tess, 0, 0, 12, 16, 4, 4, trimIcon);
		// Left-Right
		RenderUtil.drawScaledPrism(tess, 0, 0, 4, 4, 4, 12, trimIcon);
		RenderUtil.drawScaledPrism(tess, 12, 0, 4, 16, 4, 12, trimIcon);

		// Render base
		RenderUtil.drawScaledPrism(tess, 4, 0, 4, 12, 0, 12, baseIcon);

	}

}
