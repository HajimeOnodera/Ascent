const { SlashCommandBuilder } = require('discord.js');
const Code = require('../models/Code');
const Link = require('../models/Link');
const gen  = require('../util/codegen');
const emb  = require('../util/embed');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('discord')
        .setDescription('Link your Discord account to Minecraft')
        .addSubcommand(s =>
            s.setName('link').setDescription('Generate a verification code')
        ),

    async execute(interaction) {
        if (interaction.options.getSubcommand() !== 'link') return;

        if (interaction.channelId !== process.env.LINK_CHANNEL_ID) {
            return interaction.reply({
                embeds: [emb.err().setDescription(`This only works in <#${process.env.LINK_CHANNEL_ID}>.`)],
                ephemeral: true
            });
        }

        await interaction.deferReply({ ephemeral: true });

        const existing = await Link.findOne({ discordId: interaction.user.id, isVerified: true }).lean();
        if (existing) {
            return interaction.editReply({
                embeds: [emb.neutral().setDescription(`You're already linked to **${existing.minecraftIGN}**.`)]
            });
        }

        let code, attempts = 0;
        do {
            code = gen();
            attempts++;
        } while (attempts < 5 && await Code.exists({ code }));

        await Code.create({ code, discordId: interaction.user.id });

        const expTs = Math.floor(Date.now() / 1000) + 120;

        await interaction.editReply({
            embeds: [
                emb.ok()
                    .setTitle('Verification Code')
                    .setDescription(`\`\`\`\n${code}\n\`\`\``)
                    .addFields(
                        { name: 'In-game command', value: `\`/link ${code}\``, inline: true },
                        { name: 'Expires', value: `<t:${expTs}:R>`, inline: true }
                    )
                    .setFooter({ text: 'Do not share this code.' })
            ]
        });
    }
};