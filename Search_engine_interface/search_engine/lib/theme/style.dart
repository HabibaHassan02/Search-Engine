import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

ThemeData appTheme(context) {
  return ThemeData(
    primaryColor: Color.fromARGB(255, 154, 11, 211),
    primarySwatch: Colors.deepPurple,
    // textTheme: Theme.of(context)
    //     .textTheme
    //     .apply(bodyColor: Colors.black, displayColor: Colors.black),
    textTheme: GoogleFonts.soraTextTheme()
        .apply(bodyColor: Colors.black, displayColor: Colors.black),
  );
}
