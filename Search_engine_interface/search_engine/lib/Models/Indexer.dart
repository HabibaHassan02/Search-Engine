import 'package:search_engine/Models/pagedata.dart';

class Indexer{
 final String word;
 final Map<String,PageData> hm;
 final double idf;

 Indexer({
  required this.word,
  required this.hm,
  required this.idf,
 });
 factory Indexer.fromJson(Map<String, dynamic> json) {
  var hmJson = json['hm'] as Map<String, dynamic>;
  var hm = <String, PageData>{};
  hmJson.forEach((key, value) {
    hm[key] = PageData.fromJson(value);
  });
  return Indexer(
    word: json['word'],
    hm: hm,
    idf: double.parse(json['idf'].toString()),
  );
}
}