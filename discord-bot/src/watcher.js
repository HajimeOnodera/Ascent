const Link = require('./models/Link');

async function syncNickname(client, doc) {
    if (!doc?.isVerified || !doc.discordId || !doc.minecraftIGN) return;

    try {
        const guild = await client.guilds.fetch(process.env.GUILD_ID);
        const member = await guild.members.fetch(doc.discordId);

        if (member.nickname !== doc.minecraftIGN) {
            await member.setNickname(doc.minecraftIGN);
        }

        await Link.updateOne(
            { _id: doc._id },
            { $set: { nicknameSyncedAt: new Date() } }
        );
    } catch {}
}

async function pollUnsyncedNicknames(client) {
    const pending = await Link.find({
        isVerified: true,
        discordId: { $exists: true, $ne: null },
        minecraftIGN: { $exists: true, $ne: null },
        $or: [
            { nicknameSyncedAt: { $exists: false } },
            { nicknameSyncedAt: null },
            { $expr: { $lt: ['$nicknameSyncedAt', '$lastVerified'] } }
        ]
    })
        .sort({ lastVerified: 1 })
        .limit(25)
        .lean();

    for (const doc of pending) {
        await syncNickname(client, doc);
    }
}

module.exports = (client) => {
    let stream;
    try {
        stream = Link.watch(
            [{ $match: { operationType: { $in: ['insert', 'update'] } } }],
            { fullDocument: 'updateLookup' }
        );
    } catch (err) {
        console.warn('[watcher] change stream unavailable:', err.message);
        return;
    }

    stream.on('change', async (change) => {
        await syncNickname(client, change.fullDocument);
    });

    stream.on('error', (err) => {
        console.warn('[watcher] change stream stopped:', err.message);
    });

    setInterval(() => {
        pollUnsyncedNicknames(client).catch(() => {});
    }, 5000);

    pollUnsyncedNicknames(client).catch(() => {});
};
