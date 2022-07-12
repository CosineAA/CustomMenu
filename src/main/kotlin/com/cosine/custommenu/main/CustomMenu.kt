package com.cosine.custommenu.main

import com.cosine.custommenu.command.OpCommand
import com.cosine.custommenu.command.UserCommand
import com.cosine.custommenu.gui.InventoryGui
import com.cosine.custommenu.listener.ChatListener
import com.cosine.custommenu.listener.InventoryListener
import com.cosine.custommenu.util.CustomConfig
import com.cosine.custommenu.util.CustomMenuValue
import org.bukkit.plugin.java.JavaPlugin

class CustomMenu : JavaPlugin() {

    private lateinit var config: CustomConfig
    private lateinit var inventoryGui: InventoryGui
    private lateinit var value: CustomMenuValue

    override fun onEnable() {
        logger.info("커스텀 GUI 플러그인 활성화")

        config = CustomConfig(this, "menu.yml")
        config.saveDefaultConfig()

        inventoryGui = InventoryGui(this)
        value = CustomMenuValue()

        getCommand("메뉴").executor = UserCommand()
        getCommand("메뉴관리").executor = OpCommand(this)

        server.pluginManager.registerEvents(InventoryListener(this), this)
        server.pluginManager.registerEvents(ChatListener(this), this)
    }

    override fun onDisable() {
        logger.info("커스텀 GUI 플러그인 비활성화")
    }
    fun getCustomConfig(): CustomConfig {
        return this.config
    }
    fun getInventoryGui(): InventoryGui {
        return this.inventoryGui
    }
    fun getCustomMenuValue(): CustomMenuValue {
        return this.value
    }
}