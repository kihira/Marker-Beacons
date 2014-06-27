/*
 * Copyright (C) 2014  Kihira
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package kihira.markerbeacons.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11

object RenderHelper {

  //Following methods are copied from iChunUtil with permissions
  def startGlScissor(x: Int, y: Int, width: Int, height: Int) {
    val mc: Minecraft = Minecraft.getMinecraft
    val reso: ScaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
    val scaleW: Double = mc.displayWidth.asInstanceOf[Double] / reso.getScaledWidth_double
    val scaleH: Double = mc.displayHeight.asInstanceOf[Double] / reso.getScaledHeight_double
    GL11.glEnable(GL11.GL_SCISSOR_TEST)
    GL11.glScissor(Math.floor(x.asInstanceOf[Double] * scaleW).asInstanceOf[Int], Math.floor(mc.displayHeight.asInstanceOf[Double] - ((y + height).asInstanceOf[Double] * scaleH)).asInstanceOf[Int], Math.floor((x + width).asInstanceOf[Double] * scaleW).asInstanceOf[Int] - Math.floor(x.asInstanceOf[Double] * scaleW).asInstanceOf[Int], Math.floor(mc.displayHeight.asInstanceOf[Double] - (y.asInstanceOf[Double] * scaleH)).asInstanceOf[Int] - Math.floor(mc.displayHeight.asInstanceOf[Double] - ((y + height).asInstanceOf[Double] * scaleH)).asInstanceOf[Int])
  }

  def endGlScissor() {
    GL11.glDisable(GL11.GL_SCISSOR_TEST)
  }
}
