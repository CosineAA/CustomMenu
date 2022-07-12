package com.cosine.custommenu.gui

import com.cosine.custommenu.main.CustomMenu
import com.cosine.custommenu.util.CustomConfig
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryGui(plugin: CustomMenu) {

    private val menu: CustomConfig
    private val menuConfig: YamlConfiguration

    init {
        menu = plugin.getCustomConfig()
        menuConfig = menu.getConfig()
    }

    fun settingMainMenu(player: Player, name: String) {
        val row = menuConfig.getInt("$name.라인")
        val inventory: Inventory = Bukkit.createInventory(null, 3 * 9, "$name 설정§a§a§a")

        set("&b&l아이템 설정", listOf("&f클릭 시 아이템이 설정 창으로 이동합니다."), Material.CHEST, 0, 1, 9, inventory)
        set("&c&lNPC 설정", listOf("&f우클릭 시 메뉴가 나올 NPC의 이름을 설정합니다."), Material.NAME_TAG, 0, 1, 11, inventory)
        set("&d&l라인 설정", listOf("&a좌클릭&f 시 라인 1을 증가시킵니다.", "&c우클릭&f 시 라인 1을 감소시킵니다.", "&7현재 라인 수: $row"), Material.ANVIL, 0, 1, 13, inventory)
        set("&6&l슬롯 설정", listOf("&f클릭 시 각 슬롯의 정보를 설정하는 창으로 이동합니다."), Material.DIAMOND, 0, 1, 15, inventory)
        set("&a&l명령어 설정", listOf("&f클릭 시 각 슬롯의 명렁어를 설정하는 창으로 이동합니다."), Material.DIAMOND, 0, 1, 17, inventory)

        player.openInventory(inventory)
    }
    fun settingItemMenu(player: Player, name: String) {
        val row = menuConfig.getInt("$name.라인")
        val inventory: Inventory = Bukkit.createInventory(null, row * 9, "$name 설정§b§b§b")

        setMenuItem(name, row, inventory)

        player.openInventory(inventory)
    }
    fun settingSlotMenu(player: Player, name: String) {
        val row = menuConfig.getInt("$name.라인")
        val inventory: Inventory = Bukkit.createInventory(null, row * 9, "$name 설정§c§c§c")

        setSlotItem(name, row, inventory)

        player.openInventory(inventory)
    }
    private fun set(display: String, lore: List<String>, material: Material, data: Short, count: Int, slot: Int, inventory: Inventory) {
        val item = ItemStack(material, count, data)
        val meta = item.itemMeta
        meta.displayName = convertColor(display)
        meta.lore = convertListColor(lore)
        item.itemMeta = meta
        inventory.setItem(slot, item)
    }
    private fun setSlotItem(name: String, row: Int, inventory: Inventory) {
        for (loop: Int in 0..row * 9) {
            if (!menuConfig.contains("$name.슬롯.$loop")) continue

            val boolean = menuConfig.getBoolean("$name.슬롯.$loop.명령어.활성화")
            val command = menuConfig.getString("$name.슬롯.$loop.명령어.커맨드")

            val item = ItemStack(menuConfig.getItemStack("$name.슬롯.$loop.아이템"))

            val showLore: MutableList<String> = mutableListOf()
            val itemLore = item.itemMeta.lore

            showLore.add("§7")
            showLore.add("§f명령어 활성화 상태: $boolean")
            showLore.add("§f실행할 명령어: $command")
            showLore.add("§7")
            itemLore?.let { showLore.addAll(it) }

            inventory.setItem(loop, item)
        }
    }
    private fun setMenuItem(name: String, row: Int, inventory: Inventory) {
        for (loop: Int in 0..row * 9) {
            if (!menuConfig.contains("$name.슬롯.$loop")) continue
            val item: ItemStack = menuConfig.getItemStack("$name.슬롯.$loop.아이템")
            inventory.setItem(loop, item)
        }
    }
    private fun convertColor(value: String) : String {
        return value.replace("&", "§")
    }
    private fun convertListColor(list: List<String>) : List<String> {
        val finalList: ArrayList<String> = arrayListOf()
        for (value: String in list) {
            finalList.add(convertColor(value))
        }
        return finalList
    }
 }