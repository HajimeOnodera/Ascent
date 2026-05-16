const Link = require('../models/Link');

const sortCurrent = { isVerified: -1, lastVerified: -1, linkedAt: -1, _id: -1 };

function byIgnRegex(ign) {
    return new RegExp(`^${ign}$`, 'i');
}

async function findCurrentByIgn(ign) {
    return Link.findOne({ minecraftIGN: byIgnRegex(ign) }).sort(sortCurrent).lean();
}

async function findCurrentByDiscordId(discordId) {
    return Link.findOne({ discordId }).sort(sortCurrent).lean();
}

async function findCurrentByIdentifier(identifier) {
    return Link.findOne({
        $or: [{ discordId: identifier }, { minecraftIGN: byIgnRegex(identifier) }]
    }).sort(sortCurrent).lean();
}

module.exports = {
    Link,
    byIgnRegex,
    findCurrentByIgn,
    findCurrentByDiscordId,
    findCurrentByIdentifier,
};
