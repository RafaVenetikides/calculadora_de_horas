import 'package:flutter/material.dart';
import 'pages/ponto_page.dart';  // Importa a página PontoPage

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Calculadora de Horas',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: PontoPage(),  // Define PontoPage como a tela inicial
    );
  }
}