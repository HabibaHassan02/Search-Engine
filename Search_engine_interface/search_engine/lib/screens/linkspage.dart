// ignore_for_file: non_constant_identifier_names

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:search_engine/routers/routers.dart';
import 'package:search_engine/services/search_service.dart';
import '../Models/Indexer.dart';


class LinksPage extends StatefulWidget {
  final String query;
   LinksPage({required this.query,super.key});
  
  @override
  State<LinksPage> createState() => _LinksPageState();
}

class _LinksPageState extends State<LinksPage> {
  late Future<List> indexes;
  
  @override
  void initState() {
    super.initState();
    SearchService searchService = SearchService();
    indexes = searchService.getfromranker(widget.query);

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(appBar:
    AppBar(
      title: FittedBox(
        child: Row(
          children: [
            IconButton(
                onPressed: () {
                  return context.go("/");
                },
                icon: Icon(Icons.arrow_back)),
            IconButton(onPressed: () {}, icon: Icon(Icons.arrow_forward)),
            IconButton(onPressed: () {}, icon: Icon(Icons.refresh_rounded)),
            Padding(
              padding: const EdgeInsets.only(left: 8.0),
              child: Container(
                width: MediaQuery.of(context).size.width * 0.8,
                height: 30,
                child: TextField(
                  decoration: InputDecoration(
                    prefixIcon: const Icon(Icons.search,
                        color: const Color.fromARGB(255, 252, 254, 255)),
                    filled: true,
                    fillColor: const Color.fromARGB(255, 252, 254, 255)
                        .withOpacity(0.5),
                    enabledBorder: OutlineInputBorder(
                      borderSide:
                          BorderSide(color: Color.fromARGB(255, 186, 0, 233)),
                    ),
                    border: const OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(25)),
                      borderSide: BorderSide.none,
                    ),
                  ),
                  onChanged: (value) {
                    // perform search based on the current value of the TextField
                  },
                  style: TextStyle(color: Colors.white),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 10.0),
              child: Icon(Icons.extension),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 10.0),
              child: Icon(Icons.star),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 10.0),
              child: Icon(Icons.supervised_user_circle),
            ),
          ],
        ),
      ),
      backgroundColor: Color.fromARGB(255, 199, 26, 199).withOpacity(0.45),
      
      ),
    body: SingleChildScrollView(

    ));
  }
}