const { Schema, model } = require('mongoose');

const CodeSchema = new Schema({
    code:      { type: String, required: true, unique: true },
    discordId: { type: String, required: true },
    used:      { type: Boolean, default: false },
    createdAt: { type: Date, default: Date.now, expires: 120 }
});

module.exports = model('Code', CodeSchema, 'discord_codes');