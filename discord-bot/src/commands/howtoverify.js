const { SlashCommandBuilder, MessageFlags } = require('discord.js');
const createLinkGuide = require('../util/linkGuide');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('howtoverify')
        .setDescription('Post the Minecraft verification guide in this channel'),

    async execute(interaction) {
        const embed = createLinkGuide(process.env.LINK_CHANNEL_ID);
        await interaction.channel.send({ embeds: [embed] });
        await interaction.reply({
            content: 'Posted the verification guide in this channel.',
            flags: MessageFlags.Ephemeral
        });
    }
};
