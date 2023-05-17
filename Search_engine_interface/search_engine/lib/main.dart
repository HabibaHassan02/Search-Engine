import 'package:flutter/material.dart';
import 'routers/routers.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:search_engine/theme/style.dart';
void main() {
  runApp( MyApp());
}
class MyApp extends StatelessWidget {
  
   

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      theme: appTheme(context),
      routerConfig: AppRouter().router,
      
    );
    
  }

}

