const { SlashCommandBuilder } = require('discord.js');
const guard = require('../util/guard');
const emb   = require('../util/embed');
const { findCurrentByIgn } = require('../util/linkStore');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('discordsearch')
        .setDescription('[Admin] Find a Discord account by Minecraft IGN')
        .addStringOption(o =>
            o.setName('ign').setDescription('Minecraft username').setRequired(true)
        ),

    async execute(interaction) {
        if (!guard(interaction.member)) {
            return interaction.reply({ ephemeral: true, content: 'No permission.' });
        }

        await interaction.deferReply({ ephemeral: true });

        const ign  = interaction.options.getString('ign');
        const link = await findCurrentByIgn(ign);

        if (!link) {
            return interaction.editReply({
                embeds: [emb.err().setDescription(`No account linked to **${ign}**.`)]
            });
        }

        let tag = link.discordId;
        try {
            const u = await interaction.client.users.fetch(link.discordId);
            tag = `@${u.username} (<@${u.id}>)`;
        } catch {}

        const linkedTs = link.linkedAt
            ? `<t:${Math.floor(new Date(link.linkedAt).getTime() / 1000)}:F>`
            : 'Unknown';

        await interaction.editReply({
            embeds: [
                emb.info()
                    .setTitle('Search Result')
                    .addFields(
                        { name: 'IGN',         value: link.minecraftIGN,    inline: true },
                        { name: 'Discord',      value: tag,                  inline: true },
                        { name: 'Discord ID',   value: link.discordId,       inline: true },
                        { name: 'Verified',     value: link.isVerified ? 'Yes' : 'No', inline: true },
                        { name: 'Linked At',    value: linkedTs,             inline: true }
                    )
            ]
        });
    }
};
