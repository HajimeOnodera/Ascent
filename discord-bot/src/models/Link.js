const { Schema, model } = require('mongoose');

const LinkSchema = new Schema({
    discordId:    { type: String, unique: true, sparse: true },
    minecraftUUID: { type: String, unique: true, sparse: true },
    minecraftIGN: String,
    minecraftNameLower: { type: String, unique: true, sparse: true },
    isVerified:   { type: Boolean, default: false },
    linkedAt:     Date,
    lastVerified: Date,
    unlinkedAt:   Date,
    nicknameSyncedAt: Date,
    history: [{
        discordId:    String,
        minecraftIGN: String,
        linkedAt:     Date,
        status:       String,
        _id: false
    }]
}, { collection: 'discord_links' });

module.exports = model('Link', LinkSchema);
