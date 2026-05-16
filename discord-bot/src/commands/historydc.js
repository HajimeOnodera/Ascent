const { SlashCommandBuilder } = require('discord.js');
const guard = require('../util/guard');
const emb   = require('../util/embed');
const { Link } = require('../util/linkStore');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('historydc')
        .setDescription('[Admin] View Minecraft link history for a Discord account')
        .addStringOption(o =>
            o.setName('id').setDescription('Discord user ID').setRequired(true)
        ),

    async execute(interaction) {
        if (!guard(interaction.member)) {
            return interaction.reply({ ephemeral: true, content: 'No permission.' });
        }

        await interaction.deferReply({ ephemeral: true });

        const discordId = interaction.options.getString('id');
        const records = await Link.find({
            $or: [{ discordId }, { 'history.discordId': discordId }]
        }).sort({ isVerified: -1, lastVerified: -1, linkedAt: -1, _id: -1 }).lean();

        if (!records.length) {
            return interaction.editReply({
                embeds: [emb.err().setDescription(`No history found for Discord ID \`${discordId}\`.`)]
            });
        }

        const entries = records.flatMap(r => {
            const out = [];

            if (r.discordId === discordId) {
                out.push({ ign: r.minecraftIGN, linkedAt: r.linkedAt, status: r.isVerified ? 'active' : 'unlinked' });
            }

            (r.history || [])
                .filter(h => h.discordId === discordId)
                .forEach(h => out.push({ ign: h.minecraftIGN, linkedAt: h.linkedAt, status: h.status }));

            return out;
        }).filter(e => e.ign);

        const deduped = Array.from(new Map(
            entries
                .sort((a, b) => new Date(b.linkedAt || 0) - new Date(a.linkedAt || 0))
                .map(entry => [`${entry.ign}|${entry.linkedAt}|${entry.status}`, entry])
        ).values());

        let tag = discordId;
        try {
            const u = await interaction.client.users.fetch(discordId);
            tag = `@${u.username}`;
        } catch {}

        const rows = deduped.slice(0, 15).map(e => {
            const ts = e.linkedAt
                ? `<t:${Math.floor(new Date(e.linkedAt).getTime() / 1000)}:d>`
                : 'unknown';
            return `**${e.ign ?? 'Unknown'}** | ${ts} | \`${e.status ?? 'unknown'}\``;
        });

        await interaction.editReply({
            embeds: [
                emb.info()
                    .setTitle(`History for ${tag}`)
                    .setDescription(rows.join('\n') || 'No entries.')
                    .setFooter({ text: `${deduped.length} total${deduped.length > 15 ? ' | showing 15' : ''}` })
            ]
        });
    }
};
