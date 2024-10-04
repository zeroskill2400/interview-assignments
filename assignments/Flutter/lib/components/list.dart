import 'package:flutter/material.dart';
import 'package:interview/components/item.dart';

import '../type/check.dart';

class CheckList extends StatefulWidget {
  const CheckList({super.key});

  @override
  State<CheckList> createState() => _CheckListState();
}

class _CheckListState extends State<CheckList> {
  // 리스트
  List<CheckType> items = [
    CheckType(title: "Item 1"),
    CheckType(title: "Item 2"),
    CheckType(title: "Item 3"),
    CheckType(title: "Item 4"),
  ];

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const Padding(
          padding: EdgeInsets.all(8.0),
          child: TextField(
            decoration: InputDecoration(
              hintText: "Search items...",
              prefixIcon: Icon(Icons.search),
            ),
          ),
        ),
        ListTile(
          leading: Checkbox(
            value: false, onChanged: (bool? value) {  },
          ),
          title: const Text("Check All"),
          trailing: IconButton(
            icon: const Icon(Icons.add), onPressed: () {  },
          ),
        ),
            const Divider(thickness: 1, height: 1, color: Colors.black),
        Expanded(
          child: ListView.builder(
            itemCount: items.length,
            itemBuilder: (context, index) {
              return CheckItem(items[index]);
            },
          ),
        ),
      ],
    );
  }
}