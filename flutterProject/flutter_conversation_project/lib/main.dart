import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart';

void main() {
  runApp(const SamplesApp());
}

Color _resolveDynamic(BuildContext context, Color color) {
  return CupertinoDynamicColor.resolve(color, context);
}

class SamplesApp extends StatelessWidget {
  const SamplesApp({super.key});

  @override
  Widget build(BuildContext context) {
    return CupertinoApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Samples',
      theme: const CupertinoThemeData(
        primaryColor: CupertinoColors.activeBlue,
        barBackgroundColor: CupertinoColors.systemGroupedBackground,
        scaffoldBackgroundColor: CupertinoColors.systemGroupedBackground,
        textTheme: CupertinoTextThemeData(
          textStyle: TextStyle(
            inherit: true,
            fontSize: 16,
            letterSpacing: -0.2,
            color: CupertinoColors.label,
          ),
          navTitleTextStyle: TextStyle(
            inherit: false,
            fontSize: 18,
            fontWeight: FontWeight.w600,
            color: CupertinoColors.label,
          ),
        ),
      ),
      localizationsDelegates: const [
        GlobalCupertinoLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
      ],
      supportedLocales: const [Locale('en')],
      home: const SamplesHomePage(),
    );
  }
}

class SamplesHomePage extends StatelessWidget {
  const SamplesHomePage({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: const Text('Flutter Samples'),
        trailing: Semantics(
          button: true,
          label: 'Open Flutter tips',
          child: CupertinoButton(
            padding: EdgeInsets.zero,
            onPressed: () => _showTipsSheet(context),
            minimumSize: Size.zero,
            child: const Icon(CupertinoIcons.lightbulb),
          ),
        ),
      ),
      child: SafeArea(
        top: false,
        child: CupertinoScrollbar(
          child: ListView(
            padding: const EdgeInsets.symmetric(vertical: 16),
            children: [
              CupertinoListSection.insetGrouped(
                header: Text(
                  'Why Flutter',
                  style: TextStyle(
                    color: CupertinoColors.secondaryLabel.resolveFrom(context),
                    fontWeight: FontWeight.w600,
                  ),
                ),
                hasLeading: false,
                children: [
                  Padding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 12,
                    ),
                    child: Text(
                      'One Dart codebase delivers these samples to iOS and Android. Native stacks need two apps, two languages, and ongoing parity work for the same surface.',
                      style: theme.textTheme.textStyle.copyWith(
                        color: CupertinoColors.label.resolveFrom(context),
                        fontSize: 14,
                        height: 1.4,
                      ),
                      softWrap: true,
                    ),
                  ),
                ],
              ),
              CupertinoListSection.insetGrouped(
                header: Text(
                  'Try a Sample',
                  style: TextStyle(
                    color: CupertinoColors.secondaryLabel.resolveFrom(context),
                    fontWeight: FontWeight.w600,
                  ),
                ),
                children: demos
                    .map(
                      (demo) => _DemoTile(
                        demo: demo,
                        onTap: () => Navigator.of(
                          context,
                        ).push(CupertinoPageRoute(builder: demo.builder)),
                      ),
                    )
                    .toList(),
              ),
              CupertinoListSection.insetGrouped(
                header: Text(
                  'About',
                  style: TextStyle(
                    color: CupertinoColors.secondaryLabel.resolveFrom(context),
                    fontWeight: FontWeight.w600,
                  ),
                ),
                hasLeading: false,
                children: [
                  Padding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 12,
                    ),
                    child: Text(
                      'Browse common Flutter UI patterns—gradients, forms, animation, lists, and adaptive layouts. Each screen is a reusable building block you can ship across platforms from one implementation.',
                      style: theme.textTheme.textStyle.copyWith(
                        color: CupertinoColors.label.resolveFrom(context),
                        fontSize: 14,
                        height: 1.4,
                      ),
                      softWrap: true,
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showTipsSheet(BuildContext context) {
    final background = _resolveDynamic(
      context,
      CupertinoTheme.of(context).scaffoldBackgroundColor,
    );
    showCupertinoModalPopup<void>(
      context: context,
      builder: (sheetContext) {
        return CupertinoPopupSurface(
          child: SizedBox(
            height: MediaQuery.of(context).size.height * 0.5,
            child: CupertinoPageScaffold(
              backgroundColor: background,
              navigationBar: CupertinoNavigationBar(
                middle: const Text('Flutter Tips'),
                trailing: CupertinoButton(
                  padding: EdgeInsets.zero,
                  onPressed: () => Navigator.of(sheetContext).maybePop(),
                  minimumSize: Size.zero,
                  child: const Text('Done'),
                ),
              ),
              child: SafeArea(
                top: false,
                child: CupertinoScrollbar(
                  child: ListView(
                    padding: const EdgeInsets.symmetric(vertical: 8),
                    children: const [
                      _TipsSection(
                        title: 'Multi-platform',
                        tips: [
                          _Tip(
                            icon: CupertinoIcons.device_phone_portrait,
                            text:
                                'Run the same Dart UI on iOS and Android without rewriting screens.',
                          ),
                          _Tip(
                            icon: CupertinoIcons.square_stack_3d_up,
                            text:
                                'Share widgets, state, navigation, and tests instead of maintaining two native apps.',
                          ),
                        ],
                      ),
                      _TipsSection(
                        title: 'Workflow',
                        tips: [
                          _Tip(
                            icon: CupertinoIcons.arrow_2_circlepath,
                            text:
                                'Hot reload updates the UI in place so you can iterate without losing app state.',
                          ),
                          _Tip(
                            icon: CupertinoIcons.square_grid_2x2,
                            text:
                                'Compose layouts from reusable widgets—Rows, Columns, and Slivers scale cleanly.',
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}

class _DemoTile extends StatelessWidget {
  const _DemoTile({required this.demo, required this.onTap});

  final DemoDefinition demo;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    final label = CupertinoColors.label.resolveFrom(context);
    final secondary = CupertinoColors.secondaryLabel.resolveFrom(context);
    return Semantics(
      button: true,
      label: '${demo.title}. ${demo.subtitle}',
      child: CupertinoListTile.notched(
        onTap: onTap,
        leading: Container(
          width: 44,
          height: 44,
          decoration: BoxDecoration(
            color: theme.primaryColor,
            borderRadius: BorderRadius.circular(12),
          ),
          alignment: Alignment.center,
          child: Icon(demo.icon, color: CupertinoColors.white),
        ),
        title: Text(
          demo.title,
          style: theme.textTheme.textStyle.copyWith(
            fontWeight: FontWeight.w600,
            color: label,
          ),
        ),
        subtitle: Text(
          demo.subtitle,
          style: theme.textTheme.textStyle.copyWith(
            color: secondary,
            fontSize: 14,
          ),
        ),
        trailing: Icon(
          CupertinoIcons.chevron_forward,
          size: 18,
          color: CupertinoColors.tertiaryLabel.resolveFrom(context),
        ),
      ),
    );
  }
}

class _TipsSection extends StatelessWidget {
  const _TipsSection({required this.title, required this.tips});

  final String title;
  final List<_Tip> tips;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      child: DecoratedBox(
        decoration: BoxDecoration(
          color: _resolveDynamic(context, CupertinoColors.systemGrey6),
          borderRadius: BorderRadius.circular(14),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                title,
                style: theme.textTheme.textStyle.copyWith(
                  fontSize: 15,
                  fontWeight: FontWeight.w600,
                ),
              ),
              const SizedBox(height: 12),
              ...tips.map(
                (tip) => Padding(
                  padding: const EdgeInsets.only(bottom: 10),
                  child: _TipRow(tip: tip),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _TipRow extends StatelessWidget {
  const _TipRow({required this.tip});

  final _Tip tip;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        DecoratedBox(
          decoration: BoxDecoration(
            color: theme.primaryColor.withValues(alpha: 0.15),
            borderRadius: BorderRadius.circular(10),
          ),
          child: Padding(
            padding: const EdgeInsets.all(8),
            child: Icon(tip.icon, color: theme.primaryColor, size: 18),
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Text(
            tip.text,
            style: theme.textTheme.textStyle.copyWith(
              color: CupertinoColors.label.resolveFrom(context),
            ),
          ),
        ),
      ],
    );
  }
}

class _Tip {
  const _Tip({required this.icon, required this.text});

  final IconData icon;
  final String text;
}

class DemoDefinition {
  const DemoDefinition({
    required this.title,
    required this.subtitle,
    required this.icon,
    required this.builder,
  });

  final String title;
  final String subtitle;
  final IconData icon;
  final WidgetBuilder builder;
}

final List<DemoDefinition> demos = [
  DemoDefinition(
    title: 'Gradient Playground',
    subtitle: 'Animate colors and tweak gradients.',
    icon: CupertinoIcons.paintbrush,
    builder: (context) => const GradientPlaygroundPage(),
  ),
  DemoDefinition(
    title: 'Forms and Controls',
    subtitle: 'Build a form with common controls.',
    icon: CupertinoIcons.slider_horizontal_3,
    builder: (context) => const FormPlaygroundPage(),
  ),
  DemoDefinition(
    title: 'Animations',
    subtitle: 'Play with smooth transitions.',
    icon: CupertinoIcons.sparkles,
    builder: (context) => const AnimationPlaygroundPage(),
  ),
  DemoDefinition(
    title: 'Task List',
    subtitle: 'Organize data with sections.',
    icon: CupertinoIcons.check_mark_circled,
    builder: (context) => const TaskListPlaygroundPage(),
  ),
  DemoDefinition(
    title: 'Adaptive Grid',
    subtitle: 'Lay out cards with LazyVGrid.',
    icon: CupertinoIcons.square_grid_2x2,
    builder: (context) => const GridPlaygroundPage(),
  ),
];

class GradientPlaygroundPage extends StatefulWidget {
  const GradientPlaygroundPage({super.key});

  @override
  State<GradientPlaygroundPage> createState() => _GradientPlaygroundPageState();
}

class _GradientPlaygroundPageState extends State<GradientPlaygroundPage> {
  double _startHue = 0.1;
  double _endHue = 0.6;
  double _rotation = 0;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    final gradientColors = [
      HSVColor.fromAHSV(1, _startHue * 360, 0.8, 0.95).toColor(),
      HSVColor.fromAHSV(1, _endHue * 360, 0.85, 0.82).toColor(),
    ];

    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text('Gradient Playground'),
        previousPageTitle: 'Samples',
      ),
      child: SafeArea(
        top: false,
        child: CupertinoScrollbar(
          child: ListView(
            padding: const EdgeInsets.only(top: 20, bottom: 28),
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: AnimatedRotation(
                  turns: _rotation / 360,
                  duration: const Duration(milliseconds: 800),
                  curve: Curves.easeInOut,
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: gradientColors,
                      ),
                      borderRadius: BorderRadius.circular(24),
                      boxShadow: [
                        BoxShadow(
                          color: CupertinoColors.black.withValues(alpha: 0.18),
                          blurRadius: 16,
                          offset: const Offset(0, 12),
                        ),
                      ],
                    ),
                    child: const SizedBox(height: 220),
                  ),
                ),
              ),
              CupertinoListSection.insetGrouped(
                header: const Text(
                  'Controls',
                  style: TextStyle(
                    color: CupertinoColors.black,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                children: [
                  _SliderTile(
                    title: 'Rotation',
                    valueLabel: '${_rotation.toStringAsFixed(0)}°',
                    value: _rotation,
                    min: 0,
                    max: 360,
                    onChanged: (value) => setState(() => _rotation = value),
                  ),
                  _SliderTile(
                    title: 'Start Hue',
                    valueLabel: _startHue.toStringAsFixed(2),
                    value: _startHue,
                    min: 0,
                    max: 1,
                    onChanged: (value) => setState(() => _startHue = value),
                  ),
                  _SliderTile(
                    title: 'End Hue',
                    valueLabel: _endHue.toStringAsFixed(2),
                    value: _endHue,
                    min: 0,
                    max: 1,
                    onChanged: (value) => setState(() => _endHue = value),
                  ),
                ],
              ),
              Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 20,
                  vertical: 12,
                ),
                child: Text(
                  'Adjust rotation and hue values to explore different gradient moods.',
                  style: theme.textTheme.textStyle.copyWith(
                    color: CupertinoColors.black,
                    fontSize: 14,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _SliderTile extends StatelessWidget {
  const _SliderTile({
    required this.title,
    required this.valueLabel,
    required this.value,
    required this.min,
    required this.max,
    required this.onChanged,
  });

  final String title;
  final String valueLabel;
  final double value;
  final double min;
  final double max;
  final ValueChanged<double> onChanged;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return CupertinoListTile(
      title: Row(
        children: [
          Expanded(
            child: Text(
              title,
              style: theme.textTheme.textStyle.copyWith(
                fontWeight: FontWeight.w600,
                color: CupertinoColors.black,
              ),
            ),
          ),
          const SizedBox(width: 12),
          Text(
            valueLabel,
            style: theme.textTheme.textStyle.copyWith(
              color: CupertinoColors.black,
              fontSize: 13,
            ),
          ),
        ],
      ),
      subtitle: Padding(
        padding: const EdgeInsets.only(top: 12),
        child: CupertinoSlider(
          value: value,
          min: min,
          max: max,
          onChanged: onChanged,
        ),
      ),
    );
  }
}

enum FocusTime { morning, afternoon, evening }

extension on FocusTime {
  String get label {
    switch (this) {
      case FocusTime.morning:
        return 'Morning';
      case FocusTime.afternoon:
        return 'Afternoon';
      case FocusTime.evening:
        return 'Evening';
    }
  }
}

class FormPlaygroundPage extends StatefulWidget {
  const FormPlaygroundPage({super.key});

  @override
  State<FormPlaygroundPage> createState() => _FormPlaygroundPageState();
}

class _FormPlaygroundPageState extends State<FormPlaygroundPage> {
  late final TextEditingController _nameController;
  bool _receivesEmail = true;
  FocusTime _focusTime = FocusTime.afternoon;
  double _energyLevel = 0.7;
  int _dailySessions = 3;
  DateTime _reminderDate = DateTime.now().add(const Duration(hours: 2));

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: 'Taylor Developer');
  }

  @override
  void dispose() {
    _nameController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text('Forms and Controls'),
        previousPageTitle: 'Samples',
      ),
      child: SafeArea(
        top: false,
        child: CupertinoScrollbar(
          child: ListView(
            padding: const EdgeInsets.symmetric(vertical: 20),
            children: [
              CupertinoFormSection.insetGrouped(
                header: const Text('Profile'),
                children: [
                  CupertinoTextFormFieldRow(
                    controller: _nameController,
                    placeholder: 'Name',
                    keyboardType: TextInputType.name,
                    textCapitalization: TextCapitalization.words,
                  ),
                  CupertinoFormRow(
                    prefix: const Text('Reminder'),
                    child: CupertinoButton(
                      padding: EdgeInsets.zero,
                      onPressed: _pickReminder,
                      child: Text(
                        DateFormat.yMMMd().add_jm().format(_reminderDate),
                      ),
                    ),
                  ),
                ],
              ),
              CupertinoFormSection.insetGrouped(
                header: const Text('Preferences'),
                children: [
                  CupertinoFormRow(
                    prefix: const Text('Focus Time'),
                    child: CupertinoSlidingSegmentedControl<FocusTime>(
                      groupValue: _focusTime,
                      children: {
                        for (final focus in FocusTime.values)
                          focus: Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 8),
                            child: Text(focus.label),
                          ),
                      },
                      onValueChanged: (value) {
                        if (value == null) return;
                        setState(() => _focusTime = value);
                      },
                    ),
                  ),
                  CupertinoFormRow(
                    prefix: const Text('Send daily summary'),
                    child: CupertinoSwitch(
                      value: _receivesEmail,
                      onChanged: (value) =>
                          setState(() => _receivesEmail = value),
                    ),
                  ),
                  CupertinoFormRow(
                    prefix: const Text('Energy'),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        CupertinoSlider(
                          value: _energyLevel,
                          onChanged: (value) =>
                              setState(() => _energyLevel = value),
                        ),
                        Text(
                          'Current level: ${(100 * _energyLevel).round()}%',
                          style: CupertinoTheme.of(context).textTheme.textStyle
                              .copyWith(
                                color: CupertinoColors.black,
                                fontSize: 13,
                              ),
                        ),
                      ],
                    ),
                  ),
                  CupertinoFormRow(
                    prefix: const Text('Sessions per day'),
                    child: Row(
                      children: [
                        CupertinoButton(
                          padding: EdgeInsets.zero,
                          onPressed: _dailySessions > 1
                              ? () => setState(() => _dailySessions -= 1)
                              : null,
                          child: const Icon(CupertinoIcons.minus_circle),
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 12),
                          child: Text('$_dailySessions'),
                        ),
                        CupertinoButton(
                          padding: EdgeInsets.zero,
                          onPressed: _dailySessions < 8
                              ? () => setState(() => _dailySessions += 1)
                              : null,
                          child: const Icon(CupertinoIcons.add_circled),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 16,
                  vertical: 12,
                ),
                child: CupertinoButton.filled(
                  onPressed: _resetForm,
                  child: const Text('Reset to Defaults'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _pickReminder() async {
    DateTime selection = _reminderDate;
    await showCupertinoModalPopup<void>(
      context: context,
      builder: (popupContext) {
        final background = _resolveDynamic(
          context,
          CupertinoTheme.of(context).scaffoldBackgroundColor,
        );
        return Container(
          height: 320,
          color: background,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 8,
                ),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    CupertinoButton(
                      padding: EdgeInsets.zero,
                      onPressed: () => Navigator.of(popupContext).pop(),
                      child: const Text('Cancel'),
                    ),
                    CupertinoButton(
                      padding: EdgeInsets.zero,
                      onPressed: () => Navigator.of(popupContext).pop(),
                      child: const Text('Done'),
                    ),
                  ],
                ),
              ),
              Expanded(
                child: CupertinoDatePicker(
                  initialDateTime: _reminderDate,
                  mode: CupertinoDatePickerMode.dateAndTime,
                  use24hFormat: false,
                  onDateTimeChanged: (value) => selection = value,
                ),
              ),
            ],
          ),
        );
      },
    );
    if (!mounted) return;
    setState(() => _reminderDate = selection);
  }

  void _resetForm() {
    setState(() {
      _nameController.text = 'Taylor Developer';
      _receivesEmail = false;
      _focusTime = FocusTime.morning;
      _energyLevel = 0.5;
      _dailySessions = 1;
      _reminderDate = DateTime.now().add(const Duration(hours: 2));
    });
  }
}

class AnimationPlaygroundPage extends StatefulWidget {
  const AnimationPlaygroundPage({super.key});

  @override
  State<AnimationPlaygroundPage> createState() =>
      _AnimationPlaygroundPageState();
}

class _AnimationPlaygroundPageState extends State<AnimationPlaygroundPage>
    with SingleTickerProviderStateMixin {
  bool _cardExpanded = false;
  late final AnimationController _controller;
  late final List<Animation<double>> _barAnimations;
  final List<double> _barHeights = const [160, 90, 180, 120, 150];

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 1100),
      vsync: this,
    );
    _barAnimations = List.generate(_barHeights.length, (index) {
      final begin = _barHeights[index] * 0.35;
      final end = _barHeights[index];
      final start = index * 0.08;
      final curve = Interval(start.clamp(0.0, 0.9), 1, curve: Curves.easeInOut);
      return Tween<double>(
        begin: begin,
        end: end,
      ).animate(CurvedAnimation(parent: _controller, curve: curve));
    });
    Future<void>.delayed(const Duration(milliseconds: 200), () {
      if (!mounted) return;
      _controller.repeat(reverse: true);
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text('Animations'),
        previousPageTitle: 'Samples',
      ),
      child: SafeArea(
        top: false,
        child: CupertinoScrollbar(
          child: ListView(
            padding: const EdgeInsets.all(20),
            children: [
              DecoratedBox(
                decoration: BoxDecoration(
                  color: theme.primaryColor.withValues(alpha: 0.12),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(18),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      AnimatedContainer(
                        duration: const Duration(milliseconds: 600),
                        curve: Curves.easeInOutBack,
                        height: _cardExpanded ? 200 : 120,
                        decoration: BoxDecoration(
                          color: CupertinoColors.white.withValues(alpha: 0.9),
                          borderRadius: BorderRadius.circular(18),
                        ),
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: const [
                            Text(
                              'Spring Animation',
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                            SizedBox(height: 8),
                            Text(
                              'Tap the button below to expand and collapse this card with a spring animation.',
                              style: TextStyle(
                                fontSize: 14,
                                color: CupertinoColors.black,
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 16),
                      CupertinoButton.filled(
                        onPressed: () {
                          setState(() => _cardExpanded = !_cardExpanded);
                        },
                        child: Text(
                          _cardExpanded ? 'Collapse Card' : 'Expand Card',
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 32),
              Text(
                'Repeating Equalizer',
                style: theme.textTheme.textStyle.copyWith(
                  fontSize: 18,
                  fontWeight: FontWeight.w600,
                ),
              ),
              const SizedBox(height: 16),
              SizedBox(
                height: 200,
                child: AnimatedBuilder(
                  animation: _controller,
                  builder: (context, child) {
                    return Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: List.generate(_barAnimations.length, (index) {
                        return Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 7),
                          child: DecoratedBox(
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(12),
                              gradient: LinearGradient(
                                begin: Alignment.bottomCenter,
                                end: Alignment.topCenter,
                                colors: [
                                  theme.primaryColor,
                                  theme.primaryColor.withValues(alpha: 0.6),
                                ],
                              ),
                            ),
                            child: SizedBox(
                              width: 18,
                              height: _barAnimations[index].value,
                            ),
                          ),
                        );
                      }),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class TaskListPlaygroundPage extends StatefulWidget {
  const TaskListPlaygroundPage({super.key});

  @override
  State<TaskListPlaygroundPage> createState() => _TaskListPlaygroundPageState();
}

class _TaskListPlaygroundPageState extends State<TaskListPlaygroundPage> {
  final List<SampleTask> _tasks = List.of(SampleTask.examples);
  final List<SampleTask> _completed = [];
  bool _isEditing = false;

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        middle: const Text('Task List'),
        previousPageTitle: 'Samples',
        trailing: CupertinoButton(
          padding: EdgeInsets.zero,
          onPressed: () {
            setState(() => _isEditing = !_isEditing);
          },
          child: Text(_isEditing ? 'Done' : 'Edit'),
        ),
      ),
      child: SafeArea(
        top: false,
        child: Column(
          children: [
            Expanded(
              child: CupertinoScrollbar(
                child: ListView(
                  padding: const EdgeInsets.only(top: 16, bottom: 8),
                  children: [
                    _buildSection(
                      context: context,
                      title: 'In Progress',
                      emptyMessage: 'All tasks completed. Nice work!',
                      tasks: _tasks,
                      showProgress: true,
                      allowReorder: _isEditing,
                      allowSwipe: !_isEditing,
                      onMove: (oldIndex, newIndex) {
                        setState(() {
                          final task = _tasks.removeAt(oldIndex);
                          _tasks.insert(newIndex, task);
                        });
                      },
                      onDelete: (task) {
                        setState(() {
                          _tasks.remove(task);
                        });
                      },
                      onDismissed: (task) {
                        setState(() {
                          _tasks.remove(task);
                          _completed.insert(0, task);
                        });
                      },
                    ),
                    _buildSection(
                      context: context,
                      title: 'Completed',
                      emptyMessage: 'Swipe left on a task to mark it complete.',
                      tasks: _completed,
                      showProgress: false,
                      allowReorder: _isEditing,
                      allowSwipe: !_isEditing,
                      onMove: (oldIndex, newIndex) {
                        setState(() {
                          final task = _completed.removeAt(oldIndex);
                          _completed.insert(newIndex, task);
                        });
                      },
                      onDelete: (task) {
                        setState(() {
                          _completed.remove(task);
                        });
                      },
                      onDismissed: null,
                    ),
                  ],
                ),
              ),
            ),
            SafeArea(
              top: false,
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
                child: CupertinoButton.filled(
                  onPressed: _tasks.isEmpty
                      ? null
                      : () => setState(() => _tasks.shuffle()),
                  child: const Text('Shuffle Tasks'),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSection({
    required BuildContext context,
    required String title,
    required String emptyMessage,
    required List<SampleTask> tasks,
    required bool showProgress,
    required bool allowReorder,
    required bool allowSwipe,
    required void Function(int oldIndex, int newIndex)? onMove,
    required void Function(SampleTask task)? onDelete,
    required void Function(SampleTask task)? onDismissed,
  }) {
    final listChildren = <Widget>[];
    if (tasks.isEmpty) {
      listChildren.add(_EmptyListTile(message: emptyMessage));
    } else if (allowReorder && onMove != null) {
      listChildren.add(
        ReorderableListView.builder(
          physics: const NeverScrollableScrollPhysics(),
          padding: EdgeInsets.zero,
          shrinkWrap: true,
          buildDefaultDragHandles: false,
          itemCount: tasks.length,
          onReorderItem: onMove,
          itemBuilder: (context, index) {
            final task = tasks[index];
            return _TaskTile(
              key: task.id,
              task: task,
              showProgress: showProgress,
              showDragHandle: true,
              dragIndex: index,
              onDelete: onDelete != null ? () => onDelete(task) : null,
            );
          },
        ),
      );
    } else {
      listChildren.addAll(
        tasks.map((task) {
          final tile = _TaskTile(task: task, showProgress: showProgress);
          if (!allowSwipe || onDismissed == null) {
            return tile;
          }
          return Dismissible(
            key: task.id,
            direction: DismissDirection.endToStart,
            background: ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Container(
                alignment: Alignment.centerRight,
                padding: const EdgeInsets.symmetric(horizontal: 16),
                color: CupertinoColors.activeGreen,
                child: const Icon(
                  CupertinoIcons.check_mark,
                  color: CupertinoColors.white,
                ),
              ),
            ),
            onDismissed: (_) => onDismissed(task),
            child: tile,
          );
        }),
      );
    }

    return CupertinoListSection.insetGrouped(
      header: Text(title),
      children: listChildren,
    );
  }
}

class _TaskTile extends StatelessWidget {
  const _TaskTile({
    super.key,
    required this.task,
    required this.showProgress,
    this.showDragHandle = false,
    this.dragIndex,
    this.onDelete,
  });

  final SampleTask task;
  final bool showProgress;
  final bool showDragHandle;
  final int? dragIndex;
  final VoidCallback? onDelete;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    final dateText = task.dueDate != null ? _formatDue(task.dueDate!) : null;

    return CupertinoListTile(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
      leading: DecoratedBox(
        decoration: BoxDecoration(
          color: theme.primaryColor,
          shape: BoxShape.circle,
        ),
        child: Padding(
          padding: const EdgeInsets.all(10),
          child: Icon(task.icon, color: CupertinoColors.white, size: 20),
        ),
      ),
      title: Text(
        task.title,
        style: theme.textTheme.textStyle.copyWith(fontWeight: FontWeight.w600),
      ),
      subtitle: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(height: 6),
          Text(
            task.detail,
            style: theme.textTheme.textStyle.copyWith(
              color: CupertinoColors.black,
              fontSize: 14,
            ),
          ),
          if (dateText != null) ...[
            const SizedBox(height: 4),
            Text(
              'Due $dateText',
              style: theme.textTheme.textStyle.copyWith(
                color: CupertinoColors.black,
                fontSize: 13,
              ),
            ),
          ],
          if (showProgress) ...[
            const SizedBox(height: 10),
            _ProgressBar(value: task.progress),
          ],
        ],
      ),
      trailing: showDragHandle
          ? Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                if (onDelete != null)
                  CupertinoButton(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 8,
                      vertical: 6,
                    ),
                    onPressed: onDelete,
                    minimumSize: Size(0, 0),
                    child: const Text(
                      'Delete',
                      style: TextStyle(color: CupertinoColors.systemRed),
                    ),
                  ),
                if (onDelete != null && dragIndex != null)
                  const SizedBox(width: 8),
                if (dragIndex != null)
                  ReorderableDragStartListener(
                    index: dragIndex!,
                    child: const Icon(
                      CupertinoIcons.line_horizontal_3,
                      color: CupertinoColors.systemGrey,
                    ),
                  ),
              ],
            )
          : null,
    );
  }

  String _formatDue(DateTime date) {
    final datePart = DateFormat.yMMMd().format(date);
    final timePart = DateFormat.jm().format(date);
    return '$datePart $timePart';
  }
}

class _EmptyListTile extends StatelessWidget {
  const _EmptyListTile({required this.message});

  final String message;

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        child: Text(
          message,
          style: theme.textTheme.textStyle.copyWith(
            color: CupertinoColors.black,
          ),
        ),
      ),
    );
  }
}

class _ProgressBar extends StatelessWidget {
  const _ProgressBar({required this.value});

  final double value;

  @override
  Widget build(BuildContext context) {
    final clamped = value.clamp(0.0, 1.0);
    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: SizedBox(
        height: 6,
        child: Stack(
          children: [
            Container(
              color: _resolveDynamic(context, CupertinoColors.systemGrey4),
            ),
            FractionallySizedBox(
              alignment: Alignment.centerLeft,
              widthFactor: clamped,
              child: Container(color: CupertinoColors.activeBlue),
            ),
          ],
        ),
      ),
    );
  }
}

class SampleTask {
  SampleTask({
    required this.title,
    required this.detail,
    required this.progress,
    required this.icon,
    this.dueDate,
  }) : id = UniqueKey();

  final Key id;
  final String title;
  final String detail;
  final double progress;
  final IconData icon;
  final DateTime? dueDate;

  static List<SampleTask> get examples => [
    SampleTask(
      title: 'Design onboarding flow',
      detail: 'Review the latest wireframes and add feedback.',
      dueDate: DateTime.now().add(const Duration(hours: 6)),
      progress: 0.45,
      icon: CupertinoIcons.paintbrush,
    ),
    SampleTask(
      title: 'Stand-up meeting',
      detail: 'Share yesterday\'s progress and today\'s plan.',
      dueDate: DateTime.now().add(const Duration(hours: 2)),
      progress: 0.1,
      icon: CupertinoIcons.person_3,
    ),
    SampleTask(
      title: 'Refactor data layer',
      detail: 'Clean up the networking code and add unit tests.',
      dueDate: DateTime.now().add(const Duration(days: 1)),
      progress: 0.75,
      icon: CupertinoIcons.gear_solid,
    ),
    SampleTask(
      title: 'Prepare release notes',
      detail: 'Summarize new features for the upcoming release.',
      dueDate: DateTime.now().add(const Duration(days: 2)),
      progress: 0.3,
      icon: CupertinoIcons.doc_text,
    ),
  ];
}

class GridPlaygroundPage extends StatelessWidget {
  const GridPlaygroundPage({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = CupertinoTheme.of(context);
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        middle: Text('Adaptive Grid'),
        previousPageTitle: 'Samples',
      ),
      child: SafeArea(
        top: false,
        child: CupertinoScrollbar(
          child: CustomScrollView(
            slivers: [
              SliverPadding(
                padding: const EdgeInsets.all(16),
                sliver: SliverGrid(
                  gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
                    maxCrossAxisExtent: 220,
                    mainAxisSpacing: 16,
                    crossAxisSpacing: 16,
                    childAspectRatio: 0.7,
                  ),
                  delegate: SliverChildBuilderDelegate((context, index) {
                    final palette = SamplePalette.examples[index];
                    return DecoratedBox(
                      decoration: BoxDecoration(
                        color: _resolveDynamic(
                          context,
                          CupertinoColors.systemGrey6,
                        ),
                        borderRadius: BorderRadius.circular(24),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Container(
                              height: 130,
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(18),
                                gradient: LinearGradient(
                                  begin: Alignment.topLeft,
                                  end: Alignment.bottomRight,
                                  colors: palette.colors,
                                ),
                              ),
                              padding: const EdgeInsets.all(12),
                              alignment: Alignment.topLeft,
                              child: Text(
                                palette.name,
                                style: theme.textTheme.textStyle.copyWith(
                                  color: CupertinoColors.black,
                                  fontWeight: FontWeight.w600,
                                  fontSize: 16,
                                ),
                              ),
                            ),
                            const SizedBox(height: 14),
                            Text(
                              palette.description,
                              style: theme.textTheme.textStyle.copyWith(
                                color: CupertinoColors.black,
                                fontSize: 14,
                              ),
                            ),
                            const SizedBox(height: 12),
                            Row(
                              children: palette.colors
                                  .map(
                                    (color) => Expanded(
                                      child: Container(
                                        height: 8,
                                        margin: const EdgeInsets.symmetric(
                                          horizontal: 3,
                                        ),
                                        decoration: BoxDecoration(
                                          color: color,
                                          borderRadius: BorderRadius.circular(
                                            12,
                                          ),
                                        ),
                                      ),
                                    ),
                                  )
                                  .toList(),
                            ),
                          ],
                        ),
                      ),
                    );
                  }, childCount: SamplePalette.examples.length),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class SamplePalette {
  const SamplePalette({
    required this.name,
    required this.description,
    required this.colors,
  });

  final String name;
  final String description;
  final List<Color> colors;

  static List<SamplePalette> get examples => [
    const SamplePalette(
      name: 'Sunset',
      description: 'Warm accents that fade from orange to purple.',
      colors: [
        CupertinoColors.systemOrange,
        CupertinoColors.systemPink,
        CupertinoColors.systemPurple,
      ],
    ),
    const SamplePalette(
      name: 'Forest',
      description: 'Earthy greens that work for nature inspired designs.',
      colors: [
        CupertinoColors.systemGreen,
        CupertinoColors.systemTeal,
        CupertinoColors.activeGreen,
      ],
    ),
    const SamplePalette(
      name: 'Ocean',
      description: 'Cool blues that feel right at home in dashboard UI.',
      colors: [
        CupertinoColors.activeBlue,
        CupertinoColors.systemTeal,
        CupertinoColors.systemIndigo,
      ],
    ),
    const SamplePalette(
      name: 'Mono',
      description: 'Neutral grays that keep the focus on typography.',
      colors: [
        CupertinoColors.systemGrey,
        CupertinoColors.systemGrey3,
        CupertinoColors.black,
      ],
    ),
  ];
}
