package com.cosine.custommenu.util

import java.util.*
import kotlin.collections.HashMap

class CustomMenuValue {

    val npc: HashMap<UUID, String> = HashMap()

    val clickSlot: HashMap<UUID, Int> = HashMap()
    val changeDisplay: HashMap<UUID, String> = HashMap()
    val addLore: HashMap<UUID, String> = HashMap()
    val removeLore: HashMap<UUID, String> = HashMap()
    val changeLore: HashMap<UUID, String> = HashMap()
}