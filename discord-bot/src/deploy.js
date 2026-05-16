require('dotenv').config();

const { REST, Routes } = require('discord.js');
const fs   = require('fs');
const path = require('path');

const commands = fs.readdirSync(path.join(__dirname, 'commands'))
    .filter(f => f.endsWith('.js'))
    .map(f => require(`./commands/${f}`).data.toJSON());

const rest = new REST().setToken(process.env.DISCORD_TOKEN);

rest.put(
    Routes.applicationGuildCommands(process.env.CLIENT_ID, process.env.GUILD_ID),
    { body: commands }
).then(() => console.log(`Deployed ${commands.length} commands.`))
    .catch(console.error);