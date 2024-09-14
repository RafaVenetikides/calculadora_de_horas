import 'package:flutter/material.dart';
import 'package:flutter/services.dart';  
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../widgets/calculadora_resultados.dart';
import '../widgets/input_hora.dart';

class PontoPage extends StatefulWidget {
  @override
  _PontoPageState createState() => _PontoPageState();
}

class _PontoPageState extends State<PontoPage> {
  final TextEditingController _cargaHorariaController = TextEditingController();
  final List<TextEditingController> _marcacoesControllers = [
    TextEditingController(),
    TextEditingController(),
    TextEditingController(),
    TextEditingController()
  ];

  Map<String, dynamic> _calculos = {};

  // TextInputFormatter para formatar o campo como HH:MM
  final hhmmFormatter = TextInputFormatter.withFunction((oldValue, newValue) {
    String digitsOnly = newValue.text.replaceAll(RegExp(r'[^0-9]'), '');
    if (digitsOnly.length > 4) {
      digitsOnly = digitsOnly.substring(0, 4);
    }
    String formatted = '';
    if (digitsOnly.length >= 2) {
      formatted = '${digitsOnly.substring(0, 2)}:${digitsOnly.substring(2)}';
    } else if (digitsOnly.isNotEmpty) {
      formatted = digitsOnly;
    }
    return TextEditingValue(
      text: formatted,
      selection: TextSelection.collapsed(offset: formatted.length),
    );
  });

  // Função para fazer a requisição ao backend
  Future<void> _calcular() async {
    final cargaHoraria = _cargaHorariaController.text;
    final marcacoes = _marcacoesControllers
        .map((c) => c.text)
        .where((marcacao) => marcacao.isNotEmpty)
        .toList();

    final response = await http.post(
      Uri.parse('http://localhost:8080/api/v1/periodo/calcular'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'cargaHoraria': cargaHoraria,
        'marcacoes': marcacoes,
      }),
    );

    if (response.statusCode == 200) {
      setState(() {
        _calculos = jsonDecode(response.body);
      });
    } else {
      setState(() {
        _calculos = {'error': 'Erro ao calcular'};
      });
    }
  }

  void _removerUltimaMarcacao() {
    if (_marcacoesControllers.isNotEmpty) {
      setState(() {
        _marcacoesControllers.removeLast();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Calculadora de Ponto'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Align(
              alignment: Alignment.centerLeft,
              child: InputHora(
                controller: _cargaHorariaController,
                labelText: 'Carga Horária',
                formatter: hhmmFormatter,
              ),
            ),
            SizedBox(height: 20),
            Row(
              children: List.generate(_marcacoesControllers.length, (index) {
                return Expanded(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 4.0),
                    child: InputHora(
                      controller: _marcacoesControllers[index],
                      labelText: 'Marcação ${index + 1}',
                      formatter: hhmmFormatter,
                    ),
                  ),
                );
              }),
            ),
            SizedBox(height: 20),
            Row(
              children: [
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      _marcacoesControllers.add(TextEditingController());
                    });
                  },
                  child: Icon(Icons.add),
                ),
                SizedBox(width: 10),
                IconButton(
                  icon: Icon(Icons.delete),
                  onPressed: _removerUltimaMarcacao,
                  tooltip: 'Remover última marcação',
                ),
              ],
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _calcular,
              child: Text('Calcular'),
            ),
            SizedBox(height: 20),
            CalculadoraResultados(calculos: _calculos),
          ],
        ),
      ),
    );
  }
}