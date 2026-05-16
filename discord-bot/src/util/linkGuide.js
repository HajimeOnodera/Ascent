const { EmbedBuilder } = require('discord.js');

module.exports = function createLinkGuide(linkChannelId) {
    return new EmbedBuilder()
        .setColor(0x5865f2)
        .setTitle('How to verify your Minecraft account')
        .setDescription('Follow these steps to connect your Discord account to your in-game name.')
        .addFields(
            {
                name: '1. Generate a code',
                value: `Go to <#${linkChannelId}> and run \`/discord link\`. You will get a private 6-character code.`,
                inline: false
            },
            {
                name: '2. Use it in game',
                value: 'Join the server and run `/link XXXXXX` with the exact code you received.',
                inline: false
            },
            {
                name: '3. Wait for sync',
                value: 'After the command succeeds, your Discord account is linked and your nickname updates shortly after.',
                inline: false
            },
            {
                name: 'Need to remove it?',
                value: 'Run `/unlink` in game whenever you want to disconnect the account.',
                inline: false
            },
            {
                name: 'Code expired?',
                value: 'Codes last 2 minutes. Generate a new one with `/discord link`.',
                inline: false
            }
        )
        .setFooter({ text: 'Each code is single-use and tied to your Discord account.' })
        .setTimestamp();
};
