import 'package:flutter/material.dart';
import 'package:interview/type/check.dart';

class CheckItem extends StatelessWidget {
  final CheckType item;

  const CheckItem(this.item, {super.key});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Checkbox(
        value: false,
        onChanged: (bool? value) {
        },
      ),
      title: Text(item.title),
      trailing: IconButton(
        icon: const Icon(Icons.delete), onPressed: () {  },
      ),
    );
  }
}
