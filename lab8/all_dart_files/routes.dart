import 'package:flutter/material.dart';
import 'package:trial/home_screen.dart';
import 'package:trial/login_screen.dart';

final routes = {
  '/login':         (BuildContext context) => new LoginScreen(),
  '/home':         (BuildContext context) => new HomeScreen(),
  '/' :          (BuildContext context) => new LoginScreen(),
};