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

package kihira.beacons.client.gui

import java.util

import kihira.beacons.client.icon.{IconManager, IconData}
import kihira.beacons.client.render.RenderHelper
import kihira.beacons.common._
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry
import net.minecraft.client.gui._
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.{StatCollector, ResourceLocation}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

import scala.collection.JavaConversions._
import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer

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
  var listIcon: GuiIconList = null

  val backgroundTexture: ResourceLocation = new ResourceLocation("beacons", "textures/gui/beacon.png")
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

    this.textField = new GuiTextField(this.fontRendererObj, leftBorder + 140, topBorder + 135, 103, 12)
    this.textField.setTextColor(-1)
    this.textField.setDisabledTextColour(-1)
    this.textField.setEnableBackgroundDrawing(false)
    this.textField.setMaxStringLength(40)
    if (textComp != null) {
      this.textField.setText(textComp.text)
      this.textField.setTextColor(textComp.textColour)
    }

    this.listIcon = new GuiIconList(110, 120, topBorder + 5, topBorder + 125, leftBorder + 140, IconManager.iconList)
    if (imageComp != null) {
      this.listIcon.currentIndex = IconManager.iconList.indexOf(imageComp.iconData)
    }

    this.sliderHeight = new GuiSlider(2, leftBorder + 5, topBorder + 5, 120, 20, StatCollector.translateToLocal("gui.button.height"), 1, 50, tileEntityMarkerBeacon.beaconData.height, 0)
    this.sliderScale = new GuiSlider(3, leftBorder + 5, topBorder + 27, 120, 20, StatCollector.translateToLocal("gui.button.scale"), 0, 5, tileEntityMarkerBeacon.beaconData.scale, 2)
    this.sliderOffset = new GuiSlider(4, leftBorder + 5, topBorder + 49, 120, 20, StatCollector.translateToLocal("gui.button.offset"), 0, 20, tileEntityMarkerBeacon.beaconData.offset, 2)
    this.toggleXAxis = new GuiButtonToggle(5, leftBorder + 5, topBorder + this.ySize - 47, 80, 20, "facex")
    this.toggleYAxis = new GuiButtonToggle(6, leftBorder + 5 + 82, topBorder + this.ySize - 47, 80, 20, "facey")
    this.sliderXAngle = new GuiSlider(7, leftBorder + 5, topBorder + this.ySize - 25, 80, 20, StatCollector.translateToLocal("gui.button.anglex"), 0, 360, tileEntityMarkerBeacon.beaconData.angleX, 1)
    this.sliderYAngle = new GuiSlider(8, leftBorder + 5 + 82, topBorder + this.ySize - 25, 80, 20, StatCollector.translateToLocal("gui.button.anglex"), 0, 360, tileEntityMarkerBeacon.beaconData.angleY, 1)
    this.sliderCount = new GuiSlider(9, leftBorder + 5, topBorder + 71, 120, 20, StatCollector.translateToLocal("gui.button.count"), 1, 25, tileEntityMarkerBeacon.beaconData.count, 0)
    this.sliderRotationSpeed = new GuiSlider(10, leftBorder + 5, topBorder + 93, 120 ,20, StatCollector.translateToLocal("gui.button.rotspeed"), -10, 10, tileEntityMarkerBeacon.beaconData.rotationSpeed, 2)

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
        this.saveSettings()
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

  private def saveSettings() {
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
    imageComp.iconData = IconManager.iconList(this.listIcon.currentIndex)
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

  class GuiIconList(width: Int, height: Int, top: Int, bottom: Int, leftM: Int, iconList: ListBuffer[IconData]) extends GuiListExtended(Minecraft.getMinecraft, width, height, top, bottom, 20) {
    this.left = leftM
    this.right = this.left + width

    var listLength: Int = 1000
    var currentIndex: Int = 0
    var iconEntryList: ListBuffer[IGuiListEntry] = {
      val list: ListBuffer[IGuiListEntry] = ListBuffer[IGuiListEntry]()
      for (icon: IconData <- iconList) {
        list += new IconEntry(icon)
      }
      list
    }

    //Use a scissor helper here so it renders within its bounds
    //Also change list length to allow selecting the entries correctly
    override def drawScreen(p_148128_1_ : Int, p_148128_2_ : Int, p_148128_3_ : Float) {
      RenderHelper.startGlScissor(this.left, this.top, this.width, this.height)
      this.listLength = 2000
      super.drawScreen(p_148128_1_, p_148128_2_, p_148128_3_)
      this.listLength = this.width
      RenderHelper.endGlScissor()
    }

    override def elementClicked(index : Int, p_148144_2_ : Boolean, p_148144_3_ : Int, p_148144_4_ : Int) {
      this.currentIndex = index
    }

    override def isSelected(index : Int): Boolean = index == this.currentIndex

    override def getSize: Int = this.iconEntryList.size
    override def getListEntry(index: Int): IGuiListEntry = this.iconEntryList(index)
    override def getScrollBarX: Int = this.right - 6
    override def getListWidth: Int = this.listLength

    class IconEntry(iconData: IconData) extends IGuiListEntry {
      override def drawEntry(var1: Int, x: Int, y: Int, listWidth: Int, var5: Int, var6: Tessellator, var7: Int, var8: Int, var9: Boolean) {
        IconManager.bindTexture(iconData)
        GL11.glPushMatrix()
        //Move to this point to allow us to scale properly
        GL11.glTranslatef(left + 1, y - 1, 0)
        //If wider then taller (or equal), scale along width
        if (iconData.width >= iconData.height) {
          GL11.glScalef(18F / iconData.width, 18F / iconData.width, 1F)
        }
        else {
          GL11.glScalef(18F / iconData.height, 18F / iconData.height, 1F)
        }
        Gui.func_146110_a(0, 0, iconData.minU, iconData.minV, iconData.width, iconData.height, iconData.textureXSize, iconData.textureYSize)
        GL11.glPopMatrix()

        val fontRenderer: FontRenderer = Minecraft.getMinecraft.fontRenderer
        fontRenderer.drawString(iconData.iconName, left + 22, y + 5, 0xFFFFFF)
        GL11.glColor4f(1F, 1F, 1F, 1F)
      }

      override def mousePressed(index: Int, var2: Int, var3: Int, var4: Int, var5: Int, var6: Int): Boolean = {
        true
      }

      override def mouseReleased(index: Int, var2: Int, var3: Int, var4: Int, var5: Int, var6: Int) {}
    }
  }
}
