const mongoose = require('mongoose');

let ready = false;

module.exports = async () => {
    if (ready) return;
    await mongoose.connect(process.env.MONGO_URI, {
        dbName: process.env.MONGO_DB || 'donutsmp'
    });
    ready = true;
};