import 'package:flutter/material.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Column(
        children: [
          Row(
            children: [
              Column(
                children: [
                  Container(
                    width: 500,
                    alignment: Alignment.center,
                    child: Text('Hello'),
                    ),
                    Container(
                      width: 500,
                      alignment: Alignment.center,
                      child: Text('World'),
                    ),
                ],
              ),
              Column(
                children: [
                  Text('Hello'),
                  Text('World'),
                ],
              ),
              Column(
                children: [
                  Text('Hello'),
                  Text('World'),
                ],
              ),
            ],
          ),
          Row(
            children: [
              Text('Hello'),
              Text('World'),
            ],
          ),
        ],
      ),
    );
  }
}
