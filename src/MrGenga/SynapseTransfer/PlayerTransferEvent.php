<?php

/*
 * SynapseTransfer plugin for Genisys
 * Copyright (C) 2016 Raul Vakhitov <https://github.com/MrGenga/SynapseTransfer>
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

namespace MrGenga\SynapseTransfer;

use pocketmine\event\Cancellable;
use pocketmine\event\player\PlayerEvent;
use pocketmine\Player;

class PlayerTransferEvent extends PlayerEvent implements Cancellable{
	public static $handlerList = null;

	/** @var string */
	private $serverName;

	/** @var string */
	private $message;

	/**
	 * @param Player $player
	 * @param string $server
	 * @param string $message
	 */
	public function __construct(Player $player, $server, $message = ""){
		$this->player = $player;
		$this->serverName = $server;
		$this->message = $message;
	}

	/**
	 * @return string
	 */
	public function getServerName(){
		return $this->serverName;
	}

	/**
	 * @param string $server
	 */
	public function setServerName($server){
		$this->serverName = $server;
	}

	public function getMessage(){
		return $this->message;
	}

	/**
	 * Set the message sent to the target player before teleporting.
	 * If null or empty, it won't be sent.
	 *
	 * @param $message
	 */
	public function setMessage($message){
		$this->message = $message;
	}
}