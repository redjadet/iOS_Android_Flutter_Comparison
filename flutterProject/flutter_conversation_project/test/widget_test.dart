import 'package:flutter/cupertino.dart';
import 'package:flutter_conversation_project/main.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Samples home shows title and sample list', (tester) async {
    await tester.pumpWidget(const SamplesApp());
    await tester.pumpAndSettle();

    expect(find.text('Flutter Samples'), findsOneWidget);
    expect(find.text('Why Flutter'), findsOneWidget);
    expect(find.text('Try a Sample'), findsOneWidget);
    expect(find.byType(CupertinoListSection), findsWidgets);
  });
}
