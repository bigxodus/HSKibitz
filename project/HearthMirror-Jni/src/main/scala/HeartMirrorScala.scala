class MirrorArenaInfo(rewards: Array[MirrorRewardData], currentSlot: Int, wins: Int, losses: Int, deck: MirrorDeck) {
}

class MirrorMountRewardData(mountType: Int) extends MirrorRewardData {
}

class MirrorGoldRewardData(amount: Int) extends MirrorRewardData {
}

class MirrorForgeTicketRewardData(quantity: Int) extends MirrorRewardData {
}

class MirrorCardBackRewardData(cardbackId: Int) extends MirrorRewardData {
}

class MirrorCardRewardData(premium: Boolean, count: Int, cardId: String) extends MirrorRewardData {
}

class MirrorBoosterPackRewardData(count: Int, boosterId: Int) extends MirrorRewardData {
}

class MirrorArcaneDustRewardData(amount: Int) extends MirrorRewardData {
}

class MirrorRewardData() {
}

class MirrorDeck(cards: Array[MirrorCard], heroPremium: Int, cardBackId: Int, seasonId: Int, `type`: Int, isWild: Boolean, hero: String, name: String, id: Int) {
}

class MirrorCard(premium: Boolean, count: Int, cardId: String) {
}

class MirrorHeroLevel(maxXp: Int, xp: Int, maxLevel: Int, level: Int, heroClass: Int) {
}

class MirrorAccountId(hi: Int, lo: Int) {
}

class MirrorMatchInfo(spectator: Boolean, formatType: Int, gameType: Int, rankedSeasonId: Int, missionId: Int, brawlSeasonId: Int, opposingPlayer: MirrorPlayer, localPlayer: MirrorPlayer) {
}

class MirrorPlayer(cardBackId: Int, wildStars: Int, wildLegendRank: Int, wildRank: Int, standardStars: Int, standardLegendRank: Int, standardRank: Int, playerId: Int, name: String) {
}

class MirrorGameServerInfo(version: String, spectatorPassword: String, spectatorMode: Boolean, resumable: Boolean, port: Int, mission: Int, gameHandle: Int, clientHandle: Int, auroraPassword: String, address: String) {
}

class MirrorBrawlInfo(winStreak: Int, gamesPlayed: Int, losses: Int, wins: Int, isSessionBased: Boolean, maxLosses: Int, maxWins: Int) {
}