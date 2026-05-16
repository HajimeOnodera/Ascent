const { EmbedBuilder } = require('discord.js');

const C = { ok: 0x57f287, err: 0xed4245, info: 0x5865f2, neutral: 0x2b2d31 };

const base = (color) => new EmbedBuilder().setColor(color).setTimestamp();

module.exports = {
    ok:      () => base(C.ok),
    err:     () => base(C.err),
    info:    () => base(C.info),
    neutral: () => base(C.neutral),
};