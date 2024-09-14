import 'package:flutter/material.dart';

class CalculadoraResultados extends StatelessWidget {
  final Map<String, dynamic> calculos;

  CalculadoraResultados({required this.calculos});

  @override
  Widget build(BuildContext context) {
    return calculos.isEmpty
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
                DataCell(Text(calculos['horasTrabalhadas'] ?? '')),
                DataCell(Text(calculos['debito'] ?? '')),
                DataCell(Text(calculos['credito'] ?? '')),
                DataCell(Text(calculos['adicionalNoturno'] ?? '')),
                DataCell(Text(calculos['intervalo'] ?? '')),
              ]),
            ],
          );
  }
}