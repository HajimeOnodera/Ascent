const ROLES = (process.env.ADMIN_ROLE_IDS || '').split(',').filter(Boolean);

module.exports = (member) => member.roles.cache.some(r => ROLES.includes(r.id));