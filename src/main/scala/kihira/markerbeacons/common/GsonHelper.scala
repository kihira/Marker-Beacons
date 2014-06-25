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

package kihira.markerbeacons.common

import java.lang.reflect.Type

import com.google.gson._

object GsonHelper {

  def getGson: Gson = {
    new GsonBuilder()
      .registerTypeAdapter(classOf[ImageComponent], new SubClassDeserializer[ImageComponent])
      .registerTypeAdapter(classOf[TextComponent], new SubClassDeserializer[TextComponent])
      .registerTypeAdapter(classOf[LogoComponent], new SubClassDeserializer[LogoComponent])
      .create()
  }

  class SubClassDeserializer[T] extends JsonDeserializer[T] {
    def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T = {
      val jsonObject: JsonObject = json.getAsJsonObject
      val className: String = jsonObject.get("clazz").getAsString
      var clazz: Class[_] = null
      try {
        clazz = Class.forName(className)
      }
      catch {
        case e: ClassNotFoundException =>
          e.printStackTrace()
          throw new JsonParseException(e.getMessage)
      }
      new Gson().fromJson(jsonObject.toString, clazz).asInstanceOf[T]
    }
  }
}
