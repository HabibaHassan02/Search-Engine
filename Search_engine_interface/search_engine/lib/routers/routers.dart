import 'package:go_router/go_router.dart';
import '../screens/homepage.dart';
import '../screens/linkspage.dart';
class AppRouter {
  final GoRouter router = GoRouter(routes: <GoRoute>[
    GoRoute(
      path: '/',
      builder: (context, state) {
        return HomePage();
      },
    ),
    GoRoute(
      path: '/linkspage/:query',
      builder: (context, state) {
        return LinksPage(query: state.pathParameters["query"]!,);
      },
    ),
  ]);}