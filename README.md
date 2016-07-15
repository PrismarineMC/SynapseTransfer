# SynapseTransfer
Port [PHP-Version](https://github.com/MrGenga/SynapseTransfer) of SynapseTransfer for [Nukkit](https://github.com/Nukkit/Nukkit).
# Commands
```/transfer [player] <server>``` - Transfer SynapsePlayer to other server. Servers can be setting in config.yml.
# Permissions
```synapsetransfer.command.transfer``` - Allow to use ```/transfer``` command. Default for op.
# API
```SynapseTransfer.transfer(Player player, String server, String message)``` - Transfer SynapsePlayer to other SynapseClient. Example: ```this.getServer().getPluginManager().getPlugin("SynapseTransfer").transfer(player, "hub", ""); ```
# JAR
Coming soon...
