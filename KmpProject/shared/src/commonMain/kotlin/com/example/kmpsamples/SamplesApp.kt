package com.example.kmpsamples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SamplesApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SamplesDestination.Home.route
    ) {
        composable(SamplesDestination.Home.route) {
            SamplesHomeScreen(
                demos = Demo.entries.toList(),
                onDemoSelected = { demo -> navController.navigate(demo.route) }
            )
        }
        composable(SamplesDestination.Gradient.route) {
            GradientPlaygroundScreen(onBack = { navController.popBackStack() })
        }
        composable(SamplesDestination.Controls.route) {
            FormPlaygroundScreen(onBack = { navController.popBackStack() })
        }
        composable(SamplesDestination.Animation.route) {
            AnimationPlaygroundScreen(onBack = { navController.popBackStack() })
        }
        composable(SamplesDestination.List.route) {
            TaskListPlaygroundScreen(onBack = { navController.popBackStack() })
        }
        composable(SamplesDestination.Grid.route) {
            GridPlaygroundScreen(onBack = { navController.popBackStack() })
        }
    }
}

private enum class SamplesDestination(val route: String) {
    Home("home"),
    Gradient("gradient"),
    Controls("controls"),
    Animation("animation"),
    List("list"),
    Grid("grid")
}

private enum class Demo(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String
) {
    Gradient(
        title = "Gradient Playground",
        subtitle = "Animate colors and tweak gradients.",
        icon = Icons.Outlined.Palette,
        route = SamplesDestination.Gradient.route
    ),
    Controls(
        title = "Forms and Controls",
        subtitle = "Build a form with common controls.",
        icon = Icons.Outlined.Tune,
        route = SamplesDestination.Controls.route
    ),
    Animation(
        title = "Animations",
        subtitle = "Play with smooth transitions.",
        icon = Icons.Outlined.AutoAwesome,
        route = SamplesDestination.Animation.route
    ),
    List(
        title = "Dynamic Lists",
        subtitle = "Organize data with sections.",
        icon = Icons.AutoMirrored.Outlined.ListAlt,
        route = SamplesDestination.List.route
    ),
    Grid(
        title = "Adaptive Grid",
        subtitle = "Lay out cards with LazyVerticalGrid.",
        icon = Icons.Outlined.GridView,
        route = SamplesDestination.Grid.route
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SamplesHomeScreen(
    demos: List<Demo>,
    onDemoSelected: (Demo) -> Unit
) {
    var showTips by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showTips) {
        ModalBottomSheet(
            onDismissRequest = { showTips = false },
            sheetState = sheetState
        ) {
            TipsSheetContent(onDismiss = { showTips = false })
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("KMP Samples") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { showTips = true }) {
                        Icon(
                            imageVector = Icons.Outlined.TipsAndUpdates,
                            contentDescription = "KMP tips"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 12.dp,
                bottom = padding.calculateBottomPadding() + 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SectionHeader(title = "Try a Sample")
            }
            items(demos) { demo ->
                DemoRow(
                    demo = demo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDemoSelected(demo) }
                )
            }
            item {
                SectionHeader(title = "About")
            }
            item {
                Text(
                    text = "Shared Compose Multiplatform UI for the same samples on Android and iOS. Compare one Kotlin UI tree with native SwiftUI, Jetpack Compose-only, and Flutter.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun DemoRow(
    demo: Demo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = demo.icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = demo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = demo.subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TipsSheetContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "KMP Tips",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TipsSection(
            title = "Shared UI",
            tips = listOf(
                "Compose Multiplatform keeps one Kotlin UI tree for Android and iOS.",
                "Compare delivery cost against separate SwiftUI and Compose apps."
            )
        )
        TipsSection(
            title = "Workflow",
            tips = listOf(
                "Use expect/actual for platform APIs such as native date pickers.",
                "Keep shared widgets in commonMain; validate on both mobile targets."
            )
        )
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Done")
        }
    }
}

@Composable
private fun TipsSection(
    title: String,
    tips: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        tips.forEach { tip ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun GradientPlaygroundScreen(onBack: () -> Unit) {
    SamplesPlaygroundScaffold(
        title = "Gradient Playground",
        onBack = onBack
    ) { padding ->
        var startHue by rememberSaveable { mutableFloatStateOf(0.1f) }
        var endHue by rememberSaveable { mutableFloatStateOf(0.6f) }
        var rotation by rememberSaveable { mutableFloatStateOf(0f) }

        val gradientColors = remember(startHue, endHue) {
            listOf(
                Color.hsl(startHue * 360f, 0.8f, 0.95f),
                Color.hsl(endHue * 360f, 0.85f, 0.8f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(colors = gradientColors))
                    .rotate(rotation)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                GradientSlider(
                    title = "Rotation",
                    value = rotation,
                    onValueChange = { rotation = it },
                    valueRange = 0f..360f,
                    displayValue = "${rotation.toInt()} deg"
                )
                GradientSlider(
                    title = "Start Hue",
                    value = startHue,
                    onValueChange = { startHue = it },
                    valueRange = 0f..1f,
                    displayValue = formatFixed2(startHue)
                )
                GradientSlider(
                    title = "End Hue",
                    value = endHue,
                    onValueChange = { endHue = it },
                    valueRange = 0f..1f,
                    displayValue = formatFixed2(endHue)
                )
            }
        }
    }
}

@Composable
private fun GradientSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = displayValue,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}

@Composable
private fun FormPlaygroundScreen(onBack: () -> Unit) {
    val openReminderPicker = rememberReminderPickerHandler()
    SamplesPlaygroundScaffold(
        title = "Forms and Controls",
        onBack = onBack
    ) { padding ->
        var name by remember { mutableStateOf("Taylor Developer") }
        var receivesEmail by remember { mutableStateOf(true) }
        var preferredFocus by remember { mutableStateOf(FocusOption.AFTERNOON) }
        var energyLevel by rememberSaveable { mutableFloatStateOf(0.7f) }
        var dailySessions by rememberSaveable { mutableIntStateOf(3) }
        var reminderDate by remember { mutableStateOf(nowLocalDateTime().plusHours(2)) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SectionCard(title = "Profile") {
                    androidx.compose.material3.OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ReminderRow(
                        label = "Reminder",
                        value = formatReminder(reminderDate),
                        onClick = { openReminderPicker(reminderDate) { reminderDate = it } }
                    )
                }
            }
            item {
                SectionCard(title = "Preferences") {
                    FocusSelector(
                        selected = preferredFocus,
                        onSelected = { preferredFocus = it }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Send daily summary",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        androidx.compose.material3.Switch(
                            checked = receivesEmail,
                            onCheckedChange = { receivesEmail = it }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Energy",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Slider(
                            value = energyLevel,
                            onValueChange = { energyLevel = it }
                        )
                        Text(
                            text = "Current level: ${(energyLevel * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    StepperRow(
                        value = dailySessions,
                        range = 1..8,
                        onValueChange = { dailySessions = it }
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        name = ""
                        receivesEmail = false
                        preferredFocus = FocusOption.MORNING
                        energyLevel = 0.5f
                        dailySessions = 1
                        reminderDate = nowLocalDateTime().plusHours(2)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset to Defaults")
                }
            }
        }
    }
}

private enum class FocusOption(val title: String) {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening")
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            content()
        }
    }
}

@Composable
private fun ReminderRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() },
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
private fun FocusSelector(
    selected: FocusOption,
    onSelected: (FocusOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FocusOption.values().forEach { option ->
            val isSelected = option == selected
            Surface(
                shape = RoundedCornerShape(14.dp),
                tonalElevation = if (isSelected) 4.dp else 0.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelected(option) }
            ) {
                Text(
                    text = option.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun StepperRow(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Sessions per day: $value",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
            Button(
                onClick = { if (value > range.first) onValueChange(value - 1) },
                enabled = value > range.first
            ) {
                Text("-")
            }
        Button(
            onClick = { if (value < range.last) onValueChange(value + 1) },
            enabled = value < range.last
        ) {
            Text("+")
        }
    }
}

@Composable
private fun AnimationPlaygroundScreen(onBack: () -> Unit) {
    SamplesPlaygroundScaffold(
        title = "Animations",
        onBack = onBack
    ) { padding ->
        var cardExpanded by rememberSaveable { mutableStateOf(false) }
        var animateBars by rememberSaveable { mutableStateOf(false) }
        val barHeights = remember {
            listOf(160f, 90f, 180f, 120f, 150f)
        }

        val animatedCardHeight by animateDpAsState(
            targetValue = if (cardExpanded) 200.dp else 120.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "cardHeight"
        )

        val animatables = remember {
            barHeights.map { Animatable(it * 0.35f) }
        }
        LaunchedEffect(animateBars) {
            if (animateBars) {
                animatables.forEachIndexed { index, animatable ->
                    launch {
                        delay(index * 80L)
                        while (true) {
                            animatable.animateTo(
                                targetValue = barHeights[index],
                                animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
                            )
                            animatable.animateTo(
                                targetValue = barHeights[index] * 0.35f,
                                animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
                            )
                        }
                    }
                }
            } else {
                animatables.forEachIndexed { index, animatable ->
                    animatable.snapTo(barHeights[index] * 0.35f)
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(200)
            animateBars = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedCardHeight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Spring Animation",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "Tap the button below to expand and collapse this card with a spring animation.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Button(
                    onClick = { cardExpanded = !cardExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (cardExpanded) "Collapse Card" else "Expand Card")
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Repeating Equalizer",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    animatables.forEachIndexed { index, animatable ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            modifier = Modifier
                                .width(18.dp)
                                .height(animatable.value.dp)
                        ) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskListPlaygroundScreen(onBack: () -> Unit) {
    val tasks = remember { mutableStateListOf(*SampleTask.examples.toTypedArray()) }
    val completed = remember { mutableStateListOf<SampleTask>() }
    SamplesPlaygroundScaffold(
        title = "Task List",
        onBack = onBack,
        topBarActions = {
            IconButton(
                onClick = {
                    tasks.clear()
                    tasks.addAll(SampleTask.examples)
                    completed.clear()
                }
            ) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Reset sample data")
            }
        },
        bottomBar = {
            ShuffleTasksBar(
                onShuffle = {
                    if (tasks.isNotEmpty()) {
                        tasks.shuffle()
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SectionHeader(title = "In Progress")
            }
            if (tasks.isEmpty()) {
                item {
                    EmptyStateText("All tasks completed. Nice work!")
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        showProgress = true,
                        onComplete = {
                            tasks.remove(task)
                            completed.add(0, task)
                        }
                    )
                }
            }
            item {
                SectionHeader(title = "Completed")
            }
            if (completed.isEmpty()) {
                item {
                    EmptyStateText("Swipe left on a task to mark it complete.")
                }
            } else {
                items(completed, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        showProgress = false,
                        onComplete = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun ShuffleTasksBar(onShuffle: () -> Unit) {
    Surface(
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onShuffle) {
                Text("Shuffle Tasks")
            }
        }
    }
}

@Composable
private fun EmptyStateText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
private fun TaskRow(
    task: SampleTask,
    showProgress: Boolean,
    onComplete: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = task.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = task.detail,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                task.dueDate?.let { due ->
                    Text(
                        text = "Due ${formatTaskDue(due)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                if (showProgress) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { task.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp)),
                    )
                }
            }
            if (showProgress) {
                TextButton(onClick = onComplete) {
                    Text("Done")
                }
            }
        }
    }
}

private data class SampleTask(
    val id: String = randomSampleId(),
    val title: String,
    val detail: String,
    val dueDate: kotlinx.datetime.LocalDateTime?,
    val progress: Float,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    companion object {
        val examples = listOf(
            SampleTask(
                title = "Design onboarding flow",
                detail = "Review the latest wireframes and add feedback.",
                dueDate = nowLocalDateTime().plusHours(6),
                progress = 0.45f,
                icon = Icons.Outlined.Palette
            ),
            SampleTask(
                title = "Stand-up meeting",
                detail = "Share yesterday's progress and today's plan.",
                dueDate = nowLocalDateTime().plusHours(2),
                progress = 0.1f,
                icon = Icons.Outlined.PeopleAlt
            ),
            SampleTask(
                title = "Refactor data layer",
                detail = "Clean up the networking code and add unit tests.",
                dueDate = nowLocalDateTime().plusDays(1),
                progress = 0.75f,
                icon = Icons.Outlined.Build
            ),
            SampleTask(
                title = "Prepare release notes",
                detail = "Summarize new features for the upcoming release.",
                dueDate = nowLocalDateTime().plusDays(2),
                progress = 0.3f,
                icon = Icons.AutoMirrored.Outlined.Article
            )
        )
    }
}

@Composable
private fun GridPlaygroundScreen(onBack: () -> Unit) {
    SamplesPlaygroundScaffold(
        title = "Adaptive Grid",
        onBack = onBack
    ) { padding ->
        val palettes = remember { SamplePalette.examples }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 32.dp,
                start = 20.dp,
                end = 20.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(palettes, key = { it.id }) { palette ->
                PaletteCard(palette = palette)
            }
        }
    }
}

@Composable
private fun PaletteCard(palette: SamplePalette) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = palette.colors
                        )
                    )
            ) {
                Text(
                    text = palette.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                )
            }
            Text(
                text = palette.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                palette.colors.forEachIndexed { index, color ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = color.copy(alpha = if (index == palette.colors.lastIndex) 0.9f else 0.8f),
                        modifier = Modifier
                            .width(32.dp)
                            .height(8.dp)
                    ) {}
                }
            }
        }
    }
}

private data class SamplePalette(
    val id: String = randomSampleId(),
    val name: String,
    val description: String,
    val colors: List<Color>
) {
    companion object {
        val examples = listOf(
            SamplePalette(
                name = "Sunset",
                description = "Warm accents that fade from orange to purple.",
                colors = listOf(Color(0xFFFF8A65), Color(0xFFF06292), Color(0xFFAB47BC))
            ),
            SamplePalette(
                name = "Forest",
                description = "Earthy greens that work for nature inspired designs.",
                colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A), Color(0xFF26A69A))
            ),
            SamplePalette(
                name = "Ocean",
                description = "Cool blues right at home in dashboard UI.",
                colors = listOf(Color(0xFF039BE5), Color(0xFF26C6DA), Color(0xFF00838F))
            ),
            SamplePalette(
                name = "Mono",
                description = "Neutral grays that keep the focus on typography.",
                colors = listOf(Color(0xFF9E9E9E), Color(0xFF757575), Color(0xFF424242))
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SamplesPlaygroundScaffold(
    title: String,
    onBack: () -> Unit,
    topBarActions: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = topBarActions
            )
        },
        bottomBar = bottomBar,
        content = content
    )
}
