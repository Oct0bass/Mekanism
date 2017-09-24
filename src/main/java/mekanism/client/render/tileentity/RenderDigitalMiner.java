package mekanism.client.render.tileentity;

import mekanism.client.render.MinerVisualRenderer;
import mekanism.common.tile.TileEntityDigitalMiner;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDigitalMiner extends TileEntitySpecialRenderer<TileEntityDigitalMiner>
{
	@Override
	public void render(TileEntityDigitalMiner tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha)
	{
		if(tileEntity.clientRendering)
		{
			MinerVisualRenderer.render(tileEntity);
		}
	}
}
