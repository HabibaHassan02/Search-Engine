// ignore_for_file: unused_import

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../constants.dart';
import 'package:mongo_dart/mongo_dart.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:search_engine/Models/Indexer.dart';
import 'package:search_engine/Models/pagedata.dart';
import 'dart:collection';
class SearchService {
  http.Client _httpClient;

  SearchService([http.Client? httpClient]) : _httpClient = httpClient ?? http.Client();

  Future<List<dynamic>> getfromranker(String query) async {
    final Map<String, String> headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };
     
    final Uri url = Uri.parse('http://10.0.2.2:8080/queryprocessor/search/$query');
    
    try {
      final response = await _httpClient.get(url, headers: headers);
       print(response);
      if (response.statusCode == 200) {
        print("sucess");
        final jsonList = json.decode(response.body) as List<dynamic>;
        //final List<Indexer> result = jsonList.map((json) => Indexer.fromJson(json)).toList();
        return jsonList;
      } else {
        print("failure");
        throw 'Request failed with status: ${response.statusCode}';
      }
    } catch (e) {
      throw 'Something went wrong: $e';
    }
  }

  ////////////////////////////
  
}