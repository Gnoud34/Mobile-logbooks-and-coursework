import 'package:flutter/material.dart';
import 'screen/home_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MHikeApp());
}

class MHikeApp extends StatelessWidget {
  const MHikeApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'M-Hike',
      debugShowCheckedModeBanner: false,
      showPerformanceOverlay: false,
      theme: ThemeData(useMaterial3: true, colorSchemeSeed: Colors.green),
      home: const HomeScreen(),
    );
  }
}
