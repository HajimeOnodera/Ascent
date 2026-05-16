require('dotenv').config();

const { Client, GatewayIntentBits, Collection } = require('discord.js');
const fs      = require('fs');
const path    = require('path');
const connect = require('./db');
const watcher = require('./watcher');

const client = new Client({ intents: [GatewayIntentBits.Guilds] });
client.commands = new Collection();

for (const file of fs.readdirSync(path.join(__dirname, 'commands')).filter(f => f.endsWith('.js'))) {
    const cmd = require(`./commands/${file}`);
    client.commands.set(cmd.data.name, cmd);
}

client.once('ready', () => {
    console.log(`[bot] ${client.user.tag} online`);
    client.user.setActivity('/discord link', { type: 3 });
});

client.on('interactionCreate', async interaction => {
    if (!interaction.isChatInputCommand()) return;
    if (interaction.guildId !== process.env.GUILD_ID) return;

    const cmd = client.commands.get(interaction.commandName);
    if (!cmd) return;

    try {
        await cmd.execute(interaction);
    } catch (err) {
        console.error(`[${interaction.commandName}]`, err);
        const payload = { content: 'Something went wrong.', ephemeral: true };
        if (interaction.deferred || interaction.replied) {
            await interaction.editReply(payload).catch(() => {});
        } else {
            await interaction.reply(payload).catch(() => {});
        }
    }
});

connect().then(() => {
    console.log('[db] mongodb connected');
    client.login(process.env.DISCORD_TOKEN).then(() => watcher(client));
}).catch(err => {
    console.error('[db] connection failed:', err);
    process.exit(1);
});