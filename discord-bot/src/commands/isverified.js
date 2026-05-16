const { SlashCommandBuilder } = require('discord.js');
const guard = require('../util/guard');
const emb   = require('../util/embed');
const { findCurrentByIdentifier } = require('../util/linkStore');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('isverified')
        .setDescription('[Admin] Check if a user is verified')
        .addStringOption(o =>
            o.setName('identifier')
                .setDescription('Discord ID or Minecraft IGN')
                .setRequired(true)
        ),

    async execute(interaction) {
        if (!guard(interaction.member)) {
            return interaction.reply({ ephemeral: true, content: 'No permission.' });
        }

        await interaction.deferReply({ ephemeral: true });

        const id   = interaction.options.getString('identifier');
        const link = await findCurrentByIdentifier(id);

        if (!link) {
            return interaction.editReply({
                embeds: [emb.err().setDescription(`No account found for \`${id}\`.`)]
            });
        }

        let dcTag = link.discordId;
        try {
            const u = await interaction.client.users.fetch(link.discordId);
            dcTag = `@${u.username}`;
        } catch {}

        const verifiedTs = link.lastVerified
            ? `<t:${Math.floor(new Date(link.lastVerified).getTime() / 1000)}:F>`
            : 'N/A';

        const statusLine = link.isVerified
            ? `Yes. User **${link.minecraftIGN}** is verified.`
            : `No. User **${link.minecraftIGN}** is not verified.`;

        await interaction.editReply({
            embeds: [
                (link.isVerified ? emb.ok() : emb.neutral())
                    .setTitle('Verification Status')
                    .addFields(
                        { name: 'Status',               value: statusLine,               inline: false },
                        { name: 'IGN',                  value: link.minecraftIGN ?? 'Unknown', inline: true },
                        { name: 'Discord',              value: dcTag,                    inline: true },
                        { name: '\u200b',               value: '\u200b',                 inline: true },
                        { name: 'Date of Verification', value: verifiedTs,               inline: false }
                    )
            ]
        });
    }
};
