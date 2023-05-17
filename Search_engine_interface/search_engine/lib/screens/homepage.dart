import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:search_engine/routers/routers.dart';
import 'package:http/http.dart' as http;

import 'linkspage.dart';


class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final _textController = TextEditingController();
  

  @override
  Widget build(BuildContext context) {
   

  
    return Stack(
      children: [
        Container(
          color: Color.fromARGB(255, 99, 10, 110), // your colored background
        ),
        Opacity(
          opacity: 0.45, // adjust the opacity of the watermark

        ),
        Scaffold(
          appBar: AppBar(
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
                  controller:  _textController,
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
      backgroundColor: Color.fromARGB(255, 252, 254, 255).withOpacity(0.45),
      
      ),
          backgroundColor: Colors.transparent,
          body: SingleChildScrollView(
            physics: const BouncingScrollPhysics(),
            child: Center(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [

                  Container(
                    margin: EdgeInsets.only(top: 40),
                    width: MediaQuery.of(context).size.width * 0.5,
                    child: TextField(
                      controller: _textController,
                      decoration: InputDecoration(
                        hintStyle: const TextStyle(
                            color: Color.fromARGB(255, 255, 255, 255)),
                        hintText: "Search...",
                        prefixIcon: const Icon(Icons.search,
                            color: const Color.fromARGB(255, 252, 254, 255)),
                        filled: true,
                        fillColor: const Color.fromARGB(255, 252, 254, 255)
                            .withOpacity(0.5),
                        enabledBorder: OutlineInputBorder(
                          borderSide: BorderSide(
                              color: Color.fromARGB(255, 186, 0, 233)),
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
                  Padding(
                    padding: const EdgeInsets.only(top: 30),
                    child: Container(
                        width: 200,
                        height: 50,
                        child: ElevatedButton(
                          onPressed: () {
                            print("samaaaaaaaaaaaaaa");
                            print(_textController .text);
                          return context.go("/linkspage/${_textController.text}");
                            
                          },
                          child: Text(
                            "Google Search",
                            style: TextStyle(fontSize: 20),
                          ),
                          style: ElevatedButton.styleFrom(
                              backgroundColor:
                                  Color.fromARGB(255, 14, 177, 177)),
                        )),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }
}
