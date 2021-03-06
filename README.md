# Lavakord
Support discord: https://discord.gg/ZbmrRVpDwR

Extension of the [official LavaLink-Client](https://github.com/FredBoat/Lavalink-Client) to work with [Kord](https://github.com/kordlib/kord)

**Both Kord and Lavakord is still in an experimental stage, as such we can't guarantee API stability between releases. While we'd love for you to try out our library, we don't recommend you use this in production just yet.**

Lavalink download: [https://github.com/Frederikam/Lavalink](https://github.com/Frederikam/Lavalink#server-configuration)

# Download
You can get Lavakord from here: [https://package-search.jetbrains.com/package?id=me.schlaubi%3Alavakord](https://package-search.jetbrains.com/package?id=me.schlaubi%3Alavakord) (You need `jcenter()` and jitpack though)

# Usage
You can create a `Lavalink` object like this
```kotlin
    val lavalink = kord.lavakord()

    // or    

    val lavalink = kord.lavalink {
        link {
            autoReconnect = false
        }
    }
```

You can obtain and use a `Link` like this
```kotlin
    val link = guild.getLink(lavalink)

    link.connect(channel)

    // use lavalink stuff like player

    link.disconnect()
```

Playing: https://github.com/DRSchlaubi/Lavakord/blob/master/example/src/main/kotlin/me/schlaubi/lavakord/example/Lavakord.kt#L99-124

# Track loading
Lavakord provides a wrapper for the Lavalink [Track loading API](https://github.com/Frederikam/Lavalink/blob/master/IMPLEMENTATION.md#track-loading-api)

You can load a Track by using `Link.loadItem(query: String)` for a couroutine based aproach or `Link.loadItem(query: String, callback: AudioLoadResultHandler)` for a callback based approach like Lavaplayer

# Events
Since 0.3 Lavakord provides a [Flow based](https://kotlinlang.org/docs/reference/coroutines/flow.html) way to listen for events.

```kotlin
val link: KordLink // = .../

val player = link.player

player.on<TrackStartEvent> {
    channel.createMessage(track.info.asString())
}
```

# Documentation
For more info please use the [example](https://github.com/DRSchlaubi/Lavakord/blob/master/example) or [Dokka docs](https://l.mik.wtf/lavakord/)

# Multiplatform
Since Lavakord 1.0 we use only Multiplatform Kotlin libraries but Ktor doesn't support Websockets when using Kotlin native yet see [kordlib/kord#69](https://github.com/kordlib/kord/issues/69) and [ktorio/ktor#1215](https://github.com/ktorio/ktor/issues/1215) for reference. Kord doesn't support Multiplatform because of the same issue as well

# Other Discord API wrappers
Since 1.0 it should be possible to implement your own version of lavakord by implementing your own versions of the LavaKord and Link classes you can see a reference implementation [in the kord package](https://github.com/DRSchlaubi/Lavakord/blob/master/src/main/kotlin/me/schlaubi/lavakord/kord)
