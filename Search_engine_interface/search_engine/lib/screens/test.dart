// ignore_for_file: non_constant_identifier_names

import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:search_engine/routers/routers.dart';
import 'package:search_engine/services/search_service.dart';
import '../Models/Indexer.dart';
import 'package:url_launcher/url_launcher.dart';


class LinksPage extends StatefulWidget {
  final String query;
  LinksPage({required this.query,super.key});

  @override
  State<LinksPage> createState() => _LinksPageState();
}

class _LinksPageState extends State<LinksPage> {
  late Future<List<dynamic>> indexes;
  late DateTime _startTime;


  @override
  void initState() {
    super.initState();
    SearchService searchService = SearchService();
    indexes = searchService.getfromranker(widget.query);

    _startTime = DateTime.now();

    print("test");
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
          child: FutureBuilder(
              future: indexes,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.done)
                {
                  final Duration searchTime = DateTime.now().difference(_startTime);
                  final data = snapshot.data as List<dynamic>;
                  return Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text("Search time is:"+searchTime.toString()
                        ),
                      ),
                      for (final item in data)
                        Container(
                          width: double.infinity,
                          decoration: BoxDecoration(
                            border:Border.all(color: Colors.grey),

                          ),
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                GestureDetector(
                                  onTap: () async {
                                    final url = Uri.parse(item["value"][0]) ;
                                    if (await canLaunchUrl(url)) {
                                      await launchUrl(url);
                                    } else {
                                      throw 'Could not launch $url';
                                    }
                                  },
                                  child: Text(
                                    item["value"][1],
                                    style: TextStyle(
                                      color: Colors.blue,
                                      decoration: TextDecoration.underline,
                                    ),
                                  ),
                                ),
                                Text(item["value"][0],
                                  style: TextStyle(
                                      fontSize: 10,
                                      color: Colors.grey

                                  ),
                                ),
                                Text(item["value"][2],
                                  style: TextStyle(
                                    fontSize: 15,

                                  ),
                                ),

                              ],
                            ),
                          ),
                        ),

                    ],
                  );

                }
                else {
                  return Container(
                    decoration: const BoxDecoration(color: Colors.white),
                    child: const Center(child: CircularProgressIndicator()),
                  );
                }
              }
          ),
        ));
  }
}