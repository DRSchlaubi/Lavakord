package me.schlaubi.lavakord.audio

import me.schlaubi.lavakord.audio.player.Player
import me.schlaubi.lavakord.checkImplementation
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Representation of the filter configuration.
 */
@Suppress("KDocMissingDocumentation", "KDocMissingDocumentation") // I don't know anything about music
public interface Filters {
    public val karaoke: Karaoke?
    public val timescale: Timescale?
    public val tremolo: Tremolo?
    public val vibrato: Vibrato?

    public fun reset() {
        require(this is GatewayPayload.FiltersCommand)
        karaoke = null
        timescale = null
        tremolo = null
        vibrato = null
    }

    public interface Karaoke {
        public var level: Float
        public var monoLevel: Float
        public var filterBand: Float
        public var filterWidth: Float
    }

    public interface Timescale {
        public var speed: Float
        public var pitch: Float
        public var rate: Float
    }

    public interface Tremolo {
        public var frequency: Float
        public var depth: Float
    }

    /**
     * This has the same properties as [Tremolo].
     */
    public interface Vibrato : Tremolo
}

// This part seems is not implemented to Lavalink yet I guess
///**
// * Resets all applied filters.
// */
//public suspend fun Player.resetFilters() {
//    applyEqualizer { reset() }
//}
//
///**
// * Applies all Filters to this player.
// */
//@OptIn(ExperimentalContracts::class)
//public suspend fun Player.applyFilters(block: Filters.() -> Unit) {
//    contract {
//        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
//    }
//
//    checkImplementation()
//    val filters = filters ?: error("This should never happen")
//    filters.apply(block)
//
//    node.send(filters)
//}

/**
 * Configures the [Filters.Karaoke] filter.
 */
@OptIn(ExperimentalContracts::class)
public fun Filters.karaoke(block: Filters.Karaoke.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    checkImplementation()
    if (karaoke == null) {
        karaoke = GatewayPayload.FiltersCommand.Karaoke()
    }
    val filter = karaoke ?: return

    filter.apply(block)
}

/**
 * Configures the [Filters.Timescale] filter.
 */
@OptIn(ExperimentalContracts::class)
public fun Filters.timescale(block: Filters.Timescale.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    checkImplementation()
    if (timescale == null) {
        timescale = GatewayPayload.FiltersCommand.Timescale()
    }
    val filter = timescale ?: return

    filter.apply(block)
}

/**
 * Configures the [Filters.Tremolo] filter.
 */
@OptIn(ExperimentalContracts::class)
public fun Filters.tremolo(block: Filters.Tremolo.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    checkImplementation()
    if (tremolo == null) {
        tremolo = GatewayPayload.FiltersCommand.Tremolo()
    }
    val filter = tremolo ?: return

    filter.apply(block)
}

/**
 * Configures the [Filters.Vibrato] filter.
 */
@OptIn(ExperimentalContracts::class)
public fun Filters.vibrato(block: Filters.Vibrato.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    checkImplementation()
    if (vibrato == null) {
        vibrato = GatewayPayload.FiltersCommand.Vibrato()
    }
    val filter = vibrato ?: return

    filter.apply(block)
}

@OptIn(ExperimentalContracts::class)
private fun Filters.checkImplementation() {
    contract {
        returns() implies (this@checkImplementation is GatewayPayload.FiltersCommand)
    }
    require(this is GatewayPayload.FiltersCommand) { "This has to be a internal implementation instance" }
}
