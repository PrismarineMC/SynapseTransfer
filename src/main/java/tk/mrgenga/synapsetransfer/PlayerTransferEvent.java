package tk.mrgenga.synapsetransfer;

/*
 * SynapseTransfer plugin for Nukkit
 * Copyright (C) 2016 Raul Vakhitov <https://github.com/MrGenga/SynapseTransfer/tree/java>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.Player;

public class PlayerTransferEvent extends PlayerEvent implements Cancellable{
    private static final HandlerList handlers = new HandlerList(); 

    public static HandlerList getHandlers() { 
        return handlers; 
    }

    private String serverName;

    private String message;

    public PlayerTransferEvent(Player player, String server){
        this.player = player;
        this.server = server;
        this.message = "";
    }

    public PlayerTransferEvent(Player player, String server, String message){
        this.player = player;
        this.server = server;
        this.message = message;
    }

    public String getServerName(){
        return this.serverName;
    }

    public void setServerName(String serverName){
        this.serverName = serverName;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}