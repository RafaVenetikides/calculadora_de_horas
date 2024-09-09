import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Calculadora de Horas",
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.white),
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: HomePage(),
    );
  }

}

  class HomePage extends StatefulWidget{
    @override
    _HomePageState createState() => _HomePageState();
  }

  class _HomePageState extends State<HomePage>{
    final TextEditingController cargaHorariaController = TextEditingController();
    final TextEditingController marcacaoController = TextEditingController();

    String horasTrabalhadas = "99:45";
    String debito = "13:25";
    String credito = "28:12";
    String horasNormais = "49:25";
    String adicionalNoturno = "25:25";
    String intervalo = "00:30";

    @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Calculadora de Horas"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Carga Horária Field
            Row(
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start, // Aligns text to the left
                  children: [
                    // Title/Label outside the box
                    Text(
                      "Carga Horária",
                      style: TextStyle(fontSize: 16), // Customize the font size if needed
                    ),
                    SizedBox(height: 4), // Add some spacing between the label and the input box
                    // Input box
                    Container(
                      height: 50,
                      width: 100,
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.black),
                        borderRadius: BorderRadius.circular(4),
                      ),
                      padding: EdgeInsets.symmetric(horizontal: 8.0),
                      child: TextFormField(
                        controller: cargaHorariaController,
                        decoration: InputDecoration(
                          border: InputBorder.none,
                          contentPadding: EdgeInsets.symmetric(vertical: 10),
                        ),
                        keyboardType: TextInputType.number,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            SizedBox(height: 16),
            // Marcações Fields
            Row(
              children: [
                Expanded(
                  child: TextFormField(
                    controller: marcacaoController,
                    decoration: InputDecoration(labelText: "Marcação 1"),
                    keyboardType: TextInputType.number,
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.add_circle),
                  onPressed: () {
                    // Lógica para adicionar mais marcações
                  },
                ),
              ],
            ),
            SizedBox(height: 16),
            // Botão de Calcular
            ElevatedButton(
              onPressed: () {
                // Lógica para calcular os resultados
              },
              child: Text("CALCULAR"),
            ),
            SizedBox(height: 16),
            // Tabela de Resultados
            DataTable(columns: [
              DataColumn(label: Text('HORAS TRABALHADAS')),
              DataColumn(label: Text('DÉBITO')),
              DataColumn(label: Text('CRÉDITO')),
              DataColumn(label: Text('HORAS TRABALHADAS NORMAIS')),
              DataColumn(label: Text('ADICIONAL NOTURNO')),
              DataColumn(label: Text('INTERVALO')),
            ], rows: [
              DataRow(cells: [
                DataCell(Text(horasTrabalhadas)),
                DataCell(Text(debito)),
                DataCell(Text(credito)),
                DataCell(Text(horasNormais)),
                DataCell(Text(adicionalNoturno)),
                DataCell(Text(intervalo)),
              ]),
            ]),
          ],
        ),
      ),
    );
  }
}
  