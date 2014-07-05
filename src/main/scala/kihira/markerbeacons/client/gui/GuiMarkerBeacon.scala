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

package kihira.markerbeacons.client.gui

import java.util

import kihira.foxlib.client.gui.{GuiList, GuiSlider}
import kihira.markerbeacons.client.icon.{IconData, IconManager}
import kihira.markerbeacons.common._
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry
import net.minecraft.client.gui._
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.{ResourceLocation, StatCollector}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._

class GuiMarkerBeacon(tileEntityMarkerBeacon: TileEntityMarkerBeacon) extends GuiScreen {

  var textField: GuiTextField = null
  var sliderHeight: GuiSlider = null
  var sliderScale: GuiSlider = null
  var sliderOffset: GuiSlider = null
  var toggleYAxis: GuiButtonToggle = null
  var toggleXAxis: GuiButtonToggle = null
  var sliderXAngle: GuiSlider = null
  var sliderYAngle: GuiSlider = null
  var sliderCount: GuiSlider = null
  var sliderRotationSpeed: GuiSlider = null
  var listIcon: GuiList = null

  val backgroundTexture: ResourceLocation = new ResourceLocation(MarkerBeacons.MOD_ID, "textures/gui/beacon.png")
  val xSize: Int = 256
  val ySize: Int = 200

