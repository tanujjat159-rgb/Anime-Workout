package com.example.data

import com.example.model.AnimeQuote
import com.example.model.WorkoutPhase

object AnimeQuotesRepository {

    val allQuotes: List<AnimeQuote> = listOf(
        // WORK / FIGHT QUOTES
        AnimeQuote(
            id = "db_goku_1",
            quote = "Push through the pain! Surpass your limits right here, right now!",
            character = "Son Goku",
            anime = "Dragon Ball Super",
            japaneseRomanji = "Genkai o koero!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9001
        ),
        AnimeQuote(
            id = "db_vegeta_1",
            quote = "There's only one certainty in life: A strong man stands above and conquers all!",
            character = "Prince Vegeta",
            anime = "Dragon Ball Z",
            japaneseRomanji = "Watashi wa Saiyajin no Oji da!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9500
        ),
        AnimeQuote(
            id = "mha_allmight_1",
            quote = "If you feel yourself hitting up against your limit, remember for what cause you clench your fists! PLUS ULTRA!",
            character = "All Might",
            anime = "My Hero Academia",
            japaneseRomanji = "Sara ni mukou e! Purusu Urutora!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 10000
        ),
        AnimeQuote(
            id = "mha_deku_1",
            quote = "Don't you dare stop! Give everything you've got until your heart screams!",
            character = "Izuku Midoriya",
            anime = "My Hero Academia",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 8800
        ),
        AnimeQuote(
            id = "sl_jinwoo_1",
            quote = "I will keep getting stronger. ARISE!",
            character = "Sung Jin-woo",
            anime = "Solo Leveling",
            japaneseRomanji = "Iro-na-ra!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9999
        ),
        AnimeQuote(
            id = "ds_rengoku_1",
            quote = "Set your heart ablaze! Go beyond your limits! Grit your teeth and move forward!",
            character = "Kyojuro Rengoku",
            anime = "Demon Slayer",
            japaneseRomanji = "Kokoro o moyase!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9800
        ),
        AnimeQuote(
            id = "ds_tanjiro_1",
            quote = "Total Concentration Breathing! Don't let your stamina falter, strike with all your spirit!",
            character = "Tanjiro Kamado",
            anime = "Demon Slayer",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 8500
        ),
        AnimeQuote(
            id = "jjk_gojo_1",
            quote = "Throughout heaven and earth, I alone am the honored one. Now let me see your true strength!",
            character = "Satoru Gojo",
            anime = "Jujutsu Kaisen",
            japaneseRomanji = "Tenjo tenge yuiga dokuson",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 12000
        ),
        AnimeQuote(
            id = "jjk_yuji_1",
            quote = "I don't care if it hurts! I'll keep punching until there's nothing left!",
            character = "Yuji Itadori",
            anime = "Jujutsu Kaisen",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 8700
        ),
        AnimeQuote(
            id = "aot_erwin_1",
            quote = "My soldiers do not buckle or yield when faced with the cruelty of this world! My soldiers roar! ADVANCE!",
            character = "Erwin Smith",
            anime = "Attack on Titan",
            japaneseRomanji = "Shinzo o sasageyo!",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9400
        ),
        AnimeQuote(
            id = "naruto_guy_1",
            quote = "The Springtime of Youth has no limits! Open the Eighth Gate of Death!",
            character = "Might Guy",
            anime = "Naruto Shippuden",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9900
        ),
        AnimeQuote(
            id = "naruto_naruto_1",
            quote = "I never go back on my word! That is my nindo, my ninja way!",
            character = "Naruto Uzumaki",
            anime = "Naruto",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9100
        ),
        AnimeQuote(
            id = "opm_saitama_1",
            quote = "100 push-ups, 100 sit-ups, 100 squats, and a 10km run every single day! NO EXCUSES!",
            character = "Saitama",
            anime = "One Punch Man",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 99999
        ),
        AnimeQuote(
            id = "gl_kamina_1",
            quote = "Don't believe in yourself! Believe in me that believes in you! Pierce the heavens!",
            character = "Kamina",
            anime = "Gurren Lagann",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9700
        ),
        AnimeQuote(
            id = "bc_asta_1",
            quote = "My magic is never giving up! Even without talent, I'll carve my own destiny!",
            character = "Asta",
            anime = "Black Clover",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9200
        ),
        AnimeQuote(
            id = "bleach_ichigo_1",
            quote = "If miracle only happens once, what is it called the second time? BANKAI!",
            character = "Ichigo Kurosaki",
            anime = "Bleach",
            phase = WorkoutPhase.WORK,
            powerLevelHype = 9600
        ),

        // REST / RECOVERY QUOTES
        AnimeQuote(
            id = "rest_tanjiro_1",
            quote = "Slow down your breath. Circulate oxygen to every single muscle fiber.",
            character = "Tanjiro Kamado",
            anime = "Demon Slayer",
            phase = WorkoutPhase.REST,
            powerLevelHype = 7000
        ),
        AnimeQuote(
            id = "rest_kakashi_1",
            quote = "Resting is also part of training. Steady your chakra before the next assault.",
            character = "Kakashi Hatake",
            anime = "Naruto",
            phase = WorkoutPhase.REST,
            powerLevelHype = 7500
        ),
        AnimeQuote(
            id = "rest_whis_1",
            quote = "Relax your shoulders. Fighting with tension only wastes precious divine energy.",
            character = "Whis",
            anime = "Dragon Ball Super",
            phase = WorkoutPhase.REST,
            powerLevelHype = 11000
        ),
        AnimeQuote(
            id = "rest_zoro_1",
            quote = "Scars on the back are a swordsman's shame. Catch your breath; the battle isn't over.",
            character = "Roronoa Zoro",
            anime = "One Piece",
            phase = WorkoutPhase.REST,
            powerLevelHype = 8300
        ),
        AnimeQuote(
            id = "rest_roshi_1",
            quote = "Work hard, study well, and eat and sleep plenty. That is the Turtle Hermit way!",
            character = "Master Roshi",
            anime = "Dragon Ball",
            phase = WorkoutPhase.REST,
            powerLevelHype = 7800
        ),
        AnimeQuote(
            id = "rest_nanami_1",
            quote = "Take a breath. Going into overtime requires maximum mental efficiency.",
            character = "Kento Nanami",
            anime = "Jujutsu Kaisen",
            phase = WorkoutPhase.REST,
            powerLevelHype = 8000
        ),

        // VICTORY / COMPLETED QUOTES
        AnimeQuote(
            id = "win_allmight_1",
            quote = "You were fantastic out there! Stand proud, for you are today's true hero!",
            character = "All Might",
            anime = "My Hero Academia",
            phase = WorkoutPhase.COMPLETED,
            powerLevelHype = 15000
        ),
        AnimeQuote(
            id = "win_goku_1",
            quote = "Haha, that was an awesome battle! You're definitely getting stronger every day!",
            character = "Son Goku",
            anime = "Dragon Ball Super",
            phase = WorkoutPhase.COMPLETED,
            powerLevelHype = 16000
        ),
        AnimeQuote(
            id = "win_sukuna_1",
            quote = "Stand proud. You were strong.",
            character = "Ryomen Sukuna",
            anime = "Jujutsu Kaisen",
            phase = WorkoutPhase.COMPLETED,
            powerLevelHype = 20000
        ),
        AnimeQuote(
            id = "win_jinwoo_1",
            quote = "Daily quest complete. Stat points allocated. You survived the penalty zone.",
            character = "System",
            anime = "Solo Leveling",
            phase = WorkoutPhase.COMPLETED,
            powerLevelHype = 18000
        )
    )

    fun getRandomQuoteForPhase(phase: WorkoutPhase): AnimeQuote {
        val matches = allQuotes.filter { it.phase == phase }
        return if (matches.isNotEmpty()) matches.random() else allQuotes.random()
    }

    fun getAnimeSeriesList(): List<String> {
        return allQuotes.map { it.anime }.distinct().sorted()
    }
}
