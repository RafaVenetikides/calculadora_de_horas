import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Calculadora de Ponto',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: PontoPage(),
    );
  }
}

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

  void _adicionarMarcacao() {
    setState(() {
      _marcacoesControllers.add(TextEditingController());
    });
  }

  Future<void> _calcular() async {
    final cargaHoraria = _cargaHorariaController.text;
    final marcacoes = _marcacoesControllers
      .map((c) => c.text)
      .where((marcacao) => marcacao.isNotEmpty)
      .toList();
    print(marcacoes);

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
      // Handle error
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
            // Input para a carga horária
            Align(
              alignment: Alignment.centerLeft,  // Alinha o container à esquerda
              child: Container(
                width: 150,  // Definindo largura fixa
                decoration: BoxDecoration(
                  border: Border.all(color: Colors.grey, width: 2),
                  borderRadius: BorderRadius.circular(8),
                ),
                padding: EdgeInsets.all(8),
                child: TextFormField(
                  controller: _cargaHorariaController,
                  decoration: InputDecoration(
                    labelText: 'Carga Horária',
                    border: InputBorder.none, // Remove a borda padrão do TextFormField
                  ),
                  keyboardType: TextInputType.datetime,
                  textAlign: TextAlign.center,
                ),
              ),
            ),
            SizedBox(height: 20),
            // Inputs para as marcações (em linha com quadrados ao redor)
            Row(
              children: List.generate(_marcacoesControllers.length, (index) {
                return Expanded(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 4.0),
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.grey, width: 2),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: TextFormField(
                          controller: _marcacoesControllers[index],
                          decoration: InputDecoration(
                            border: InputBorder.none,
                            labelText: 'Marcação ${index + 1}',
                          ),
                          keyboardType: TextInputType.datetime,
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                  ),
                );
              }),
            ),
            SizedBox(height: 20),
            // Botão para adicionar mais marcações
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
            // Botão de calcular
            ElevatedButton(
              onPressed: _calcular,
              child: Text('Calcular'),
            ),
            SizedBox(height: 20),
            // Tabela de Resultados
            _calculos.isEmpty
                ? Container()
                : DataTable(
                    columns: [
                      DataColumn(label: Text('Horas Trabalhadas')),
                      DataColumn(label: Text('Débito')),
                      DataColumn(label: Text('Crédito')),
                      DataColumn(label: Text('Adicional Noturno')),
                      DataColumn(label: Text('Intervalo')),
                    ],
                    rows: [
                      DataRow(cells: [
                        DataCell(Text(_calculos['horasTrabalhadas'] ?? '')),
                        DataCell(Text(_calculos['debito'] ?? '')),
                        DataCell(Text(_calculos['credito'] ?? '')),
                        DataCell(Text(_calculos['adicionalNoturno'] ?? '')),
                        DataCell(Text(_calculos['intervalo'] ?? '')),
                      ]),
                    ],
                  ),
          ],
        ),
      ),
    );
  }
}