const { randomInt } = require('crypto');

const POOL = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';

module.exports = () => Array.from(
    { length: 6 },
    () => POOL[randomInt(0, POOL.length)]
).join('');