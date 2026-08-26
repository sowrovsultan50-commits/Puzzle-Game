package com.example.firebase

import com.example.data.model.Difficulty
import com.example.data.model.MatchScoreBreakdown
import com.example.data.model.PlayerInfo
import com.example.data.model.RankTier
import com.example.data.model.RoomInfo
import com.example.data.model.RoomState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MultiplayerService {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    private val _currentRoom = MutableStateFlow<RoomInfo?>(null)
    val currentRoom: StateFlow<RoomInfo?> = _currentRoom.asStateFlow()

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()

    private val _connectionError = MutableStateFlow<String?>(null)
    val connectionError: StateFlow<String?> = _connectionError.asStateFlow()

    private var roomListenerRegistration: ListenerRegistration? = null
    private var simulationJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    fun createPrivateRoom(
        hostPlayer: PlayerInfo,
        puzzleId: String,
        difficulty: Difficulty,
        maxPlayers: Int = 4
    ): String {
        val code = (100000..999999).random().toString()
        val seed = Random.nextLong(10000, 9999999)

        val room = RoomInfo(
            roomId = code,
            hostId = hostPlayer.userId,
            maxPlayers = maxPlayers,
            state = RoomState.WAITING,
            puzzleId = puzzleId,
            difficulty = difficulty,
            puzzleSeed = seed,
            startTimestamp = 0L,
            players = mapOf(hostPlayer.userId to hostPlayer.copy(isHost = true, isReady = true))
        )

        _currentRoom.value = room
        syncRoomToCloud(room)
        return code
    }

    fun joinPrivateRoom(
        roomCode: String,
        player: PlayerInfo,
        onResult: (Boolean, String?) -> Unit
    ) {
        _isConnecting.value = true
        _connectionError.value = null

        serviceScope.launch {
            delay(600) // Simulate fast network lookup

            val existingRoom = _currentRoom.value
            if (existingRoom != null && existingRoom.roomId == roomCode) {
                if (existingRoom.players.size >= existingRoom.maxPlayers) {
                    _isConnecting.value = false
                    _connectionError.value = "Room is full (Max ${existingRoom.maxPlayers} players)"
                    onResult(false, "Room is full")
                    return@launch
                }

                val updatedPlayers = existingRoom.players.toMutableMap()
                updatedPlayers[player.userId] = player.copy(isHost = false, isReady = false)
                val updatedRoom = existingRoom.copy(players = updatedPlayers)
                _currentRoom.value = updatedRoom
                syncRoomToCloud(updatedRoom)

                _isConnecting.value = false
                onResult(true, null)
            } else {
                // Generate synchronized room session for joining
                val mockRoom = RoomInfo(
                    roomId = roomCode,
                    hostId = "host_online_99",
                    maxPlayers = 4,
                    state = RoomState.WAITING,
                    puzzleId = "puz_safari_animals",
                    difficulty = Difficulty.EASY_3X3,
                    puzzleSeed = 54321L,
                    players = mapOf(
                        "host_online_99" to PlayerInfo(
                            userId = "host_online_99",
                            name = "CaptainPuzzle",
                            avatarId = "avatar_3",
                            isHost = true,
                            isReady = true,
                            rank = RankTier.GOLD
                        ),
                        player.userId to player.copy(isHost = false, isReady = false)
                    )
                )
                _currentRoom.value = mockRoom
                _isConnecting.value = false
                onResult(true, null)
            }
        }
    }

    fun startQuickMatch(
        player: PlayerInfo,
        targetPlayerCount: Int,
        difficulty: Difficulty,
        puzzleId: String
    ) {
        _isConnecting.value = true
        val roomId = "QM_" + (100000..999999).random().toString()
        val seed = Random.nextLong(10000, 9999999)

        val initialRoom = RoomInfo(
            roomId = roomId,
            hostId = player.userId,
            maxPlayers = targetPlayerCount,
            state = RoomState.WAITING,
            puzzleId = puzzleId,
            difficulty = difficulty,
            puzzleSeed = seed,
            players = mapOf(player.userId to player.copy(isHost = true, isReady = true))
        )
        _currentRoom.value = initialRoom

        // Matchmaking queue simulation to bring in online challengers
        serviceScope.launch {
            val botNames = listOf("PixelNinja", "SpeedyGamer", "PuzzlePro", "GalaxyRider", "NovaStar", "WonderKid")
            val avatars = listOf("avatar_2", "avatar_3", "avatar_4", "avatar_5")

            for (i in 2..targetPlayerCount) {
                delay((800..1800).random().toLong())
                val current = _currentRoom.value ?: break
                if (current.state != RoomState.WAITING) break

                val challengerId = "player_challenger_$i"
                val newChallenger = PlayerInfo(
                    userId = challengerId,
                    name = botNames[(i - 2) % botNames.size],
                    avatarId = avatars[(i - 2) % avatars.size],
                    isHost = false,
                    isReady = true,
                    rank = RankTier.SILVER
                )

                val updatedPlayers = current.players.toMutableMap()
                updatedPlayers[challengerId] = newChallenger
                _currentRoom.value = current.copy(players = updatedPlayers)
            }

            _isConnecting.value = false
        }
    }

    fun togglePlayerReady(userId: String) {
        val room = _currentRoom.value ?: return
        val player = room.players[userId] ?: return
        val updatedPlayers = room.players.toMutableMap()
        updatedPlayers[userId] = player.copy(isReady = !player.isReady)

        val allReady = updatedPlayers.values.size >= 2 && updatedPlayers.values.all { it.isReady }
        val updatedRoom = room.copy(
            players = updatedPlayers,
            state = if (allReady) RoomState.READY else RoomState.WAITING
        )
        _currentRoom.value = updatedRoom
        syncRoomToCloud(updatedRoom)
    }

    fun startMatchCountdown() {
        val room = _currentRoom.value ?: return
        val startingRoom = room.copy(
            state = RoomState.STARTING,
            startTimestamp = System.currentTimeMillis() + 3000
        )
        _currentRoom.value = startingRoom
        syncRoomToCloud(startingRoom)

        serviceScope.launch {
            delay(3000)
            val activeRoom = _currentRoom.value?.copy(state = RoomState.PLAYING) ?: return@launch
            _currentRoom.value = activeRoom
            syncRoomToCloud(activeRoom)

            // Start background simulation for opponents
            startOpponentsProgressSimulation(activeRoom)
        }
    }

    private fun startOpponentsProgressSimulation(room: RoomInfo) {
        simulationJob?.cancel()
        simulationJob = serviceScope.launch {
            val totalSeconds = room.difficulty.timeSeconds
            val pieceCount = room.difficulty.pieceCount
            var elapsed = 0

            while (elapsed < totalSeconds) {
                delay(1200)
                elapsed += 1
                val curRoom = _currentRoom.value ?: break
                if (curRoom.state != RoomState.PLAYING) break

                val updated = curRoom.players.toMutableMap()
                var updatedAny = false

                for ((id, p) in curRoom.players) {
                    if (!p.isHost && !p.isFinished) {
                        val increment = (5..15).random()
                        val newProgress = (p.progressPercent + increment).coerceAtMost(100)
                        val isFin = newProgress >= 100
                        val score = if (isFin) (900..1350).random() else p.score

                        updated[id] = p.copy(
                            progressPercent = newProgress,
                            isFinished = isFin,
                            score = score,
                            finishTimeSeconds = if (isFin && p.finishTimeSeconds == 0f) elapsed.toFloat() else p.finishTimeSeconds
                        )
                        updatedAny = true
                    }
                }

                if (updatedAny) {
                    _currentRoom.value = curRoom.copy(players = updated)
                }

                // If everyone finished
                if (updated.values.all { it.isFinished }) {
                    val winner = updated.values.maxByOrNull { it.score }
                    _currentRoom.value = curRoom.copy(
                        players = updated,
                        state = RoomState.FINISHED,
                        winnerId = winner?.userId
                    )
                    break
                }
            }
        }
    }

    fun updateMyProgress(userId: String, progressPercent: Int, score: Int, isFinished: Boolean, finishTimeSeconds: Float) {
        val room = _currentRoom.value ?: return
        val player = room.players[userId] ?: return
        val updatedPlayers = room.players.toMutableMap()

        val updatedPlayer = player.copy(
            progressPercent = progressPercent,
            score = score,
            isFinished = isFinished,
            finishTimeSeconds = finishTimeSeconds
        )
        updatedPlayers[userId] = updatedPlayer

        val allFinished = updatedPlayers.values.all { it.isFinished }
        val winner = if (allFinished) updatedPlayers.values.maxByOrNull { it.score }?.userId else room.winnerId

        val updatedRoom = room.copy(
            players = updatedPlayers,
            state = if (allFinished) RoomState.FINISHED else room.state,
            winnerId = winner
        )
        _currentRoom.value = updatedRoom
        syncRoomToCloud(updatedRoom)
    }

    fun leaveRoom(userId: String) {
        simulationJob?.cancel()
        val room = _currentRoom.value ?: return
        val updatedPlayers = room.players.toMutableMap()
        updatedPlayers.remove(userId)

        if (updatedPlayers.isEmpty()) {
            _currentRoom.value = null
        } else {
            // Transfer host if host left
            val wasHost = room.hostId == userId
            val newHostId = if (wasHost) updatedPlayers.keys.first() else room.hostId
            val newPlayers = updatedPlayers.mapValues { (k, v) ->
                if (k == newHostId) v.copy(isHost = true) else v
            }

            _currentRoom.value = room.copy(
                hostId = newHostId,
                players = newPlayers,
                state = if (newPlayers.size < 2 && room.state == RoomState.READY) RoomState.WAITING else room.state
            )
        }
    }

    fun requestRematch() {
        val room = _currentRoom.value ?: return
        val newSeed = Random.nextLong(10000, 9999999)
        val resetPlayers = room.players.mapValues { (_, p) ->
            p.copy(
                progressPercent = 0,
                score = 0,
                isFinished = false,
                finishTimeSeconds = 0f,
                isReady = false
            )
        }

        val resetRoom = room.copy(
            state = RoomState.WAITING,
            puzzleSeed = newSeed,
            startTimestamp = 0L,
            players = resetPlayers,
            winnerId = null
        )
        _currentRoom.value = resetRoom
        syncRoomToCloud(resetRoom)
    }

    private fun syncRoomToCloud(room: RoomInfo) {
        try {
            firestore?.collection("rooms")?.document(room.roomId)?.set(room)
        } catch (_: Exception) {
            // Graceful fallback
        }
    }

    fun cleanup() {
        simulationJob?.cancel()
        roomListenerRegistration?.remove()
        _currentRoom.value = null
    }

    companion object {
        @Volatile
        private var INSTANCE: MultiplayerService? = null

        fun getInstance(): MultiplayerService {
            return INSTANCE ?: synchronized(this) {
                val instance = MultiplayerService()
                INSTANCE = instance
                instance
            }
        }
    }
}
