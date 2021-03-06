@file:Suppress("unused")

package me.schlaubi.lavakord.audio

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.schlaubi.lavakord.audio.StatsEvent as PublicStatsEvent

@Serializable
public data class DiscordVoiceServerUpdateData(
    val token: String,
    @SerialName("guild_id")
    val guildId: String,
    val endpoint: String?,
)

@Serializable
internal sealed class GatewayPayload {

    abstract val guildId: String?

    @Serializable
    @SerialName("voiceUpdate")
    data class VoiceUpdateCommand(
        override val guildId: String,
        val sessionId: String,
        val event: DiscordVoiceServerUpdateData
    ) : GatewayPayload()

    @Serializable
    @SerialName("play")
    data class PlayCommand(
        override val guildId: String,
        val track: String,
        val startTime: Long? = null,
        val endTime: Long? = null,
        val volume: Int? = null,
        val noReplace: Boolean? = null,
        val pause: Boolean? = null
    ) : GatewayPayload()

    @Serializable
    @SerialName("stop")
    data class StopCommand(
        override val guildId: String
    ) : GatewayPayload()

    @Serializable
    @SerialName("pause")
    data class PauseCommand(
        override val guildId: String, val pause: Boolean
    ) : GatewayPayload()

    @Serializable
    @SerialName("seek")
    data class SeekCommand(
        override val guildId: String, val position: Long
    ) : GatewayPayload()

    @Serializable
    @SerialName("volume")
    data class VolumeCommand(
        override val guildId: String, val volume: Int
    ) : GatewayPayload()

    @Serializable
    @SerialName("equalizer")
    data class EqualizerCommand(
        override val guildId: String, val bands: List<Band>
    ) : GatewayPayload() {
        @Serializable
        data class Band(val band: Int, val gain: Float)
    }

    @Serializable
    @SerialName("filters")
    data class FiltersCommand(
        override val guildId: String,
        override var karaoke: Karaoke? = null,
        override var timescale: Timescale? = null,
        override var tremolo: Tremolo? = null,
        override var vibrato: Vibrato? = null
    ) : GatewayPayload(), Filters {
        @Serializable
        data class Karaoke(
            override var level: Float = 1F,
            override var monoLevel: Float = 1F,
            override var filterBand: Float = 220F,
            override var filterWidth: Float = 100F
        ) : Filters.Karaoke

        @Serializable
        data class Timescale(
            @SerialName("speed")
            private var _speed: Float = 1F,
            @SerialName("pitch")
            private var _pitch: Float = 1F,
            @SerialName("rate")
            private var _rate: Float = 1F
        ) : Filters.Timescale {
            override var speed: Float
                get() = _speed
                set(value) {
                    require(value > 0) { "Speed must be greater than 0" }
                    _speed = value
                }

            override var pitch: Float
                get() = _pitch
                set(value) {
                    require(value > 0) { "Pitch must be greater than 0" }
                    _pitch = value
                }

            override var rate: Float
                get() = _rate
                set(value) {
                    require(value > 0) { "Rate must be greater than 0" }
                    _rate = value
                }
        }

        @Serializable
        data class Tremolo(
            @SerialName("frequency") private var _frequency: Float = 2F,
            @SerialName("depth") private var _depth: Float = .5F
        ) : Filters.Tremolo {
            override var frequency: Float
                get() = _frequency
                set(value) {
                    require(value > 0) { "Frequency must be greater than 0" }
                }

            override var depth: Float
                get() = _depth
                set(value) {
                    require(value > 0 && value <= 1) { "Frequency must be between 0 and 1" }
                    _depth = value
                }
        }

        @Serializable
        data class Vibrato(
            @SerialName("frequency") private var _frequency: Float = 2F,
            @SerialName("depth") private var _depth: Float = .5F
        ) : Filters.Vibrato {
            override var frequency: Float
                get() = _frequency
                set(value) {
                    require(value > 0) { "Frequency must be greater than 0" }
                }

            override var depth: Float
                get() = _depth
                set(value) {
                    require(value > 0 && value <= 1) { "Frequency must be between 0 and 1" }
                    _depth = value
                }
        }
    }

    @Serializable
    @SerialName("destroy")
    data class DestroyCommand(
        override val guildId: String
    ) : GatewayPayload()

    @Serializable
    @SerialName("configureResuming")
    data class ConfigureResumingCommand(val key: String, val timeout: Int) : GatewayPayload() {
        override val guildId: String
            get() = throw UnsupportedOperationException("guildId is not supported for this event")
    }

    @Serializable
    @SerialName("playerUpdate")
    data class PlayerUpdateEvent(
        override val guildId: String, val state: State
    ) : GatewayPayload() {
        @Serializable
        data class State(val time: Long, val position: Long? = null)
    }

    @Serializable
    @SerialName("stats")
    data class StatsEvent(
        override val players: Int,
        override val playingPlayers: Int,
        override val uptime: Long,
        override val memory: PublicStatsEvent.Memory,
        override val cpu: PublicStatsEvent.Cpu,
        override val frameStats: PublicStatsEvent.FrameStats? = null
    ) : GatewayPayload(), me.schlaubi.lavakord.audio.StatsEvent {
        override val guildId: String
            get() = throw UnsupportedOperationException("Stats event does not provide a guild id but all other events to thank you Lavalink")
    }

    @Serializable
    @SerialName("event")
    data class EmittedEvent(
        override val guildId: String,
        val type: Type,
        val track: String,
        val reason: String? = null,
        val error: String? = null,
        val thresholdMs: Long? = null
    ) : GatewayPayload() {

        @Serializable
        enum class Type {
            @SerialName("TrackStartEvent")
            TRACK_START_EVENT,

            @SerialName("TrackEndEvent")
            TRACK_END_EVENT,

            @SerialName("TrackExceptionEvent")
            TRACK_EXCEPTION_EVENT,

            @SerialName("TrackStuckEvent")
            TRACK_STUCK_EVENT
        }
    }
}
