package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Difficulty
import com.example.data.model.PuzzlePiece
import com.example.domain.engine.PuzzleEngine
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBorderDark
import com.example.ui.theme.BrandCardDark
import com.example.ui.theme.BrandSurfaceDark
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.SuccessGreen

@Composable
fun PuzzleBoardView(
    puzzleEngine: PuzzleEngine,
    drawableRes: Int,
    onPiecePlaced: (pieceId: Int, targetSlot: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = remember(drawableRes) {
        try {
            BitmapFactory.decodeResource(context.resources, drawableRes)
        } catch (e: Exception) {
            null
        }
    }

    var selectedPieceId by remember { mutableStateOf<Int?>(puzzleEngine.trayPieces.firstOrNull()?.id) }

    val rows = puzzleEngine.difficulty.rows
    val cols = puzzleEngine.difficulty.cols

    val infiniteTransition = rememberInfiniteTransition(label = "hint_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Puzzle Grid Canvas Frame
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(BrandSurfaceDark)
                .border(2.dp, BrandBorderDark, RoundedCornerShape(20.dp))
                .shadow(12.dp, RoundedCornerShape(20.dp))
        ) {
            // Optional Ghost Image Overlay
            if (puzzleEngine.isGhostVisible && bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Ghost Preview",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.28f)
                )
            }

            // Grid Cells
            Column(modifier = Modifier.fillMaxSize()) {
                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (c in 0 until cols) {
                            val slotIndex = r * cols + c
                            val placedPiece = puzzleEngine.boardGrid.getOrNull(slotIndex)
                            val isHighlighted = puzzleEngine.highlightedTargetSlot == slotIndex

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .border(
                                        width = if (isHighlighted) 3.dp else 0.5.dp,
                                        color = if (isHighlighted) AccentGold.copy(alpha = glowAlpha)
                                        else BrandBorderDark.copy(alpha = 0.6f)
                                    )
                                    .background(
                                        if (isHighlighted) AccentGold.copy(alpha = 0.18f)
                                        else if (placedPiece != null) Color.Transparent
                                        else BrandCardDark.copy(alpha = 0.4f)
                                    )
                                    .clickable {
                                        selectedPieceId?.let { pieceId ->
                                            onPiecePlaced(pieceId, slotIndex)
                                            // Reset selected piece or pick next tray piece
                                            selectedPieceId = puzzleEngine.trayPieces.firstOrNull()?.id
                                        }
                                    }
                                    .testTag("board_slot_$slotIndex"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (placedPiece != null && bitmap != null) {
                                    // Render Sliced Piece Bitmap inside Cell
                                    PuzzlePieceSlice(
                                        bitmap = bitmap,
                                        originalRow = placedPiece.originalRow,
                                        originalCol = placedPiece.originalCol,
                                        totalRows = rows,
                                        totalCols = cols,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    // Empty slot placeholder indicator
                                    if (isHighlighted) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = "Place Here",
                                            tint = AccentGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${slotIndex + 1}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.15f),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tray Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AVAILABLE PIECES (${puzzleEngine.trayPieces.size})",
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            if (selectedPieceId != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Tap Board Slot to Place",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Piece Selection Tray
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (puzzleEngine.trayPieces.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "All Pieces Placed! Puzzle Complete!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            } else {
                puzzleEngine.trayPieces.forEach { piece ->
                    val isSelected = selectedPieceId == piece.id

                    Card(
                        modifier = Modifier
                            .size(72.dp)
                            .scale(if (isSelected) 1.08f else 1.0f)
                            .clickable { selectedPieceId = piece.id }
                            .testTag("tray_piece_${piece.id}"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) PrimaryCyan else BrandBorderDark
                        ),
                        colors = CardDefaults.cardColors(containerColor = BrandSurfaceDark)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (bitmap != null) {
                                PuzzlePieceSlice(
                                    bitmap = bitmap,
                                    originalRow = piece.originalRow,
                                    originalCol = piece.originalCol,
                                    totalRows = rows,
                                    totalCols = cols,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(PrimaryCyan.copy(alpha = 0.2f))
                                        .border(2.dp, PrimaryCyan, RoundedCornerShape(14.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PuzzlePieceSlice(
    bitmap: Bitmap,
    originalRow: Int,
    originalCol: Int,
    totalRows: Int,
    totalCols: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val srcWidth = bitmap.width / totalCols
        val srcHeight = bitmap.height / totalRows
        val srcX = originalCol * srcWidth
        val srcY = originalRow * srcHeight

        val imageBitmap = bitmap.asImageBitmap()

        drawImage(
            image = imageBitmap,
            srcOffset = IntOffset(srcX, srcY),
            srcSize = IntSize(srcWidth, srcHeight),
            dstOffset = IntOffset(0, 0),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}