  override def initGui() {
    Keyboard.enableRepeatEvents(true)

    val leftBorder: Int = (this.width - this.xSize) / 2
    val topBorder: Int = (this.height - this.ySize) / 2

    //Temp for now until we add support for adding in more
    var textComp: TextComponent = null
    var imageComp: ImageComponent = null

    for (component: LogoComponent <- tileEntityMarkerBeacon.beaconData.components) {
      component match {
        case component: TextComponent => textComp = component
        case component: ImageComponent => imageComp = component
      }
    }

    this.textField = new GuiTextField(this.fontRendererObj, leftBorder + 143, topBorder + 134, 103, 12)
    this.textField.setTextColor(-1)
    this.textField.setDisabledTextColour(-1)
    this.textField.setEnableBackgroundDrawing(false)
    this.textField.setMaxStringLength(40)
    if (textComp != null) {
      this.textField.setText(textComp.text)
      this.textField.setTextColor(textComp.textColour)
    }

    this.listIcon = new GuiList(110, 120, topBorder + 5, topBorder + 125, leftBorder + 140, {
        val list: ListBuffer[IGuiListEntry] = ListBuffer[IGuiListEntry]()
        for (icon: IconData <- IconManager.iconList) {
            list += new IconEntry(icon)
        }
        list
    })
    if (imageComp != null) {
      this.listIcon.currentIndex = IconManager.iconList.indexOf(imageComp.iconData)
    }

    this.sliderHeight = new GuiSlider(null, 2, leftBorder + 5, topBorder + 5, 120, 20, StatCollector.translateToLocal("gui.button.height"), 1, 50, tileEntityMarkerBeacon.beaconData.height, 0)
    this.sliderScale = new GuiSlider(null, 3, leftBorder + 5, topBorder + 27, 120, 20, StatCollector.translateToLocal("gui.button.scale"), 0, 5, tileEntityMarkerBeacon.beaconData.scale, 2)
    this.sliderOffset = new GuiSlider(null, 4, leftBorder + 5, topBorder + 49, 120, 20, StatCollector.translateToLocal("gui.button.offset"), 0, 20, tileEntityMarkerBeacon.beaconData.offset, 2)
    this.toggleXAxis = new GuiButtonToggle(5, leftBorder + 5, topBorder + this.ySize - 47, 80, 20, "facex")
    this.toggleYAxis = new GuiButtonToggle(6, leftBorder + 5 + 82, topBorder + this.ySize - 47, 80, 20, "facey")
    this.sliderXAngle = new GuiSlider(null, 7, leftBorder + 5, topBorder + this.ySize - 25, 80, 20, StatCollector.translateToLocal("gui.button.anglex"), 0, 360, tileEntityMarkerBeacon.beaconData.angleX, 1)
    this.sliderYAngle = new GuiSlider(null, 8, leftBorder + 5 + 82, topBorder + this.ySize - 25, 80, 20, StatCollector.translateToLocal("gui.button.anglex"), 0, 360, tileEntityMarkerBeacon.beaconData.angleY, 1)
    this.sliderCount = new GuiSlider(null, 9, leftBorder + 5, topBorder + 71, 120, 20, StatCollector.translateToLocal("gui.button.count"), 1, 25, tileEntityMarkerBeacon.beaconData.count, 0)
    this.sliderRotationSpeed = new GuiSlider(null, 10, leftBorder + 5, topBorder + 93, 120 ,20, StatCollector.translateToLocal("gui.button.rotspeed"), -10, 10, tileEntityMarkerBeacon.beaconData.rotationSpeed, 2)

    this.toggleXAxis.enabled = tileEntityMarkerBeacon.beaconData.facePlayerX
    this.toggleYAxis.enabled = tileEntityMarkerBeacon.beaconData.facePlayerY

    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, leftBorder + this.xSize - 55, topBorder + this.ySize - 25, 50, 20, StatCollector.translateToLocal("gui.button.done")))
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(1, leftBorder + this.xSize - 55, topBorder + this.ySize - 46, 50, 20, StatCollector.translateToLocal("gui.button.cancel")))
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderHeight)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderScale)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderOffset)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.toggleYAxis)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.toggleXAxis)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderXAngle)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderYAngle)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderCount)
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(this.sliderRotationSpeed)

    updateButtons()
  }

  override protected def actionPerformed(button : GuiButton) {
    button.id match {
      case 0 =>
        this.saveBeaconData()
        Minecraft.getMinecraft.displayGuiScreen(null)
      case 1 => Minecraft.getMinecraft.displayGuiScreen(null)
      case 5 =>
        //Toggle state
        button.enabled = !button.enabled
      case 6 =>
        //Toggle state
        button.enabled = !button.enabled
      //Default
      case _ =>
    }

    this.updateButtons()
  }

  private def updateButtons() {
    //Update X/Y angle sliders based on if their face toggle state
    this.sliderXAngle.enabled = !this.toggleXAxis.enabled
    this.sliderYAngle.enabled = !this.toggleYAxis.enabled
  }

  protected override def mouseClicked(par1: Int, par2: Int, par3: Int) {
    super.mouseClicked(par1, par2, par3)
    this.textField.mouseClicked(par1, par2, par3)
  }

  protected override def keyTyped(par1: Char, par2: Int) {
    super.keyTyped(par1, par2)
    this.textField.textboxKeyTyped(par1, par2)
  }

  private def saveBeaconData() {
    val beaconData: BeaconData = tileEntityMarkerBeacon.beaconData

    beaconData.height = this.sliderHeight.currentValue
    beaconData.scale = this.sliderScale.currentValue
    beaconData.offset = this.sliderOffset.currentValue
    beaconData.angleX = this.sliderXAngle.currentValue
    beaconData.angleY = this.sliderYAngle.currentValue
    beaconData.facePlayerX = this.toggleXAxis.enabled
    beaconData.facePlayerY = this.toggleYAxis.enabled
    beaconData.count = this.sliderCount.currentValue.asInstanceOf[Int]
    beaconData.rotationSpeed = this.sliderRotationSpeed.currentValue
    beaconData.components = new util.ArrayList[LogoComponent]()

    val textComp: TextComponent  = new TextComponent
    textComp.text = this.textField.getText
    textComp.textColour = 0xFFFFFF
    beaconData.components.add(textComp)

    val imageComp: ImageComponent  = new ImageComponent
    if (this.listIcon.currentIndex > 0 && this.listIcon.currentIndex < this.listIcon.getSize) imageComp.iconData = IconManager.iconList(this.listIcon.currentIndex)
    beaconData.components.add(imageComp)

    PacketHandler.eventChannel.sendToServer(PacketHandler.createBeaconDataPacket(tileEntityMarkerBeacon.getWorldObj.provider.dimensionId,
      tileEntityMarkerBeacon.xCoord, tileEntityMarkerBeacon.yCoord, tileEntityMarkerBeacon.zCoord, beaconData))
  }

  override def onGuiClosed() {
    super.onGuiClosed()
    Keyboard.enableRepeatEvents(false)
  }

  private def drawBackground(p_146976_1_ : Float, p_146976_2_ : Int, p_146976_3_ : Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    this.mc.getTextureManager.bindTexture(this.backgroundTexture)
    val k: Int = (this.width - this.xSize) / 2
    val l: Int = (this.height - this.ySize) / 2
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize)
  }

  override def drawScreen(par1: Int, par2: Int, par3: Float) {
    this.drawBackground(par3, par1, par2)
    this.listIcon.drawScreen(par1, par2, par3)
    super.drawScreen(par1, par2, par3)
/*    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glDisable(GL11.GL_BLEND)*/
    this.textField.drawTextBox()

    breakable {
      for (button <- this.buttonList) {
        if (button.asInstanceOf[GuiButton].func_146115_a) {
          button.asInstanceOf[GuiButton].func_146111_b(par1, par2)
        }
      }
    }
  }

  class GuiButtonToggle(id: Int, xPos: Int, yPos: Int, width: Int, height: Int, text: String) extends GuiButtonTooltip(id, xPos, yPos, width, height, text) {

    override def mousePressed(p_146116_1_ : Minecraft, p_146116_2_ : Int, p_146116_3_ : Int): Boolean = {
      this.visible && p_146116_2_ >= this.xPosition && p_146116_3_ >= this.yPosition && p_146116_2_ < this.xPosition + this.width && p_146116_3_ < this.yPosition + this.height
    }
  }

  class GuiButtonTooltip(id: Int, xPos: Int, yPos: Int, width: Int, height: Int, text: String) extends GuiButton(id, xPos, yPos, width, height, StatCollector.translateToLocal("gui.button." + text)) {

    override def func_146111_b(x: Int, y: Int) {
      drawCreativeTabHoveringText(StatCollector.translateToLocal("gui.button." + text + ".tooltip"), x, y)
    }
  }

    class IconEntry(iconData: IconData) extends IGuiListEntry {
        override def drawEntry(var1: Int, x: Int, y: Int, listWidth: Int, var5: Int, var6: Tessellator, var7: Int, var8: Int, var9: Boolean) {
            IconManager.bindTexture(iconData)
            GL11.glPushMatrix()
            //Move to this point to allow us to scale properly
            GL11.glTranslatef(listIcon.left + 1, y - 1, 0)
            //If wider then taller (or equal), scale along width
            if (iconData.width >= iconData.height) {
                //Center icon
                if (iconData.width > iconData.height) GL11.glTranslatef(0F, 18F / iconData.width, 0F)
                GL11.glScalef(18F / iconData.width, 18F / iconData.width, 1F)
            }
            else {
                //Center the icon
                GL11.glTranslatef(18F / iconData.height, 18F / iconData.height, 0F)
                GL11.glScalef(18F / iconData.height, 18F / iconData.height, 1F)
            }
            //Draw the icon on a GUI
            Gui.func_146110_a(0, 0, iconData.minU, iconData.minV, iconData.width, iconData.height, iconData.textureXSize, iconData.textureYSize)
            GL11.glPopMatrix()

            //Render the icon name
            val fontRenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
            fontRenderer.drawString(iconData.iconName, listIcon.left + 22, y + 5, 0xFFFFFF)
            GL11.glColor4f(1F, 1F, 1F, 1F)
        }

        override def mousePressed(index: Int, var2: Int, var3: Int, var4: Int, var5: Int, var6: Int): Boolean = {
            true
        }

        override def mouseReleased(index: Int, var2: Int, var3: Int, var4: Int, var5: Int, var6: Int) {}
    }
}
