const { SlashCommandBuilder } = require('discord.js');
const guard = require('../util/guard');
const emb   = require('../util/embed');
const { Link, byIgnRegex } = require('../util/linkStore');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('historymc')
        .setDescription('[Admin] View Discord link history for a Minecraft account')
        .addStringOption(o =>
            o.setName('ign').setDescription('Minecraft IGN').setRequired(true)
        ),

    async execute(interaction) {
        if (!guard(interaction.member)) {
            return interaction.reply({ ephemeral: true, content: 'No permission.' });
        }

        await interaction.deferReply({ ephemeral: true });

        const ign = interaction.options.getString('ign');

        const records = await Link.find({
            $or: [
                { minecraftIGN: byIgnRegex(ign) },
                { 'history.minecraftIGN': byIgnRegex(ign) }
            ]
        }).sort({ isVerified: -1, lastVerified: -1, linkedAt: -1, _id: -1 }).lean();

        if (!records.length) {
            return interaction.editReply({
                embeds: [emb.err().setDescription(`No history found for **${ign}**.`)]
            });
        }

        const entries = records.flatMap(r => {
            const out = [];

            if (r.minecraftIGN?.toLowerCase() === ign.toLowerCase()) {
                out.push({
                    discordId: r.discordId,
                    linkedAt:  r.linkedAt,
                    status:    r.isVerified ? 'active' : 'unlinked'
                });
            }

            (r.history || [])
                .filter(h => h.minecraftIGN?.toLowerCase() === ign.toLowerCase())
                .forEach(h => out.push({ discordId: h.discordId, linkedAt: h.linkedAt, status: h.status }));

            return out;
        }).filter(e => e.discordId);

        const deduped = Array.from(new Map(
            entries
                .sort((a, b) => new Date(b.linkedAt || 0) - new Date(a.linkedAt || 0))
                .map(entry => [`${entry.discordId}|${entry.linkedAt}|${entry.status}`, entry])
        ).values());

        const rows = await Promise.all(deduped.slice(0, 15).map(async e => {
            let tag = e.discordId;
            try {
                const u = await interaction.client.users.fetch(e.discordId);
                tag = `@${u.username}`;
            } catch {}
            const ts = e.linkedAt
                ? `<t:${Math.floor(new Date(e.linkedAt).getTime() / 1000)}:d>`
                : 'unknown';
            return `${tag} | ${ts} | \`${e.status ?? 'unknown'}\``;
        }));

        await interaction.editReply({
            embeds: [
                emb.info()
                    .setTitle(`History for ${ign}`)
                    .setDescription(rows.join('\n') || 'No entries.')
                    .setFooter({ text: `${deduped.length} total${deduped.length > 15 ? ' | showing 15' : ''}` })
            ]
        });
    }
};
