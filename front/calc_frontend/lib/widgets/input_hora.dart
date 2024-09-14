import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class InputHora extends StatelessWidget {
  final TextEditingController controller;
  final String labelText;
  final TextInputFormatter formatter;

  InputHora({
    required this.controller,
    required this.labelText,
    required this.formatter,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 150,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey, width: 2),
        borderRadius: BorderRadius.circular(8),
      ),
      padding: EdgeInsets.all(8),
      child: TextFormField(
        controller: controller,
        decoration: InputDecoration(
          labelText: labelText,
          border: InputBorder.none,
        ),
        keyboardType: TextInputType.number,
        inputFormatters: [formatter],
        textAlign: TextAlign.center,
      ),
    );
  }
}