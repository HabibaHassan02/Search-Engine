import 'dart:core';


class PageData {
  final String title;
  final List<String> instancepages;
  final double tf;
  final double popularity;

  PageData({
    required this.title,
    required this.instancepages,
    required this.tf,
    required this.popularity
  });

  factory PageData.fromJson(Map<String, dynamic> json) {
    return PageData(
      title: json['title'],
      instancepages: List<String>.from(json['instancepages']),
      tf: double.parse(json['instancepages'].toString()),
      popularity: double.parse(json['popularity'].toString()),
    );
  }
}